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

package object macros {

  type RouteBlockWithParams = LambdaRequestContext ⇒ LambdaHTTPResponse
  type RouteBlockWithBodyParams[T] = LambdaRequestBody[T] ⇒ LambdaHTTPResponse

  type RouteXBlockWithBodyParams[T] = (LambdaRequestBody[T], String*) ⇒ LambdaHTTPResponse

  type RouteBlock[T <: Product] =  T ⇒ LambdaHTTPResponse
  type RouteXBlock[T <: Product] =  (T, String*) ⇒ LambdaHTTPResponse

  implicit def route1ArgToRouteX[T <: Product](
    block: (T, String) ⇒ LambdaHTTPResponse
  ): RouteXBlock[T] = (body, args) ⇒ block(body, args(0))

  implicit def route2ArgToRouteX[T <: Product](
    block: (T, String, String) ⇒ LambdaHTTPResponse
  ): RouteXBlock[T] = (body, args) ⇒ block(body, args(0), args(1))



  def get(route: String)(block: RouteBlockWithParams): Any = macro get_impl

  def get_impl(c: scala.reflect.macros.whitebox.Context)(route: c.Expr[String])(block: c.Expr[RouteBlockWithParams]): c.Expr[Any] = {
    import c.universe._

    val obj = q"""
    val handler = new codes.bytes.quaich.api.http.routing.HTTPGetRoute {
      def apply(requestContext: LambdaRequestContext): LambdaHTTPResponse = {
        $block(requestContext)
      }
    }

    addRoute(codes.bytes.quaich.api.http.GET, $route, handler)
    """

    c.Expr[Any](obj)
  }

  def head(route: String)(block: RouteBlockWithParams): Any = macro head_impl

  def head_impl(c: scala.reflect.macros.whitebox.Context)(route: c.Expr[String])(block: c.Expr[RouteBlockWithParams]): c.Expr[Any] = {
    import c.universe._

    val obj = q"""
    val handler = new codes.bytes.quaich.api.http.routing.HTTPHeadRoute {
      def apply(requestContext: LambdaRequestContext): LambdaHTTPResponse = {
        $block(requestContext)
      }
    }

    addRoute(codes.bytes.quaich.api.http.HEAD, $route, handler)
    """

    c.Expr[Any](obj)
  }


  def options(route: String)(block: RouteBlockWithParams): Any = macro options_impl


  def options_impl(c: scala.reflect.macros.whitebox.Context)(route: c.Expr[String])(block: c.Expr[RouteBlockWithParams]): c.Expr[Any] = {
    import c.universe._

    val obj = q"""
    val handler = new codes.bytes.quaich.api.http.routing.HTTPOptionsRoute {
      def apply(requestContext: LambdaRequestContext): LambdaHTTPResponse = {
        $block(requestContext)
      }
    }

    addRoute(codes.bytes.quaich.api.http.OPTIONS, $route, handler)
    """

    c.Expr[Any](obj)
  }

  def postX[T <: Product](route: String)(block: RouteXBlockWithBodyParams[T]): Any =
    macro postX_impl[T]


  def postX_impl[T <: Product : c.WeakTypeTag](c: scala.reflect.macros.whitebox.Context)(route: c.Expr[String])(block: c.Expr[RouteXBlockWithBodyParams[T]]): c.Expr[Any] = {
    import c.universe._

    val tpe = weakTypeOf[T]

    if (tpe =:= typeOf[Nothing])
      c.abort(c.enclosingPosition, "POST routes require a case class argument describing how the JSON body should be deserialized. Please call it as `post[<TYPE>](<route>) ...`")

    val obj = q"""
    val handler = new codes.bytes.quaich.api.http.routing.HTTPPostRoute[$tpe] {
      def apply(requestContext: LambdaRequestContext): LambdaHTTPResponse = {
        val body = requestContext.request.body.extract[$tpe]
        $block(requestContext.withBody(body))
      }
    }

    addRoute(codes.bytes.quaich.api.http.POST, $route, handler)
    """

    c.Expr[Any](obj)
  }

  def post[T <: Product](route: String)(block: RouteBlockWithBodyParams[T]): Any = macro post_impl[T]

  def post_impl[T <: Product : c.WeakTypeTag](c: scala.reflect.macros.whitebox.Context)(route: c.Expr[String])(block: c.Expr[RouteBlockWithBodyParams[T]]): c.Expr[Any] = {
    import c.universe._

    val tpe = weakTypeOf[T]

    if (tpe =:= typeOf[Nothing])
      c.abort(c.enclosingPosition, "POST routes require a case class argument describing how the JSON body should be deserialized. Please call it as `post[<TYPE>](<route>) ...`")

    val obj = q"""
    val handler = new codes.bytes.quaich.api.http.routing.HTTPPostRoute[$tpe] {
      def apply(requestContext: LambdaRequestContext): LambdaHTTPResponse = {
        val body = requestContext.request.body.extract[$tpe]
        $block(requestContext.withBody(body))
      }
    }

    addRoute(codes.bytes.quaich.api.http.POST, $route, handler)
    """

    c.Expr[Any](obj)
  }

  def put[T <: Product](route: String)(block: RouteBlockWithBodyParams[T]): Any = macro put_impl[T]

  def put_impl[T <: Product : c.WeakTypeTag](c: scala.reflect.macros.whitebox.Context)(route: c.Expr[String])(block: c.Expr[RouteBlockWithBodyParams[T]]): c.Expr[Any] = {
    import c.universe._

    val tpe = weakTypeOf[T]

    if (tpe =:= typeOf[Nothing])
      c.abort(c.enclosingPosition, "PUT routes require a case class argument describing how the JSON body should be deserialized. Please call it as `put[<TYPE>](<route>) ...`")

    val obj = q"""
    val handler = new codes.bytes.quaich.api.http.routing.HTTPPutRoute[$tpe] {
      def apply(requestContext: LambdaRequestContext): LambdaHTTPResponse = {
        val body = requestContext.request.body.extract[$tpe]
        $block(requestContext.withBody(body))
      }
    }

    addRoute(codes.bytes.quaich.api.http.PUT, $route, handler)
    """

    c.Expr[Any](obj)
  }

  def deleteWithBody[T <: Product](route: String)(block: RouteBlock[T]): Any = macro delete_with_body_impl[T]


  def delete_with_body_impl[T <: Product : c.WeakTypeTag](c: scala.reflect.macros.whitebox.Context)(route: c.Expr[String])(block: c.Expr[T ⇒ LambdaHTTPResponse]): c.Expr[Any] = {
    import c.universe._

    val tpe = weakTypeOf[T]

    val obj = q"""
        val handler = new codes.bytes.quaich.api.http.routing.HTTPPostRoute[$tpe] {
          def apply(): LambdaHTTPResponse = {
            val body = request.body.extract[$tpe]
            $block(body)
          }
        }

        addRoute(codes.bytes.quaich.api.http.DELETE, $route, handler)
        """

    c.Expr[Any](obj)
  }

  def delete(route: String)(block: RouteBlockWithParams): Any = macro delete_typeless_impl

  def delete_typeless_impl(c: scala.reflect.macros.whitebox.Context)(route: c.Expr[String])(block: c.Expr[RouteBlockWithParams]): c.Expr[Any] = {
    import c.universe._

/*
      c.warning(
        c.enclosingPosition, "DELETE Route invoked with no type argument. " +
          "the type of the `body` will be Unit. If you didn't mean to do this, please " +
          "ensure to call delete with a type argument, such as `delete[T](<route>)...`, where T is a case class."
      )
*/

    val obj = q"""
        val handler = new codes.bytes.quaich.api.http.routing.HTTPDeleteRoute[Nothing] {
          def apply(requestContext: LambdaRequestContext): LambdaHTTPResponse = {
            $block(requestContext)
          }
        }

        addRoute(codes.bytes.quaich.api.http.DELETE, $route, handler)
        """

    c.Expr[Any](obj)
  }

  def patch[T <: Product](route: String)(block: RouteBlockWithBodyParams[T]): Any = macro patch_impl[T]

  def patch_impl[T <: Product : c.WeakTypeTag](c: scala.reflect.macros.whitebox.Context)(route: c.Expr[String])(block: c.Expr[RouteBlockWithBodyParams[T]]): c.Expr[Any] = {
    import c.universe._

    val tpe = weakTypeOf[T]

    if (tpe =:= typeOf[Nothing])
      c.abort(c.enclosingPosition, "PATCH routes require a case class argument describing how the JSON body should be deserialized. Please call it as `patch[<TYPE>](<route>) ...`")

    val obj = q"""
    val handler = new codes.bytes.quaich.api.http.routing.HTTPPostRoute[$tpe] {
      def apply(requestContext: LambdaRequestContext): LambdaHTTPResponse = {
        val body = requestContext.request.body.extract[$tpe]
        $block(requestContext.withBody(body))
      }
    }

    addRoute(codes.bytes.quaich.api.http.PATCH, $route, handler)
    """

    c.Expr[Any](obj)
  }
}
