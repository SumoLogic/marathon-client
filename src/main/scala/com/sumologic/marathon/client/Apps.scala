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
package com.sumologic.marathon.client

import akka.actor.ActorSystem
import akka.util.Timeout
import com.sumologic.marathon.client.model.MarathonJsonProtocol._
import com.sumologic.marathon.client.model._
import org.parboiled.common.StringUtils
import spray.http._
import spray.httpx.SprayJsonSupport._

import scala.concurrent.{ExecutionContext, Future}

class Apps private[client] (client: RestClient)
          (implicit val system: ActorSystem, implicit val executor: ExecutionContext, implicit val timeout: Timeout) {

  // Create and start a new application.
  def create(app: App, headers: List[HttpHeader] = List.empty): Future[App] = {
    client.post[App, App](Marathon.Paths.Apps, app, headers = headers)
  }

  // List all running applications.
  def list(cmd: String = "", embed: String = "none", headers: List[HttpHeader] = List.empty): Future[AppList] = {
    val params = Uri.Query("cmd" -> cmd, "embed" -> embed)
    client.get[AppList](Marathon.Paths.Apps, params, headers)
  }

  // List the application with id `appId`.
  def app(appId: String, headers: List[HttpHeader] = List.empty): Future[SingleApp] = {
    val relativePath = Marathon.Paths.Apps / appId
    client.get[SingleApp](relativePath, headers = headers)
  }

  // List the versions of the application with id `appId`.
  def getAppVersion(appId: String, headers: List[HttpHeader] = List.empty): Future[VersionList] = {
    val relativePath = Marathon.Paths.Apps / appId / "versions"
    client.get[VersionList](relativePath, headers = headers)
  }

  //List the configuration of the application with id `appId` at version `version`.
  def appConfiguration(appId: String, version: String, headers: List[HttpHeader] = List.empty): Future[App] = {
    val relativePath = Marathon.Paths.Apps/ appId / "versions" / version
    client.get[App](relativePath, headers = headers)
  }

  // Change parameters of a running application with `appId`. The new application parameters
  // apply only to subsequently created tasks. Currently running tasks are
  // restarted, while maintaining the `minimumHealthCapacity`.
  def updateApp(appId: String, appUpdate: App, force: Boolean = false, headers: List[HttpHeader] = List.empty): Future[GeneralResponse] = {
    val params = Uri.Query("force" -> force.toString)
    val relativePath = Marathon.Paths.Apps / appId
    client.put[App, GeneralResponse](relativePath, appUpdate, params, headers)
  }

  // Initiates a rolling restart of all running tasks of the given `appId`. This
  // call respects the configured `minimumHealthCapacity`.
  def restart(appId: String, force: Boolean = false, headers: List[HttpHeader] = List.empty): Future[GeneralResponse] = {
    val relativePath = Marathon.Paths.Apps / appId / "restart"
    val params = Uri.Query("force" -> force.toString)
    client.post[Empty, GeneralResponse](relativePath, Empty(), params, headers)
  }

  // Destroy an application with `appId`. All data about that application will
  // be deleted.
  def delete(appId: String, headers: List[HttpHeader] = List.empty): Future[GeneralResponse] = {
    val relativePath = Marathon.Paths.Apps / appId
    client.delete[GeneralResponse](relativePath, headers = headers)
  }

  // List all running tasks for application `appId`.
  def appTasks(appId: String, headers: List[HttpHeader] = List.empty): Future[TaskList] = {
    val relativePath = Marathon.Paths.Apps / appId / "tasks"
    client.get[TaskList](relativePath, headers = headers)
  }

  // Kill tasks that belong to the application `appId`.
  def killAppTasks(appId: String, host: String = "none", scale: Boolean = false, headers: List[HttpHeader] = List.empty): Future[TaskList] = {
    val relativePath = Marathon.Paths.Apps / appId / "tasks"
    val params = Uri.Query("host" -> host, "scale" -> scale.toString)
    client.delete[TaskList](relativePath, params, headers)
  }

  // Kill the task with ID `taskId` that belongs to the application `appId`.
  def killAppTask(appId: String, taskId: String, scale: Boolean = false, headers: List[HttpHeader] = List.empty): Future[TaskList] = {
    val relativePath = Marathon.Paths.Apps / appId / "tasks" / taskId
    val params = Uri.Query("scale" -> scale.toString)
    client.delete[TaskList](relativePath, params, headers)
  }
}
