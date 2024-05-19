pipeline {
    agent any

    environment {
        SERVER_IP = '84.201.138.119'
        REMOTE_PATH = '~'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Backend Docker Image') {
            steps {
                script {
                    dir('/Users/engend/IdeaProjects/EverGrowFinance') {
                        sh 'mvn clean install -DskipTests'
                        sh 'docker build --build-arg JAR_FILE=target/EverGrowFinance-0.0.1-SNAPSHOT.jar -t evergrowfinance-backend .'
                        sh 'docker save evergrowfinance-backend -o evergrowfinance-backend.tar'
                    }
                }
            }
        }

        stage('Transfer and Deploy Backend') {
           steps {
               script {
                   // Копирование образа на сервер
                   sh "scp -i /Users/engend/Desktop/keys/edKey /Users/engend/IdeaProjects/EverGrowFinance/evergrowfinance-backend.tar ever-admin@${SERVER_IP}:${REMOTE_PATH}"
                   // SSH в сервер для загрузки образа и запуска
                   sh "ssh -i /Users/engend/Desktop/keys/edKey ever-admin@${SERVER_IP} 'docker load -i ~/evergrowfinance-backend.tar && docker run -d -p 8080:8080 evergrowfinance-backend'"
               }
           }


//         stage('Transfer and Deploy Backend') {
//             steps {
//                 script {
//                     // Копирование образа на сервер
//                     sh "scp -i /Users/engend/Desktop/keys/edKey /Users/engend/IdeaProjects/EverGrowFinance/evergrowfinance-backend.tar ever-admin@84.201.138.119:~"
//                     // SSH в сервер для загрузки образа и запуска
//                     sh """
//                         ssh ${SERVER_IP} '
//                         docker load -i /Users/engend/Desktop/keys/edKey evergrowfinance-backend.tar
//                         docker run -i /Users/engend/Desktop/keys/edKey -d -p 8080:8080 evergrowfinance-backend
//                         '
//                     """
//                 }
//             }
//         }
    }

    post {
        always {
            // Чистка после сборки
            sh 'echo "Cleaning up"'
            sh 'rm -f /Users/engend/IdeaProjects/EverGrowFinance/evergrowfinance-backend.tar'
        }
    }
}
