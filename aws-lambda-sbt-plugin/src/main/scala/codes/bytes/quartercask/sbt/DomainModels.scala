/*
 * Copyright (c) 2016 Brendan McAdams & Thomas Lockney
 * Portions Copyright (c) Gilt Groupe, based upon their work
 * at https://github.com/gilt/sbt-aws-lambda
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
package codes.bytes.quartercask.sbt

case class Region(value: String)
case class S3BucketId(value: String)
case class S3Key(value: String)
case class LambdaName(value: String)
case class LambdaARN(value: String)
case class HandlerName(value: String)
case class RoleARN(value: String)
case class Timeout(value: Int) {
  require(value > 0 && value <= 300, "Lambda timeout must be between 1 and 300 seconds")
}
case class Memory(value: Int) {
  require(value >= 128 && value <= 1536, "Lambda memory must be between 128 and 1536 MBs")
  require(value % 64 == 0)
}


object EnvironmentVariables {
  val region = "AWS_REGION"
  val bucketId = "AWS_LAMBDA_BUCKET_ID"
  val s3KeyPrefix = "AWS_LAMBDA_S3_KEY_PREFIX"
  val lambdaName = "AWS_LAMBDA_NAME"
  val handlerName = "AWS_LAMBDA_HANDLER_NAME"
  val roleArn = "AWS_LAMBDA_IAM_ROLE_ARN"
  val timeout = "AWS_LAMBDA_TIMEOUT"
  val memory = "AWS_LAMBDA_MEMORY"
}
