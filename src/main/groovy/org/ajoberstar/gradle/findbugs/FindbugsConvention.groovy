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
package org.ajoberstar.gradle.findbugs

import org.gradle.api.Project
import org.gradle.api.internal.project.ProjectInternal

/**
 * Convention specifying defaults for
 * the Findbugs plugin.
 * 
 * @author Andrew Oberstar
 * @version 0.1.0
 * @see FindbugsPlugin
 * @since 0.1.0
 */
class FindbugsConvention {
	private ProjectInternal project
	
	/**
	 * The name of the directory to use for
	 * Findbugs results.
	 */
	String resultsDirName
	
	/**
	 * Creates a convention instance tied
	 * to the specified project.
	 * 
	 * Defaults the {@code resultsDirName} to "findbugs"
	 * @param project
	 */
	FindbugsConvention(Project project) {
		this.project = project
		resultsDirName = 'findbugs'
	}
	
	/**
	 * Gets the directory to be used for Findbugs results. This is determined
	 * using the {@code resultsDirName} property, evaluated relative to the
	 * project's build directory.
	 * @return the results dir for Findbugs
	 */
	File getResultsDir() {
		project.fileResolver.withBaseDir(project.buildDir).resolve(resultsDirName)
	}
}
