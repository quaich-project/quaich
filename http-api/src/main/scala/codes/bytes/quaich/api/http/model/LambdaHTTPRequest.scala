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
package codes.bytes.quaich.api.http.model

import org.json4s.JObject


case class LambdaHTTPRequest(
  resource: String,
  path: String, // todo - wire into an object that can extract vars
  httpMethod: String, // todo - in place validation
  headers: Option[Map[String, String]],
  queryStringParameters: Option[Map[String, String]],
  pathParameters: Option[Map[String, String]],
  stageVariables: Option[Map[String, String]],
  requestContext: LambdaHTTPRequestContext,
  body: JObject
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
// vim: set ts=2 sw=2 sts=2 et:
