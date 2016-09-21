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

import com.amazonaws.auth._

private[quartercask] object AwsCredentials {
  lazy val provider: AWSCredentialsProvider = new DefaultAWSCredentialsProviderChain()
}
