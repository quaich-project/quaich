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

package codes.bytes.quaich.api.http.macros

import codes.bytes.quaich.api.http.model.LambdaHTTPResponse
import codes.bytes.quaich.api.http.routing.HTTPGetRoute

import scala.language.experimental.macros
import scala.reflect.api.Trees
import scala.reflect.macros.whitebox


object LambdaHTTPApiMacros {
  def get[T](route: String)(block: => LambdaHTTPResponse): HTTPGetRoute[T] = macro get_impl[T]

  def get_impl[T : c.WeakTypeTag](c: whitebox.Context)(route: c.Expr[String])(block: c.Tree): c.Expr[HTTPGetRoute[T]] = {
    import c.universe._
    import Flag._

    val tpe = weakTypeOf[T]

    val obj = q"""
    new codes.bytes.quaich.api.http.routing.HTTPGetRoute[$tpe] {
      def apply(req: $tpe, httpRequest: LambdaHTTPRequest, context: LambdaContext): LambdaHTTPResponse = {
        ..$block
      }
    }
    """

    println(obj)

    c.Expr[HTTPGetRoute[T]](obj)
  }

}
// vim: set ts=2 sw=2 sts=2 et:
