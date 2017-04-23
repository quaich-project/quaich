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

import codes.bytes.quaich.api.http._
import codes.bytes.quaich.api.http.macros._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization._
import org.json4s.{NoTypeHints, _}
import org.scalatest.{MustMatchers, WordSpec}

class HTTPRoutingSpec extends WordSpec with MustMatchers {
  protected implicit val formats = Serialization.formats(NoTypeHints)

  val sampleRequestPost =
    """
      |{
      |    "resource": "/quaich-http-demo/users/{username}/foo/{bar}",
      |    "path": "/quaich-http-demo/users/brendan/foo/123",
      |    "httpMethod": "POST",
      |    "headers": {
      |        "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
      |        "Accept-Encoding": "gzip, deflate, sdch, br",
      |        "Accept-Language": "en-US,en;q=0.8",
      |        "CloudFront-Forwarded-Proto": "https",
      |        "CloudFront-Viewer-Country": "US",
      |        "DNT": "1",
      |        "Host": "f7hyd8m7yl.execute-api.us-east-1.amazonaws.com",
      |        "Upgrade-Insecure-Requests": "1",
      |        "User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36",
      |        "Via": "1.1 9bf53fbf949b06bf7635b36bc0201861.cloudfront.net (CloudFront)",
      |        "X-Amz-Cf-Id": "ooOkkDuDZjyNIifl8ASTciCp2gkUCmUM5n-PxOytKwwyq1Wqd39dXA==",
      |        "X-Forwarded-For": "207.91.160.7, 54.240.149.12",
      |        "X-Forwarded-Port": "443",
      |        "X-Forwarded-Proto": "https"
      |    },
      |    "queryStringParameters": null,
      |    "pathParameters": {
      |        "bar": "123",
      |        "username": "brendan"
      |    },
      |    "stageVariables": null,
      |    "requestContext": {
      |        "accountId": "176770676006",
      |        "resourceId": "kas444",
      |        "stage": "prod",
      |        "requestId": "4d296250-8a60-11e6-bf73-358d1757f263",
      |        "identity": {
      |            "cognitoIdentityPoolId": null,
      |            "accountId": null,
      |            "cognitoIdentityId": null,
      |            "caller": null,
      |            "apiKey": null,
      |            "sourceIp": "207.91.160.7",
      |            "cognitoAuthenticationType": null,
      |            "cognitoAuthenticationProvider": null,
      |            "userArn": null,
      |            "userAgent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36",
      |            "user": null
      |        },
      |        "resourcePath": "/quaich-http-demo/users/{username}/foo/{bar}",
      |        "httpMethod": "POST",
      |        "apiId": "f7hyd8m7yl"
      |    },
      |    "body": {
      |       "foo": "I will not buy this record, it is scratched.",
      |       "bar": "my hovercraft is full of eels!"
      |    }
      |}
    """.stripMargin

  val sampleRequestPut  =
    """
      |{
      |    "resource": "/quaich-http-demo/users/{username}",
      |    "path": "/quaich-http-demo/users/brendan",
      |    "httpMethod": "PUT",
      |    "headers": {
      |        "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
      |        "Accept-Encoding": "gzip, deflate, sdch, br",
      |        "Accept-Language": "en-US,en;q=0.8",
      |        "Cache-Control": "max-age=0",
      |        "CloudFront-Forwarded-Proto": "https",
      |        "CloudFront-Is-Desktop-Viewer": "true",
      |        "CloudFront-Is-Mobile-Viewer": "false",
      |        "CloudFront-Is-SmartTV-Viewer": "false",
      |        "CloudFront-Is-Tablet-Viewer": "false",
      |        "CloudFront-Viewer-Country": "US",
      |        "DNT": "1",
      |        "Host": "f7hyd8m7yl.execute-api.us-east-1.amazonaws.com",
      |        "Upgrade-Insecure-Requests": "1",
      |        "User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36",
      |        "Via": "1.1 efe8f585d51dfd5d8d354f74f0385a50.cloudfront.net (CloudFront)",
      |        "X-Amz-Cf-Id": "l8tkw9ISay4QU1FVsfhq48OQqxm4fDbTVtGCqxiL92atksvwZHXYrQ==",
      |        "X-Forwarded-For": "207.91.160.7, 216.137.42.125",
      |        "X-Forwarded-Port": "443",
      |        "X-Forwarded-Proto": "https"
      |    },
      |    "queryStringParameters": null,
      |    "pathParameters": {
      |        "username": "brendan"
      |    },
      |    "stageVariables": null,
      |    "requestContext": {
      |        "accountId": "176770676006",
      |        "resourceId": "w11v9j",
      |        "stage": "prod",
      |        "requestId": "d2eb971d-8a5f-11e6-b30c-19c3d9849330",
      |        "identity": {
      |            "cognitoIdentityPoolId": null,
      |            "accountId": null,
      |            "cognitoIdentityId": null,
      |            "caller": null,
      |            "apiKey": null,
      |            "sourceIp": "207.91.160.7",
      |            "cognitoAuthenticationType": null,
      |            "cognitoAuthenticationProvider": null,
      |            "userArn": null,
      |            "userAgent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36",
      |            "user": null
      |        },
      |        "resourcePath": "/quaich-http-demo/users/{username}",
      |        "httpMethod": "PUT",
      |        "apiId": "f7hyd8m7yl"
      |    },
      |    "body": {
      |       "foo": "I will not buy this record, it is scratched.",
      |       "bar": "my hovercraft is full of eels!"
      |    }
      |}
    """.stripMargin

