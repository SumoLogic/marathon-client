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
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.sumologic.marathon.client.model.TaskList
import spray.can.Http
import spray.http.HttpMethods._
import spray.http._
import spray.httpx.unmarshalling.Unmarshaller._
import spray.httpx.unmarshalling._

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

/**
 * Marathon client using spray.io http client.
 * Requires an actor system to be running.
 */
class Marathon(baseUrl: Uri)
              (implicit val system: ActorSystem, implicit val executor: ExecutionContext, implicit val timeout: Timeout) {

  import com.sumologic.marathon.client.model.MarathonJsonProtocol._
  import spray.httpx.SprayJsonSupport._

  def tasks: Future[TaskList] = {
    get[TaskList]("/v2/tasks", List(HttpHeaders.Accept(MediaTypes.`application/json`)))
  }

  private def get[T](relativePath: String, headers: List[HttpHeader] = List.empty)(implicit um: Unmarshaller[T]): Future[T] = {
    IO(Http).ask(HttpRequest(GET, baseUrl.copy(path = baseUrl.path + relativePath), headers = headers)).mapTo[HttpResponse].flatMap { response =>
      unmarshal[T](response.entity) match {
        case Right(value) => Future.successful(value)
        case Left(error) => Future.failed(new IllegalStateException(error.toString))
      }
    }
  }

}
