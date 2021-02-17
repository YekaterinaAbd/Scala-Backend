package Week3

import scala.collection.mutable.ListBuffer

class CustomFunction {
  // Returns f(x, y) for any given positive integers x and y.
  // Note that f(x, y) is increasing with respect to both x and y.
  // i.e. f(x, y) < f(x + 1, y), f(x, y) < f(x, y + 1)
  def f(x: Int, y: Int): Int = {
    null
  }
}

object PositiveIntegerSolution8 {
  def findSolution(customFunction: CustomFunction, z: Int): List[List[Int]] = {
    val start = 1
    val end = 1000
    var l = start
    var r = end
    var list = new ListBuffer[List[Int]]()
    while (r >= start && l <= end) {
      val answer = customFunction.f(l, r)
      if (answer == z) {
        list += List(l, r)
        l += 1
        r -= 1
      } else if (answer < z) {
        l += 1
      } else r -= 1
    }
    list.toList
  }
}
