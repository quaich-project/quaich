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
import scala.reflect.macros.blackbox
import scala.language.experimental.macros

object LambdaHTTPApi {
  // todo - check for companion object and reject
  def annotation_impl(c: blackbox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._
    import Flag._

    val p = c.enclosingPosition

    val inputs = annottees.map(_.tree).toList



    val result: Tree = inputs match {
      case (cls @ q"$mods class $name[..$tparams] extends ..$parents { ..$body }") :: Nil if mods.hasFlag(ABSTRACT) ⇒
        c.abort(p, "! The @LambdaHTTPApi annotation is not valid on abstract classes.")
        cls
      // todo - detect and handle companion object!
      case (cls @ q"$mods class $name[..$tparams] extends ..$parents { ..$body }") :: Nil ⇒
        //val baseName = name.decodedName.toString
        //val handlerName = TermName(s"$baseName$$RequestHandler")
        //val handlerName = name.toTermName
        val handlerName = name.asInstanceOf[TypeName].toTermName

        val cls = q"""
        $mods class $name[..$tparams](
            val request: codes.bytes.quaich.api.http.LambdaHTTPRequest,
            val context: codes.bytes.quaich.api.http.LambdaContext
          )
          extends ..$parents
          with codes.bytes.quaich.api.http.HTTPHandler {
            import org.json4s.jackson.JsonMethods._
            import org.json4s.jackson.Serialization
            import org.json4s.jackson.Serialization._
            import org.json4s.{NoTypeHints, _}

            protected implicit val formats = Serialization.formats(NoTypeHints)

            ..$body
          }
        """

        val obj = q"""
        object $handlerName extends codes.bytes.quaich.api.http.HTTPApp {
          def newHandler(
            request: codes.bytes.quaich.api.http.LambdaHTTPRequest,
            context: codes.bytes.quaich.api.http.LambdaContext
          ): codes.bytes.quaich.api.http.HTTPHandler =
            new $name(request, context)


        }
        """

        q"$cls; $obj"

      case Nil ⇒
        c.abort(p, s"Cannot annotate an empty Tree.")
      case _ ⇒
        c.abort(p, s"! The @LambdaHTTPApi Annotation is only valid on non-abstract Classes")
    }

    //c.info(p, "result: " + result, force = true)

    c.Expr[Any](result)

  }

}

@compileTimeOnly("Setup the macro paradise compiler plugin to enable expansion of macro annotations.")
class LambdaHTTPApi extends StaticAnnotation {

  def macroTransform(annottees: Any*): Any = macro LambdaHTTPApi.annotation_impl

}
// vim: set ts=2 sw=2 sts=2 et:
