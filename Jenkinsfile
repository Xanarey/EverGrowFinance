pipeline {
    agent any

    environment {
        // Определите переменные окружения, если нужно
        MAVEN_OPTS = '-Dmaven.test.skip=true'
    }

    stages {
        stage('Checkout') {
            steps {
                // Получаем код из репозитория GitHub
                git 'https://github.com/Xanarey/EverGrowFinance.git'
            }
        }

        stage('Build') {
            steps {
                // Запускаем сборку Maven без тестов
                sh '/path/to/mvn clean package -DskipTests'
            }
        }

        stage('Deploy to Yandex Cloud') {
            steps {
                // Копируем всю папку проекта на сервер
                sh 'scp -r /Users/engend/Desktop/ever-remote engend@51.250.90.24:~'
                // Выполняем деплой с помощью docker-compose
                sh 'ssh engend@51.250.90.24 docker-compose -f ~/ever-remote/EverGrowFinance/docker-compose.yml up -d backend'
            }
        }

    }

    post {
        // Действия после выполнения пайплайна
        always {
            // Например, удалить Docker образы с Jenkins агента
            cleanWs()
        }
    }
}
