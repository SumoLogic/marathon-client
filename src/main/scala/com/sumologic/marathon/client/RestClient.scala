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

// non pipeline imports
import akka.actor.ActorSystem
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout
import com.sumologic.marathon.client.model.Empty
import spray.can.Http
import spray.http.HttpMethods._
import spray.http._
import spray.httpx.marshalling.{Marshaller, _}
import spray.httpx.unmarshalling.Unmarshaller
import spray.httpx.unmarshalling.Unmarshaller._

import scala.concurrent.{ExecutionContext, Future}

class RestClient private[client] (baseUri: Uri, auth: Option[BasicHttpCredentials], defaultHeaders: Set[HttpHeader])
                (implicit val system: ActorSystem, implicit val executor: ExecutionContext, implicit val timeout: Timeout) {

  private val basicAuth: Set[HttpHeader] = auth match {
    case Some(a) => Set(a.basicAuthorization)
    case None => Set.empty
  }

  private val requestHeaders = (defaultHeaders ++ basicAuth)

  private def http[Rqst,Rspns](method: HttpMethod,
                               relativePath: Uri.Path,
                               entity: Rqst,
                               params: Uri.Query = Uri.Query.Empty,
                               headers: List[HttpHeader])
                              (implicit um: Unmarshaller[Rspns], m: Marshaller[Rqst]): Future[Rspns] = {

    marshal(entity) match {
      case Right(value) => {
        val uri = baseUri withPath baseUri.path ++ relativePath withQuery params
        val h = requestHeaders ++ headers.toSet
        IO(Http).ask(HttpRequest(method, uri, headers = h.toList, entity = value)).mapTo[HttpResponse].flatMap { response =>
          unmarshal[Rspns](response.entity) match {
            case Right(value) => Future.successful(value)
            case Left(error) => Future.failed(new IllegalStateException(error.toString))
          }
        }
      }
      case Left(error) => Future.failed(new IllegalStateException(error.toString))
    }
  }

  def get[R](relativePath: Uri.Path, headers: List[HttpHeader], params: Uri.Query = Uri.Query.Empty)
            (implicit m: Marshaller[Empty], um: Unmarshaller[R]): Future[R] = {
    http[Empty, R](GET, relativePath, Empty(), params, headers)
  }

  def delete[R](relativePath: Uri.Path, headers: List[HttpHeader], params: Uri.Query = Uri.Query.Empty)
               (implicit m: Marshaller[Empty], um: Unmarshaller[R]): Future[R] = {
    http[Empty, R](DELETE, relativePath, Empty(), params, headers)
  }

  def post[Rqst, Rspsn](relativePath: Uri.Path, headers: List[HttpHeader], entity: Rqst, params: Uri.Query = Uri.Query.Empty)
                       (implicit m: Marshaller[Rqst], um: Unmarshaller[Rspsn]): Future[Rspsn] = {
    http[Rqst, Rspsn](POST, relativePath, entity, params, headers)
  }

  def put[Rqst, Rspsn](relativePath: Uri.Path, headers: List[HttpHeader], entity: Rqst, params: Uri.Query = Uri.Query.Empty)
                      (implicit m: Marshaller[Rqst], um: Unmarshaller[Rspsn]): Future[Rspsn] = {
    http[Rqst, Rspsn](PUT, relativePath, entity, params, headers)
  }
}
