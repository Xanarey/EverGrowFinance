pipeline {
    agent any

    tools {
        // Убедитесь, что имя 'M3' совпадает с именем, указанным в настройках Jenkins
        maven 'M3'
    }

    environment {
        // Установите переменные окружения, если необходимо
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
                // Теперь копируем только бэкенд
                sh 'scp -r /Users/engend/IdeaProjects/EverGrowFinance engend@51.250.90.24:~/ever-remote'
                sh 'ssh engend@51.250.90.24 "docker-compose -f ~/EverGrowFinance/docker-compose.yml up -d backend"'
            }
        }
    }

    post {
        // Действия после выполнения пайплайна
        always {
            // Удаляем рабочий каталог после сборки
            cleanWs()
        }
    }
}
