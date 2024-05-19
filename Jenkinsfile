pipeline {
    agent any

    environment {
        SERVER_IP = '84.201.138.119'
        REMOTE_PATH = '~/evergrowfinance'
        DOCKER_COMPOSE_FILE = 'docker-compose.yml'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Save Images') {
            steps {
                script {
                    dir('/Users/engend/IdeaProjects/EverGrowFinance') {
                        // Сборка проекта, если это требуется
                        sh 'mvn clean install -DskipTests'
                        // Загрузка образов, если они не существуют, и сборка Docker образов с помощью Docker Compose
                        sh 'docker-compose pull'
                        sh 'docker-compose build'
                        // Сохранение образов в tar архивы
                        sh 'docker save evergrowfinance-backend -o evergrowfinance-backend.tar'
                        sh 'docker save postgres:latest -o postgres.tar'
                    }
                }
            }
        }

        stage('Transfer and Deploy') {
            steps {
                script {
                    // Передача архивов, Dockerfile и docker-compose.yml на сервер
                    sh "scp -i /Users/engend/Desktop/keys/edKey /Users/engend/IdeaProjects/EverGrowFinance/evergrowfinance-backend.tar ever-admin@${SERVER_IP}:${REMOTE_PATH}"
                    sh "scp -i /Users/engend/Desktop/keys/edKey /Users/engend/IdeaProjects/EverGrowFinance/postgres.tar ever-admin@${SERVER_IP}:${REMOTE_PATH}"
                    sh "scp -i /Users/engend/Desktop/keys/edKey /Users/engend/IdeaProjects/EverGrowFinance/${DOCKER_COMPOSE_FILE} ever-admin@${SERVER_IP}:${REMOTE_PATH}"
                    sh "scp -r -i /Users/engend/Desktop/keys/edKey /Users/engend/IdeaProjects/EverGrowFinance/Dockerfile ever-admin@${SERVER_IP}:${REMOTE_PATH}"
                    sh "scp -r -i /Users/engend/Desktop/keys/edKey /Users/engend/IdeaProjects/EverGrowFinance/target ever-admin@${SERVER_IP}:${REMOTE_PATH}"

                    // SSH на сервер для загрузки образов и запуска с помощью Docker Compose
                    sh """
                        ssh -i /Users/engend/Desktop/keys/edKey ever-admin@${SERVER_IP} '
                        docker load -i ${REMOTE_PATH}/evergrowfinance-backend.tar
                        docker load -i ${REMOTE_PATH}/postgres.tar
                        cd ${REMOTE_PATH}
                        docker-compose up -d
                        '
                    """
                }
            }
        }
    }

    post {
        always {
            // Чистка после сборки
            sh "echo 'Cleaning up'"
            sh "rm -f /Users/engend/IdeaProjects/EverGrowFinance/evergrowfinance-backend.tar"
            sh "rm -f /Users/engend/IdeaProjects/EverGrowFinance/postgres.tar"
        }
    }
}
