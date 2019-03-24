package com.smur89.password

import org.scalatest.{Matchers, WordSpec}

class SolutionTest extends WordSpec with Matchers {

  "Solution" must {
    "a0Ba" in {
      val password = "a0Ba"
      Solution.solution(password) shouldBe 2 // Ba
    }

    "a0bb" in {
      val password = "a0bb"
      Solution.solution(password) shouldBe -1 // None
    }
  }
}
