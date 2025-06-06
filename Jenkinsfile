// Just a test comment
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

        stage('Prepare Environment') {
            steps {
                echo "setting permissions for gradlew..."
                sh '''
                    chmod +x Backend/delivery/gradlew
                    chmod +x Backend/order/gradlew
                    chmod +x Frontend/customer/gradlew
                    chmod +x Frontend/Intern/gradlew
                '''
            }
        }

        stage('Run Tests') {
            steps {
                echo "Running tests for all modules..."
                sh '''
                    echo "Running Backend tests..."
                    cd Backend/delivery && ./gradlew test && cd ../..
                    cd Backend/order && ./gradlew test && cd ../..

                    echo "Running Frontend tests..."
                    cd Frontend/customer && ./gradlew test && cd ../..
                    cd Frontend/Intern && ./gradlew test && cd ../..
                '''
            }
        }
    }
}