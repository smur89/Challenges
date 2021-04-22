import io.odin.formatter.Formatter
import io.odin.loggers.ConsoleLogger
import io.odin.{Level, Logger}
import org.http4s.circe.CirceEntityCodec.circeEntityDecoder
import org.http4s.client._
import org.http4s.client.blaze._
import squants.market.{Currency, EUR, MoneyContext, defaultMoneyContext}
import types.{From, To}

import cats.Show
import cats.implicits._
import cats.effect.{ContextShift, IO, Resource, Timer}

import scala.annotation.tailrec
import scala.concurrent.ExecutionContext.global
import scala.util.control.NoStackTrace

package object types {
  /*@newtype*/
  case class From[A](coerce: A)
  /*@newtype*/
  case class To[A](coerce: A)
}

sealed abstract class Error(val msg: String) extends Throwable with NoStackTrace with Product with Serializable {
  override def getMessage: String = msg
}
final case class EmptyGraph(override val msg: String) extends Error(msg)
final case class EmptyRelaxations(override val msg: String) extends Error(msg)

case class ExchangeRate[A](from: From[A], to: To[A], rate: Double)

object ExchangeRate {
  implicit val show: Show[ExchangeRate[Currency]] = Show.show(exchange => s"${exchange.from}->${exchange.to}:${exchange.rate}")
  implicit val ctx: MoneyContext = defaultMoneyContext.copy(defaultCurrency = EUR)

  def decode(responseMap: Map[String, Double]): IO[List[ExchangeRate[Currency]]] =
    responseMap
      .map {
        case (k, v) =>
          k.split("_").toList match {
            case from :: to :: Nil =>
              IO.fromTry((Currency(from), Currency(to)).mapN { (fromCurrency, toCurrency) =>
                ExchangeRate(From(fromCurrency), To(toCurrency), v)
              })
          }
      }
      .toList
      .sequence
}

object Graph {

  final case class Edge[A](from: From[A], to: To[A], weight: Double)
  type Graph[A] = Map[A, List[Edge[A]]]

  def apply[A](rates: List[ExchangeRate[A]]): Graph[A] =
    rates.groupBy(_.from).map {
      case (from, edges) => from.coerce -> edges.map(edge => Edge[A](from, edge.to, -Math.log10(edge.rate)))
    }

  def relaxGraph[A](source: A, graph: Graph[A]): (Map[A, Double], Map[From[A], To[A]]) = {
    // V -1 times
    val iterations = graph.keys.size - 1
    val allEdges = graph.values.flatten.toList
    val allVertices = graph.keys.map(v => v -> Double.PositiveInfinity).toMap

    relaxEdges(iterations, allEdges, allVertices.updated(source, 0d), Map.empty[From[A], To[A]])
  }

  /**
    * Check one more iteration, if a negative sum cycle is present, it will have changed the distance weight
    */
  def hasNegativeSumCycle[A](source: A, graph: Graph[A], distance: Map[A, Double]): Boolean = {
    val allEdges = graph.values.flatten.toList
    val allVertices = graph.keys.map(v => v -> Double.PositiveInfinity).toMap

    val relaxations = relaxEdges(1, allEdges, allVertices.updated(source, 0d), Map.empty[From[A], To[A]])
    relaxations._1.values.toSet.diff(distance.values.toSet).nonEmpty
  }

  @tailrec
  def relaxEdges[A](
      iterations: Int,
      edges: List[Edge[A]],
      distance: Map[A, Double],
      predecessor: Map[From[A], To[A]]
  ): (Map[A, Double], Map[From[A], To[A]]) =
    iterations match {
      case 0 => (distance, predecessor)
      case _ =>
        val (dist, pre) = edges.foldLeft((distance, predecessor)) {
          case ((accDistance, accPredecessor), edge) => relax(edge, accDistance, accPredecessor)
        }
        relaxEdges(iterations - 1, edges, dist, pre)
    }

