import akka.actor.typed.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.{Directives, Route}

import scala.concurrent.ExecutionContext

trait Router {
  def route: Route
}

class ToDoRouter(todoRepository: TodoRepository, calculatorRepository: CalculatorRepository)(implicit system: ActorSystem[_], ex: ExecutionContext) extends Router with Directives {

  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import io.circe.generic.auto._

  override def route = concat(
    path("ping") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "pong"))
      }
    },
    path("todos") {
      pathEndOrSingleSlash {
        get {
          complete(todoRepository.all())
        }
      }
    },
    path("calculate") {
      get {
        parameters('equation.as[String]) { equation =>
          complete {
            calculatorRepository.calculate(equation)
          }
        }
      }
    }
  )
}
