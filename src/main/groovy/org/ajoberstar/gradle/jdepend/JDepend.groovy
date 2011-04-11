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
package org.ajoberstar.gradle.jdepend

import org.ajoberstar.gradle.jdepend.internal.AntJDepend
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.VerificationTask
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.ConventionTask

/**
 * @author Andy
 *
 */
class JDepend extends ConventionTask implements VerificationTask {
	private FileCollection defaultClasses
	private File resultsFile
	private boolean ignoreFailures
	
	private AntJDepend antJdepend = new AntJDepend()
	
	@TaskAction
	void check() {
		antJdepend.jdepend(getAnt(), getDefaultClasses(), getResultsFile(), isIgnoreFailures())
	}
	
	FileCollection getDefaultClasses() {
		return defaultClasses;
	}
	
	void setDefaultClasses(FileCollection defaultClasses) {
		this.defaultClasses = defaultClasses
	}
	
	@OutputFile
	File getResultsFile() {
		return resultsFile
	}
	
	void setResultsFile(File resultsFile) {
		this.resultsFile = resultsFile
	}
	
	boolean isIgnoreFailures() {
		return ignoreFailures
	}
	
	JDepend setIgnoreFailures(boolean ignoreFailures) {
		this.ignoreFailures = ignoreFailures
		return this
	}
}
