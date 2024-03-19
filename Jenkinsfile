pipeline {
    agent any

    tools {
            // Используем Maven, настроенный в Global Tool Configuration Jenkins
            maven 'M3'
        }

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
                        sh 'mvn clean package -DskipTests'
                    }
                }

        stage('Сборка') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Развертывание на Yandex Cloud') {
            steps {
                sh 'scp -r /Users/engend/Desktop/ever-remote engend@51.250.90.24:~'
                sh 'ssh engend@51.250.90.24 "docker-compose -f ~/ever-remote/EverGrowFinance/docker-compose.yml up -d backend"'
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
