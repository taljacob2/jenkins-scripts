import hudson.*
import hudson.model.*
import jenkins.*
import jenkins.model.*
import org.jenkinsci.plugins.workflow.*

def getWORKING_DIRECTORY_PATH() {
    def workingDirectoryPath = ""

    try {
        def list = []
        workingDirectoryPath =
                new File(getClass().protectionDomain.codeSource.location.path).
                        parent
        workingDirectoryPath.eachFileRecurse() { file ->
            list << file
        }
    } catch (e) {
        workingDirectoryPath = System.getProperty("user.dir")
    }

    return workingDirectoryPath
}

/**
 * Allows to import a groovy "class" or a groovy "script" to a groovy file,
 * based on its relative file path.
 *
 * For example, if you want to import a "class":
 * There should be a groovy script in `test/buildRerunnerFile.groovy` that its
 * content is:
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
 * There should be a groovy script in `test/buildRerunnerFile.groovy` that its
 * content is:
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
 * Either way, the usage is the same. This is how you import that groovy file:
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
 * @param relativeFilePathToImport The relative file path of the groovy script to import.
 * @return clazz for a "class" or for a "script".
 */
def importFile(String relativeFilePathToImport) {
    File classFile = new File(getWORKING_DIRECTORY_PATH(),
            relativeFilePathToImport)
    return evaluate(classFile)
}

// Get all the "production" builds.
productionBuilds = Jenkins.instance.getView('All').getBuilds().
        findAll() { it.toString().contains("prod") }

// Ascending sort them by their start time.
productionBuilds.sort { it.getTime() }

// Descending sort them by their start time.
productionBuilds = productionBuilds.reverse()

// Rerun the only last "production" build.
def buildRerunner =
        importFile("com/tal/jenkins/build/buildRerunnerFile.groovy").
                newInstance()
buildRerunner.runNewWorkFlowAndEnforceItDoesNotFailWithinGivenTimeFrame(
        productionBuilds[0])
