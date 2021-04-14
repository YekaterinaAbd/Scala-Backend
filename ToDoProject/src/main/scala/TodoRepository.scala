import TodoRepository.{TitleAlreadyExists, TodoNotFound}

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

trait TodoRepository {
  def all(): Future[Seq[ToDo]]
  def done(): Future[Seq[ToDo]]
  def pending(): Future[Seq[ToDo]]

  def create(createTodo: CreateTodo): Future[ToDo]
  def update(updateTodo: UpdateTodo): Future[ToDo]
}

object TodoRepository {

  final case class TodoNotFound(id: String) extends Exception("")
  final case class TitleAlreadyExists(createTodo: CreateTodo) extends Exception("")

}

class InMemoryToDoRepository(initialTodos: Seq[ToDo] = Seq.empty)(implicit ec: ExecutionContext) extends TodoRepository {

  private var todos: Vector[ToDo] = initialTodos.toVector

  override def all(): Future[Seq[ToDo]] = Future.successful(todos)

  override def done(): Future[Seq[ToDo]] = Future.successful(todos.filter(_.done))

  override def pending(): Future[Seq[ToDo]] = Future.successful(todos.filterNot(_.done))

  override def create(createTodo: CreateTodo): Future[ToDo] = {
    todos.find(_.title == createTodo.title) match {
      case Some(_) =>
        Future.failed(TitleAlreadyExists(createTodo))
      case None =>
        Future.successful {
          val todo = ToDo(
            id = UUID.randomUUID().toString,
            title = createTodo.title,
            description = createTodo.description,
            done = false
          )
          todos = todos :+ todo
          todo
        }
    }
  }

  override def update(updateTodo: UpdateTodo): Future[ToDo] =
    todos.find(_.id == updateTodo.id) match {
      case None =>
        Future.failed(TodoNotFound(updateTodo.id))
      case Some(_) =>
        Future.successful {
          val currentTodo = todos.find(_.id == updateTodo.id).get
          val index = todos.indexOf(currentTodo)
          val newTodo = ToDo(
            id = currentTodo.id,
            title = updateTodo.title match {
              case Some(value) => value
              case None => currentTodo.title
            },
            description = updateTodo.description match {
              case Some(value) => value
              case None => currentTodo.description
            },
            done = updateTodo.done match {
              case Some(value) => value
              case None => currentTodo.done
            }
          )
          todos = todos.updated(index, newTodo)
          newTodo
        }
    }
}

