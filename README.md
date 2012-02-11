# gradle-plugins

A set of plugins for the Gradle build tool.  They are available through Maven Central.

The existing plugins are:
* Findbugs
* JDepend
* PMD

These have been contributed to Gradle and will be included in the 1.0-milestone-8 release.
They will no longer be supported through this project, though I may add other plugins at
some point.

---

## Adding the Plugins

Add the following block to your build to use the gradle-plugins JAR.

    buildscript {
	    repositories { mavenCentral() }
	    dependencies { classpath group:'org.ajoberstar', name:'gradle-plugins', version:'0.1.1' }
    }

## Using the Plugins

All 3 plugins require the tools to be included in their respective configurations.

    apply plugin: 'findbugs'
    apply plugin: 'jdepend'
    apply plugin: 'pmd'
    
    convention.plugins.pmd.rulesets 'rulesets/basic.xml', 'rulesets/other.xml'
    
    dependencies {
        findbugs group:'com.google.code.findbugs', name:'findbugs', version:'1.3.9'
        findbugs group:'com.google.code.findbugs', name:'findbugs-ant', version:'1.3.9'
        
        pmd group:'pmd', name:'pmd', version:'4.2.5'	
        
        jdepend group:'jdepend', name:'jdepend', version:'2.9.1'
        jdepend group:'org.apache.ant', name:'ant-jdepend', version:'1.7.1'	
    }

Each plugin generates a task per source set dependent on the check task.

---

## Release Notes

**v0.1.0**

Initial release.

**v0.1.1**

* Improved up to date checking.
* Defaulting PMD plugin to look for configuration in `config/pmd/rulesets.xml`.
