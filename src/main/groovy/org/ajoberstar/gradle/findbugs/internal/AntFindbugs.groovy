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
package org.ajoberstar.gradle.findbugs.internal

import groovy.util.AntBuilder;

import java.io.File;

import org.ajoberstar.gradle.findbugs.FindbugsPlugin;
import org.gradle.api.Project;
import org.gradle.api.file.FileCollection

/**
 * @author Andy
 *
 */
class AntFindbugs {
	def call(AntBuilder ant, Project project, FileCollection source, File classesDir, File resultsFile, boolean ignoreFailures) {
		ant.taskdef(name:'findbugs', classname:'edu.umd.cs.findbugs.anttask.FindBugsTask', classpath:project.configurations[FindbugsPlugin.FINDBUGS_CONFIGURATION_NAME].asPath)
		ant.findbugs(outputFile:resultsFile, warningsProperty:'foundBugs', failOnError:true, pluginList:'', classpath:project.configurations[FindbugsPlugin.FINDBUGS_CONFIGURATION_NAME].asPath) {
			auxClasspath(path:project.configurations.runtime.asPath)
			sourcePath(path:source.asPath)
			'class'(location:classesDir)
		}
		if (!ignoreFailures) {
			ant.fail(message:'FindBugs reported warnings.', if:'foundBugs')
		}
	}
}
