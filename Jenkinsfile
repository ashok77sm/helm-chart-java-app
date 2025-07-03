pipeline {
    agent {
        label 'build'
    }
    environment {
	    DOCKER_IMAGE = 'ashokm77/test-repo'
	    DOCKER_CREDENTIAL_ID= 'docker-creds'
    }	
    stages {
            stage('checkout-stage') {
                steps {
                    git branch: 'master', credentialsId: 'ashoksm', url: 'https://github.com/ashok77sm/docker-sample-java-webapp.git'
                }
            }   
	    stage('build-stage') {
                steps {
                   sh 'mvn clean install'
                }
            }
	    stage('SonarQube Analysis Stage') {
                steps{
                   withSonarQubeEnv('sonarqube') { 
                       sh "mvn clean verify sonar:sonar"
                   }
                }
            }
	    stage('Build Docker Image') {
                steps {
                   script {
                       dockerImage = docker.build("${DOCKER_IMAGE}:V${env.BUILD_NUMBER}")
                   }
                }
            }
	    stage('Push Docker Image') {
                steps {
                   script {
                      docker.withRegistry('https://index.docker.io/v1/', "${DOCKER_CREDENTIAL_ID}") {
                        dockerImage.push()
                      }
                   }
                }
            }
	    stage('Run Docker Container') {
               steps {
                  script {
                    sh "docker run -d -p 9000:8080 ${DOCKER_IMAGE}:V${env.BUILD_NUMBER}"
                  }
               }
	    }
    }
}
