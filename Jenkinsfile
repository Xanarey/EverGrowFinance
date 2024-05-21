pipeline {
    agent any

    environment {
        SERVER_IP = '158.160.154.130'
        REMOTE_PATH = '~'
        DOCKER_COMPOSE_FILE = 'docker-compose.yml'
        DOCKER_FILE = 'Dockerfile'
        P_KEY = '/users/engend/desktop/keys/edkey'
        USER = 'ever-cloud'

        PRODUCTION_IP = 'http://158.160.154.130'
        DEVELOPMENT_IP = 'http://localhost:3000'
        APP_PROPERTIES_PATH = '/Users/engend/IdeaProjects/EverGrowFinance/src/main/resources/application.properties'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Prepare Environment') {
            steps {
                script {
                    sh "sed -i '' 's|CORS_ALLOWED_ORIGIN=.*|CORS_ALLOWED_ORIGIN=${PRODUCTION_IP}|' ${APP_PROPERTIES_PATH}"
                }
            }
        }

        stage('Prepare Environment') {
            steps {
                script {
                    sh "sed -i '' 's|CORS_ALLOWED_ORIGIN=${DEVELOPMENT_IP}|CORS_ALLOWED_ORIGIN=${PRODUCTION_IP}|' ${APP_PROPERTIES_PATH}"
                }
            }
        }

        stage('Transfer and Deploy') {
            steps {
                script {
                    // Передача файлов
                    sh "scp -i ${P_KEY} /Users/engend/IdeaProjects/EverGrowFinance/${DOCKER_COMPOSE_FILE} ${USER}@${SERVER_IP}:${REMOTE_PATH}"
                    sh "scp -r -i ${P_KEY} /Users/engend/IdeaProjects/EverGrowFinance/${DOCKER_FILE} ${USER}@${SERVER_IP}:${REMOTE_PATH}"
                    sh "scp -r -i ${P_KEY} /Users/engend/IdeaProjects/EverGrowFinance/target ${USER}@${SERVER_IP}:${REMOTE_PATH}"

                    // Развертывание
                    sh """
                        ssh -i ${P_KEY} ${USER}@${SERVER_IP} '
                        cd ${REMOTE_PATH}
                        sudo docker-compose down
                        sudo docker system prune -a -f
                        sudo docker-compose up --build -d
                        echo "Docker Compose started."
                        '
                    """
                }
            }
        }
    }

    post {
        always {
            sh "echo 'Cleaning up'"
        }
    }
}
