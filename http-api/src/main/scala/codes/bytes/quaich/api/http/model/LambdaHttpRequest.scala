package codes.bytes.quaich.api.http.model

import org.json4s.JsonAST.JObject

case class LambdaHttpRequest(
  body: JObject,
  pathParameters: Map[String, String],
  querystring: Map[String, String],
  headers: Map[String, String],
  stageVariables: Map[String, String],
  accountId: Option[String],
  apiId: String, // I think this is always set...
  apiKey: Option[String],
  authorizerPrincipalId: Option[String],
  caller: Option[String],
  cognito: CognitoData,
  httpMethod: String, // todo - in place validation
  stage: String,
  sourceIp: String, // todo -parse as net address?
  user: Option[String],
  userAgent: String,
  userArn: Option[String],
  requestId: String, // todo - parse as UUID?
  resourceId: String,
  resourcePath: String // todo - wire into an object that can extract vars
)

case class CognitoData(
  authenticationProvider: Option[String],
  authenticationType: Option[String],
  identityId: Option[String],
  identityPoolId: Option[String]
)

//{
//  body: { },
//  pathParameters: {
//    username: "brendan"
//  },
//  querystring: {
//    Foo: "bar",
//    spam: "eggs"
//  },
//  headers: {
//    Accept: "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
//    Accept-Encoding: "gzip, deflate, sdch, br",
//    Accept-Language: "en-US,en;q=0.8",
//    Cache-Control: "max-age=0",
//    CloudFront-Forwarded-Proto: "https",
//    CloudFront-Is-Desktop-Viewer: "true",
//    CloudFront-Is-Mobile-Viewer: "false",
//    CloudFront-Is-SmartTV-Viewer: "false",
//    CloudFront-Is-Tablet-Viewer: "false",
//    CloudFront-Viewer-Country: "GB",
//    DNT: "1",
//    Host: "sy8pwbe5yi.execute-api.us-east-1.amazonaws.com",
//    Upgrade-Insecure-Requests: "1",
//    User-Agent: "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36",
//    Via: "1.1 91cea9fc2b8f62088b9a1281098c2a59.cloudfront.net (CloudFront)",
//    X-Amz-Cf-Id: "9VpmdHjFhb6s1J7TUJCohQdrxVzIbgb9p-QwdLBbzFkaFbZclmcU9w==",
//    X-Forwarded-For: "88.210.160.60, 54.239.166.44",
//    X-Forwarded-Port: "443",
//    X-Forwarded-Proto: "https"
//  },
//  stageVariables: { },
//  accountId: "",
//  apiId: "sy8pwbe5yi",
//  apiKey: "",
//  authorizerPrincipalId: "",
//  caller: "",
//  cognito: {
//    authenticationProvider: "",
//    authenticationType: "",
//    identityId: "",
//    identityPoolId: ""
//  },
//  httpMethod: "GET",
//  stage: "test",
//  sourceIp: "88.210.160.60",
//  user: "",
//  userAgent: "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.116 Safari/537.36",
//  userArn: "",
//  requestId: "6944b512-794a-11e6-8bb9-b5c3a8dac780",
//  resourceId: "2o4gqw",
//  resourcePath: "/user/{username}"
//}
// vim: set ts=2 sw=2 sts=2 et:
