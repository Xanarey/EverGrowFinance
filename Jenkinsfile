pipeline {
    agent any
    environment {
        DOCKER_IMAGE = 'evergrowfinance:latest'
        DEPLOY_PATH = '/Users/engend/Desktop/ever-remote/EverGrowFinance'
        SERVER_IP = '51.250.90.24'
    }
    stages {
        stage('Clone repository') {
            steps {
                git 'https://github.com/Xanarey/EverGrowFinance.git'
            }
        }
        stage('Build Docker image') {
            steps {
                script {
                    docker.build("${DOCKER_IMAGE}")
                }
            }
        }
        stage('Push Docker image') {
            steps {
                script {
                    docker.withRegistry('', 'docker-credentials-id') {
                        docker.image("${DOCKER_IMAGE}").push()
                    }
                }
            }
        }
        stage('Deploy to Yandex Cloud') {
            steps {
                script {
                    // Заменяем localhost на IP адрес сервера в docker-compose.yml
                    sh "sed -i '' 's/localhost/${SERVER_IP}/g' ${DEPLOY_PATH}/docker-compose.yml"
                    // Подключаемся по SSH и запускаем docker-compose
                    sshagent(['ssh-credentials-id']) {
                        sh "scp ${DEPLOY_PATH}/docker-compose.yml engend@${SERVER_IP}:/path/to/remote/directory"
                        sh "ssh engend@${SERVER_IP} 'docker-compose -f /path/to/remote/directory/docker-compose.yml up -d --build'"
                    }
                }
            }
        }
    }
    post {
        always {
            echo 'Cleaning up'
            cleanWs()
        }
    }
}
