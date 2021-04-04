import scala.concurrent.{ExecutionContext, Future}

trait TodoRepository {
  def all(): Future[Seq[ToDo]]

  def done(): Future[Seq[ToDo]]

  def pending(): Future[Seq[ToDo]]
}

class InMemoryToDoRepository(initialTodos: Seq[ToDo] = Seq.empty)(implicit ec: ExecutionContext) extends TodoRepository {

  private var todos: Vector[ToDo] = initialTodos.toVector

  override def all(): Future[Seq[ToDo]] = Future.successful(todos)

  override def done(): Future[Seq[ToDo]] = Future.successful(todos.filter(_.done))

  override def pending(): Future[Seq[ToDo]] = Future.successful(todos.filterNot(_.done))
}

