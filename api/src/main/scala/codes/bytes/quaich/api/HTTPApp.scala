package codes.bytes.quaich.api

import java.io._
import java.nio.ByteBuffer

import com.amazonaws.services.lambda.runtime.{LambdaLogger, RequestStreamHandler, Context }
import org.apache.commons.io.IOUtils
import org.json4s._
import org.json4s.jackson.JsonMethods._

trait HTTPApp extends RequestStreamHandler {
  val routeBuilder = Map.newBuilder[String, (JValue, LambdaContext) => JValue]
  lazy val routes = routeBuilder.result

  override def handleRequest(
    input: InputStream,
    output: OutputStream,
    context: Context): Unit = {

    val ctx = new LambdaContext(context)
    val logger: LambdaLogger = ctx.logger

    val json = parse(input)
    // route
    logger.log(s"Input: ${pretty(render(json))}")


    try {
      IOUtils.write(pretty(render(json)), output)
    } catch {
      case e: Exception =>
        logger.log("Error while writing response\n" + e.getMessage);
        throw new IllegalStateException(e)
    }
  }
}

// vim: set ts=2 sw=2 sts=2 et:
