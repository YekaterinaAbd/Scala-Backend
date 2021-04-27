package todo

import akka.http.scaladsl.server.{Directives, Route}
import validator.{CreateTodoValidator, ErrorDirectives, UpdateTodoValidator, ValidatorDirectives}

case class TodoRoutes(repository: TodoRepository) extends Directives
  with ErrorDirectives
  with ValidatorDirectives {

  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import io.circe.generic.auto._

  val routes: Route = concat (
    path("todos") {
      pathEndOrSingleSlash {
        get {
          handleWithGeneric(repository.all()) { todos =>
            complete(todos)
          }
        } ~
          post {
            entity(as[CreateTodo]) { createTodo => {
              validateWith(CreateTodoValidator)(createTodo) {
                handle(repository.create(createTodo)) { todo => complete(todo) }
              }
            }
            }
          } ~
          put {
            entity(as[UpdateTodo]) { updateTodo => {
              validateWith(UpdateTodoValidator)(updateTodo) {
                handle(repository.update(updateTodo)) { todo =>
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
              handleWithGeneric(repository.all()) { todos =>
                complete(todos)
              }
            }
          }
        },
        path("done") {
          pathEndOrSingleSlash {
            get {
              handleWithGeneric(repository.done()) { todos =>
                complete(todos)
              }
            }
          }
        },
        path("pending") {
          pathEndOrSingleSlash {
            get {
              handleWithGeneric(repository.pending()) { todos =>
                complete(todos)
              }
            }
          }
        }
      )
    },
  )

}
