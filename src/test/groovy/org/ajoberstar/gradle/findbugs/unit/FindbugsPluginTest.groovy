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
package org.ajoberstar.gradle.findbugs.unit

import org.ajoberstar.gradle.findbugs.Findbugs
import org.ajoberstar.gradle.findbugs.FindbugsConvention
import org.ajoberstar.gradle.findbugs.FindbugsPlugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.ReportingBasePlugin
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test


/**
 * @author Andy
 *
 */
class FindbugsPluginTest {
	private final Project project = ProjectBuilder.builder().withProjectDir(new File('build/tmp/test')).build()
	private final FindbugsPlugin plugin = new FindbugsPlugin()
	
	
	@Test
	void apply_appliesReportingBasePlugin() {
		plugin.apply(project)
		assert project.plugins.hasPlugin(ReportingBasePlugin)
	}
	
	@Test
	void apply_addsConventionObjectsToProject() {
		plugin.apply(project)
		assert project.convention.plugins.findbugs instanceof FindbugsConvention
	}
	
	@Test
	void apply_createsTasksAndAppliesMappingsForEachJavaSourceSet() {
		plugin.apply(project)

		project.plugins.apply(JavaPlugin)
		project.sourceSets.add('custom')
		verifyTaskForSet('main')
		verifyTaskForSet('test')
		verifyTaskForSet('custom')
	}
	
	private void verifyTaskForSet(String setName) {
		def taskSet = setName.substring(0, 1).toUpperCase() + setName.substring(1)
		def task = project.tasks[FindbugsPlugin.FINDBUGS_TASK_NAME + taskSet]
		assert task instanceof Findbugs
		assert task.defaultSource == project.sourceSets[setName].allJava
		assert task.classes == project.sourceSets[setName].classes
		assert task.resultsFile == project.file("build/findbugs/${setName}.xml")
		
		//assert project.tasks[JavaBasePlugin.CHECK_TASK_NAME].dependsOn.contains(task)
	}
	
	@Test
	void add_configuresAdditionalTasksDefinedByTheBuildScript() {
		plugin.apply(project)
		
		def task = project.tasks.add('customFindbugs', Findbugs)
		assert task.defaultSource == null
		assert task.classes == null
		assert task.resultsFile == null
	}
}
