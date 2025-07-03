pipeline {
    agent any
    environment {
        AWS_REGION = 'ap-south-1'
        ECR_REPO = '767224848485.dkr.ecr.ap-south-1.amazonaws.com/testrepo'
        APP_NAME = 'java-app'
        HELM_CHART_PATH = './java-app-chart' 
    }	
    stages {
        stage('checkout-stage') {
            steps {
                git branch: 'master', credentialsId: 'ashoksm', url: 'https://github.com/ashok77sm/helm-chart-java-app.git'
            }
        }

        stage('build-stage') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    dockerImage = docker.build("${ECR_REPO}:V${env.BUILD_NUMBER}")
                }
            }
        }

        stage('Login to AWS ECR') {
                steps {
                  withCredentials([usernamePassword(credentialsId: 'aws-ecr-creds', usernameVariable: 'AWS_ACCESS_KEY_ID', passwordVariable: 'AWS_SECRET_ACCESS_KEY')]) {
                    sh '''
                        aws configure set aws_access_key_id $AWS_ACCESS_KEY_ID
                        aws configure set aws_secret_access_key $AWS_SECRET_ACCESS_KEY
                        aws configure set default.region $AWS_REGION
                        aws ecr get-login-password --region $AWS_REGION | docker login --username AWS --password-stdin $ECR_REPO
                    '''
                  }
                }
        }

        stage('Push Docker Image to ECR') {
            steps {
                sh "docker push ${ECR_REPO}:V${env.BUILD_NUMBER}"
            }
        }
        stage('Deploy to EKS') {
            steps {
              withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-ecr-creds']]) {
                sh """
                  aws eks update-kubeconfig --region ap-south-1 --name eks-cluster && \
                  helm upgrade --install java-app ./java-app-chart \
                     --set image.repository=767224848485.dkr.ecr.ap-south-1.amazonaws.com/testrepo \
                     --set image.tag=V${env.BUILD_NUMBER} \
                     --wait
                """
              }
            }
        }

    }
}
