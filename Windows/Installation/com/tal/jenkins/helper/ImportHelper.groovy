package com.tal.jenkins.helper

class ImportHelper {

    private static groovyShell = new GroovyShell()

    static def getWORKING_DIRECTORY_PATH() {
        def workingDirectoryPath

        try {
            def list = []
            workingDirectoryPath =
                    new File(getClass().protectionDomain.codeSource.location.
                            path).parent
            workingDirectoryPath.eachFileRecurse() { file ->
                list << file
            }
        } catch (ignored) {
            workingDirectoryPath = System.getProperty("user.dir")
        }

        return workingDirectoryPath
    }

    /**
     * Allows to import a groovy "class" or a groovy "script" to a groovy file,
     * based on its relative file path.
     *
     * For example, if you want to import a "class":
     * There should be a groovy script in `test/buildRerunnerFile.groovy` that
     * its content is:
     *
     * ```groovy
     * package test
     *
     * import jenkins.*
     * import jenkins.model.*
     * import hudson.*
     * import hudson.model.*
     * import org.jenkinsci.plugins.workflow.*
     *
     *
     * public class BuildRerunner {
     *     def ENFORCEMENT_TIME_FRAME = 61 * 60 * 1000
     *
     *     def printTest() {
     *         println "hello test"
     *     }
     * }
     *
     * return BuildRerunner.class
     *
     * ```
     *
     * For example, if you want to import a "script":
     * There should be a groovy script in `test/buildRerunnerFile.groovy` that
     * its content is:
     *
     * ```groovy
     * package test
     *
     * import jenkins.*
     * import jenkins.model.*
     * import hudson.*
     * import hudson.model.*
     * import org.jenkinsci.plugins.workflow.*
     *
     *
     * def printTest() {
     *     println "hello test"
     * }
     *
     * return this
     *
     * ```
     *
     * Either way, the usage is the same. This is how you import that groovy
     * file:
     *
     * ```groovy
     * def buildRerunnerClass = importFile("test/buildRerunnerFile.groovy")
     * def buildRerunner = buildRerunnerClass.newInstance()
     * buildRerunner.printTest()
     * ```
     *
     * output:
     *
     * ```
     * hello test
     * ```
     *
     * @param relativeFilePathToImport The relative file path of the groovy
     * script to import.
     * @return clazz for a "class" or for a "script".
     */
    static def importFile(String relativeFilePathToImport) {
        final File classFile = new File(getWORKING_DIRECTORY_PATH(),
                relativeFilePathToImport)
        return groovyShell.evaluate(classFile)
    }

}
