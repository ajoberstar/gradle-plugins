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

import org.gradle.api.Project
import org.gradle.api.internal.project.ProjectInternal

/**
 * Convention specifying defaults for
 * the PMD plugin.
 * 
 * @author Andrew Oberstar
 * @version 0.1.0
 * @see PMDPlugin
 * @since 0.1.0
 */
class PMDConvention {
	private ProjectInternal project
	
	/**
	 * Location of the html report
	 */
	String reportsDirName
	
	/**
	 * Location of the xml results
	 */
	String resultsDirName
	
	/**
	 * Paths to ruleset files.
	 */
	Set<String> rulesets = [] as Set
	
	/**
	 * Creates a convention instance tied
	 * to the specified project.
	 * 
	 * Defaults the {@code resultsDirName} and 
	 * {@code reportsDirName} to "pmd"
	 * @param project
	 */
	PMDConvention(Project project) {
		this.project = project
		this.reportsDirName = this.resultsDirName = 'pmd'
		rulesets.add(project.file('config/pmd/rulesets.xml'))
	}
	
	/**
	* Adds a ruleset file path to the set.
	* @param rulesets the ruleset path to add
	*/
   void rulesets(String... rulesets) {
	   this.rulesets.addAll(rulesets)
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
	
	/**
	 * Gets the directory to be used for Findbugs reports. This is determined
	 * using the {@code resultsDirName} property, evaluated relative to the
	 * project's build directory.
	 * @return the reports  dir for Findbugs
	 */
	File getReportsDir() {
		project.fileResolver.withBaseDir(project.reportsDir).resolve(reportsDirName)
	}
}
