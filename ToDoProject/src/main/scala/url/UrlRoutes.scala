package url

import akka.http.scaladsl.model.{StatusCodes, Uri}
import akka.http.scaladsl.server.{Directives, Route}
import validator.{CreateShortUrlValidator, ErrorDirectives, ValidatorDirectives}

case class UrlRoutes(repository: UrlRepository) extends Directives
  with ErrorDirectives
  with ValidatorDirectives {

  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import io.circe.generic.auto._

  val routes: Route = concat(
    path("original_url") {
      get {
        handleWithGeneric(repository.getAll()) { urls =>
          complete(urls)
        }
      } ~
        post {
          entity(as[CreateUrl]) { createShortUrl => {
            validateWith(CreateShortUrlValidator)(createShortUrl) {
              handle(repository.save(createShortUrl)) { url => complete(url) }
            }
          }
          }
        }
    },
    path(Segment) { url =>
      get {
        handleWithGeneric(repository.get(url)) { url2 =>
          val uri = Uri.apply(url2)
          redirect(uri, StatusCodes.PermanentRedirect)
        }
      }
    }
  )
}
