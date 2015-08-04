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

import spray.json.DefaultJsonProtocol

/**
 * Spray json format for marathon model classes.
 */
object MarathonJsonProtocol extends DefaultJsonProtocol {
  // task formats
  implicit val taskFormat = jsonFormat8(Task)
  implicit val taskListFormat = jsonFormat1(TaskList)
  implicit val taskKillListFormat = jsonFormat1(TaskKillList)

  // format for empty request or response
  implicit val emptyFormat = jsonFormat0(Empty)
}
