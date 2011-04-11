package org.ajoberstar.gradle.jdepend.internal

import java.io.File;
import java.util.Map;
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.AntBuilderAware

class AntJDepend {
	def jdepend(AntBuilder ant, FileCollection classes, File resultsFile, boolean ignoreFailures) {
		ant.project.addTaskDefinition('jdepend', getClass().classLoader.loadClass('org.apache.tools.ant.taskdefs.optional.jdepend.JDependTask'))
		ant.jdepend(format:'xml', outputFile:resultsFile, haltOnError:!ignoreFailures) {
			classespath {
				pathElement(location:classes.asPath)
			}
		}
	}
}
