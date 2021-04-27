import java.net.URL
import java.time.LocalDateTime
import java.util.Base64

object Utils {

  def encode(longUrl: String): String =
    Base64.getEncoder.encodeToString(longUrl.getBytes())

  def decode(shortUrl: String): String =
    new String(Base64.getDecoder.decode(shortUrl))

  import java.net.MalformedURLException

  def isValidURL(urlStr: String): Boolean = try {
    val url = new URL(urlStr)
    true
  } catch {
    case _: MalformedURLException =>
      false
  }

  def getCurrentDateTime: String = {
    import java.text.SimpleDateFormat
   new SimpleDateFormat("YYYY/MM/dd/HH:mm:ss").format(LocalDateTime.now())
  }

}


