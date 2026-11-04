
def call() {
    stage('Build Application') {
        sh 'mvn test'
        sh 'mvn clean package -DskipTests'
    }
}
