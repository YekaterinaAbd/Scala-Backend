package Calculator

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object CalculatorInteractive {
  def apply(): Behavior[Calculator.ExpressionResult] = {
    outputExpression()
  }

  private def outputExpression(): Behavior[Calculator.ExpressionResult] =
    Behaviors.receive { (context, message) =>
      context.log.info(s"Expression result: ${message.result} for ${message.expression}")
      Behaviors.stopped
    }
}
