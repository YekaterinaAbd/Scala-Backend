package url

case class Url(id: String, originalUrl: String, shortUrl: String)

case class CreateUrl(original: String)
