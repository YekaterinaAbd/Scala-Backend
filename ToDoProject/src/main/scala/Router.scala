import akka.actor.typed.ActorSystem
import akka.http.scaladsl.server.{Directives, Route}
import calculator.{CalculatorRepository, CalculatorRoutes}
import todo.{TodoRepository, TodoRoutes}
import url.{UrlRepository, UrlRoutes}
import validator.{ErrorDirectives, ValidatorDirectives}

import scala.concurrent.ExecutionContext

trait Router {
  def route: Route
}

class ToDoRouter(todoRepository: TodoRepository, calculatorRepository: CalculatorRepository, urlRepository: UrlRepository)
                (implicit system: ActorSystem[_], ex: ExecutionContext)
  extends Router with Directives with ErrorDirectives with ValidatorDirectives {

  override def route = concat(
    TodoRoutes(todoRepository).routes,
    CalculatorRoutes(calculatorRepository).routes,
    UrlRoutes(urlRepository).routes
  )

}
