pipeline {
    agent any

    tools {
        maven 'M3' // Убедитесь, что имя 'M3' совпадает с именем, указанным в настройках Jenkins
    }

    environment {
        MAVEN_OPTS = '-Dmaven.test.skip=true -Xmx1024m -XX:ReservedCodeCacheSize=512m'
        PATH+MAVEN = "${tool 'M3'}/bin"
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    // Клонируем репозиторий
                    checkout scm
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    // Запускаем сборку Maven без тестов
                    sh 'mvn -T 1C clean package -DskipTests'
                }
            }
        }

        stage('Deploy to Yandex Cloud') {
            steps {
                script {
                    // Отправляем проект и jar файл на сервер
                    sh 'scp -r /Users/engend/IdeaProjects/EverGrowFinance engend@84.201.138.119:~'
                    sh 'scp target/EverGrowFinance-0.0.1-SNAPSHOT.jar engend@84.201.138.119:~/EverGrowFinance'
                    // Запускаем приложение через Docker Compose
                    sh 'ssh engend@84.201.138.119 "docker-compose -f ~/EverGrowFinance/docker-compose.yml up -d backend"'
                }
            }
        }
    }

    post {
        always {
            // Очищаем рабочую область после выполнения сборки
            cleanWs()
        }
        success {
            echo 'Сборка и деплой прошли успешно!'
        }
        failure {
            echo 'Произошла ошибка в процессе сборки или деплоя.'
        }
    }
}
