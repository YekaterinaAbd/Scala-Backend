package Week3

import scala.collection.mutable.ArrayBuffer

object DecompressList5 {
  def decompressRLElist(nums: Array[Int]): Array[Int] = {
    var answer = ArrayBuffer[Int]()
    for (i <- nums.indices by 2) {
      //nums(i) times with value nums(i+1)
      answer ++= ArrayBuffer.fill(nums(i))(nums(i + 1))
    }
    answer.toArray
  }
}
