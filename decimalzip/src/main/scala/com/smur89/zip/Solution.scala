package com.smur89.zip

object Solution {
  def solution(a: Int, b: Int): Int = {
    Math
      .abs(a)
      .toString
      .map(_.asDigit)
      .toList
      .zipAll(Math.abs(b).toString.map(_.asDigit).toList, "", "")
      .flatMap(tuple => Seq(tuple._1, tuple._2))
      .mkString("")
      .toInt match {
      case num if num > 100000000 => -1
      case num                    => num
    }
  }
}
