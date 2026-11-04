
def call(String imageName, String imageTag) {
    stage('Build and Push Docker Image') {
        withCredentials([
            usernamePassword(
                credentialsId: 'dockerhub-credentials',
                usernameVariable: 'DOCKER_USER',
                passwordVariable: 'DOCKER_PASS'
            )
        ]) {
            sh """
                docker build -t ${imageName}:${imageTag} .
                trivy image --exit-code 0 --severity HIGH,CRITICAL ${imageName}:${imageTag}
                echo "\$DOCKER_PASS" | docker login -u "\$DOCKER_USER" --password-stdin
                docker push ${imageName}:${imageTag}
                docker image rm ${imageName}:${imageTag}
            """
        }
    }
}
