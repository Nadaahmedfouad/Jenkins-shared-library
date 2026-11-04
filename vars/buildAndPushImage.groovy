
def call(String imageName, String imageTag) {
    stage('Build and Push Docker Image') {

        sh """
            echo "Building Docker image..."
            docker build -t ${imageName}:${imageTag} .

            echo "Scanning Docker image with Trivy..."
            trivy image --timeout 15m --scanners vuln \
              --exit-code 0 ${imageName}:${imageTag}
        """

        withCredentials([
            usernamePassword(
                credentialsId: 'dockerhub-creds',
                usernameVariable: 'DOCKER_USER',
                passwordVariable: 'DOCKER_TOKEN'
            )
        ]) {
            sh """
                set +x
                echo "\$DOCKER_TOKEN" | docker login \
                  -u "\$DOCKER_USER" --password-stdin

                docker push ${imageName}:${imageTag}
                docker logout
            """
        }

        sh """
            echo "Removing local Docker image..."
            docker rmi ${imageName}:${imageTag}
        """
    }
}