  def relax[A](
      edge: Edge[A],
      distance: Map[A, Double],
      predecessor: Map[From[A], To[A]]
  ): (Map[A, Double], Map[From[A], To[A]]) = {
    val (u, v) = (edge.from, edge.to)
    val w = edge.weight
    (distance.get(u.coerce), distance.get(v.coerce)) match {
      case (Some(distanceU), Some(distanceV)) if distanceU + w < distanceV =>
        (distance + (v.coerce -> (distanceU + w)), predecessor + (u -> v))
      case (Some(distanceU), None) =>
        (distance + (v.coerce -> (distanceU + w)), predecessor + (u -> v))
      case _ => (distance, predecessor)
    }
  }

  def getCycleMessage[A](precedence: Map[From[A], To[A]], sourceCurrency: A, exchangeRates: List[ExchangeRate[A]]): String = {
    def printStep(p: Map[From[A], To[A]], from: A, exchangeRates: List[ExchangeRate[A]], accValue: Double, accString: String): String = {
      val nextCurrency = p.get(From(from))
      if (nextCurrency.isEmpty) accString
      else
        (for {
          next <- nextCurrency
          exchangeRate <- exchangeRates.find(rate => rate.from.coerce == from && rate.to == next).map(_.rate)
          newValue = accValue * exchangeRate
          outputString = accString + s"\n${next.coerce} @ $exchangeRate = $newValue"
          value = p.-(From(from))
        } yield {
          printStep(value, next.coerce, exchangeRates, newValue, outputString)
        }).getOrElse(accString)
    }

    printStep(precedence, sourceCurrency, exchangeRates, 1, s"Using the graph $precedence\nYou could convert 1 $sourceCurrency to: ")
  }

}

object Main extends App {
  implicit val cs: ContextShift[IO] = IO.contextShift(global)
  implicit val timer: Timer[IO] = IO.timer(global)

  val logger: Logger[IO] = ConsoleLogger[IO](Formatter.colorful, Level.Info)
  val httpClientResource: Resource[IO, Client[IO]] = BlazeClientBuilder[IO](global).resource

//  val responseMap = Map[String, Double](
//    "USD_EUR" -> 0.7779,
//    "USD_JPY" -> 102.4590,
//    "USD_BTC" -> 0.0083,
//    "EUR_USD" -> 1.2851,
//    "EUR_JPY" -> 131.7110,
//    "EUR_BTC" -> 0.01125,
//    "JPY_USD" -> 0.0098,
//    "JPY_EUR" -> 0.0075,
//    "JPY_BTC" -> 0.0000811,
//    "BTC_USD" -> 115.65,
//    "BTC_EUR" -> 88.8499,
//    "BTC_JPY" -> 12325.44
//  )
  def run: IO[_] =
    logger.info("Starting...") *>
      httpClientResource
        .use { client =>
          for {
            responseMap <- client.expect[Map[String, Double]]("https://fx.priceonomics.com/v1/rates/")
            _ <- logger.trace(s"Retrieved rates: $responseMap")
            exchangeRates <- ExchangeRate.decode(responseMap)
            _ <- logger.info(s"Decoded rates to: ${exchangeRates.show}")
            graph = Graph(exchangeRates)
            arbitrarySourceCurrency <- graph.headOption.map(_._1).liftTo[IO](EmptyGraph("No elements in graph"))
            (d, precedence) = Graph.relaxGraph(arbitrarySourceCurrency, graph)
            hasNegativeSumCycle = Graph.hasNegativeSumCycle(arbitrarySourceCurrency, graph, d)
            _ <- logger.info(s"Negative sum cycle exists?: $hasNegativeSumCycle")
            sourceCurrency <- precedence.headOption.liftTo[IO](EmptyRelaxations("No relaxations")).map(_._1)
            _ <- logger.info(Graph.getCycleMessage(precedence, sourceCurrency.coerce, exchangeRates)).whenA(hasNegativeSumCycle)
          } yield exchangeRates
        }
        .handleErrorWith(e => logger.error(e.getMessage).void)

  run.unsafeRunSync()
}
