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
import org.json4s._


@LambdaHTTPApi
class DemoHTTPServer {

  get("/quaich-http-demo/users/{username}/foo/{bar}") {
    LambdaHTTPResponse(JString("OK"))
  }

  head("/quaich-http-demo/users/{username}/foo/{bar}") {
    LambdaHTTPResponse(JString("OK"))
  }

  options("/quaich-http-demo/users/{username}/foo/{bar}") {
    LambdaHTTPResponse(JString("OK"))
  }

  delete("/quaich-http-demo/users/{username}/foo/{bar}") {
    LambdaHTTPResponse(JString("OK"))
  }

  post[TestObject]("/quaich-http-demo/users/{username}/foo/{bar}") { body ⇒
    println(s"Post Body: $body")
    LambdaHTTPResponse(JString("OK"))
  }

  put[TestObject]("/quaich-http-demo/users/{username}/foo/{bar}") { body ⇒
    println(s"Put Body: $body")
    LambdaHTTPResponse(JString("OK"))
  }

  patch[TestObject]("/quaich-http-demo/users/{username}/foo/{bar}") { body ⇒
    println(s"Patch Body: $body")
    LambdaHTTPResponse(JString("OK"))
  }

  println(routes)
}

// vim: set ts=2 sw=2 sts=2 et:
