package Calculator

import akka.actor.typed.{ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import scala.io.StdIn.readLine

object CalculatorMain {

  final case class ReadExpression(expression: String)

  def apply(): Behavior[ReadExpression] =
    Behaviors.setup { context =>
      val calculator = context.spawn(Calculator(), "calculator")
      Behaviors.receiveMessage { message =>
        val calculatorOutput = context.spawn(CalculatorInteractive(), message.expression)
        calculator ! Calculator.Expression(message.expression, calculatorOutput)
        Behaviors.same
      }
    }

    def main(args: Array[String]): Unit = {
      val system: ActorSystem[ReadExpression] = ActorSystem(CalculatorMain(), "calculatorDemo")
      system ! ReadExpression(readLine())
      system ! ReadExpression("18-2*4+3*2-5=")
    }
}
