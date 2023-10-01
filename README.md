# jenkins-scripts

## Jenkins Service

[Jenkins Service/init.groovy.d](/Jenkins%20Service/init.groovy.d/) bundles scripts to be run by the "init" hook in Jenkins.

> See [Groovy Hook Scripts](https://www.jenkins.io/doc/book/managing/groovy-hook-scripts/#post-initialization-script-init-hook)
>
> See [Create Groovy Hook Scripts Jenkins](https://linuxhint.com/create-groovy-hook-scripts-jenkins/)

### Run Groovy Scripts

To run groovy scripts, you need to [download the groovy CLI interpreter](https://groovy.jfrog.io/ui/native/dist-release-local/groovy-windows-installer).

Then you can run a groovy script with:

```
groovy <fileToExecute.groovy>
```

## Scripts

[Scripts](/Scripts/) bundles generic scripts, mainly to be run by the operating system.

In Windows, in the Task Scheduler, you can configure a specific script to be run "with highest privilleges", and to be triggered at "logon".

So that it would be run as an administrator at system boot.

For example:

![](https://i.imgur.com/5XbqHK3.png)
