package calculator

import scala.util.{Failure, Success, Try}

object Calculator {

  def calculate(equation: String): String = {

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
        else answer = computeAnswer(answer.toInt, current.toInt, operation)
        current = ""
      }
      if (isOperation(equation(index))) {
        operation = equation(index)
      }
      index += 1
    }
    answer
  }

  private def computeAnswer(firstNum: Int, secondNum: Int, operation: Char): String = {
    makeOperation(firstNum.toInt, secondNum.toInt, operation) match {
      case Success(n) => n.toString
      case Failure(e) => "Error: " + e.getLocalizedMessage
    }
  }

  private def makeOperation(firstNum: Int, secondNum: Int, operation: Char): Try[Int] = {
    try {
      operation match {
        case 'p' => Success(firstNum + secondNum)
        case '-' => Success(firstNum - secondNum)
        case '*' => Success(firstNum * secondNum)
        case '/' => Success(firstNum / secondNum)
        case ' ' => Success(firstNum)
      }
    } catch {
      case e: ArithmeticException => Failure(e)
    }
  }

  private def isOperation(symbol: Char): Boolean = {
    val operations = List('p', '-', '*', '/')
    operations.contains(symbol)
  }
}
