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
package org.ajoberstar.gradle.findbugs;

import java.io.File
import java.util.Map

import org.apache.tools.ant.taskdefs.Java
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.VerificationTask

/**
 * @author Andrew Oberstar
 */
class Findbugs extends SourceTask implements VerificationTask {
	@InputDirectory File classesDir = null
	@Input Map<String, String> systemProps = [:]
	boolean ignoreFailures = false
	
	@OutputFile File resultsFile = null
	@OutputFile File reportsFile = null
	
	@TaskAction
	public void check() {
		Java findbugs = new Java();
		findbugs.taskName = this.name
		findbugs.fork = true
		//findbugs.timeout = this.timeout
		
		systemProp 'findbugs.home', project.configurations[FindbugsPlugin.FINDBUGS_CONFIGURATION_NAME].asPath
		this.systemProps.each { name, value ->
			findbugs.createJvmarg().value = "-D${name}=${value}"	
		}
		
		findbugs.mainClass = 'edu.umd.cs.findbugs.FindBugs2'
		
		def rc = findbugs.executeJava();
	}
	
	void systemProp(String name, String value) {
		this.systemProps.put(name, value)
	}
	
	VerificationTask setIgnoreFailures(boolean ignoreFailures) {
		this.ignoreFailures = ignoreFailures
		return this
	}
}