package Week3

import scala.collection.mutable.ListBuffer

object BuildArray10 extends App {
  def buildArray(target: Array[Int], n: Int): List[String] = {
    var index = 0
    var number = 1
    val output = ListBuffer[String]()

    // 2 3 4 6 <- target
    // 1 2 3 4 5 6 <- list
    while (index < target.length) {
      output += "Push"
      if (number == target(index)) {
        index += 1
      } else output += "Pop"
      number += 1
    }
    output.toList
  }
}
