import scala.util.{Failure, Success, Try}
import scala.io.StdIn.readLine

object Main extends App {

  println(Console.BLUE + "\nPlease, write your equation using operations (+, -, *, /) and put equals sing in the end (=)\n")
  val equation = readLine()
  val answer = calculateAnswer(equation)
  println(Console.RED + answer)

  def calculateAnswer(equation: String): String = {
    var answer = ""
    var current = ""
    var operation = ' '

    val end = equation.length
    var index = 0

    while (index < end) {
      if (equation(index).isDigit) {
        while (equation(index).isDigit) {
          current += equation(index)
          index += 1
        }
        if (answer.isEmpty) answer = current
        else answer = compute(answer.toInt, current.toInt, operation)
        current = ""
      }
      if (isOperation(equation(index))) {
        operation = equation(index)
      }
      index += 1
    }
    answer
  }

  def compute(firstNum: Int, secondNum: Int, operation: Char): String = {
    makeOperation(firstNum.toInt, secondNum.toInt, operation) match {
      case Success(n) => n.toString
      case Failure(e) => "Error: " + e.getLocalizedMessage
    }
  }

  def makeOperation(firstNum: Int, secondNum: Int, operation: Char): Try[Int] = {
    try {
      operation match {
        case '+' => Success(firstNum + secondNum)
        case '-' => Success(firstNum - secondNum)
        case '*' => Success(firstNum * secondNum)
        case '/' => Success(firstNum / secondNum)
        case ' ' => Success(firstNum)
      }
    } catch {
      case e: ArithmeticException => Failure(e)
    }
  }

  def isOperation(symbol: Char): Boolean = {
    val operations = List('+', '-', '*', '/')
    operations.contains(symbol)
  }
}
