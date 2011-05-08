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
import org.gradle.api.plugins.ReportingBasePlugin
import org.gradle.api.tasks.GroovySourceSet
import org.gradle.api.tasks.SourceSet

/**
 * <p>
 * A {@link Plugin} that generates design quality metrics by
 * scanning your source packages.  This is done using the JDepend
 * tool.
 * </p>
 * <p>
 * This plugin will automtically generate a task for each Java source set.
 * </p>
 * See {@link http://www.clarkware.com/software/JDepend.html} for more information.
 * @author Andrew Oberstar
 * @version 0.1.0
 * @see JDepend
 * @see JDependConvention
 * @since 0.1.0
 */
class JDependPlugin implements Plugin<Project> {
	private static final String JDEPEND_TASK_NAME = 'jdepend'
	private static final String JDEPEND_CONFIGURATION_NAME = 'jdepend'
	
	/**
	 * Applies the plugin to the specified project.
	 * @param project the project to apply this plugin too
	 */
	void apply(Project project) {
		project.plugins.apply(ReportingBasePlugin)
		
		project.configurations.add(JDEPEND_CONFIGURATION_NAME)
			.setVisible(false)
			.setTransitive(true)
			.setDescription('The jdepend libraries to be used for this project.')
		
		def convention = new JDependConvention(project)
		project.convention.plugins.jdepend = convention
		
		project.plugins.withType(JavaBasePlugin) {
			configureForJavaPlugin(project, convention)
		}
		project.plugins.withType(GroovyBasePlugin) {
			configureForGroovyPlugin(project, convention)
		}
	}
	
	/**
	 * Adds a dependency for the check task on the all
	 * JDepend tasks.
	 * @param project the project to configure the check task for
	 */
	private void configureCheckTask(Project project) {
		def task = project.tasks[JavaBasePlugin.CHECK_TASK_NAME]
		task.dependsOn project.tasks.withType(JDepend)
	}
	
	/**
	 * Configures JDepend tasks for Java source sets.
	 * @param project the project to configure jdepend for
	 * @param convention the jdepend conventions to use
	 */
	private void configureForJavaPlugin(final Project project, final JDependConvention convention) {
		configureCheckTask(project)
		
		project.convention.getPlugin(JavaPluginConvention).sourceSets.all { SourceSet set ->
			def jdepend = project.tasks.add(set.getTaskName(JDEPEND_TASK_NAME, null), JDepend)
			jdepend.description = "Run jdepend analysis for ${set.name} classes"
			jdepend.conventionMapping.classesDir = { set.classesDir }
			jdepend.conventionMapping.resultsFile = { new File(convention.resultsDir, "${set.name}.xml") }
		}
	}
	
	/**
	 * Configures JDepend tasks for Groovy source sets.
	 * @param project the project to configure jdepend for
	 * @param convention the jdepend conventions to use
	 */
	private void configureForGroovyPlugin(final Project project, final JDependConvention convention) {		
		project.convention.getPlugin(JavaPluginConvention).sourceSets.all { SourceSet set ->
			def groovySourceSet = set.convention.getPlugin(GroovySourceSet)
			def jdepend = project.tasks.add(set.getTaskName(JDEPEND_TASK_NAME, null), JDepend)
			jdepend.description = "Run jdepend analysis for ${set.name} classes"
			jdepend.conventionMapping.classesDir = { groovySourceSet.classesDir }
			jdepend.conventionMapping.resultsFile = { new File(convention.resultsDir, "${set.name}.xml") }
		}
	}
}
