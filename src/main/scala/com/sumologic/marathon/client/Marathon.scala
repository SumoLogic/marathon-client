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
import spray.http._

import scala.concurrent.ExecutionContext

/**
 * Marathon client using spray.io http client.
 * Requires an actor system to be running.
 */
class Marathon(baseUri: Uri, auth: Option[BasicHttpCredentials] = None, defaultHeaders: Set[HttpHeader] = Marathon.DefaultHeaders)
              (implicit val system: ActorSystem, implicit val executor: ExecutionContext, implicit val timeout: Timeout) {

  private val client = new RestClient(baseUri, auth, defaultHeaders)

  val apps = new Apps(client)
  val deployments = new Deployments(client)
  val groups = new Groups(client)
  val queues = new Queues(client)
  val serverInfo = new ServerInfo(client)
  val tasks = new Tasks(client)
}

private[client] object Marathon {
  val ApiVersion = Uri.Path("v2")

  object Paths {
    val Apps        = ApiVersion / "apps"
    val Deployments = ApiVersion / "deployments"
    val Groups      = ApiVersion / "groups"
    val Info        = ApiVersion / "info"
    val Leader      = ApiVersion / "leader"
    val Queues      = ApiVersion / "queue"
    val Tasks       = ApiVersion / "tasks"
  }

  val DefaultHeaders: Set[HttpHeader] = Set(
    HttpHeaders.Accept(MediaTypes.`application/json`),
    HttpHeaders.`Content-Type`(ContentTypes.`application/json`)
  )
}