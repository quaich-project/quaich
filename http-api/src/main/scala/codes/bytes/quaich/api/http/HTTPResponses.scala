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

import org.json4s._

import scala.language.implicitConversions

trait HTTPResponses {

  trait HTTPResponseMagnet {
    type Result = LambdaHTTPResponse
    def apply(): Result
  }

  final case class LambdaHTTPResponse(
    body: Option[String] = None,
    statusCode: Int = 200,
    headers: Map[String, Any] = Map.empty[String, Any]
  )


  implicit def fromStatusObject[T : HTTPResponseMarshaller](tuple: (StatusCode, T)): HTTPResponseMagnet =
    new HTTPResponseMagnet {
      override def apply(): LambdaHTTPResponse = {
        val marshaller = implicitly[HTTPResponseMarshaller[T]]
        LambdaHTTPResponse(
          statusCode = tuple._1.httpStatus,
          body = marshaller(tuple._2)
        )
      }
    }

  implicit def fromString(body: String): HTTPResponseMagnet =
    new HTTPResponseMagnet {
      override def apply(): LambdaHTTPResponse = LambdaHTTPResponse(
        body = Option(body)
      )
    }

  implicit def fromStatusCode(status: StatusCode): HTTPResponseMagnet =
    new HTTPResponseMagnet {
      override def apply(): LambdaHTTPResponse = LambdaHTTPResponse(
        statusCode = status.httpStatus
      )
    }

  implicit def fromInt(status: Int): HTTPResponseMagnet =
    new HTTPResponseMagnet {
      override def apply(): LambdaHTTPResponse = LambdaHTTPResponse(
        statusCode = status
      )
    }

  implicit def fromObject[T : HTTPResponseMarshaller](body: T): HTTPResponseMagnet =
    new HTTPResponseMagnet {
      override def apply(): LambdaHTTPResponse = {
        val marshaller = implicitly[HTTPResponseMarshaller[T]]
        LambdaHTTPResponse(
          body = marshaller(body)
        )
      }
    }


  sealed abstract class StatusCode(val httpStatus: Int)

  object HTTPStatus
    extends HTTPSuccesses
    with HTTPRedirections
    with HTTPClientErrors
    with HTTPServerErrors {

    def apply(statusCode: Int): StatusCode = new StatusCode(statusCode) {}
  }

  trait HTTPSuccesses {
    case object OK extends StatusCode(200)
    case object Created extends StatusCode(201)
    case object Accepted extends StatusCode(202)
    case object NoContent extends StatusCode(204)
  }

  trait HTTPRedirections {
    case object MovedPermanently extends StatusCode(301)
    case object NotModified extends StatusCode(304)
    case object TemporaryRedirect extends StatusCode(307)
    case object PermanentRedirect extends StatusCode(308)
  }

  trait HTTPClientErrors {
    case object BadRequest extends StatusCode(400)
    case object Unauthorized extends StatusCode(401)
    case object Forbidden extends StatusCode(403)
    case object NotFound extends StatusCode(404)
    case object NotAcceptable extends StatusCode(406)
    case object RequestTimeout extends StatusCode(408)
    // "Unavailable for Legal Reasons"
    case object DoublePlusUngood extends StatusCode(415)

    case object ImATeapot extends StatusCode(418)
  }

  trait HTTPServerErrors {
    case object InternalServerError extends StatusCode(500)
  }

}

// vim: set ts=2 sw=2 sts=2 et:
