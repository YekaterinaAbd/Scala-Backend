package Week3

object KWeakestRows7 extends App {
  def kWeakestRows(mat: Array[Array[Int]], k: Int): Array[Int] = {

    //ofDim = of particular size
    val counts = Array.ofDim[Int](mat.length)

    // i in 0..mat.size
    // j in 0..mat.size

    for (i <- mat(0).indices; j <- mat.indices) {
      counts(i) += mat(i)(j)
    }

    //(2, 0), (4, 1), (1, 2), (2, 3), (5, 4)
    //sort by first value
    //take first k values
    //convert to simple array with required indexes
    counts.zipWithIndex.sortBy(count => count._1).take(k).map(_._2)
  }

  val mat = Array(Array(1, 1, 0, 0, 0), Array(1, 1, 1, 1, 0), Array(1, 0, 0, 0, 0), Array(1, 1, 0, 0, 0), Array(1, 1, 1, 1, 1))
  println(kWeakestRows(mat, 3).mkString("Array(", ", ", ")"))
}
