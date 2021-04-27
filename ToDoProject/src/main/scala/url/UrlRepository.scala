package url

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

trait UrlRepository {
  def save(createUrl: CreateUrl): Future[String]

  def get(shortUrl: String): Future[String]

  def delete(id: String)

  def getAll(): Future[Seq[Url]]
}

class LocalUrlRepository(initialUrls: Seq[Url] = Seq.empty)(implicit ec: ExecutionContext) extends UrlRepository {

  private var urls: Vector[Url] = initialUrls.toVector

  override def save(createUrl: CreateUrl): Future[String] = {
    val id = UUID.randomUUID().toString
    val url = Url(
      id = id,
      shortUrl = UrlUtils.encodeMurMur(id + createUrl.original),
      originalUrl = createUrl.original
    )
    urls = urls :+ url
    Future.successful(UrlUtils.getShortUrlLink(url.shortUrl))
  }

  override def get(shortUrl: String): Future[String] = {
    urls.find(_.shortUrl == shortUrl) match {
      case None => Future.failed(new Throwable())
      case Some(url) => Future.successful(url.originalUrl)
    }
  }

  override def delete(shortUrl: String): Unit = {
    urls = urls.filter(_.shortUrl != shortUrl)
  }

  override def getAll(): Future[Seq[Url]] = Future.successful(urls)
}

class RedisDataStore
