pipeline {
    agent any

    tools {
        // Убедитесь, что имя 'M3' совпадает с именем, указанным в настройках Jenkins
        maven 'M3'
    }

    environment {
        // Пропуск тестов и оптимизация Maven
        MAVEN_OPTS = '-Dmaven.test.skip=true -Xmx1024m -XX:ReservedCodeCacheSize=512m'
    }

    stages {
        stage('Checkout') {
            steps {
                // Клонируем репозиторий
                checkout scm
            }
        }

        stage('Build') {
            steps {
                // Запускаем сборку Maven без тестов
                sh 'mvn -T 1C clean package -DskipTests'
            }
        }

        stage('Deploy to Yandex Cloud') {
            steps {
                // Отправляем проект и jar файл на сервер
                sh 'scp -r /Users/engend/IdeaProjects/EverGrowFinance engend@84.201.138.119:~'
                sh 'scp target/EverGrowFinance-0.0.1-SNAPSHOT.jar engend@84.201.138.119:~/EverGrowFinance'
                // Запускаем приложение через Docker Compose
                sh 'ssh engend@84.201.138.119 "docker-compose -f ~/EverGrowFinance/docker-compose.yml up -d backend"'
            }
        }
    }

    post {
        always {
            // Очищаем рабочую область после выполнения сборки
            cleanWs()
        }
    }
}