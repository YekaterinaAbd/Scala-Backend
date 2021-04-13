import akka.actor.typed.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.{Directives, Route}

import scala.concurrent.ExecutionContext

trait Router {
  def route: Route
}

class ToDoRouter(todoRepository: TodoRepository, calculatorRepository: CalculatorRepository)(implicit system: ActorSystem[_], ex: ExecutionContext)
  extends Router
    with Directives
    with TodoDirectives
    with ValidatorDirectives {

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
          handleWithGeneric(todoRepository.all()) { todos =>
            complete(todos)
          }
        } ~
          post {
            entity(as[CreateTodo]) { createTodo => {
              validateWith(CreateTodoValidator)(createTodo) {
                handle(todoRepository.create(createTodo)) { todo => complete(todo) }
              }
            }
            }
          } ~
          put {
            entity(as[UpdateTodo]) { updateTodo => {
              validateWith(UpdateTodoValidator)(updateTodo) {
                handleWithGeneric(todoRepository.update(updateTodo)) { todo =>
                  complete(todo)
                }
              }
            }
            }
          }
      }
    },
    pathPrefix("todos") {
      concat(
        path("all") {
          pathEndOrSingleSlash {
            get {
              handleWithGeneric(todoRepository.all()) { todos =>
                complete(todos)
              }
            }
          }
        },
        path("done") {
          pathEndOrSingleSlash {
            get {
              handleWithGeneric(todoRepository.done()) { todos =>
                complete(todos)
              }
            }
          }
        },
        path("pending") {
          pathEndOrSingleSlash {
            get {
              handleWithGeneric(todoRepository.pending()) { todos =>
                complete(todos)
              }
            }
          }
        }
      )
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
