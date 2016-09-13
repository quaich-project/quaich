package codes.bytes.quaich.api

import java.io._
import java.nio.ByteBuffer

import codes.bytes.quaich.api.model.LambdaHttpRequest
import com.amazonaws.services.lambda.runtime.{Context, LambdaLogger, RequestStreamHandler}
import org.apache.commons.io.IOUtils
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.{read, write}


trait HTTPApp extends RequestStreamHandler {
  val routeBuilder = Map.newBuilder[String, (JValue, LambdaContext) => JValue]
  lazy val routes = routeBuilder.result

  implicit val formats = Serialization.formats(NoTypeHints)

  override def handleRequest(
    input: InputStream,
    output: OutputStream,
    context: Context): Unit = {

    val ctx = new LambdaContext(context)
    val logger: LambdaLogger = ctx.logger

    val json = parse(input)

    val req = json.extract[LambdaHttpRequest]

    // route
    logger.log(s"Input: ${pretty(render(json))}")


    try {
      IOUtils.write(write(req), output)
    } catch {
      case e: Exception =>
        logger.log("Error while writing response\n" + e.getMessage);
        throw new IllegalStateException(e)
    }
  }
}

// vim: set ts=2 sw=2 sts=2 et:
