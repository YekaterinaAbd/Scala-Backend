import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import org.slf4j.{Logger, LoggerFactory}

object Boot extends App {

  implicit val log: Logger = LoggerFactory.getLogger(getClass)

  val rootBehavior = Behaviors.setup[Nothing] { context =>

    implicit val ec = context.executionContext
    implicit val sys = context.system

    val mockTodos: Seq[ToDo] = Seq(
      ToDo("1", "title1", "description1", done = true),
      ToDo("2", "title2", "description2", done = false)
    )

    val todos = new InMemoryToDoRepository(mockTodos)
    val calculator = new CalculatorRepositoryImpl()

    val router = new ToDoRouter(todos, calculator)

    ToDoServer.startHttpServer(router.route)
    Behaviors.empty
  }
  val system = ActorSystem[Nothing](rootBehavior, "HelloAkkaHttpServer")
}