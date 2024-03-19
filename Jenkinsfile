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
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Docker Build and Push') {
            steps {
                // Собираем Docker образ и пушим его в ваш Docker registry
                script {
                    def app = docker.build("your-docker-registry/evergrowfinance:$BUILD_NUMBER")
                    app.push()
                }
            }
        }

        stage('Deploy to Yandex Cloud') {
            steps {
                // Заменяем localhost на IP-адрес и деплоим используя docker-compose
                sh 'sed -i "s/localhost/51.250.90.24/g" src/main/resources/application.properties'
                sh 'scp docker-compose.yml engend@51.250.90.24:/path/to/remote'
                sh 'ssh engend@51.250.90.24 docker-compose up -d backend'
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
