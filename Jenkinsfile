pipeline {
    agent any
    environment {
        DEPLOY_PATH = '/Users/engend/IdeaProjects/EverGrowFinance'
        SERVER_IP = '51.250.90.24'
    }
    stages {
        stage('Clone repository') {
            steps {
                git 'https://github.com/Xanarey/EverGrowFinance.git'
            }
        }
        stage('Deploy to Yandex Cloud') {
            steps {
                script {
                    // Подключаемся по SSH и запускаем сборку и деплой
                    sshagent(['ever-id-engend']) {
                        // Копируем исходники на сервер
                        sh "scp -r ${DEPLOY_PATH}/* engend@${SERVER_IP}:~"
                        // Выполняем сборку и запуск через docker-compose на сервере
                        sh "ssh engend@${SERVER_IP} 'docker-compose up -d --build'"
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
