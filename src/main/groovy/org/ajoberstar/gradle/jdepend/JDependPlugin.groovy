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

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.GroovyBasePlugin
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.plugins.ReportingBasePlugin;
import org.gradle.api.tasks.GroovySourceSet
import org.gradle.api.tasks.SourceSet

/**
 * @author Andy
 *
 */
class JDependPlugin implements Plugin<Project> {
	private static final String JDEPEND_TASK_NAME = 'jdepend'
	private static final String JDEPEND_CONFIGURATION_NAME = 'jdependConf'
	
	void apply(Project project) {
		project.plugins.apply(ReportingBasePlugin)
		
		def convention = new JDependConvention(project)
		project.convention.plugins.jdepend = convention
		
//		configureJdependDefaults(project, convention)
		
		project.plugins.withType(JavaBasePlugin) {
			configureForJavaPlugin(project, convention)
		}
		project.plugins.withType(GroovyBasePlugin) {
			configureForGroovyPlugin(project, convention)
		}
	}
	
//	private void configureJdependDefaults(final Project project, final JDependConvention convention) {
//		project.tasks.withType(JDepend) { JDepend jdepend ->
//			jdepend.conventionMapping.resultsDir = { convention.jdependResultsDir }
//			jdepend.conventionMapping.resportsDir = { convention.jdependReportsDir }
//		}
//	}
	
	private void configureCheckTask(Project project) {
		def task = project.tasks[JavaBasePlugin.CHECK_TASK_NAME]
		task.dependsOn project.tasks.withType(JDepend)
	}
	
	private void configureJdependTask(final Project project) {
		JDepend jdepend = project.getTasks().add(JDEPEND_TASK_NAME, new JDepend())
		jdepend.setDescription('Run jdepend analysis')
		
	}
	
	private void configureForJavaPlugin(final Project project, final JDependConvention convention) {
		configureCheckTask(project)
		
		project.convention.getPlugin(JavaPluginConvention).sourceSets.all { SourceSet set ->
			def jdepend = project.tasks.add(set.getTaskName(JDEPEND_TASK_NAME, null), JDepend)
			jdepend.description = "Run jdepend analysis for ${set.name} classes"
			jdepend.conventionMapping.defaultClasses = { set.classes }
			jdepend.conventionMapping.resultsFile = { new File(convention.jdependResultsDir, "${set.name}.xml") }
		}
	}
	
	private void configureForGroovyPlugin(final Project project, final JDependConvention convention) {		
		project.convention.getPlugin(JavaPluginConvention).sourceSets.all { SourceSet set ->
			def groovySourceSet = set.convention.getPlugin(GroovySourceSet)
			def jdepend = project.tasks.add(set.getTaskName(JDEPEND_TASK_NAME, null), JDepend)
			jdepend.description = "Run jdepend analysis for ${set.name} classes"
			jdepend.conventionMapping.defaultClasses = { set.classes }
			jdepend.conventionMapping.resultsFile = { new File(convention.jdependResultsDir, "${set.name}.xml") }
		}
	}
}
