import hudson.*
import hudson.model.*
import jenkins.*
import jenkins.model.*
import org.jenkinsci.plugins.workflow.*
import com.tal.jenkins.build.BuildRerunner

// Get all the builds that are still "in-progress" (i.e. "zombie" builds).
def runningBuilds = Jenkins.instance.getView('All').getBuilds().
        findAll() { it.getResult().equals(null) }

// Rerun each build.
final BuildRerunner buildRerunner = new BuildRerunner()
runningBuilds.each { build ->
    buildRerunner.
            runNewWorkFlowAndEnforceItDoesNotFailWithinGivenTimeFrame(build)
}

return this
