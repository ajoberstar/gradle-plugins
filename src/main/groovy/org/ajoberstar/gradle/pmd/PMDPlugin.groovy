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

import org.ajoberstar.gradle.jdepend.JDependConvention;
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.plugins.ReportingBasePlugin
import org.gradle.api.tasks.SourceSet

/**
 * <p>
 * A {@link Plugin} that generates code quality metrics by
 * scanning your source packages.  This is done using the
 * PMD tool.  
 * </p>
 * <p>
 * This plugin will automtically generate a task for each Java source set.
 * </p>
 * See {@link http://pmd.sourceforge.net/} for more information.
 * @author Andrew Oberstar
 * @version 0.1.0
 * @see Findbugs
 * @see FindbugsConvention
 * @since 0.1.0
 */
class PMDPlugin implements Plugin<Project> {
	private static final PMD_TASK_NAME = 'pmd'
	private static final PMD_CONFIGURATION_NAME = 'pmd'
	
	/**
	 * Applies the plugin to the specified project.
	 * @param project the project to apply this plugin too
	 */
	void apply(Project project) {
		project.plugins.apply(ReportingBasePlugin)
		
		project.getConfigurations().add(PMD_CONFIGURATION_NAME)
		.setVisible(false)
		.setTransitive(true)
		.setDescription("The jdepend libraries to be used for this project.")
		
		def convention = new PMDConvention(project)
		project.convention.plugins.pmd = convention
		
		project.plugins.withType(JavaBasePlugin) {
			configureForJavaPlugin(project, convention)
		}
	}
	
	/**
	* Adds a dependency for the check task on the all
	* PMD tasks.
	* @param project the project to configure the check task for
	*/
   private void configureCheckTask(Project project) {
	   def task = project.tasks[JavaBasePlugin.CHECK_TASK_NAME]
	   task.dependsOn project.tasks.withType(PMD)
   }
   
   /**
	* Configures PMD tasks for Java source sets.
	* @param project the project to configure PMD for
	* @param convention the PMD conventions to use
	*/
   private void configureForJavaPlugin(final Project project, final PMDConvention convention) {
	   configureCheckTask(project)
	   
	   project.convention.getPlugin(JavaPluginConvention).sourceSets.all { SourceSet set ->
		   def pmd = project.tasks.add(set.getTaskName(PMD_TASK_NAME, null), PMD)
		   pmd.description = "Run PMD analysis for ${set.name} source files."
		   pmd.conventionMapping.defaultSource = { set.allJava }
		   pmd.conventionMapping.rulesets = { convention.rulesets }
		   pmd.conventionMapping.resultsFile = { new File(convention.resultsDir, "${set.name}.xml") }
		   pmd.conventionMapping.reportsFile = { new File(convention.reportsDir, "${set.name}.html") }
	   }
   }
}
