
def call(String deploymentFile, String imageName, String imageTag) {
    stage('Deploy to Kubernetes') {
        sh """
            sed -i -E 's|image: ${imageName}:[^[:space:]]+|image: ${imageName}:${imageTag}|' '${deploymentFile}'
            kubectl apply -f '${deploymentFile}'
        """
    }
}
