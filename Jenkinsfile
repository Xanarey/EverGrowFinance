pipeline {
    agent any
    environment {

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
                        sh "scp -r /Users/engend/Desktop/ever-remote engend@51.250.90.24:~"
                        sh "ssh engend@51.250.90.24 'docker-compose up -d --build'"
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
