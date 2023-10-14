import jenkins.*
import jenkins.model.*
import hudson.*
import hudson.model.*
import org.jenkinsci.plugins.workflow.*
import test.buildRerunnerFile


// Get all the "production" builds.
productionBuilds = Jenkins.instance.getView('All').getBuilds().findAll() { it.toString().contains("prod") }

// Ascending sort them by their start time.
productionBuilds.sort{ it.getTime() }

// Descending sort them by their start time.
productionBuilds = productionBuilds.reverse()

// Rerun the only last "production" build.
buildRerunner = new test.BuildRerunner()
buildRerunner.runNewWorkFlowAndEnforceItDoesNotFailWithinGivenTimeFrame(productionBuilds[0])
