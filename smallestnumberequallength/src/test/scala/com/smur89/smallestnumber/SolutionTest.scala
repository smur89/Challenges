package com.smur89.smallestnumber

import org.scalatest.{Matchers, WordSpec}

class SolutionTest extends WordSpec with Matchers {

  "125" in {
    Solution.solution(125) shouldBe 100
  }

  "-125" in {
    Solution.solution(-125) shouldBe -100
  }

  "0" in {
    Solution.solution(0) shouldBe 0
  }
  "1" in {
    Solution.solution(1) shouldBe 0
  }

  "-1" in {
    Solution.solution(-1) shouldBe 0
  }

  "-0" in {
    Solution.solution(-0) shouldBe 0
  }

  "Max" in {
    Solution.solution(Integer.MAX_VALUE) shouldBe 1000000000
  }


  "Min" in {
    Solution.solution(Integer.MIN_VALUE) shouldBe -1000000000
  }

  "billion" in {
    Solution.solution(10^9) shouldBe 0
  }
}
