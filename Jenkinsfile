pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // Получение последней версии из репозитория
                git 'https://github.com/Xanarey/EverGrowFinance'
            }
        }

        stage('Build and Push Docker Image') {
            steps {
                // Здесь должны быть команды для сборки Docker образа
                sh 'docker build -t evergrowfinance-backend .'
                // Затем пушим образ в ваш Docker репозиторий
                sh 'docker push evergrowfinance-backend'
            }
        }

        stage('Deploy to Yandex Cloud') {
            steps {
                // Замена localhost на IP в docker-compose.yml
                sh "sed -i 's/localhost/51.250.90.24/g' docker-compose.yml"
                // Запуск docker-compose для деплоя
                sh 'docker-compose up -d backend'
            }
        }
    }
}
