pipeline {
    agent any

    options {
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '5', artifactNumToKeepStr: '5'))
    }

    stages {
        stage("Pipeline") {
            stages {
                stage("Checkout") {
                    steps {
                        checkout([
                            $class: 'GitSCM',
                            branches: scm.branches,
                            extensions: scm.extensions + [[$class: 'CleanBeforeCheckout']],
                            userRemoteConfigs: scm.userRemoteConfigs
                        ])
                    }
                }

                stage("Build") {
                    steps {
                        echo "Building.."
                        sh "./gradlew --no-daemon clean build"
                        sh "cd examples/maven; mvn clean package; cd ../.."
                        sh "cd examples/gradle;  ./gradlew --no-daemon clean shadowJar; cd ../.."
                    }
                }

                stage("Checks") {
                    stage("Vulnerability scanning") {
                        steps {
                            script {
                               echo "Running snyk scan.."
                               sh "./gradlew --no-daemon snyk-test --continue"
                            }
                        }
                    }
                }
            }
        }
    }

    post {
        cleanup {
            cleanWs()
        }
    }
}
