pipeline {
    agent any
    environment {
        // Определяем переменную для Docker image
        DOCKER_IMAGE = 'evergrowfinance/backend'
        // Указываем адрес регистра для Docker
        DOCKER_REGISTRY = 'registry.hub.docker.com'
        // Версия для Docker-образа
        IMAGE_TAG = 'latest'
        // Доменное имя для развертывания
        DEPLOY_DOMAIN = '51.250.90.24'
    }
    stages {
        stage('Checkout') {
            steps {
                // Получаем код из GitHub
                checkout scm
            }
        }
        stage('Build') {
            steps {
                // Собираем проект, можно использовать скрипт или Maven plugin
                sh 'mvn clean package -DskipTests'
            }
        }
        stage('Build Docker Image') {
            steps {
                // Собираем Docker-образ
                sh "docker build -t ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${IMAGE_TAG} ."
            }
        }
        stage('Push Docker Image') {
            steps {
                // Публикуем образ в регистр
                sh "docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${IMAGE_TAG}"
            }
        }
        stage('Deploy') {
            steps {
                // Выполняем деплой используя docker-compose и подменяем домен
                sh "sed -i 's/localhost/${DEPLOY_DOMAIN}/g' docker-compose.yml"
                sh "docker-compose up -d"
            }
        }
    }
    post {
        always {
            // Чистим после себя, удаляем образ
            sh "docker rmi ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${IMAGE_TAG}"
        }
    }
}
