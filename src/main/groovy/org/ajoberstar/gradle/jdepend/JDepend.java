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
package org.ajoberstar.gradle.jdepend;

import java.io.File;

import org.ajoberstar.gradle.jdepend.internal.AntJDepend;
import org.gradle.api.DefaultTask;
import org.gradle.api.Task;
import org.gradle.api.specs.Spec;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.VerificationTask;

/**
 * @author Andy
 *
 */
public class JDepend extends DefaultTask implements VerificationTask {
	private File classesDir;
	private File resultsFile;
	private boolean ignoreFailures;
	
	private AntJDepend antJDepend = new AntJDepend();
	
	public JDepend() {
		Spec<Task> spec = new Spec<Task>() {
			public boolean isSatisfiedBy(Task task) {
				if (task instanceof JDepend) {
					return ((JDepend) task).getClassesDir().exists();	
				} else {
					return false;
				}
			}
		};
		this.onlyIf(spec);
	}
	
	@TaskAction
	void check() {
		antJDepend.call(getAnt(), getProject(), getClassesDir(), getResultsFile(), isIgnoreFailures());
	}

	/**
	 * @return the classesDir
	 */
	@InputDirectory
	public File getClassesDir() {
		return classesDir;
	}

	/**
	 * @param classesDir the classesDir to set
	 */
	public void setClassesDir(File classesDir) {
		this.classesDir = classesDir;
	}

	/**
	 * @return the resultsFile
	 */
	@OutputFile
	public File getResultsFile() {
		return resultsFile;
	}

	/**
	 * @param resultsFile the resultsFile to set
	 */
	public void setResultsFile(File resultsFile) {
		this.resultsFile = resultsFile;
	}

	/**
	 * @return the ignoreFailures
	 */
	public boolean isIgnoreFailures() {
		return ignoreFailures;
	}

	/**
	 * @param ignoreFailures the ignoreFailures to set
	 */
	public VerificationTask setIgnoreFailures(boolean ignoreFailures) {
		this.ignoreFailures = ignoreFailures;
		return this;
	}
	
}
