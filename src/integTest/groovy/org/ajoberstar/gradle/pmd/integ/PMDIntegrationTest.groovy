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
package org.ajoberstar.gradle.pmd.integ

/**
 * @author Andrew Oberstar
 */
import org.gradle.tooling.BuildException
import org.gradle.tooling.BuildLauncher
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ProjectConnection
import org.junit.After
import org.junit.Before
import org.junit.Test

class PMDIntegrationTest {
	private static final String PLUGIN_DIR = new File('build/classes/main').canonicalPath.replaceAll('\\\\', '/')
	private static final File TEST_DIR = new File('build/tmp/testProjects')
	
	private BuildLauncher launcher
	
	@Before
	void newBuild() {
		GradleConnector connector = GradleConnector.newConnector()
		ProjectConnection connection = connector.forProjectDirectory(TEST_DIR).connect()
		writeBuildFile()
		launcher = connection.newBuild().forTasks('check')
	}
	
	@Test
	void check_emptyProject_Success() {
		launcher.run()
	}
	
	@Test
	void check_JavaSource_GenerateReport() {
		testFile('build.gradle') << '''\
		pmdMain.ignoreFailures = true
		pmdTest.ignoreFailures = true
		'''
		testFile('src/main/java/org/ajoberstar/Class1.java') << 'package org.ajoberstar; class Class1 { {} public boolean equals(Object arg) { return true; } }'
		testFile('src/test/java/org/ajoberstar/Class1Test.java') << 'package org.ajoberstar; class Class1Test { {} public boolean equals(Object arg) { return true; } }'
		
		launcher.run()
		
		assert testFile('build/pmd/main.xml').text.contains('org/ajoberstar/Class1')
		assert testFile('build/pmd/test.xml').text.contains('org/ajoberstar/Class1Test')
	}
	
	@Test(expected=BuildException.class)
	void check_JavaSource_FailsBuild() {
		testFile('src/main/java/org/ajoberstar/Class1.java') << 'package org.ajoberstar; class Class1 { {} public boolean equals(Object arg) { return true; } }'
		testFile('src/test/java/org/ajoberstar/Class1Test.java') << 'package org.ajoberstar; class Class1Test { {} public boolean equals(Object arg) { return true; } }'
		
		launcher.run()
	}
	
	@After
	void cleanBuild() {
		assert !TEST_DIR.exists() || TEST_DIR.deleteDir()
	}
	
	private File testFile(String path) {
		File file = new File(TEST_DIR, path)
		file.parentFile.mkdirs()
		return file
	}
	
	private void writeBuildFile() {
		testFile('build.gradle') << """\
		buildscript {
			dependencies {
				classpath files('${PLUGIN_DIR}')
			}
		}
		apply plugin: 'groovy'
		apply plugin: 'pmd'
		
		convention.plugins.pmd.rulesets 'rulesets/basic.xml'
		
		repositories {
			mavenCentral()
		}
		
		dependencies {
			pmd group:'pmd', name:'pmd', version:'4.2.5'
		}
		"""
	}
}
