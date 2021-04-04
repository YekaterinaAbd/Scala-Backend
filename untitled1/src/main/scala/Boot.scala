import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext

object Boot extends App {

  implicit val log: Logger = LoggerFactory.getLogger(getClass)

  val rootBehavior = Behaviors.setup[Nothing] { context =>

//    implicit val ec = context.executionContext
//    implicit val sys = context.system

//    val todos = new InMemoryTodoRepository(mockTodos)
    val router = new MyRouter()(context.system,  context.executionContext)

    MyServer.startHttpServer(router.route)(context.system,  context.executionContext)
    Behaviors.empty
  }
  val system = ActorSystem[Nothing](rootBehavior, "HelloAkkaHttpServer")
}