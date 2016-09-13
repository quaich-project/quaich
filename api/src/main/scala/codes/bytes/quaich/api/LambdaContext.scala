package codes.bytes.quaich.api

import com.amazonaws.services.lambda.runtime.{ClientContext, CognitoIdentity, LambdaLogger, Context }

class LambdaContext(ctx: Context) {
  def identity: Option[CognitoIdentity] = Option(ctx.getIdentity)

  def clientContext: Option[ClientContext] = Option(ctx.getClientContext)

  def logger: LambdaLogger = ctx.getLogger

  def memoryLimitInMB: Int = ctx.getMemoryLimitInMB

  def remainingTimeInMillis: Int = ctx.getRemainingTimeInMillis

  def awsRequestId: String = ctx.getAwsRequestId

  def functionName: String = ctx.getFunctionName


  def logGroupName: Option[String] = Option(ctx.getLogGroupName)

  def logStreamName: Option[String] = Option(ctx.getLogStreamName)
}

// vim: set ts=2 sw=2 sts=2 et:
