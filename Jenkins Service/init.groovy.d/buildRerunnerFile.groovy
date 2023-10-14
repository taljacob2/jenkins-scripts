import jenkins.*
import jenkins.model.*
import hudson.*
import hudson.model.*
import org.jenkinsci.plugins.workflow.*


class BuildRerunner {

    // A time frame that enforces that the `newWorkflowRun` did not fail during it.
    def ENFORCEMENT_TIME_FRAME = 61 * 60 * 1000

    def runNewWorkFlowAndEnforceItDoesNotFailWithinGivenTimeFrame(build, enforcementTimeFrame = ENFORCEMENT_TIME_FRAME) {
        println("rebuilding the old build: " + build.toString())

        // Get the `WorkflowJob` that contains this `build`.
        def workflowJob = build.getParent()

        /*
         * Run the `WorkflowJob`.
         * This also creates a new `WorkflowRun` to replace the old `build`.
         */
        def newWorkflowRun = workflowJob.scheduleBuild2(0)

        /*
         * Make sure that the new `WorkflowRun` is not failing within a certain
         * timeframe. In case it is, then create a new `WorkflowRun` to replace it,
         * and try again.
         */
        def thread = Thread.start {
            while(true) {

                // Check in intervals.
                sleep(10 * 1000)

                // Wait for the `WorkflowRun` to finish, to be able to access the `Build` object.
                if (!newWorkflowRun.isDone()) { continue; }
                
                if (newWorkflowRun.get().getDuration() > enforcementTimeFrame) {

                    /*
                     * The new `WorkflowRun` startup is normal enough that we can stop
                     * enforcing whether it failed at the time of its initiation.
                     */
                    Thread.currentThread().stop()
                }

                if (newWorkflowRun.get().getResult().equals(Result.FAILURE)) {
                    println("rebuilding the build: " + newWorkflowRun.get().toString() + " that replaced the old build: " + build.toString() + ", because it ended up in a failure within the `enforcementTimeFrame` (" + enforcementTimeFrame + "ms)")

                    // Create another new `WorkflowRun`, and try again.
                    newWorkflowRun = workflowJob.scheduleBuild2(0)
                }
            }
        }
    }
}

return this
