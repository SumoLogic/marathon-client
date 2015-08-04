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
import spray.http._
import spray.httpx.SprayJsonSupport._

import scala.concurrent.{ExecutionContext, Future}

class ServerInfo private[client] (client: RestClient)
                                 (implicit val system: ActorSystem, implicit val executor: ExecutionContext, implicit val timeout: Timeout) {
  // Get info about the Marathon Instance.
  def get(headers: List[HttpHeader] = List.empty): Future[model.ServerInfo] = {
    client.get[model.ServerInfo](Marathon.Paths.Info, headers)
  }

  // Returns the current leader.
  def leader(headers: List[HttpHeader] = List.empty): Future[Leader] = {
    client.get[Leader](Marathon.Paths.Leader, headers)
  }

  // Causes the current leader to abdicate, triggering a new election.
  def releaseLeader(headers: List[HttpHeader] = List.empty): Future[Message] = {
    client.delete[Message](Marathon.Paths.Leader, headers)
  }
}
