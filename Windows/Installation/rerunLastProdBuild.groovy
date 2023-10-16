import hudson.*
import hudson.model.*
import jenkins.*
import jenkins.model.*
import org.jenkinsci.plugins.workflow.*
import com.tal.jenkins.build.BuildRerunner

// Get all the "production" builds.
productionBuilds = Jenkins.instance.getView('All').getBuilds().
        findAll() { it.toString().contains("prod") }

// Ascending sort them by their start time.
productionBuilds.sort { it.getTime() }

// Descending sort them by their start time.
productionBuilds = productionBuilds.reverse()

// Rerun the only last "production" build.
buildRerunner.runNewWorkFlowAndEnforceItDoesNotFailWithinGivenTimeFrame(
        productionBuilds[0])

return this
