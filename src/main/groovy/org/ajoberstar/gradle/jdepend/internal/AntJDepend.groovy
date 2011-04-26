/*
 * Copyright 2011 Andrew Oberstar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ajoberstar.gradle.jdepend.internal

import org.ajoberstar.gradle.jdepend.JDependPlugin
import org.gradle.api.Project

/**
 * @author Andy
 *
 */
class AntJDepend {
	def call(AntBuilder ant, Project project, File classesDir, File resultsFile, boolean ignoreFailures) {
		ant.taskdef(name:'jdepend', classname:'org.apache.tools.ant.taskdefs.optional.jdepend.JDependTask', classpath:project.configurations[JDependPlugin.JDEPEND_CONFIGURATION_NAME].asPath)
		ant.jdepend(format:'xml', outputFile:resultsFile, haltOnError:!ignoreFailures) {
			classespath {
				pathElement(location:classesDir)
			}
		}
	}
}
