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
package codes.bytes.quaich.demo.http

import codes.bytes.quaich.api.http.model.LambdaHTTPRequest
import codes.bytes.quaich.api.http.{HTTPApp, LambdaContext}
import com.amazonaws.HttpMethod

object DemoHTTPServer extends HTTPApp {

  /*
  @route("/")
  def index(req: LambdaHttpRequest, ctx: LambdaContext): String = {
    """{ "hello" : "world" }"""
  }

  @route("/users/{username}")
  def stateOfCity(req: LambdaHttpRequest, ctx: LambdaContext,
    username: String): String = {
    val user = db.getUser(username) match {
      case Some(userData) => userData.toJson
      case None =>
        ctx.log(s"ERROR: User '$username' not found")
        s""" { "error": "No such user" } """
    }
  }

  @route("/users", methods=Vector(HttpMethod.POST))
  def createUser(req: LambdaHttpRequest, ctx: LambdaContext): String = {
    // Lambda will handle exceptions somewhat nicely, 500ing and logging
    val userData = req.body.extract[User]
    db.createUser(userData)
    s""" { "success": 1 } """
  }
  */

}

// vim: set ts=2 sw=2 sts=2 et:
