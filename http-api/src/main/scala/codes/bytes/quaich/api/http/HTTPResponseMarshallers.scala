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


import scala.annotation.implicitNotFound

import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization._
import org.json4s.{NoTypeHints, _}

object HTTPResponseMarshallers extends HTTPResponseMarshallers

trait HTTPResponseMarshallers {
  implicit val formats = Serialization.formats(NoTypeHints)

  @implicitNotFound("Unable to determine how to marshal ${T} to a JSON4S JValue; please provide an implicit instance of HTTPResponseMarshaller[${T}]")
  trait HTTPResponseMarshaller[-T] {
    def apply(value: T): Option[String]
  }

  implicit object JValueResponseMarshaller extends HTTPResponseMarshaller[JValue] {
    def apply(value: JValue) = Option(compact(value))
  }

  implicit object ClassResponseMarshaller extends HTTPResponseMarshaller[AnyRef] {
    def apply(value: AnyRef) = Option(write(value))
  }

}

// vim: set ts=2 sw=2 sts=2 et:
