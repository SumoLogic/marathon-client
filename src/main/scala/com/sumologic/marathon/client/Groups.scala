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

class Groups private[client] (client: RestClient)
                             (implicit val system: ActorSystem, implicit val executor: ExecutionContext, implicit val timeout: Timeout) {
  // List all groups.
  def list(headers: List[HttpHeader] = List.empty): Future[Group] = {
    client.get[Group](Marathon.Paths.Groups, headers = headers)
  }

  // List the group with the specified `groupId`.
  def listGroup(groupId: String, headers: List[HttpHeader] = List.empty): Future[Group] = {
    val relativePath = Marathon.Paths.Groups / groupId
    client.get[Group](relativePath, headers = headers)
  }

  /*
   * Create and start a new application group. Application groups can contain
   * other application groups. An application group can either hold other groups
   * or applications, but can not be mixed in one.
   *
   * Since the deployment of the group can take a considerable amount of time,
   * this endpoint returns immediately with a version. The failure or success of
   * the action is signalled via event. There is a `group_change_success` and
   * `group_change_failed` event with the given version.
   */
  def create(group: Group, headers: List[HttpHeader] = List.empty): Future[Version] = {
    client.post[Group, Version](Marathon.Paths.Groups, group, headers = headers)
  }

  // Change parameters of a deployed application group of `groupId`.
  def update(groupId: String, group: Group, headers: List[HttpHeader] = List.empty): Future[GeneralResponse] = {
    val relativePath = Marathon.Paths.Groups / groupId
    client.post[Group, GeneralResponse](relativePath, group, headers = headers)
  }

  // Scale a group by `groupId`.
  def scale(scaleBy: Float, groupId: String, headers: List[HttpHeader] = List.empty): Future[GeneralResponse] = {
    val relativePath = Marathon.Paths.Groups / groupId
    val ent = Map("scaleBy" -> scaleBy)
    client.put[Map[String, Float], GeneralResponse](relativePath, ent, headers = headers)
  }

  // Rollback a group.
  def rollback(groupId: String, version: String, headers: List[HttpHeader] = List.empty): Future[GeneralResponse] = {
    val relativePath = Marathon.Paths.Groups / groupId
    val ent = Map("version" -> version)
    client.put[Map[String, String], GeneralResponse](relativePath, ent, headers = headers)
  }

  // Deployment dry run.
  def dryRun(group: Group, headers: List[HttpHeader] = List.empty): Future[StepList] = {
    val relativePath = Marathon.Paths.Groups / group.id
    val params = Uri.Query("dryRun" -> true.toString)
    client.put[Group, StepList](relativePath, group, params, headers)
  }

  // Destroy a group by `groupId`. All data about that group and all
  // associated applications will be deleted.
  def delete(groupId: String, headers: List[HttpHeader] = List.empty): Future[Version] = {
    val relativePath = Marathon.Paths.Groups / groupId
    client.delete[Version](relativePath, headers = headers)
  }
}
