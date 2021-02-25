package Week5

object Solution2 {

  private def getMinMax(array: Array[Int]): (Int, Int) = {
    var max = array(0)
    var min = array(0)
    for (value <- array) {
      max = Math.max(value, max)
      min = Math.min(value, min)
    }
    (min, max)
  }

  private def arraySumWithoutExtremes(array: Array[Int]): Double = {
    var sum = 0
    val (min, max) = getMinMax(array)
    for (value <- array) {
      if (value != min && value != max) sum += value
    }
    sum
  }

  private def averageWithoutExtremes(array: Array[Int]): Double = {
    array.length match {
      case 0 => 0.0d
      case 1 => array(0)
      case _ => arraySumWithoutExtremes(array) / (array.length - 2)
    }
  }

  def average(salary: Array[Int]): Double = {
    averageWithoutExtremes(salary)
  }
}

object AverageSalary2 extends App {

}
