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

// https://mesosphere.github.io/marathon/docs/rest-api.html#apps
case class App(id: String,
               cmd: String,
               disk: Float,
               cpus: Float,
               mem: Float,
               instances: Int,
               args: Option[Seq[String]] = None,
               ports: Option[Seq[Int]] = None,
               requirePorts: Option[Boolean] = None,
               executor: Option[String] = None,
               container: Option[Container] = None,
               env: Option[Map[String, String]] = None,
               constraints: Option[Seq[Seq[String]]] = None,
               acceptedResourceRoles: Option[List[String]] = None,
               labels: Option[Map[String, String]] = None,
               uris: Option[Seq[String]] = None,
               dependencies: Option[Seq[String]] = None,
               healthChecks: Option[Seq[HealthCheck]] = None,
               backoffSeconds: Option[Int] = None,
               backoffFactor: Option[Float] = None,
               maxLaunchDelaySeconds: Option[Int] = None,
               upgradeStrategy: Option[UpgradeStrategy] = None)

case class AppList(apps: Seq[App])

case class SingleApp(app: App)

case class Container(`type`: String, docker: Docker, volumes: Seq[Volume])

case class Docker(image: String,
                  network: String,
                  portMappings: Seq[Port],
                  privileged: Boolean,
                  parameters: Seq[Parameter])

case class Port(containerPort: Int, hostPort: Int, servicePort: Int, protocol: String)

case class Parameter(key: String, value: String)

case class Volume(containerPath: String, hostPath: String, mode: String)

case class HealthCheck(protocol: String,
                       path: String,
                       gracePeriodSeconds: Int,
                       intervalSeconds: Int,
                       portIndex: Int,
                       timouteSeconds: Int,
                       maxConsecutiveFailures: Int,
                       command: Command)

case class Command(value: String)

case class UpgradeStrategy(minimumHealthCapacity: Float, maximumOverCapacity: Float)

case class VersionList(versions: Seq[String])

case class GeneralResponse(deploymentId: String, version: String)
