[![Build Status](https://travis-ci.org/pdolega/quaich.svg)](https://travis-ci.org/pdolega/quaich)

# quaich

A Scala “Serverless” Microframework for AWS Lambda, inspired by Amazon's Chalice (https://github.com/awslabs/chalice)

## WTF is a "quaich"

Amazon named their version of the AWS Lamba Microframework “Chalice”, which references what we suppose is a perfectly acceptable drinking vessel.

Being partial to whisky, however, the quaich (pronounced as 'quake') – A Scottish two-handled drinking bowl, often used to serve whisky – sounded like a better inspiration. Sir Walter Scott was said to have served whisky from a quaich carved from the elm tree that served as the Duke of Wellington's command post at the Battle of Waterloo. Which is pretty awesome.

AWS Lambda is an interesting emerging platform for “serverless” programming. Entirely event driven, it provides an easy model to handle both ‘built-in’ Amazon events – such as DynamoDB & S3 Bucket changes, and HTTP calls through services such as Amazon's API Gateway... as well as custom events. Being “serverless”, Lambda allows us to drop in simple JVM code via assembly JAR that responds through an event loop, minimizing execution costs and eliminating the need to setup and maintain dedicated server instances.

quaich (pronounced ‘quake’) is a Scala microframework, inspired by the Python based [chalice](https://github.com/awslabs/chalice) released by Amazon recently. The concept is simple, single file applications that can receive and handle the JSON events pushed by the Lambda system. Through clever tricks with macros, etc. we provide an easy model for defining your routes files and even parsing custom variables.

# Authors

 * [Brendan McAdams](https://github.com/bwmcadams)
 * [Thomas Lockney](https://github.com/tlockney) (mostly just an _idea_ guy) 
 * [Pawel (Paul) Dolega](https://github.com/pdolega) (only following up on the idea these 2 above initiated) 

# TODO

- [Logging support](http://docs.aws.amazon.com/lambda/latest/dg/java-logging.html)
- Flexibility using both core, structured base handler APIs such as Amazon provides, and customizable ones.
- Support for multiple pluggable JSON libraries, because Scala has about 64k of them and everyone has their own preference
- [Magnet pattern](http://spray.io/blog/2012-12-13-the-magnet-pattern/) for HTTP Responses vs. type classes?
- sbt deployment plugin
- Support for POJO translation
- Support for S3 & DynamoDB Triggers
- Offline simulator for easier testing / dev. There's an [interesting one for Node](https://github.com/dherault/serverless-offline)
- Explore support for Alexa/Amazon Echo integration, as well as IoT buttons. Thanks a ton to [Dick Wall](https://github.com/dickwall) for the truly superb idea. And deep Pinky & the Brain Knowledge.

## Notes

Permissions I've been setting on my roles:

- AmazonAPIGatewayPushToCloudWatchLogs
- AmazonS3ReadOnlyAccess
- AWSLambdaExecute

