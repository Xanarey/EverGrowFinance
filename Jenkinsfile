pipeline {
    agent any

    environment {
        SERVER_IP = '90.156.216.53'
        REMOTE_PATH = '~'
        DOCKER_COMPOSE_FILE = 'docker-compose.yml'
        DOCKER_FILE = 'Dockerfile'
        P_KEY = '/Users/engend/Desktop/keys/ubuntu-STD2-1-1-15GB-k3n66suc.pem'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                script {
                    dir('/Users/engend/IdeaProjects/EverGrowFinance') {
                        sh 'mvn clean install -DskipTests'
                    }
                }
            }
        }

        stage('Transfer and Deploy') {
            steps {
                script {
                    sh "scp -i ${P_KEY} /Users/engend/IdeaProjects/EverGrowFinance/${DOCKER_COMPOSE_FILE} ever-admin@${SERVER_IP}:${REMOTE_PATH}"
                    sh "scp -r -i ${P_KEY} /Users/engend/IdeaProjects/EverGrowFinance/${DOCKER_FILE} ever-admin@${SERVER_IP}:${REMOTE_PATH}"
                    sh "scp -r -i ${P_KEY} /Users/engend/IdeaProjects/EverGrowFinance/target ever-admin@${SERVER_IP}:${REMOTE_PATH}"

                    sh """
                        ssh -i ${P_KEY} ever-admin@${SERVER_IP} '
                        cd ${REMOTE_PATH}
                        docker-compose down
                        docker system prune -a -f
                        docker-compose up --build -d
                        echo 'Docker Compose started.'
                        '
                    """
                }
            }
        }
    }

    post {
        always {
            sh "echo 'Cleaning up'"
            sh "rm -f /Users/engend/IdeaProjects/EverGrowFinance/evergrowfinance-backend.tar"
            sh "rm -f /Users/engend/IdeaProjects/EverGrowFinance/postgres.tar"
        }
    }
}
