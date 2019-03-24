package com.smur89.elevator

object Solution {
  val ReturnToGroundFloor = 1

  /**
    * @param a persons weight
    * @param b persons target floor
    * @param m number of floors
    * @param x elevator capacity
    * @param y elevator weight limit
    */
  def solution(a: Array[Int], b: Array[Int], m: Int, x: Int, y: Int): Int = {
    val queueOfPeople = a.toList.zip(b.toList)

    def carryLoadOfPeopleAndReturn(it: List[(Int, Int)], numStopsAcc: Int): Int = it match {
      case Nil => numStopsAcc
      case queue =>
        val destinations = queue.scanLeft(0, Seq.empty[(Int, Int)]) {
          case ((sum, combo), curr) if curr._2 > m => (sum, combo)
          case ((sum, combo), curr) => (sum + curr._1, combo :+ curr)
        }.takeWhile(weightAccumulatedList => weightAccumulatedList._1 < y && weightAccumulatedList._2.size <= x)
          .flatMap(_._2.lastOption.map(_._2))
        val remainingQueue = queue.slice(destinations.size, queue.size)
        carryLoadOfPeopleAndReturn(remainingQueue, numStopsAcc + destinations.distinct.size + ReturnToGroundFloor)
    }

    carryLoadOfPeopleAndReturn(queueOfPeople, 0)
  }
}
