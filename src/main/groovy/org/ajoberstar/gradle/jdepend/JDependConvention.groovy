package org.ajoberstar.gradle.jdepend

import java.io.File

import org.gradle.api.Project
import org.gradle.api.internal.project.ProjectInternal

/**
 * 
 */
class JDependConvention {
	private ProjectInternal project
	
	String resultsDirName
	
	/**
	 * 
	 * @param project
	 */
	JDependConvention(Project project) {
		this.project = project
		resultsDirName = 'jdepend'
	}
	
	/**
	 * 
	 * @return
	 */
	File getResultsDir() {
		project.fileResolver.withBaseDir(project.buildDir).resolve(resultsDirName)
	}
}
