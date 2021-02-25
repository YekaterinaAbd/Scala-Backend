package Week5

object Solution {
  private def decreaseValue(value: Int): Int = value - 1

  private def getLastValues(array: Array[Int], amount: Int): Array[Int] = {
    array.slice(array.length - amount, array.length)
  }

  def maxProduct(nums: Array[Int]): Int = {
   getLastValues(nums.sorted, 2).map(decreaseValue).product
  }
}

object MaxProduct1 extends App {

  val nums = Array(3, 4, 5, 2)
  println(Solution.maxProduct(nums))
}