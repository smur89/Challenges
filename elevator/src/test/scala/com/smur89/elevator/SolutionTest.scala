package com.smur89.elevator

import org.scalatest.{Matchers, WordSpec}

class SolutionTest extends WordSpec with Matchers {

  "Solution" should {
    "work for example 1" in {
      val a = Array(60, 80, 40)
      val b = Array(2, 3, 5)
      val m = 5
      val x = 2
      val y = 200
      val result = Solution.solution(a, b, m, x, y)
      result shouldBe 5
    }

    "work for example 2" in {
      val a = Array(40, 40, 100, 80, 20)
      val b = Array(3, 3, 2, 2, 3)
      val m = 3
      val x = 5
      val y = 200
      Solution.solution(a, b, m, x, y) shouldBe 6
    }

    "discard remaining elements for uneven arrays" in {
      val a = Array(40, 40, 100)
      val b = Array(3, 3, 2, 2, 3)
      val m = 3
      val x = 5
      val y = 200
      Solution.solution(a, b, m, x, y) shouldBe 3
    }

    "ignore stops greater than num floors (m)" in {
      val a = Array(60, 80, 80, 40)
      val b = Array(2, 10, 3, 5)
      val m = 5
      val x = 2
      val y = 200
      val result = Solution.solution(a, b, m, x, y)
      result shouldBe 5
    }
  }

}
