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

import com.amazonaws.{AmazonServiceException, AmazonClientException}
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClient
import com.amazonaws.services.identitymanagement.model.{CreateRoleRequest, Role}

import scala.util.{Failure, Success, Try}

private[quartercask] object AwsIAM {

  val BasicLambdaRoleName = "lambda_basic_execution"

  lazy val iamClient = new AmazonIdentityManagementClient(AwsCredentials.provider)

  def basicLambdaRole(): Option[Role] = {
    import scala.collection.JavaConverters._
    val existingRoles = iamClient.listRoles().getRoles.asScala

    existingRoles.find(_.getRoleName == BasicLambdaRoleName)
  }

  def createBasicLambdaRole(): Try[RoleARN] = {
    val createRoleRequest = {
      val policyDocument = """{"Version":"2012-10-17","Statement":[{"Sid":"","Effect":"Allow","Principal":{"Service":"lambda.amazonaws.com"},"Action":"sts:AssumeRole"}]}"""
      val c = new CreateRoleRequest
      c.setRoleName(BasicLambdaRoleName)
      c.setAssumeRolePolicyDocument(policyDocument)
      c
    }

    try {
      val result = iamClient.createRole(createRoleRequest)
      Success(RoleARN(result.getRole.getArn))
    } catch {
      case ex @ (_ : AmazonClientException |
                 _ : AmazonServiceException) =>
        Failure(ex)
    }
  }
}
