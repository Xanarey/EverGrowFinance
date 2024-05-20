pipeline {
    agent any

    environment {
        SERVER_IP = '158.160.165.159'
        REMOTE_PATH = '~'
        DOCKER_COMPOSE_FILE = 'docker-compose.yml'
        DOCKER_FILE = 'Dockerfile'
        P_KEY = '/users/engend/desktop/keys/edkey'
        USER = 'ever-cloud'
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
                    sh "scp -i ${P_KEY} /Users/engend/IdeaProjects/EverGrowFinance/${DOCKER_COMPOSE_FILE} ${USER}@${SERVER_IP}:${REMOTE_PATH}"
                    sh "scp -r -i ${P_KEY} /Users/engend/IdeaProjects/EverGrowFinance/${DOCKER_FILE} ${USER}@${SERVER_IP}:${REMOTE_PATH}"
                    sh "scp -r -i ${P_KEY} /Users/engend/IdeaProjects/EverGrowFinance/target ${USER}@${SERVER_IP}:${REMOTE_PATH}"

                    sh """
                        ssh -i ${P_KEY} ${USER}@${SERVER_IP} '
                        cd ${REMOTE_PATH}
                        sudo docker-compose down
                        sudo docker system prune -a -f
                        sudo docker-compose up --build -d
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
        }
    }
}
