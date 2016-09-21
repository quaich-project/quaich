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

import sbt._

import scala.util.{Either, Failure, Success}

object AwsLambdaPlugin extends AutoPlugin {

  object autoImport {
    val deployLambda = taskKey[Map[String, LambdaARN]](
      "Create or Update the AWS Lambda project, with any appropriate trigger types & metadata.")

    val s3Bucket = settingKey[Option[String]]("ID of an S3 Bucket to upload the deployment jar to.")
    val s3KeyPrefix = settingKey[String]("A prefix to the S3 key to which the jar will be " +
      "uploaded.")
    val lambdaName = settingKey[Option[String]]("Name of the AWS Lambda to update or create.")
    val handlerName = settingKey[Option[String]](
      "Code path to the code for AWS Lambda  to execute, in form of <class::function>.")
    val roleArn = settingKey[Option[String]](
      "ARN of the IAM role with which to configure the Lambda function.")
    val region = settingKey[Option[String]](
      "Name of the AWS region to setup the Lambda function in.")
    val awsLambdaTimeout = settingKey[Option[Int]](
      "In seconds, the Lambda function's timeout length (1-300).")
    val awsLambdaMemory = settingKey[Option[Int]](
      "How much memory (in MB) to allocate to execution of the Lambda function (128-1536, " +
        "multiple of 64).")
    val lambdaHandlers = settingKey[Seq[(String, String)]](
      "A sequence of name to lambda function pairs, if you want multiple handlers in one jar.")
  }

  import autoImport._

  override def requires = sbtassembly.AssemblyPlugin


  override lazy val projectSettings = Seq(
    deployLambda := doDeployLambda(
      region = region.value,
      jar = sbtassembly.AssemblyKeys.assembly.value,
      s3Bucket = s3Bucket.value,
      s3KeyPrefix = s3KeyPrefix.?.value,
      lambdaName = lambdaName.value,
      handlerName = handlerName.value,
      lambdaHandlers = lambdaHandlers.value,
      roleArn = roleArn.value,
      timeout = awsLambdaTimeout.value,
      memory = awsLambdaMemory.value
    ),
    s3Bucket := None,
    lambdaName := Some(sbt.Keys.name.value),
    handlerName := None,
    lambdaHandlers := List.empty[(String, String)],
    roleArn := None,
    region := Some("us-east-1"),
    awsLambdaMemory := None,
    awsLambdaTimeout := None
  )


  private def doDeployLambda(
    region: Option[String],
    jar: File,
    s3Bucket: Option[String],
    s3KeyPrefix: Option[String],
    lambdaName: Option[String],
    handlerName: Option[String],
    lambdaHandlers: Seq[(String, String)],
    roleArn: Option[String],
    timeout: Option[Int],
    memory: Option[Int]): Map[String, LambdaARN] = {
    val resolvedRegion = resolveRegion(region)
    val resolvedLambdaHandlers = resolveLambdaHandlers(lambdaName, handlerName, lambdaHandlers)
    val resolvedRoleName = resolveRoleARN(roleArn)
    val resolvedBucketId = resolveBucketId(s3Bucket)
    val resolvedS3KeyPrefix = resolveS3KeyPrefix(s3KeyPrefix)
    val resolvedTimeout = resolveTimeout(timeout)
    val resolvedMemory = resolveMemory(memory)

    AwsS3.pushJarToS3(jar, resolvedBucketId, resolvedS3KeyPrefix) match {
      case Success(s3Key) =>
        for ((resolvedLambdaName, resolvedHandlerName) <- resolvedLambdaHandlers) yield {
          AwsLambda.deployLambda(resolvedRegion, jar, resolvedLambdaName, resolvedHandlerName, resolvedRoleName, resolvedBucketId, s3Key, resolvedTimeout, resolvedMemory) match {
          case Success(Left(deployFunctionCodeResult)) =>
            resolvedLambdaName.value -> LambdaARN(deployFunctionCodeResult.getFunctionArn)
          case Failure(exception) =>
            sys
              .error(
                s"Failed to create lambda function: ${
                  exception
                    .getLocalizedMessage
                }\n${exception.getStackTraceString}")
          }
        }
      case Failure(exception) =>
        sys
          .error(
            s"Error upload jar to S3 lambda: ${exception.getLocalizedMessage}\n${
              exception
                .getStackTraceString
            }")
    }
  }


  private def resolveRegion(sbtSettingValueOpt: Option[String]): Region =
    sbtSettingValueOpt orElse sys.env.get(EnvironmentVariables.region) map Region getOrElse
      promptUserForRegion()


  private def resolveBucketId(sbtSettingValueOpt: Option[String]): S3BucketId =
    sbtSettingValueOpt orElse sys.env.get(EnvironmentVariables.bucketId) map S3BucketId getOrElse
      promptUserForS3BucketId()


