package org.ajoberstar.gradle.jdepend

import java.io.File

import org.gradle.api.Project
import org.gradle.api.internal.project.ProjectInternal

/**
 * 
 */
class JDependConvention {
	private ProjectInternal project
	
	String jdependReportsDirName
	String jdependResultsDirName
	
	Set<String> classes = new HashSet<String>()
	Set<String> excludes = new HashSet<String>()
	
	/**
	 * 
	 * @param project
	 */
	JDependConvention(Project project) {
		this.project = project
		jdependReportsDirName = 'reports/jdepend'
		jdependResultsDirName = 'jdepend'
	}
	
	/**
	 * 
	 * @param classes
	 */
	void classes(String... classes) {
		classes.addAll(classes)
	}
	
	/**
	 * 
	 * @param excludes
	 */
	void excludes(String... excludes) {
		excludes.addAll(excludes)
	}
	
	/**
	 * 
	 * @return
	 */
	File getJdependResultsDir() {
		project.fileResolver.withBaseDir(project.buildDir).resolve(jdependResultsDirName)
	}
	
	/**
	 * 
	 * @return
	 */
	File getJdependReportsDir() {
		project.fileResolver.withBaseDir(project.buildDir).resolve(jdependReportsDirName)
	}
}
