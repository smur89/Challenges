package com.smur89.squareroots

object Solution {
  def solution(a: Int, b: Int): Int = {
    def checkSquares(num: Int, count: Int): Int = {
      val d = Math.sqrt(num)
      if (Math.floor(d) == d) {
        checkSquares(d.toInt, count + 1)
      } else {
        count
      }
    }
    (a to b).foldLeft(0) { (acc, i) =>
      checkSquares(i, 0).max(acc)
    }
  }

}
