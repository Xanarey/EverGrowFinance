pipeline {
    agent any

    environment {
        SERVER_IP = '84.201.138.119'
        REMOTE_PATH = '/'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Backend Docker Image') {
            steps {
                script {

                    dir('/Users/engend/IdeaProjects/EverGrowFinance') {
                        sh 'mvn clean install -DskipTests'
                        sh 'docker build -t evergrowfinance-backend .'
                        sh 'docker save evergrowfinance-backend -o evergrowfinance-backend.tar'
                    }
                }
            }
        }

        stage('Transfer and Deploy Backend') {
            steps {
                script {
                    // Копирование образа на сервер
                    sh "scp /Users/engend/IdeaProjects/EverGrowFinance/evergrowfinance-backend.tar ${SERVER_IP}:${REMOTE_PATH}/"
                    // SSH в сервер для загрузки образа и запуска
                    sh """
                        ssh ${SERVER_IP} '
                        docker load -i ${REMOTE_PATH}/evergrowfinance-backend.tar
                        docker run -d -p 8080:8080 evergrowfinance-backend
                        '
                    """
                }
            }
        }
    }

    post {
        always {
            // Чистка после сборки
            sh 'echo "Cleaning up"'
            sh 'rm -f /Users/engend/IdeaProjects/EverGrowFinance/evergrowfinance-backend.tar'
        }
    }
}
