pipeline {
    agent any

    environment {
        SERVER_IP = '84.201.138.119'
        REMOTE_PATH = '~'
        DOCKER_IMAGE = 'evergrowfinance-backend'
        DOCKER_TAG = 'latest'
        DOCKER_PORT = 8080
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
                        sh "docker build --build-arg JAR_FILE=target/EverGrowFinance-0.0.1-SNAPSHOT.jar -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                        sh "docker save ${DOCKER_IMAGE}:${DOCKER_TAG} -o ${DOCKER_IMAGE}.tar"
                    }
                }
            }
        }

        stage('Transfer and Deploy Backend') {
            steps {
                script {
                    // Копирование образа на сервер
                    sh "scp -i /Users/engend/Desktop/keys/edKey /Users/engend/IdeaProjects/EverGrowFinance/${DOCKER_IMAGE}.tar ever-admin@${SERVER_IP}:${REMOTE_PATH}"
                    // SSH в сервер для загрузки образа и запуска
                    sh "ssh -i /Users/engend/Desktop/keys/edKey ever-admin@${SERVER_IP} 'docker load -i ${REMOTE_PATH}/${DOCKER_IMAGE}.tar && docker run -d -p ${DOCKER_PORT}:${DOCKER_PORT} ${DOCKER_IMAGE}:${DOCKER_TAG}'"
                }
            }
        }
    }

    post {
        always {
            // Чистка после сборки
            sh "echo 'Cleaning up'"
            sh "rm -f /Users/engend/IdeaProjects/EverGrowFinance/${DOCKER_IMAGE}.tar"
        }
    }
}
