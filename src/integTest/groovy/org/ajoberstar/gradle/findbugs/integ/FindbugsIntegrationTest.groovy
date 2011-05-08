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
package org.ajoberstar.gradle.findbugs.integ

import org.gradle.tooling.BuildException
import org.gradle.tooling.BuildLauncher
import org.gradle.tooling.GradleConnector
import org.gradle.tooling.ProjectConnection
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * @author Andrew Oberstar
 *
 */
class FindbugsIntegrationTest {
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
	void check_JavaSource_GeneratesReport() {
		testFile('build.gradle') << '''\
		findbugsMain.ignoreFailures = true
		findbugsTest.ignoreFailures = true
		'''
		testFile('src/main/java/org/ajoberstar/Class1.java') << 'package org.ajoberstar; class Class1 { public boolean equals(Object arg) { return true; } }'
		testFile('src/test/java/org/ajoberstar/Class1Test.java') << 'package org.ajoberstar; class Class1Test { public boolean equals(Object arg) { return true; } }'
		
		launcher.run()
		
		assert testFile('build/findbugs/main.xml').text.contains('org.ajoberstar.Class1')
		assert testFile('build/findbugs/test.xml').text.contains('org.ajoberstar.Class1Test')
	}
	
	@Test(expected=BuildException.class)
	void check_JavaSource_FailsBuild() {
		testFile('src/main/java/org/ajoberstar/Class1.java') << 'package org.ajoberstar; class Class1 { public boolean equals(Object arg) { return true; } }'
		testFile('src/test/java/org/ajoberstar/Class1Test.java') << 'package org.ajoberstar; class Class1Test { public boolean equals(Object arg) { return true; } }'
		
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
		apply plugin: 'findbugs'
		
		repositories {
			mavenCentral()
		}
		
		dependencies {
			findbugs group:'com.google.code.findbugs', name:'findbugs', version:'1.3.9'
			findbugs group:'com.google.code.findbugs', name:'findbugs-ant', version:'1.3.9'
		}
		
		findbugsMain.systemProp 'findbugs.debug', 'true'
		findbugsTest.systemProp 'findbugs.debug', 'true'
		"""
	}
}
/*	
	
	@Test
	void generatesReportsForJavaSource() {	
		testFile('build.gradle') << '''
apply plugin: 'java'
apply plugin: 'findbugs'
'''
		
		testFile('src/main/java/org/ajoberstar/Class1.java') << 'package org.ajoberstar; class Class1 { }'
		testFile('src/test/java/org/ajoberstar/TestClass1.java') << 'package org.ajoberstar; class TestClass1 { }'
		inTestDirectory().withTasks('check').run()
		
		testFile('build/findbugs/main.xml').assertContents(containsClass('org.ajoberstar.Class1'))
		testFile('build/findbugs/test.xml').assertContents(containsClass('org.ajoberstar.TestClass1'))
	}
	
	@Test
	void generatesReportsForJavaSourceInGroovySourceDirs() {	
		testFile('build.gradle') << '''
apply plugin: 'java'
apply plugin: 'findbugs'
'''
		
		testFile('src/main/groovy/org/ajoberstar/Class1.java') << 'package org.ajoberstar; class Class1 { }'
		testFile('src/test/groovy/org/ajoberstar/TestClass1.java') << 'package org.ajoberstar; class TestClass1 { }'
		inTestDirectory().withTasks('check').run()
		
		testFile('build/findbugs/main.xml').assertContents(containsClass('org.ajoberstar.Class1'))
		testFile('build/findbugs/test.xml').assertContents(containsClass('org.ajoberstar.TestClass1'))
	} 
	
	@Test
	void generatesReportsOnlyForJavaSource() {
		testFile('build.gradle') << '''
apply plugin: 'java'
apply plugin: 'findbugs'
'''
		
		testFile('src/main/groovy/org/ajoberstar/Class1.java') << 'package org.ajoberstar; class Class1 { }'
		testFile('src/main/groovy/org/ajoberstar/Class2.java') << 'package org.ajoberstar; class Class2 { }'
		testFile('src/main/groovy/org/ajoberstar/Class3.groovy') << 'package org.ajoberstar; class Class3 { }'
		inTestDirectory().withTasks('check').run()
		
		testFile('build/findbugs/main.xml').assertExists()
		testFile('build/findbugs/main.xml').assertContents(not(containsClass('org.ajoberstar.Class3')))
	}
	
	@Test
	void findbugsWarningBreaksBuild() {
		
	}
	
	private Matcher<String> containsClass(String classname) {
		return containsLine(containsString(classname.replace('.', File.separator) + '.java'))
	}
}

package org.gradle.integtests

import org.gradle.integtests.fixtures.ExecutionFailure
import org.gradle.util.TestFile
import org.hamcrest.Matcher
import org.junit.Test
import static org.gradle.util.Matchers.*
import static org.hamcrest.Matchers.*
import org.gradle.integtests.fixtures.internal.AbstractIntegrationTest

class CodeQualityIntegrationTest extends AbstractIntegrationTest {


   @Test
   public void checkstyleViolationBreaksBuild() {
	   testFile('build.gradle') << '''
apply plugin: 'groovy'
apply plugin: 'code-quality'
'''
	   writeCheckstyleConfig()

	   testFile('src/main/java/org/gradle/class1.java') << 'package org.gradle; class class1 { }'
	   testFile('src/main/groovy/org/gradle/class2.java') << 'package org.gradle; class class2 { }'

	   ExecutionFailure failure = inTestDirectory().withTasks('check').runWithFailure()
	   failure.assertHasDescription('Execution failed for task \':checkstyleMain\'')
	   failure.assertThatCause(startsWith('Checkstyle check violations were found in main Java source. See the report at'))

	   testFile('build/checkstyle/main.xml').assertExists()
   }

   @Test
   public void generatesReportForGroovySource() {
	   testFile('build.gradle') << '''
apply plugin: 'groovy'
apply plugin: 'code-quality'
dependencies { groovy localGroovy() }
'''
	   writeCodeNarcConfigFile()

	   testFile('src/main/groovy/org/gradle/Class1.groovy') << 'package org.gradle; class Class1 { }'
	   testFile('src/test/groovy/org/gradle/TestClass1.groovy') << 'package org.gradle; class TestClass1 { }'

	   inTestDirectory().withTasks('check').run()

	   testFile('build/reports/codenarc/main.html').assertExists()
	   testFile('build/reports/codenarc/test.html').assertExists()
   }

   @Test
   public void codeNarcOnlyChecksGroovySource() {
	   testFile('build.gradle') << '''
apply plugin: 'groovy'
apply plugin: 'code-quality'
'''

	   writeCodeNarcConfigFile()

	   testFile('src/main/groovy/org/gradle/class1.java') << 'package org.gradle; class class1 { }'
	   testFile('src/main/groovy/org/gradle/Class2.groovy') << 'package org.gradle; class Class2 { }'

	   inTestDirectory().withTasks('codenarcMain').run()

	   testFile('build/reports/codenarc/main.html').assertExists()
   }

   @Test
   public void codeNarcViolationBreaksBuild() {
	   testFile('build.gradle') << '''
apply plugin: 'groovy'
apply plugin: 'code-quality'
dependencies { groovy localGroovy() }
'''

	   writeCodeNarcConfigFile()

	   testFile('src/main/groovy/org/gradle/class1.groovy') << 'package org.gradle; class class1 { }'

	   ExecutionFailure failure = inTestDirectory().withTasks('check').runWithFailure()
	   failure.assertHasDescription('Execution failed for task \':codenarcMain\'')
	   failure.assertThatCause(startsWith('CodeNarc check violations were found in main Groovy source. See the report at '))

	   testFile('build/reports/codenarc/main.html').assertExists()
   }

   private TestFile writeCheckstyleConfig() {
	   return testFile('config/checkstyle/checkstyle.xml') << '''
<!DOCTYPE module PUBLIC
	   "-//Puppy Crawl//DTD Check Configuration 1.2//EN"
	   "http://www.puppycrawl.com/dtds/configuration_1_2.dtd">
<module name="Checker">
   <module name="TreeWalker">
	   <module name="TypeName"/>
   </module>
</module>'''
   }

   private TestFile writeCodeNarcConfigFile() {
	   return testFile('config/codenarc/codenarc.xml') << '''
<ruleset xmlns="http://codenarc.org/ruleset/1.0"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xsi:schemaLocation="http://codenarc.org/ruleset/1.0 http://codenarc.org/ruleset-schema.xsd"
	   xsi:noNamespaceSchemaLocation="http://codenarc.org/ruleset-schema.xsd">
   <ruleset-ref path='rulesets/naming.xml'/>
</ruleset>
'''
   }

}
*/
