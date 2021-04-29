package url

import shade.memcached._

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

class LocalUrlRepository(initialUrls: Seq[Url] = Seq.empty)(implicit ec: ExecutionContext) extends UrlRepository {

  private var urls: Vector[Url] = initialUrls.toVector

  override def save(createUrl: CreateUrl): Future[String] = {
    val url = UrlUtils.createUrl(createUrl)
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

class MemcachedUrlRepository(initialUrls: Seq[Url] = Seq.empty)(implicit ec: ExecutionContext) extends UrlRepository {

  private var urls: Vector[Url] = initialUrls.toVector

  val memcached = Memcached(Configuration("127.0.0.1:11211"))(ec)

  override def save(createUrl: CreateUrl): Future[String] = {
    val url = UrlUtils.createUrl(createUrl)
    urls = urls :+ url
    memcached.set(url.shortUrl, url.originalUrl, 1.minute)
    Future.successful(UrlUtils.getShortUrlLink(url.shortUrl))
  }

  override def get(shortUrl: String): Future[String] = {
    memcached.awaitGet[String](shortUrl) match {
      case Some(value) => Future.successful(value)
      case None =>
        urls.find(_.shortUrl == shortUrl) match {
        case None => Future.failed(new Throwable())
        case Some(url) => Future.successful(url.originalUrl)
      }
    }
  }

  override def delete(shortUrl: String): Unit = {
    memcached.delete(shortUrl)
    urls = urls.filter(_.shortUrl != shortUrl)
  }

  override def getAll(): Future[Seq[Url]] = Future.successful(urls)
}