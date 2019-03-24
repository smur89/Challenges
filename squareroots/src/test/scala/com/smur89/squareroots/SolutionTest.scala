package com.smur89.squareroots

import org.scalatest.{Matchers, WordSpec}

class SolutionTest extends WordSpec with Matchers {

  "2" in {
    Solution.solution(10, 20) shouldBe 2
  }

  "600" in {
    Solution.solution(6000, 7000) shouldBe 3
  }

  "minus" in {
    Solution.solution(-6000, -7000) shouldBe 3
  }
}
