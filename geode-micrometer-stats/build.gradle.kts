/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.2.61"
}

group = "org.apache.geode"
version = "1.8.0-SNAPSHOT"

repositories {
    maven { setUrl("http://dl.bintray.com/kotlin/kotlin-eap") }
    maven { setUrl("file:///home/ukohlmeyer/.m2/repository") }
    mavenCentral()
    mavenLocal()
}

dependencies {
    compile( project(":geode-stats-common"))
    compile(kotlin("stdlib-jdk8"))
    compile(kotlin("stdlib"))
    compile(kotlin("reflect"))
    compile(group = "io.micrometer", name = "micrometer-core", version = "1.0.6")
    compile(group = "io.micrometer", name = "micrometer-registry-influx", version = "1.0.6")
    compile(group = "io.micrometer", name = "micrometer-registry-jmx", version = "1.0.6")
    compile(group = "io.micrometer", name = "micrometer-registry-prometheus", version = "1.0.6")
    compile(group = "test", name = "micrometer-stats", version = "1.0-SNAPSHOT")

}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.suppressWarnings = true
}