import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import calculator.CalculatorRepositoryImpl
import org.slf4j.{Logger, LoggerFactory}
import server.BootServer
import todo.{InMemoryToDoRepository, Todo}
import url.{LocalUrlRepository, Url}

object Boot extends App {

  implicit val log: Logger = LoggerFactory.getLogger(getClass)

  val rootBehavior = Behaviors.setup[Nothing] { context =>

    implicit val ec = context.executionContext
    implicit val sys = context.system

    val mockTodos: Seq[Todo] = Seq(
      Todo("1", "title1", "description1", done = true),
      Todo("2", "title2", "description2", done = false)
    )

    val mockUrls: Seq[Url] = Seq(
      Url("1", "hfkdjha", "fhdsjfka"),
      Url("2", "hfkdjha", "fhdsjfka")
    )

    val todos = new InMemoryToDoRepository(mockTodos)
    val calculator = new CalculatorRepositoryImpl()
    val urls = new LocalUrlRepository(mockUrls)

    val router = new ToDoRouter(todos, calculator, urls)

    BootServer.startHttpServer(router.route)
    Behaviors.empty
  }
  val system = ActorSystem[Nothing](rootBehavior, "HelloAkkaHttpServer")
}