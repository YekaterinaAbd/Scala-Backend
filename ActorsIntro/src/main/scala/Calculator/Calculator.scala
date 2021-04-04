package Calculator

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorRef, Behavior}

import scala.util.{Failure, Success, Try}

object Calculator {

  final case class Expression(body: String, replyTo: ActorRef[ExpressionResult])
  final case class ExpressionResult(result: String, expression: String, from: ActorRef[Expression])

  def apply(): Behavior[Expression] = Behaviors.receive { (context, message) =>
    context.log.info(s"Got expression: ${message.body}")
    message.replyTo ! ExpressionResult(calculate(message.body), message.body, context.self)
    Behaviors.same
  }

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

  private def isOperation(symbol: Char): Boolean = {
    val operations = List('+', '-', '*', '/')
    operations.contains(symbol)
  }
}