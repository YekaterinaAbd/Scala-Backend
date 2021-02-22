import scala.io.StdIn.readLine

object Main extends App {
  println(Console.BLUE + "\nPlease, write your equation using operations (+, -, *, /) and put equals sing in the end (=)\n")
  val equation = readLine()
  val answer = Calculator.calculate(equation)
  println(Console.RED + answer)
}
