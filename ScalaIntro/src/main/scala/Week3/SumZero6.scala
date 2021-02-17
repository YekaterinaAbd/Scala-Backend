package Week3

import scala.collection.mutable.ArrayBuffer

object SumZero6 extends App {
  def sumZero(n: Int): Array[Int] = {
    var array = ArrayBuffer.empty[Int]
    if(n % 2 != 0) array += 0
    var counter = n / 2
    while(counter > 0){
      array += counter
      array += (counter - 2*counter)
      counter -= 1
    }
    array.toArray
  }
  println(sumZero(4).mkString("Array(", ", ", ")"))
}
