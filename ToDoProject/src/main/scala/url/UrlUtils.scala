package url

import server.BootServer

import java.net.URL
import java.util.{Base64, UUID}
import scala.util.hashing.MurmurHash3

object UrlUtils {

  def encodeMurMur(inputLink: String): String =
    MurmurHash3.stringHash(inputLink).toString

  def decode(shortUrl: String): String =
    new String(Base64.getDecoder.decode(shortUrl))

  import java.net.MalformedURLException

  def isValidURL(urlStr: String): Boolean = try {
    new URL(urlStr)
    true
  } catch {
    case _: MalformedURLException =>
      false
  }

  def getShortUrlLink(shortUrl: String): String = {
    if (BootServer.port == 9000) "http://localhost:9000/" + shortUrl
    else "https://todoscalaproject.herokuapp.com/" + shortUrl
  }

  def createUrl(createUrl: CreateUrl): Url = {
    val id = UUID.randomUUID().toString
    Url(
      id = id,
      shortUrl = UrlUtils.encodeMurMur(id + createUrl.original),
      originalUrl = createUrl.original
    )
  }

}


