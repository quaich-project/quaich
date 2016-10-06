/*
 * Copyright (c) 2016 Brendan McAdams & Thomas Lockney
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package codes.bytes.quaich.api.http

import java.io.{InputStream, OutputStream}

import codes.bytes.quaich.api.http.model.{LambdaContext, LambdaHTTPRequest, LambdaHTTPResponse}
import com.amazonaws.services.lambda.runtime.{Context, LambdaLogger, RequestStreamHandler}
import org.apache.commons.io.IOUtils
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization._
import org.json4s.{NoTypeHints, _}

trait HTTPApp extends RequestStreamHandler {

  def newHandler(request: LambdaHTTPRequest, context: LambdaContext): HTTPHandler


  protected implicit val formats = Serialization.formats(NoTypeHints)

  final override def handleRequest(
    input: InputStream,
    output: OutputStream,
    context: Context): Unit = {

    val ctx = new LambdaContext(context)
    val logger: LambdaLogger = ctx.logger

    val json = parse(input)

    val req = json.extract[LambdaHTTPRequest]

    // route
    logger.log(s"Input: ${pretty(render(json))}")

    val response = newHandler(req, ctx).routeRequest()

    try {
      IOUtils.write(write(response), output)
    } catch {
      case e: Exception =>
        logger.log("Error while writing response\n" + e.getMessage);
        throw new IllegalStateException(e)
    }
  }
}

// vim: set ts=2 sw=2 sts=2 et:
