pipeline {
    agent any
    environment {
        DEPLOY_PATH = '/Users/engend/Desktop/ever-remote/EverGrowFinance'
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
                    // Заменяем localhost на IP адрес сервера в docker-compose.yml
                    sh "sed -i '' 's/localhost/${SERVER_IP}/g' ${DEPLOY_PATH}/docker-compose.yml"
                    // Подключаемся по SSH и запускаем сборку и деплой
                    ssh-agent(['ssh-credentials-id']) {
                        // Копируем исходники на сервер
                        sh "scp -r ${DEPLOY_PATH}/* engend@${SERVER_IP}:/path/to/remote/directory"
                        // Выполняем сборку и запуск через docker-compose на сервере
                        sh "ssh engend@${SERVER_IP} 'cd /path/to/remote/directory && docker-compose up -d --build'"
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
