import akka.http.scaladsl.server.{Directive1, Directives}

import scala.concurrent.Future
import scala.util.{Failure, Success}

trait TodoDirectives extends Directives {

  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._

  def handleThrowable[T](f: Future[T])(e: Throwable => ApiError): Directive1[T] = onComplete(f) flatMap {
    case Success(t) =>
      provide(t)
    case Failure(error) =>
      val apiError = e(error)
      complete(apiError.statusCode, apiError.message)
  }

  def handle[T](f: Future[T]): Directive1[T] = onComplete(f) flatMap {
    case Success(t) =>
      provide(t)
    case Failure(error) =>
      val apiError = if (error.getMessage == "Title already exists.") ApiError.duplicateTitleField
      else ApiError.generic
      complete(apiError.statusCode, apiError.message)
  }

  //  def handleDuplicateTitle[T](f: Future[T]): Directive1[T] =
  //    handle[T](f)(_ => ApiError.duplicateTitleField)

  def handleWithGeneric[T](f: Future[T]): Directive1[T] =
    handleThrowable[T](f)(_ => ApiError.generic)

}
