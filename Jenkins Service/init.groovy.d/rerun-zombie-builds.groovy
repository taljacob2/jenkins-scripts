import jenkins.*
import jenkins.model.*
import hudson.*
import hudson.model.*
import org.jenkinsci.plugins.workflow.*
import BuildRerunner


// Get all the builds that are still "in-progress" (i.e. "zombie" builds).
runningBuilds = Jenkins.instance.getView('All').getBuilds().findAll() { it.getResult().equals(null) }

// Rerun each build.
buildRerunner = new BuildRerunner()
runningBuilds.each{ build ->
    buildRerunner.runNewWorkFlowAndEnforceItDoesNotFailWithinGivenTimeFrame(build)
}
