pipeline {
    agent any

    environment {
        CONNECT_SNAPSHOTS_REPO_USER = credentials('connect-snapshots-repo-user')
        CONNECT_SNAPSHOTS_REPO_PASSWORD = credentials('connect-snapshots-repo-password')
        CONNECT_SNAPSHOTS_REPO_URL = credentials('connect-snapshots-repo-url')
        SNYK_TOKENS = credentials('snyk-tokens')
        OSSRH_USERNAME = credentials('ossrh-username')
        OSSRH_PASSWORD = credentials('ossrh-password')
        SIGNING_KEY_ID = credentials('signing-key-id')
        SIGNING_PASSWORD = credentials('signing-password')
        SIGNING_SECRET_KEY_BASE64 = credentials('signing-secret-key-base64')
    }

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

                stage("Checks") {
                    parallel {
                        stage("Build") {
                            steps {
                                echo "Building.."
                                sh "cd outbound-sdk; ../gradlew --no-daemon clean build; cd ../"
                                sh "cd elasticsearch-outbound-sdk; ../gradlew --no-daemon clean build; cd ../"
                            }
                        }

                        stage("Vulnerability scanning") {
                            steps {
                               echo "Running snyk scan.."
                               sh "./gradlew --no-daemon snyk-test --no-parallel"
                            }
                        }

                        stage("Dependency Upgrades") {
                            steps {
                                script {
                                    echo "Checking dependencies.."
                                    sh "./gradlew dependencyUpdates -Drevision=release -DgradleReleaseChannel=current -DoutputFormatter=html --no-parallel --warning-mode all"
                                }
                            }
                        }
                    }
                }

                stage("Validate Maven Central credentials") {
                    steps {
                        echo "Validating OSSRH credentials.."
                        sh "./gradlew --no-daemon verifyOssrhCredentials"
                    }
                }

                stage("Upload") {
                    steps {
                        echo "Uploading archives.."
                        sh "./gradlew --no-daemon publish"
                    }
                }

                stage("Compile Examples") {
                    steps {
                        echo "Compiling Examples.."
                        sh "cd examples/maven; mvn -B -Dstyle.color=never -U clean package; cd ../.."
                        sh "cd examples/gradle;  ./gradlew --no-daemon clean shadowJar; cd ../.."
                    }
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: '**/build/dependencyUpdates/*'
            recordCoverage(tools: [[parser: 'JACOCO']])
        }
        cleanup {
            cleanWs()
        }
    }
}
