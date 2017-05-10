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

import codes.bytes.quaich.api.http._
import codes.bytes.quaich.api.http.macros._
import codes.bytes.quaich.demo.http.model.TestObject


@LambdaHTTPApi
class DemoHTTPServer {

  // this thing works!
  get("/quaich-http-demo/hello") { requestContext =>
    complete("Awesome. First small success! Version: 0.0.2")
  }

  get("/quaich-http-demo/users/{username}/foo/{bar}") { requestContext =>
    complete("OK")
  }

  head("/quaich-http-demo/users/{username}/foo/{bar}") { requestContext =>
    complete(HTTPStatus.OK)
  }

  options("/quaich-http-demo/users/{username}/foo/{bar}") { requestContext =>
    complete(HTTPStatus.ImATeapot)
  }

  delete("/quaich-http-demo/users/{username}/foo/{bar}") { requestContext =>
    complete("OK")
  }

  postX[TestObject]("/quaich-http-demo/users/{username}/foo/{bar}") { (requestWithBody: LambdaRequestBody[TestObject], username: String, bar: String) =>
    complete((HTTPStatus.Created, s"Created user $username."))
  }

  post[TestObject]("/quaich-http-demo/users/{username}") { requestWithBody =>
    requestWithBody.request.pathParameters.get("username") match {
      case Some(user) ⇒
        // create user in database blah blah blah
        complete((HTTPStatus.Created, s"Created user $user."))
      case None ⇒
        complete(HTTPStatus.BadRequest)
    }
  }

  put[TestObject]("/quaich-http-demo/users/{username}/foo/{bar}") { requestWithBody =>
    println(s"Put Body: ${requestWithBody.body} Path Parameters: ${requestWithBody.request.pathParameters}")
    val response = TestObject("OMG", "WTF")
    complete(response)
  }

  patch[TestObject]("/quaich-http-demo/users/{username}/foo/{bar}") { body ⇒
    println(s"Patch Body: $body")
    complete("OK")
  }
}