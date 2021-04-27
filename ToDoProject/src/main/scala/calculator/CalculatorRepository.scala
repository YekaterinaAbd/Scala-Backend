package calculator

import scala.concurrent.ExecutionContext

trait CalculatorRepository {
  def calculate(equation: String): String
}

class CalculatorRepositoryImpl(implicit ec: ExecutionContext) extends CalculatorRepository {
  override def calculate(equation: String): String = Calculator.calculate(equation)
}
