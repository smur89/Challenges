package com.smur89.zip

import org.scalatest.{Matchers, WordSpec}

class SolutionTest extends WordSpec with Matchers {
  "Solution" should {
    "example 1" in {
      Solution.solution(12, 56) shouldBe 1526
    }
    "example 2" in {
      Solution.solution(56, 12) shouldBe 5162
    }
    "example 3" in {
      Solution.solution(12345, 678) shouldBe 16273845
    }
    "example 4" in {
      Solution.solution(123, 67890) shouldBe 16273890
    }
    "example 5" in {
      Solution.solution(1234, 0) shouldBe 10234
    }
    "upper bound" in {
      Solution.solution(1234, 67890) shouldBe -1
    }
  }
}