  private def resolveS3KeyPrefix(sbtSettingValueOpt: Option[String]): String =
    sbtSettingValueOpt orElse sys.env.get(EnvironmentVariables.s3KeyPrefix) getOrElse ""


  private def resolveLambdaHandlers(
    lambdaName: Option[String], handlerName: Option[String],
    lambdaHandlers: Seq[(String, String)]): Map[LambdaName, HandlerName] = {
    val lhs = if (lambdaHandlers.nonEmpty) {
      lambdaHandlers.iterator
    }
    else {
      val l = lambdaName
        .getOrElse(sys.env.getOrElse(EnvironmentVariables.lambdaName, promptUserForFunctionName()))
      val h = handlerName
        .getOrElse(sys.env.getOrElse(EnvironmentVariables.handlerName, promptUserForHandlerName()))
      Iterator(l -> h)
    }
    lhs.map { case (l, h) => LambdaName(l) -> HandlerName(h) }.toMap
  }


  private def resolveRoleARN(sbtSettingValueOpt: Option[String]): RoleARN =
    sbtSettingValueOpt orElse sys.env.get(EnvironmentVariables.roleArn) map RoleARN getOrElse
      promptUserForRoleARN()


  private def resolveTimeout(sbtSettingValueOpt: Option[Int]): Option[Timeout] =
    sbtSettingValueOpt orElse sys.env.get(EnvironmentVariables.timeout).map(_.toInt) map Timeout


  private def resolveMemory(sbtSettingValueOpt: Option[Int]): Option[Memory] =
    sbtSettingValueOpt orElse sys.env.get(EnvironmentVariables.memory).map(_.toInt) map Memory


  private def promptUserForRegion(): Region = {
    val inputValue = readInput(
      s"Enter the name of the AWS region to connect to. (You also could have set the environment " +
        s"variable: ${
        EnvironmentVariables
          .region
      } or the sbt setting: region)")

    Region(inputValue)
  }


  private def promptUserForS3BucketId(): S3BucketId = {
    val inputValue = readInput(
      s"Enter the AWS S3 bucket where the lambda jar will be stored. (You also could have set the" +
        s" environment variable: ${
        EnvironmentVariables
          .bucketId
      } or the sbt setting: s3Bucket)")
    val bucketId = S3BucketId(inputValue)

    AwsS3.getBucket(bucketId) map (_ => bucketId) getOrElse {
      val createBucket = readInput(s"Bucket $inputValue does not exist. Create it now? (y/n)")

      if (createBucket == "y") {
        AwsS3.createBucket(bucketId) match {
          case Success(createdBucketId) =>
            createdBucketId
          case Failure(th) =>
            println(s"Failed to create S3 bucket: ${th.getLocalizedMessage}")
            promptUserForS3BucketId()
        }
      }
      else {
        promptUserForS3BucketId()
      }
    }
  }


  private def promptUserForFunctionName(): String =
    readInput(
      s"Enter the name of the AWS Lambda. (You also could have set the environment variable: ${
        EnvironmentVariables
          .lambdaName
      } or the sbt setting: lambdaName)")


  private def promptUserForHandlerName(): String =
    readInput(
      s"Enter the name of the AWS Lambda handler. (You also could have set the environment " +
        s"variable: ${
        EnvironmentVariables
          .handlerName
      } or the sbt setting: handlerName)")


  private def promptUserForRoleARN(): RoleARN = {
    AwsIAM.basicLambdaRole() match {
      case Some(basicRole) =>
        val reuseBasicRole = readInput(
          s"IAM role '${
            AwsIAM
              .BasicLambdaRoleName
          }' already exists. Reuse this role? (y/n)")

        if (reuseBasicRole == "y") {
          RoleARN(basicRole.getArn)
        }
        else {
          readRoleARN()
        }
      case None =>
        val createDefaultRole = readInput(s"Default IAM role for AWS Lambda has not been created " +
          s"yet. Create this role now? (y/n)")

        if (createDefaultRole == "y") {
          AwsIAM.createBasicLambdaRole() match {
            case Success(createdRole) =>
              createdRole
            case Failure(th) =>
              println(s"Failed to create role: ${th.getLocalizedMessage}")
              promptUserForRoleARN()
          }
        } else {
          readRoleARN()
        }
    }
  }


  private def readRoleARN(): RoleARN = {
    val inputValue = readInput(
      s"Enter the ARN of the IAM role for the Lambda. (You also could have set the environment variable: ${
        EnvironmentVariables
          .roleArn
      } or the sbt setting: roleArn)")
    RoleARN(inputValue)
  }


  private def readInput(prompt: String): String =
    SimpleReader.readLine(s"$prompt\n") getOrElse {
      val badInputMessage = "Unable to read input"

      val updatedPrompt = if (prompt.startsWith(badInputMessage)) prompt else s"$badInputMessage\n$prompt"

      readInput(updatedPrompt)
    }

}
