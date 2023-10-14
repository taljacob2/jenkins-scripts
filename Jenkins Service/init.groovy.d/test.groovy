#!/usr/bin/groovy
@Library('github.com/darinpope/github-api-global-lib@master') _

echo "Starting"
try {
    sleep 5
    helloWorld("Darin")
} catch (Exception ex) {
    println ex.printStackTrace()
}
echo "Finishing"
