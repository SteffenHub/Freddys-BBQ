pipeline {
    agent {
        node {
            // make shure you have the agent configured with image: jenkins/agent:alpine-jdk21
            label 'docker-agent-java-alpine-21'
        }
    }
    environment {
        TESTCONTAINERS_HOST_OVERRIDE = 'host.docker.internal'
    }

    stages {
        stage('Checkout Repository') {
            steps {
                echo "Checking out repository..."
                checkout scm
            }
        }

        stage('Run Tests') {
            parallel {
                stage('Backend Tests') {
                    steps {
                        echo "Running Backend tests..."
                        dir('Backend/delivery') {
                            sh './gradlew test'
                        }
                        dir('Backend/order') {
                            sh './gradlew test'
                        }
                    }
                }
                stage('Frontend Tests') {
                    steps {
                        echo "Running Frontend tests..."
                        dir('Frontend/customer') {
                            sh './gradlew test'
                        }
                        dir('Frontend/Intern') {
                            sh './gradlew test'
                        }
                    }
                }
            }
        }

        stage('Deploy') {
            when {
                branch 'main'
            }
            steps {
                echo "Branch is 'main', starting release process..."
                echo "Prepare docker compose"
                sh '''
                apk update
                apk add docker-cli-compose
                docker compose version
                '''
                echo "Start container"
                sh '''
                    docker-compose up -d
                    docker-compose down --rmi all
                '''
                echo "Release completed"
            }
        }

        stage('Deploy Release Version') {
            when {
                branch 'main'
            }
            steps {
                echo "Branch is 'main', starting release process..."
                echo "Prepare docker compose"
                sh '''
                apk update
                apk add docker-cli-compose
                docker compose version
                '''
                echo "Start container"
                sh '''
                    docker-compose -f docker-compose-release.yml up -d
                    docker-compose -f docker-compose-release.yml down --rmi all
                '''
                echo "Release completed"
            }
        }
    }
}