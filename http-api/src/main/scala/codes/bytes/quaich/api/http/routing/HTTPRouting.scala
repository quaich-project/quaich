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

package codes.bytes.quaich.api.http.routing


trait HTTPGetRoute extends HTTPRoute[Unit] {

}

trait HTTPPostRoute[T] extends HTTPRoute[T] {

}

trait HTTPPutRoute[T] extends HTTPRoute[T] {

}

trait HTTPDeleteRoute[T] extends HTTPRoute[T] {

}

trait HTTPPatchRoute[T] extends HTTPRoute[T] {

}

trait HTTPHeadRoute extends HTTPRoute[Unit] {

}

trait HTTPOptionsRoute extends HTTPRoute[Unit] {

}

