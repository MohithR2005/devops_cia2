pipeline {
    agent any

    tools {
        maven 'Maven-3'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                bat 'mvn clean test'
            }
        }

        stage('Package') {
            steps {
                bat 'mvn package'
            }
        }
    }

    post {
        always {
            junit(
                testResults: 'course-registration-app/target/surefire-reports/TEST-*.xml',
                allowEmptyResults: false
            )
        }

        success {
            archiveArtifacts(
                artifacts: 'course-registration-app/target/*.jar',
                fingerprint: true
            )
        }

        failure {
            echo 'Build or tests failed. Check the Maven console output.'
        }
    }
}