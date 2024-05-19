// pipeline {
//     agent any
//
//     environment {
//         SERVER_IP = '84.201.138.119'
//         REMOTE_PATH = '~'
//         DOCKER_IMAGE = 'evergrowfinance-backend'
//         DOCKER_TAG = 'latest'
//         DOCKER_PORT = 8080
//     }
//
//     stages {
//         stage('Checkout') {
//             steps {
//                 checkout scm
//             }
//         }
//
//         stage('Build Backend Docker Image') {
//             steps {
//                 script {
//                     dir('/Users/engend/IdeaProjects/EverGrowFinance') {
//                         sh 'mvn clean install -DskipTests'
//                         sh "docker build --build-arg JAR_FILE=target/EverGrowFinance-0.0.1-SNAPSHOT.jar -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
//                         sh "docker save ${DOCKER_IMAGE}:${DOCKER_TAG} -o ${DOCKER_IMAGE}.tar"
//                     }
//                 }
//             }
//         }
//
//         stage('Transfer and Deploy Backend') {
//             steps {
//                 script {
//                     // Копирование образа на сервер
//                     sh "scp -i /Users/engend/Desktop/keys/edKey /Users/engend/IdeaProjects/EverGrowFinance/${DOCKER_IMAGE}.tar ever-admin@${SERVER_IP}:${REMOTE_PATH}"
//                     // SSH в сервер для загрузки образа и запуска
//                     sh """
//                         ssh -i /Users/engend/Desktop/keys/edKey ever-admin@${SERVER_IP} '
//                         docker load -i ${REMOTE_PATH}/${DOCKER_IMAGE}.tar
//                         docker stop ${DOCKER_IMAGE} || true  // Останавливаем старый контейнер, если он запущен
//                         docker rm ${DOCKER_IMAGE} || true    // Удаляем старый контейнер, если он существует
//                         sh 'ssh ever-admin@${SERVER_IP} "docker-compose -f ~/EverGrowFinance/docker-compose.yml up -d backend"'
//                         '
//                     """
//                 }
//             }
//         }
//
//     }
//
//     post {
//         always {
//             // Чистка после сборки
//             sh "echo 'Cleaning up'"
//             sh "rm -f /Users/engend/IdeaProjects/EverGrowFinance/${DOCKER_IMAGE}.tar"
//         }
//     }
// }
//
//


pipeline {
    agent any

    environment {
        SERVER_IP = '84.201.138.119'
        REMOTE_PATH = '~'
        DOCKER_COMPOSE_FILE = 'docker-compose.yml'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Save Images') {
            steps {
                script {
                    dir('/Users/engend/IdeaProjects/EverGrowFinance') {
                        // Сборка проекта, если это требуется
                        sh 'mvn clean install -DskipTests'
                        // Сборка Docker образов с помощью Docker Compose
                        sh 'docker-compose build'
                        // Сохранение образов в tar архивы
                        sh 'docker save evergrowfinance-backend -o evergrowfinance-backend.tar'
                        sh 'docker save postgres -o postgres.tar'
                    }
                }
            }
        }

        stage('Transfer and Deploy') {
            steps {
                script {
                    // Передача архивов и docker-compose.yml на сервер
                    sh "scp -i /Users/engend/Desktop/keys/edKey /Users/engend/IdeaProjects/EverGrowFinance/evergrowfinance-backend.tar ever-admin@${SERVER_IP}:${REMOTE_PATH}"
                    sh "scp -i /Users/engend/Desktop/keys/edKey /Users/engend/IdeaProjects/EverGrowFinance/postgres.tar ever-admin@${SERVER_IP}:${REMOTE_PATH}"
                    sh "scp -i /Users/engend/Desktop/keys/edKey /Users/engend/IdeaProjects/EverGrowFinance/${DOCKER_COMPOSE_FILE} ever-admin@${SERVER_IP}:${REMOTE_PATH}"

                    // SSH на сервер для загрузки образов и запуска с помощью Docker Compose
                    sh """
                        ssh -i /Users/engend/Desktop/keys/edKey ever-admin@${SERVER_IP} '
                        docker load -i ${REMOTE_PATH}/evergrowfinance-backend.tar
                        docker load -i ${REMOTE_PATH}/postgres.tar
                        cd ${REMOTE_PATH}
                        docker-compose up -d
                        '
                    """
                }
            }
        }
    }

    post {
        always {
            // Чистка после сборки
            sh "echo 'Cleaning up'"
            sh "rm -f /Users/engend/IdeaProjects/EverGrowFinance/evergrowfinance-backend.tar"
            sh "rm -f /Users/engend/IdeaProjects/EverGrowFinance/postgres.tar"
        }
    }
}
