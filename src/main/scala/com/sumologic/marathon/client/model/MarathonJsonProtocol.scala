/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.sumologic.marathon.client.model

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

/**
 * Spray json format for marathon model classes.
 */
object MarathonJsonProtocol extends DefaultJsonProtocol {
  // misc formats
  implicit val generalResponseFormat = jsonFormat2(GeneralResponse)

  // app formats
  implicit val upgradeStrategyFormat = jsonFormat2(UpgradeStrategy)
  implicit val commandFormat = jsonFormat1(Command)
  implicit val healthCheckFormat = jsonFormat8(HealthCheck)
  implicit val volumeFormat = jsonFormat3(Volume)
  implicit val parameterFormat = jsonFormat2(Parameter)
  implicit val portFormat = jsonFormat4(Port)
  implicit val dockerFormat = jsonFormat5(Docker)
  implicit val containerFormat = jsonFormat3(Container)
  implicit val appFormat = jsonFormat21(App)
  implicit val appListFormat = jsonFormat1(AppList)
  implicit val singleAppFormat = jsonFormat1(SingleApp)
  implicit val versionListFormat = jsonFormat1(VersionList)

  // deployment formats
  implicit val deploymentActionFormat = jsonFormat2(DeploymentAction)
  implicit val deploymentFormat = jsonFormat4(Deployment)

  // group formats
  implicit val groupFormat: RootJsonFormat[Group] = rootFormat(lazyFormat(jsonFormat5(Group)))
  implicit val versionFormat = jsonFormat1(Version)
  implicit val actionFormat = jsonFormat2(Action)
  implicit val stepFormat = jsonFormat1(Step)
  implicit val stepListFormat = jsonFormat1(StepList)

  // queue formats
  implicit val delayFormat = jsonFormat2(Delay)
  implicit val queueFormat = jsonFormat3(Queue)
  implicit val queueListFormat = jsonFormat1(QueueList)

  // task formats
  implicit val taskFormat = jsonFormat8(Task)
  implicit val taskListFormat = jsonFormat1(TaskList)

  // format for empty request or response
  implicit val emptyFormat = jsonFormat0(Empty)
}
