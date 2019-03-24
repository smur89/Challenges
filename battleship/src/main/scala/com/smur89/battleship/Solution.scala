package com.smur89.battleship

object Solution {
  val hitsToTry = List(Point(1, 2), Point(1, 3), Point(1, 4))
  val ship = Ship(Point(1, 2), 3, Down)
  val boardSize = 10
  val ships = checkShipsInsideBoard(List(ship), boardSize)
  val initGame = GameState(boardSize, List.empty, List(ship))

  gameLoop(initGame, hitsToTry)

  def gameLoop(gamesState: GameState, hits: List[Point]): Boolean = {
    (gamesState.allShipsSunk, hits) match {
      case (true, _) => true
      case (_, hit :: nextHits) => if (gamesState.hits.contains(hit)) {
        println("Already tried here.")
        gameLoop(gamesState, nextHits)
      } else {
        val newGameState = gamesState.copy(hits = gamesState.hits :+ hit)
        gameLoop(newGameState, nextHits)
      }
    }
  }

  def checkShipsInsideBoard(ships: List[Ship], boardSize: Int): Boolean =
    ships.forall(ship =>
      ship.direction match {
        case Right => ship.point.x + ship.length < boardSize
        case Down => ship.point.y + ship.length < boardSize
      })

  def hitShip(ships: List[Ship], hit: Point): Boolean = {
    ships.exists(ship => ship.allPoints.contains(hit))
  }
}

case class GameState(boardSize: Int, hits: List[Point], ships: List[Ship]) {
  def allShipsSunk: Boolean = ships.flatMap(_.allPoints).forall(hits.contains)
}

case class Ship(point: Point,
                length: Int,
                direction: Direction) {
  def allPoints: List[Point] = {
    (0 to length).map(i => direction match {
      case Right => Point(point.x + i, point.y)
      case Down => Point(point.x, point.y + i)
    }).toList
  }

  def isSunk(hits: List[Point]): Boolean = {
    allPoints.forall(hits.contains)
  }
}

case class Point(x: Int, y: Int)

sealed trait Direction

case object Right extends Direction

case object Down extends Direction
