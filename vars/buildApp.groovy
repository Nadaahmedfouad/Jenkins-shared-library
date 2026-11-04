
def call() {
    stage('Build Application') {
        sh '''
            echo "Building DemoApp with Maven..."
            mvn clean package

            echo "Checking generated JAR..."
            ls -lh target/*.jar
        '''
    }
}
