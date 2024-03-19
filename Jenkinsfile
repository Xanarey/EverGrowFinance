pipeline {
    agent any

    environment {
            MAVEN_HOME = '/path/to/maven' // Specify Maven installation directory
            PATH = "$MAVEN_HOME/bin:$PATH" // Add Maven bin directory to PATH
        }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/Xanarey/EverGrowFinance.git'
            }
        }

        stage('Build') {
            steps {
                sh 'echo $PATH' // Print PATH variable for debugging
                sh './mvnw clean package -DskipTests' // Use Maven Wrapper
            }
        }

        stage('Deploy to Yandex Cloud') {
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
