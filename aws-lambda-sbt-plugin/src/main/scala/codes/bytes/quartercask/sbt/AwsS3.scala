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

import com.amazonaws.{AmazonClientException, AmazonServiceException}
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.{Bucket, CannedAccessControlList, PutObjectRequest}
import com.amazonaws.services.s3.transfer.TransferManager
import sbt._

import scala.util.{Failure, Success, Try}

private[quartercask] object AwsS3 {
  private lazy val client = new AmazonS3Client(AwsCredentials.provider)

  def pushJarToS3(jar: File, bucketId: S3BucketId, s3KeyPrefix: String): Try[S3Key] = {
    try{
      val key = s3KeyPrefix + jar.getName
      val tx = new TransferManager()

      val upload = tx.upload(bucketId.value, key, jar)

      // You can poll your transfer's status to check its progress

      if (!upload.isDone) {
        System.out.println(s"Transfer: ${upload.getDescription}")
        System.out.println(s"  - State: ${upload.getState}")
        System.out.println(s"  - Progress: ${upload.getProgress.getBytesTransferred}")
      }

      // Transfers also allow you to set a <code>ProgressListener</code> to receive
      // asynchronous notifications about your transfer's progress.
      //upload.addProgressListener(myProgressListener)

      // Or you can block the current thread and wait for your transfer to
      // to complete. If the transfer fails, this method will throw an
      // AmazonClientException or AmazonServiceException detailing the reason.
      //upload.waitForCompletion()

      // After the upload is complete, call shutdownNow to release the resources.
      tx.shutdownNow()

      tx.getAmazonS3Client.setBucketAcl(bucketId.value, CannedAccessControlList.AuthenticatedRead)

      Success(S3Key(key))
    } catch {
      case ex @ (_ : AmazonClientException |
                 _ : AmazonServiceException) =>
        Failure(ex)
    }
  }

  def getBucket(bucketId: S3BucketId): Option[Bucket] = {
    import scala.collection.JavaConverters._
    client.listBuckets().asScala.find(_.getName == bucketId.value)
  }

  def createBucket(bucketId: S3BucketId): Try[S3BucketId] = {
    try{
      client.createBucket(bucketId.value)
      Success(bucketId)
    } catch {
      case ex @ (_ : AmazonClientException |
                 _ : AmazonServiceException) =>
        Failure(ex)
    }
  }
}
