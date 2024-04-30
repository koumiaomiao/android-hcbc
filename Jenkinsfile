pipeline {
    agent any
    triggers {
        pollSCM("H/15 * * * *")
    }
    options {
        buildDiscarder(logRotator(numToKeepStr: '30', artifactNumToKeepStr: '30'))
    }

    stages {
        stage('Clean Env') {
            steps {
                sh './gradlew clean'
            }
        }
        stage('Build Hcbc') {
            options {
                retry(3)
            }
            environment {
                PROJECT = 'app'
            }
            steps {
                sh './gradlew testDebugUnitTest'
                sh './gradlew -p ${PROJECT} assembleDebug'
            }
        }
    }
}

