package url

import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala._
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.model.Filters._
import url.Helpers.GenericObservable

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

class DatabaseUrlRepository(implicit ec: ExecutionContext) extends UrlRepository {

  val codecRegistry = fromRegistries(fromProviders(classOf[Url]), DEFAULT_CODEC_REGISTRY )

  val mongoClient: MongoClient = MongoClient()
  val database: MongoDatabase = mongoClient.getDatabase("url_db").withCodecRegistry(codecRegistry)
  val collection: MongoCollection[Url] = database.getCollection("urls")

  override def save(createUrl: CreateUrl): Future[String] = {
    val url = UrlUtils.createUrl(createUrl)
    collection.insertOne(url).results()
    Future.successful(UrlUtils.getShortUrlLink(url.shortUrl))
  }

  override def get(shortUrl: String): Future[String] = {
    collection.find(equal("shortUrl", shortUrl)).headResult() match {
      case x: Url => Future.successful(x.originalUrl)
      case _ => Future.failed(new Throwable())
    }
  }

  override def delete(id: String): Unit = {
    // collection.deleteOne(eq("id", id))
  }

  override def getAll(): Future[Seq[Url]] = {
    collection.find().toFuture()
  }
}


