package Week2

object Main extends App {

  def printHello(): Unit = {
    println("Hello World")
  }

  def printVariables(): Unit = {
    val x = 1
    var y = 1
    y += 1
    val s: String = "Hello World"
    val s2 = "Hello World"
    println(x, y, s, s2)
  }

  def printCondition(number: Int): Unit = {
    if (number > 0) println("positive number")
    else if (number < 0) println("negative number")
    else println("zero")
  }

  def printSimpleCondition(number: Int): Unit = {
    val answer = if (number > 0) "positive" else if (number < 0) "negative" else "zero"
    println(answer)
  }

  def printMatch(number: Int): Unit = {
    val answer = number match {
      case 1 => "one"
      case 2 => "two"
      case _ => "something else"
    }
    println(answer)
  }

  def getClassAsString(clazz: Any): String = clazz match {
    case s: String => "string"
    case i: Int => "integer"
    case f: Float => "float"
    case l: List[_] => "list"
    case _ => "something else"
  }

  def divideNumbers(x: Int, y: Int): Unit = {
    try {
      val ans: Int = x / y
      println(ans)
    } catch {
      case ae: ArithmeticException => println(ae)
    }
  }

  def printForLoop(list: List[Int]): Unit = {
    for (item <- list) print(item)
    println()
    for (item <- 0 to 2) print(item)
    println()
    for (item <- 0 to 10 by 3) print(item)
    println()
  }

  def printYieldResult(list: List[Int]): Unit = {
    val answer = for (item <- list) yield item + 5
    println(answer)
  }

  def printComplexYieldResult(list: List[String]): Unit = {
    val answer = for (
      item <- list
      if item.length > 4
    ) yield item.length
    println(answer)
  }

  def printWhileLoopResult(counter: Int): Unit = {
    var localCounter = counter
    while (localCounter > 0) {
      println(localCounter + " seconds left")
      localCounter -= 1
    }
  }

  def printDoWhileResult(): Unit = {
    var counter = 5
    do {
      printHello()
      counter -= 1
    } while (counter > 0)
  }

  printHello()
  printVariables()
  printCondition(5)
  printSimpleCondition(-5)
  printMatch(0)
  println(getClassAsString("Hello World"))
  divideNumbers(7, 0)
  val list = List(1, 2, 3, 4, 5)
  printForLoop(list)
  printYieldResult(list)
  val cats = List("domestic", "lion", "jaguar", "puma")
  printComplexYieldResult(cats)
  printWhileLoopResult(5)
  printDoWhileResult()
}