  val sampleRequestJSONNoArgs =
    """
      |{
      |    "resource": "/quaich-http-demo",
      |    "path": "/quaich-http-demo",
      |    "httpMethod": "POST",
      |    "headers": {
      |        "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
      |        "Accept-Encoding": "gzip, deflate, sdch, br",
      |        "Accept-Language": "en-US,en;q=0.8",
      |        "CloudFront-Forwarded-Proto": "https",
      |        "CloudFront-Viewer-Country": "US",
      |        "DNT": "1",
      |        "Host": "f7hyd8m7yl.execute-api.us-east-1.amazonaws.com",
      |        "Upgrade-Insecure-Requests": "1",
      |        "User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36",
      |        "Via": "1.1 696ddb6cca7fadc053f7e4e8b21c9273.cloudfront.net (CloudFront)",
      |        "X-Amz-Cf-Id": "hHVXceyv01PJlreM9cTnjSJ-BUt7ze_3UN3zSwqNs0n99p7kJYLuCg==",
      |        "X-Forwarded-For": "207.91.160.7, 54.240.149.46",
      |        "X-Forwarded-Port": "443",
      |        "X-Forwarded-Proto": "https"
      |    },
      |    "queryStringParameters": null,
      |    "pathParameters": null,
      |    "stageVariables": null,
      |    "requestContext": {
      |        "accountId": "176770676006",
      |        "resourceId": "y168r3",
      |        "stage": "prod",
      |        "requestId": "7a1df3f3-8a57-11e6-b6f7-4dc1e2a29025",
      |        "identity": {
      |            "cognitoIdentityPoolId": null,
      |            "accountId": null,
      |            "cognitoIdentityId": null,
      |            "caller": null,
      |            "apiKey": null,
      |            "sourceIp": "207.91.160.7",
      |            "cognitoAuthenticationType": null,
      |            "cognitoAuthenticationProvider": null,
      |            "userArn": null,
      |            "userAgent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36",
      |            "user": null
      |        },
      |        "resourcePath": "/quaich-http-demo",
      |        "httpMethod": "POST",
      |        "apiId": "f7hyd8m7yl"
      |    },
      |    "body": {
      |       "foo": "I will not buy this record, it is scratched.",
      |       "bar": "my hovercraft is full of eels!"
      |    }
      |}
    """.stripMargin


  "Running a routing request" should {
    "allow a magnet response of an int" in {
      val input = sampleRequestPost
      val json = parse(input)

      val req = json.extract[LambdaHTTPRequest]

      val server = new TestHTTPServer
      val handler = server.newHandler
      val response = handler.routeRequest(req, null)

      response must have (
        'statusCode (200),
        'body (None)
      )

    }
    "allow a magnet response of a case class" in {
      val input = sampleRequestPut
      val json = parse(input)

      val req = json.extract[LambdaHTTPRequest]

      val server = new TestHTTPServer
      val handler = server.newHandler
      val response = handler.routeRequest(req, null)

      response must have (
        'statusCode (200),
        'body (Option(write(TestObject("OMG", "WTF"))))
      )

    }
  }
}

@LambdaHTTPApi
class TestHTTPServer {

  get("/quaich-http-demo/users/{username}/foo/{bar}") { requestContext: LambdaRequestContext =>
    complete("OK")
  }

  head("/quaich-http-demo/users/{username}/foo/{bar}") { requestContext: LambdaRequestContext =>
    complete(HTTPStatus.OK)
  }

  options("/quaich-http-demo/users/{username}/foo/{bar}") { requestContext: LambdaRequestContext =>
    complete(HTTPStatus.ImATeapot)
  }

  delete("/quaich-http-demo/users/{username}/foo/{bar}") { requestContext: LambdaRequestContext =>
    complete("OK")
  }

  post[TestObject]("/quaich-http-demo/users/{username}/foo/{bar}") { requestContext: LambdaRequestBody[TestObject] =>
    println(s"Post Body: ${requestContext.body}")
    complete(200)
  }

  put[TestObject]("/quaich-http-demo/users/{username}") { requestContext: LambdaRequestBody[TestObject] =>
    println(s"Username: ${requestContext.request.pathParameters("username")}")
    val response = TestObject("OMG", "WTF")
    complete(response)
  }

  patch[TestObject]("/quaich-http-demo/users/{username}/foo/{bar}") { requestContext: LambdaRequestBody[TestObject] =>
    println(s"Patch Body: ${requestContext.body}")
    complete("OK")
  }
}

case class TestObject(foo: String, bar: String)

// vim: set ts=2 sw=2 sts=2 et:
