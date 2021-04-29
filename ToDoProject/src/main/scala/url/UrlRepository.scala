package url

import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala._
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.model.Filters._
import shade.memcached.{Configuration, Memcached}
import url.Helpers.GenericObservable
import url.UrlRepository.UrlDoesNotExist

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

trait UrlRepository {
  def save(createUrl: CreateUrl): Future[String]

  def get(shortUrl: String): Future[String]

  def delete(id: String)

  def getAll(): Future[Seq[Url]]
}

object UrlRepository {

  final case class UrlDoesNotExist(urlPath: String) extends Exception("")

}

class DatabaseUrlRepository(implicit ec: ExecutionContext) extends UrlRepository {

  private val codecRegistry = fromRegistries(fromProviders(classOf[Url]), DEFAULT_CODEC_REGISTRY)
  private val MONGO_URI: String = "mongodb+srv://root:admin@cluster0.a8eyh.mongodb.net/test"

  private val memcached = Memcached(Configuration("127.0.0.1:11211"))(ec)

  private val mongoClient: MongoClient = MongoClient(MONGO_URI)
  private val database: MongoDatabase = mongoClient.getDatabase("urls_db").withCodecRegistry(codecRegistry)
  private val collection: MongoCollection[Url] = database.getCollection("urls")

  override def save(createUrl: CreateUrl): Future[String] = {
    val shortUrl = findUrlInDb(createUrl.original) match {
      case Some(shortUrl) => shortUrl
      case None =>
        val url = UrlUtils.createUrl(createUrl)
        collection.insertOne(url).results()
        url.shortUrl
    }
    Future.successful(UrlUtils.getShortUrlLink(shortUrl))
  }

  private def findUrlInDb(originalUrl: String): Option[String] = {
    collection.find(equal("originalUrl", originalUrl)).headResult() match {
      case x: Url => Some(x.shortUrl)
      case _ => None
    }
  }

  override def get(shortUrl: String): Future[String] = {
    memcached.awaitGet[String](shortUrl) match {
      case Some(value) => Future.successful(value)
      case None =>
        collection.find(equal("shortUrl", shortUrl)).headResult() match {
          case url: Url =>
            memcached.set(url.shortUrl, url.originalUrl, 1.minute)
            Future.successful(url.originalUrl)
          case _ =>
            Future.failed(UrlDoesNotExist(UrlUtils.getShortUrlLink(shortUrl)))
        }
    }
  }

  override def delete(shortUrl: String): Unit = {
    memcached.delete(shortUrl)
    collection.deleteOne(equal("shortUrl", shortUrl))
  }

  override def getAll(): Future[Seq[Url]] = {
    collection.find().toFuture()
  }
}


