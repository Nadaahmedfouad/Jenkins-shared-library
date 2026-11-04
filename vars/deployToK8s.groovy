
def call(String deploymentFile, String imageName, String imageTag) {

    stage('Deploy to Kubernetes') {

        withCredentials([
            file(
                credentialsId: 'ivolve-kubeconfig',
                variable: 'KUBECONFIG'
            )
        ]) {

            sh """
                echo "Testing Kubernetes connection..."
                kubectl config current-context
                echo "Testing Kubernetes connection..."
                
                kubectl config current-context
                
                echo "Checking Kubernetes authentication..."
                kubectl auth whoami
                kubectl get namespace ivolve
                
                echo "Updating deployment image..."
                sed -i \
                  's|image: ${imageName}:.*|image: ${imageName}:${imageTag}|' \
                  '${deploymentFile}'

                echo "Updating deployment image..."
                sed -i \
                  's|image: ${imageName}:.*|image: ${imageName}:${imageTag}|' \
                  '${deploymentFile}'

                echo "Deploying application..."
                kubectl apply -f '${deploymentFile}' -n ivolve

                echo "Checking deployment rollout..."
                kubectl rollout status deployment/demoapp \
                  -n ivolve --timeout=180s

                echo "Deployment successful!"
                kubectl get deployment demoapp -n ivolve
                kubectl get pods -n ivolve -l app=demoapp
                kubectl get svc demoapp-service -n ivolve
            """
        }
    }
}
