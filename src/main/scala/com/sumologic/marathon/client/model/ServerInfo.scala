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

case class ServerInfo(frameworkId: String,
                      leader: String,
                      http_config: HttpConfig,
                      event_subscriber: EventSubscriber,
                      marathon_config: MarathonConfig,
                      name: String,
                      version: String,
                      zookeeper_config: ZookeeperConfig)

case class HttpConfig(assets_path: String, http_port: Int, https_port: Int)

case class EventSubscriber(`type`: String, http_endpoints: Seq[String])

case class MarathonConfig(checkpoint: Boolean,
                          executor: String,
                          failover_timeout: Int,
                          ha: Boolean,
                          hostname: String,
                          local_port_max: Int,
                          local_port_min: Int,
                          master: String,
                          mesos_role: String,
                          mesos_user: String,
                          reconciliation_initial_delay: Int,
                          reconciliation_interval: Int,
                          task_launch_timeout: Int)

case class ZookeeperConfig(zk: String,
                           zk_timeout: Int,
                           zk_session_timeout: Int,
                           zk_max_version: Int)

case class Leader(leader: String)

case class Message(message: String)