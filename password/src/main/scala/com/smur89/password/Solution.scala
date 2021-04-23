package com.smur89.password

object Solution {
  def solution(s: String): Int = {
    def findPossiblePasswords(password: String, longestPasswordSize: Int): Int = {
      val (possiblePassword, tail) = password.span(isValidPasswordChar)

      (possiblePassword, tail) match {
        case (p, t) if t.isEmpty => sizeIfValid(p).max(longestPasswordSize)
        case (p, t)              => findPossiblePasswords(t.tail, sizeIfValid(p).max(longestPasswordSize))
      }
    }

    findPossiblePasswords(s, -1)
  }

  def sizeIfValid(str: String): Int = {
    if (str.toLowerCase != str) str.length else -1
  }

  def isValidPasswordChar(ch: Char): Boolean = {
    !ch.isDigit
  }
}
