pipeline {
    agent any

    environment {
        SERVER_IP = '84.201.138.119'
        REMOTE_PATH = '~'
        DOCKER_COMPOSE_FILE = 'docker-compose.yml'
        DOCKER_FILE = 'Dockerfile'
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
                    sh "scp -i /Users/engend/Desktop/keys/edKey /Users/engend/IdeaProjects/EverGrowFinance/${DOCKER_COMPOSE_FILE} ever-admin@${SERVER_IP}:${REMOTE_PATH}"
                    sh "scp -r -i /Users/engend/Desktop/keys/edKey /Users/engend/IdeaProjects/EverGrowFinance/${DOCKER_FILE} ever-admin@${SERVER_IP}:${REMOTE_PATH}"
                    sh "scp -r -i /Users/engend/Desktop/keys/edKey /Users/engend/IdeaProjects/EverGrowFinance/target ever-admin@${SERVER_IP}:${REMOTE_PATH}"

                    sh """
                        ssh -i /Users/engend/Desktop/keys/edKey ever-admin@${SERVER_IP} '
                        cd ${REMOTE_PATH}
                        rm -f docker-compose.yml
                        rm -f Dockerfile
                        rm -rf target
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
