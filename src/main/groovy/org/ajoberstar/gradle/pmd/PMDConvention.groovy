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

import java.io.File;
import java.util.Set;

import org.gradle.api.Project
import org.gradle.api.internal.project.ProjectInternal

/**
 * A convention describing PMD settings.
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
	Set<String> rulesets = new HashSet<String>()
	
	/**
	 * Constructs the convention for this project.
	 * @param project
	 */
	PMDConvention(Project project) {
		this.project = project
		reportsDirName = 'pmd'
		resultsDirName = 'pmd'
	}
	
	/**
	* Adds a ruleset file path to the set.
	* @param rulesets the ruleset path to add
	*/
   void rulesets(String... rulesets) {
	   this.rulesets.addAll(rulesets)
   }
	
	/**
	 * 
	 * @return file pointing to the results directory
	 */
	File getResultsDir() {
		project.fileResolver.withBaseDir(project.buildDir).resolve(resultsDirName)
	}
	
	/**
	 * 
	 * @return file pointing to the reports directory
	 */
	File getReportsDir() {
		project.fileResolver.withBaseDir(project.reportsDir).resolve(reportsDirName)
	}
}
