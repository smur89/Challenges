package com.smur89.smallestnumber

import scala.util.{Failure, Success, Try}

object Solution {
  def solution(n: Int): Int = {
    Math.abs(n).toString.length match {
      case length if length > 1 =>
        val mostSignificant = if (n > 0) "1" else "-1"
         Try((1 until length).map(_ => 0).mkString(mostSignificant, "", "").toInt) match {
           case Success(i) => i
           case Failure(_) => "-1000000000".toInt // Handle Integer.MIN_VALUE
         }
      case _ => 0
    }
  }
}
