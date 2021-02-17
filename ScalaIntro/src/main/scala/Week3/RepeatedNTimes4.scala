package Week3

object RepeatedNTimes4 extends App {
  def repeatedNTimes(A: Array[Int]): Int = {
    val map = collection.mutable.Map.empty[Int, Int]
    var answer: Int = -1
    for (a <- A) {
      if (map isDefinedAt a) map(a) += 1
      else map += (a -> 1)
    }
    for ((k, v) <- map) {
      if (v > 1) {
        answer = k
      }
    }
    answer
  }

  def repeatedNTimesSimple(A: Array[Int]): Int = {
    A.groupBy(identity).valuesIterator.maxBy(_.length).head
  }

  val array = Array(1, 2, 3, 1, 1)
  println(repeatedNTimes(array))
}
