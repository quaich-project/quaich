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

package codes.bytes.quaich.api


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
package object http extends HTTPResponses with HTTPResponseMarshallers {

  import org.json4s._

  import com.amazonaws.services.lambda.runtime.{ClientContext, CognitoIdentity, Context, LambdaLogger}

  object HTTPMethod {
    def apply(method: String): HTTPMethod = method match {
      case "GET" ⇒ GET
      case "POST" ⇒ POST
      case "PUT" ⇒ PUT
      case "DELETE" ⇒ DELETE
      case "HEAD" ⇒ HEAD
      case "OPTIONS" ⇒ OPTIONS
      case "PATCH" ⇒ PATCH
      case _ ⇒
        throw new IllegalArgumentException(s"Unsupported/unknown HTTP Method '$method'")
    }
  }

  sealed abstract class HTTPMethod
  case object GET extends HTTPMethod
  case object POST extends HTTPMethod
  case object PUT extends HTTPMethod
  case object DELETE extends HTTPMethod
  case object HEAD extends HTTPMethod
  case object OPTIONS extends HTTPMethod
  case object PATCH extends HTTPMethod


  case class LambdaHTTPRequest(
    resource: String,
    path: String, // todo - wire into an object that can extract vars
    httpMethod: String, // todo - in place validation
    requestContext: LambdaHTTPRequestContext,
    body: JObject,
    headers: Map[String, String] = Map.empty,
    queryStringParameters: Map[String, String] = Map.empty,
    pathParameters: Map[String, String] = Map.empty,
    stageVariables: Map[String, String] = Map.empty
  )

  case class CognitoData(
    cognitoIdentityPoolId: Option[String],
    accountId: String,
    cognitoIdentityId: Option[String],
    caller: String,
    apiKey: String,
    sourceIp: String,
    cognitoAuthenticationType: Option[String],
    cognitoAuthenticationProvider: Option[String],
    userArn: String,
    user: String
  )

  case class LambdaHTTPRequestContext(
    accountId: String,
    resourceId: String,
    stage: String,
    requestId: String,
    identity: CognitoData,
    resourcePath: String,
    httpMethod: String,
    apiId: String
  )

  class LambdaContext(ctx: Context) {
    def identity: Option[CognitoIdentity] = Option(ctx.getIdentity)

    def clientContext: Option[ClientContext] = Option(ctx.getClientContext)

    lazy val logger: LambdaLogger = ctx.getLogger

    def log(msg: => String) = logger.log(msg)

    def memoryLimitInMB: Int = ctx.getMemoryLimitInMB

    def remainingTimeInMillis: Int = ctx.getRemainingTimeInMillis

    def awsRequestId: String = ctx.getAwsRequestId

    def functionName: String = ctx.getFunctionName

    def logGroupName: Option[String] = Option(ctx.getLogGroupName)

    def logStreamName: Option[String] = Option(ctx.getLogStreamName)
  }

}

// vim: set ts=2 sw=2 sts=2 et
