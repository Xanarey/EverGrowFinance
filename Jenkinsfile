pipeline {
    agent any

    environment {
        DOCKER_COMPOSE_PATH = '/Users/engend/IdeaProjects/EverGrowFinance'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Deploy') {
            steps {
                script {
                    dir('EverGrowFinance') {
                        sh 'mvn clean install -DskipTests'
                        sh 'docker build -t evergrowfinance-backend .'
                    }

                    // Работа с Docker Compose
                    dir(DOCKER_COMPOSE_PATH) {

                        sh "sed -i 's/localhost/84.201.138.119/g' docker-compose.yml"

                        sh 'docker-compose up -d'
                    }
                }
            }
        }
    }

    post {
        always {

            sh 'echo "Cleaning up"'
        }
    }
}
