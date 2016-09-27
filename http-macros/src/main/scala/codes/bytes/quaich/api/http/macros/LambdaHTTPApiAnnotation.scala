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

import scala.annotation.{StaticAnnotation, compileTimeOnly}
import scala.language.postfixOps
import scala.reflect.macros.whitebox
import scala.language.experimental.macros
import scala.reflect.api.Trees

object LambdaHTTPApi {
  def annotation_impl(c: whitebox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._
    import Flag._

    val p = c.enclosingPosition

    val inputs = annottees.map(_.tree).toList

    def isRedundantAnnotation(parents: Seq[Trees#Tree]): Boolean = {
      parents.exists {
        case i: Ident ⇒
          i.name.encodedName.toString == "HTTPApp"
        case _ ⇒
          false
      }
    }

    val result: Tree = inputs match {
      case (cls @ q"$mods class $name[..$tparams] extends ..$parents { ..$body }") :: _ if mods
        .hasFlag(ABSTRACT) ⇒
        c.abort(p, "! The @LambdaHTTPApi annotation is not valid on abstract classes.")
        cls
      case (cls @ q"$mods class $name[..$tparams] extends ..$parents { ..$body }") :: _ ⇒
        if (isRedundantAnnotation(parents)) {
          c.warning(p, s"$name already extends HTTPApp; @LambdaHTTPApi notation is redundant and unnecessary.")
          cls
        } else
          q"$mods class $name[..$tparams] extends ..$parents with HTTPApp { ..$body }"
      case (o @ q"$mods object $name extends ..$parents { ..$body}") :: _ ⇒
        if (isRedundantAnnotation(parents)) {
          c.warning(p, s"$name already extends HTTPApp; @LambdaHTTPApi notation is redundant and unnecessary.")
          o
        } else
          q"$mods object $name extends ..$parents with HTTPApp { ..$body }"
      case Nil ⇒
        c.abort(p, s"Cannot annotate an empty Tree.")
      case _ ⇒
        c.abort(p, s"! The @LambdaHTTPApi Annotation is only valid on Objects and non-abstract Classes")
    }

    c.Expr[Any](result)

  }

}

@compileTimeOnly("Setup the macro paradise compiler plugin to enable expansion of macro annotations.")
class LambdaHTTPApi extends StaticAnnotation {

  def macroTransform(annottees: Any*): Any = macro LambdaHTTPApi.annotation_impl

}
// vim: set ts=2 sw=2 sts=2 et:
