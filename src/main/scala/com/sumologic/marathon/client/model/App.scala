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
               args: Seq[String],
               cpus: Float,
               mem: Float,
               ports: Seq[Int],
               requirePorts: Boolean,
               instances: Int,
               executor: String,
               container: Container,
               env: Map[String, String],
               constraints: Seq[Seq[String]],
               acceptedResourceRoles: List[String],
               labels: Map[String, String],
               uris: Seq[String],
               dependencies: Seq[String],
               healthChecks: Seq[HealthCheck],
               backoffSeconds: Int,
               backoffFactor: Float,
               maxLaunchDelaySeconds: Int,
               upgradeStrategy: UpgradeStrategy)

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

case class AppResponse(deploymentId: String, version: String)
