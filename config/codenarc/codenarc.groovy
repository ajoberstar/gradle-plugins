ruleset {
	description '''
		A Sample Groovy RuleSet containing all CodeNarc Rules, grouped by category.
		You can use this as a template for your own custom RuleSet.
		Just delete the rules that you don't want to include.
		'''

	ruleset('rulesets/basic.xml') {
		'AddEmptyString'
		'AssignmentInConditional'
		'BigDecimalInstantiation'
		'BooleanGetBoolean'
		'BooleanMethodReturnsNull'
		'BrokenOddnessCheck'
		'CloneableWithoutClone'
		'CompareToWithoutComparable'
		'ConfusingTernary'
		'ConsecutiveLiteralAppends'
		'ConsecutiveStringConcatenation'
		'ConstantIfExpression'
		'ConstantTernaryExpression'
		'DeadCode'
		'DoubleNegative'
		'DuplicateCaseStatement'
		'EmptyCatchBlock'
		'EmptyElseBlock'
		'EmptyFinallyBlock'
		'EmptyForStatement'
		'EmptyIfStatement'
		'EmptyInstanceInitializer'
		'EmptyMethod'
		'EmptyStaticInitializer'
		'EmptySwitchStatement'
		'EmptySynchronizedStatement'
		'EmptyTryBlock'
		'EmptyWhileStatement'
		'EqualsAndHashCode'
		'ExplicitArrayListInstantiation'
		'ExplicitCallToAndMethod'
		'ExplicitCallToCompareToMethod'
		'ExplicitCallToDivMethod'
		'ExplicitCallToEqualsMethod'
		'ExplicitCallToGetAtMethod'
		'ExplicitCallToLeftShiftMethod'
		'ExplicitCallToMinusMethod'
		'ExplicitCallToModMethod'
		'ExplicitCallToMultiplyMethod'
		'ExplicitCallToOrMethod'
		'ExplicitCallToPlusMethod'
		'ExplicitCallToPowerMethod'
		'ExplicitCallToRightShiftMethod'
		'ExplicitCallToXorMethod'
		'ExplicitGarbageCollection'
		'ExplicitHashMapInstantiation'
		'ExplicitHashSetInstantiation'
		'ExplicitLinkedListInstantiation'
		'ExplicitStackInstantiation'
		'ExplicitTreeSetInstantiation'
		'GStringAsMapKey'
		'GroovyLangImmutable'
		'IntegerGetInteger'
		'InvertedIfElse'
		'RemoveAllOnSelf'
		'ReturnFromFinallyBlock'
		'ReturnsNullInsteadOfEmptyArray'
		'ReturnsNullInsteadOfEmptyCollection'
		'SerialVersionUID'
		'SerializableClassMustDefineSerialVersionUID'
		'SimpleDateFormatMissingLocale'
		'ThrowExceptionFromFinallyBlock'
	}
	
	ruleset('rulesets/braces.xml') {
		'ElseBlockBraces'
		'ForStatementBraces'
		'IfStatementBraces'
		'WhileStatementBraces'
	}
	
	ruleset('rulesets/concurrency.xml') {
		'BusyWait'
		'DoubleCheckedLocking'
		'InconsistentPropertyLocking'
		'InconsistentPropertySynchronization'
		'NestedSynchronization'
		'StaticCalendarField'
		'StaticDateFormatField'
		'StaticMatcherField'
		'SynchronizedMethod'
		'SynchronizedOnBoxedPrimitive'
		'SynchronizedOnGetClass'
		'SynchronizedOnReentrantLock'
		'SynchronizedOnString'
		'SynchronizedOnThis'
		'SynchronizedReadObjectMethod'
		'SystemRunFinalizersOnExit'
		'ThreadGroup'
		'ThreadLocalNotStaticFinal'
		'ThreadYield'
		'UseOfNotifyMethod'
		'VolatileArrayField'
		'VolatileLongOrDoubleField'
		'WaitOutsideOfWhileLoop'
	}

	ruleset('rulesets/design.xml') {
		'AbstractClassWithoutAbstractMethod'
		'CloseWithoutCloseable'
		'ConstantsOnlyInterface'
		'EmptyMethodInAbstractClass'
		'FinalClassWithProtectedMember'
		'ImplementationAsType'
	}

	ruleset('rulesets/dry.xml') {
		'DuplicateNumberLiteral'
		'DuplicateStringLiteral'
	}
	
	ruleset('rulesets/exceptions.xml') {
		'CatchArrayIndexOutOfBoundsException'
		'CatchError'
		'CatchException'
		'CatchIllegalMonitorStateException'
		'CatchIndexOutOfBoundsException'
		'CatchNullPointerException'
		'CatchRuntimeException'
		'CatchThrowable'
		'ConfusingClassNamedException'
		'ExceptionExtendsError'
		'MissingNewInThrowStatement'
		'ReturnNullFromCatchBlock'
		'ThrowError'
		'ThrowException'
		'ThrowNullPointerException'
		'ThrowRuntimeException'
		'ThrowThrowable'
	}
	ruleset('rulesets/generic.xml') {
		'IllegalRegex'
		'RequiredRegex'
		'StatelessClass' {
			enabled = false
		}
	}
	
	ruleset('rulesets/grails.xml') {
		'GrailsPublicControllerMethod'
		'GrailsServletContextReference'
		'GrailsSessionReference'
		'GrailsStatelessService'
	}
	
	ruleset('rulesets/imports.xml') {
		'DuplicateImport'
		'ImportFromSamePackage'
		'UnnecessaryGroovyImport'
		'UnusedImport'
	}

	ruleset('rulesets/logging.xml') {
		'LoggerForDifferentClass'
		'LoggerWithWrongModifiers'
		'LoggingSwallowsStacktrace'
		'MultipleLoggers'
		'PrintStackTrace'
		'Println'
		'SystemErrPrint'
		'SystemOutPrint'
	}

	ruleset('rulesets/junit.xml') {
		'ChainedTest'
		'CoupledTestCase'
		'JUnitAssertAlwaysFails'
		'JUnitAssertAlwaysSucceeds'
		'JUnitFailWithoutMessage'
		'JUnitPublicNonTestMethod'
		'JUnitSetUpCallsSuper'
		'JUnitStyleAssertions'
		'JUnitTearDownCallsSuper'
		'JUnitTestMethodWithoutAssert' {
			enabled = false
		}
		'JUnitUnnecessarySetUp'
		'JUnitUnnecessaryTearDown'
		'UnnecessaryFail'
		'UseAssertEqualsInsteadOfAssertTrue'
		'UseAssertFalseInsteadOfNegation'
		'UseAssertNullInsteadOfAssertEquals'
		'UseAssertSameInsteadOfAssertTrue'
		'UseAssertTrueInsteadOfAssertEquals'
		'UseAssertTrueInsteadOfNegation'
	}

	ruleset('rulesets/naming.xml') {
		'AbstractClassName'
		'ClassName'
		'ConfusingMethodName'
		'FieldName' {
			finalRegex = null
			staticRegex = /[A-Z][A-Z0-9_]*/
		}
		'InterfaceName'
		'MethodName'
		'ObjectOverrideMisspelledMethodName'
		'PackageName'
		'ParameterName'
		'PropertyName'
		'VariableName'
	}

	ruleset('rulesets/size.xml') {
		'AbcComplexity'
		'ClassSize'
		'CyclomaticComplexity'
		'MethodCount'
		'MethodSize'
		'NestedBlockDepth'
	}

	ruleset('rulesets/unnecessary.xml') {
		'UnnecessaryBigDecimalInstantiation'
		'UnnecessaryBigIntegerInstantiation'
		'UnnecessaryBooleanExpression'
		'UnnecessaryBooleanInstantiation'
		'UnnecessaryCallForLastElement'
		'UnnecessaryCallToSubstring'
		'UnnecessaryCatchBlock'
		'UnnecessaryCollectCall'
		'UnnecessaryCollectionCall'
		'UnnecessaryConstructor'
		'UnnecessaryDefInMethodDeclaration'
		'UnnecessaryDoubleInstantiation'
		'UnnecessaryFloatInstantiation'
		'UnnecessaryGString'
		'UnnecessaryGetter' {
			doNotApplyToClassNames = 'Findbugs,PMD'
		}
		'UnnecessaryIfStatement'
		'UnnecessaryInstantiationToGetClass'
		'UnnecessaryIntegerInstantiation'
		'UnnecessaryLongInstantiation'
		'UnnecessaryModOne'
		'UnnecessaryNullCheck'
		'UnnecessaryNullCheckBeforeInstanceOf'
		'UnnecessaryObjectReferences'
		'UnnecessaryOverridingMethod'
		'UnnecessaryPublicModifier'
		'UnnecessaryReturnKeyword' {
			enabled = false
		}
		'UnnecessarySelfAssignment'
		'UnnecessarySemicolon'
		'UnnecessaryStringInstantiation'
		'UnnecessaryTernaryExpression'
		'UnnecessaryTransientModifier'
	}
	
	ruleset('rulesets/unused.xml') {
		'UnusedArray'
		'UnusedObject'
		'UnusedPrivateField'
		'UnusedPrivateMethod'
		'UnusedPrivateMethodParameter'
		'UnusedVariable'
	}
}

