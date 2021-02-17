package Week3

object KidsWithCandies1 {
  def kidsWithCandies(candies: Array[Int], extraCandies: Int): Array[Boolean] = {
    val maxCandies = candies.max
    val canHaveMaxCandies: Array[Boolean] = for (candy <- candies) yield candy + extraCandies >= maxCandies
    canHaveMaxCandies
  }
}
