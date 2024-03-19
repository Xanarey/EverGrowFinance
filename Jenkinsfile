pipeline {
    agent any

    environment {
        // Задаем переменные среды
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
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Deploy to Yandex Cloud') {
            steps {
                // Копируем всю папку проекта на сервер
                sh 'scp -r /Users/engend/Desktop/ever-remote engend@51.250.90.24:~'
                // Выполняем деплой с помощью docker-compose
                sh 'ssh engend@51.250.90.24 "docker-compose -f ~/ever-remote/EverGrowFinance/docker-compose.yml up -d backend"'
            }
        }
    }

    post {
        // Действия после выполнения пайплайна
        always {
            // Удалить рабочий каталог после выполнения сборки
            cleanWs()
        }
    }
}
