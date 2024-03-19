pipeline {
    agent any

    tools {
        maven 'MavenInstallationName' // Используем имя установки Maven, настроенной в Jenkins
    }

    stages {
        stage('Отладка') {
            steps {
                sh 'echo $PATH' // Вывести значение переменной PATH для отладки
                sh 'which mvn'  // Проверить местоположение исполняемого файла mvn
            }
        }

        stage('Получение кода') {
            steps {
                git 'https://github.com/Xanarey/EverGrowFinance.git'
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
