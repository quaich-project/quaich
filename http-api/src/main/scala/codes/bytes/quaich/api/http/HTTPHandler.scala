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

import codes.bytes.quaich.api.http._
import codes.bytes.quaich.api.http.routing.HTTPRoute
import org.json4s._


trait HTTPHandler {

  protected val request: LambdaHTTPRequest
  protected val context: LambdaContext

  protected val ViewArgsRE = """\{[\w0-9_\-+]+\}""".r

  protected val routeBuilder =
    Map.newBuilder[(HTTPMethod, String), HTTPRoute[_]]

  lazy val routes = routeBuilder.result

  // TODO - Magnet pattern for response handling...
  def routeRequest(): LambdaHTTPResponse = {
    routes.get(HTTPMethod(request.httpMethod) → request.resource) match {
      case Some(handler) ⇒ handler()
      case None ⇒ LambdaHTTPResponse(statusCode = 404)

    }
  }

  def complete(magnet: HTTPResponseMagnet): magnet.Result = magnet()

  def addRoute(method: HTTPMethod, route: String, handler: HTTPRoute[_]): Unit = {
    routeBuilder += (method → route) → handler
  }
}

// vim: set ts=2 sw=2 sts=2 et:
