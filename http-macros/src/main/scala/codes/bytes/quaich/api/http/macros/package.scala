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

import codes.bytes.quaich.api.http.model.LambdaHTTPResponse

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
package object macros {
  /**
    * Get route, with automatic deserialization of JSON 'body' attribute
    * (pass through of what the caller passes to HTTP Gateway)
    * to case classes via JSON4S
    *
    * // TODO - Pluggable support for Circe & Jawn
    *
    * @param route
    * @param block
    * @tparam T
    * @return
    */
  def get[T](route: String)(block: T => LambdaHTTPResponse): Any = macro get_impl[T]



  def get_impl[T : c.WeakTypeTag](c: scala.reflect.macros.whitebox.Context)(route: c.Expr[String])(block: c.Expr[T ⇒ LambdaHTTPResponse]): c.Expr[Any] = {
    import c.universe._

    val tpe = weakTypeOf[T]

    val obj = q"""
    val handler = new codes.bytes.quaich.api.http.routing.HTTPGetRoute[$tpe] {
      def apply(): LambdaHTTPResponse = {
        println(s"Request Body: " + request.body)
        val body = request.body.extract[$tpe]
        $block(body)
      }
    }

    addRoute($route, handler)
    """

    c.Expr[Any](obj)
  }


}

// vim: set ts=2 sw=2 sts=2 et
