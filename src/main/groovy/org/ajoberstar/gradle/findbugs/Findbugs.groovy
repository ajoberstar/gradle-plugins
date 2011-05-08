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
import org.apache.tools.ant.types.Path
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.VerificationTask

/**
 * <p>
 * Gradle task that runs a Findbugs analysis on your code.    
 * </p>
 * <p>
 * The main code of this is based on the Findbugs Ant task.  This was
 * bypassed in order to make this task more flexible.
 * </p>
 * <p>
 * See {link: http://findbugs.sourceforge.net/} for more information.
 * </p>
 * @author Andrew Oberstar
 * @version 0.1.0
 * @since 0.1.0
 */
class Findbugs extends SourceTask implements VerificationTask {
	@Input FileCollection classpath = null
	@Input FileCollection classes = null
	@Input List<String> findbugsProps = ['-sortByClass', '-timestampNow']
	@Input Map<String, String> systemProps = [:]
	boolean ignoreFailures = false
	
	@OutputFile File resultsFile = null
	
	/**
	 * Runs the Findbugs analysis on the code.
	 * <ul>
	 * <li>{@code source} is used as the {@code -sourcepath} parameter</li>
	 * <li>{@code classes} is used as the class location parameter</li>
	 * <li>{@code classpath} is used as the {@code -auxclasspath} parameter</li>
	 * <li>{@code findbugsProps} are passed directly to findbugs, in order</li>
	 * <li>{@code systemProps} are passed as JVM arguments</li> 
	 * </ul>
	 *
	 */
	@TaskAction
	public void check() {
		getResultsFile().parentFile.mkdirs()
		Java findbugs = new Java()
		findbugs.project = getAnt().getProject()
		findbugs.taskName = this.name
		findbugs.classname = 'edu.umd.cs.findbugs.FindBugs2'
		findbugs.fork = true
		findbugs.failOnError = false
		//findbugs.timeout = this.timeout
		findbugs.classpath = new Path(getAnt().getProject(), project.configurations[FindbugsPlugin.FINDBUGS_CONFIGURATION_NAME].asPath)
		systemProp 'findbugs.home', project.configurations[FindbugsPlugin.FINDBUGS_CONFIGURATION_NAME].asPath
		this.systemProps.each { name, value ->
			findbugs.createJvmarg().value = "-D${name}=${value}"	
		}
		
		if (getClasspath().files.size() > 0) {
			findbugsProp '-auxclasspath', getClasspath().asPath
		}
		findbugsProp '-sourcepath', getSource().asPath
		findbugsProp '-xml'
		findbugsProp '-outputFile', getResultsFile().canonicalPath
		findbugsProp '-exitcode'
		findbugsProp getClasses().asPath
		this.findbugsProps.each {
			findbugs.createArg().value = it
		}
		
		def rc = findbugs.executeJava();
		
		if ((rc & 0x4) != 0) {
			throw new Exception('Execution of findbugs failed.')
		} else if ((rc & 0x2) != 0) {
			this.logger.warn('Classes needed for analysis were missing')
		}
		if (!ignoreFailures && ((rc & 0x1) != 0)) {
			throw new Exception('Findbugs reported warnings.')
		}
	}
	
	/**
	 * Adds a property to the end of the list of 
	 * findbugs properties.
	 * @param props one or more {@code String} properties to add
	 */
	void findbugsProp(String... props) {
		findbugsProps.addAll(props)
	}
	
	/**
	 * Adds a system property to the map of system properties.
	 * These are treated as JVM arguments
	 * 
	 * @param name the name of the system property
	 * @param value the value of the system property
	 */
	void systemProp(String name, String value) {
		this.systemProps[name] = value
	}
	
	/**
	 * {@inheritDoc} 
	 */
	@Override
	VerificationTask setIgnoreFailures(boolean ignoreFailures) {
		this.ignoreFailures = ignoreFailures
		return this
	}
}