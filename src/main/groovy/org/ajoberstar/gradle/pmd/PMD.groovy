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
package org.ajoberstar.gradle.pmd

import java.io.File
import java.util.Set

import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.VerificationTask

/**
 * @author Andrew Oberstar
 *
 */
class PMD extends SourceTask implements VerificationTask {	
	@Input Set<File> rulesets
	boolean ignoreFailures
	
	@OutputFile File resultsFile
	@OutputFile File reportsFile
	
	@TaskAction
	void check() {
		ant.taskdef(name:'pmd', classname:'net.sourceforge.pmd.ant.PMDTask', classpath:project.configurations[PMDPlugin.PMD_CONFIGURATION_NAME].asPath)
		ant.pmd(failOnRuleViolation:!ignoreFailures) {
			source.addToAntBuilder(ant, 'fileset', FileCollection.AntType.FileSet)
			rulesets.each {
				ruleset(it)
			}
			formatter(type:'html', toFile:reportsFile)
			formatter(type:'xml', toFile:resultsFile)
		}
	}
	
	VerificationTask setIgnoreFailures(boolean ignoreFailures) {
		this.ignoreFailures = ignoreFailures
		return this
	}
}
