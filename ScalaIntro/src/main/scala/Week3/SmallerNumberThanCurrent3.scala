package Week3

object SmallerNumberThanCurrent3 extends App {
  def smallerNumbersThanCurrent(nums: Array[Int]): Array[Int] = {
    val answer = for (num <- nums) yield nums.count(_ < num)
    answer
  }
}
