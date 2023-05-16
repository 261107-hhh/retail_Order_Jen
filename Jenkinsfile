pipeline {
    agent any
	tools {
        // Install the Maven version configured as "M3" and add it to the path.
         maven 'mvn'
    }
    stages {
        stage('Build') {
            steps {
              bat 'mvn clean compile'
              echo 'mvn clean and compiled'
              
              
            }
        }
         stage('Test') {
            steps {
                //bat './mvnw test'
                //bat 'mvn test'
                //bat 'mvn test'
                //echo 'mvn tested'
                bat 'mvn test'
                echo 'packed and tested'  
            }
        }
         stage('Deploy') {
            steps {
	            bat 'java -jar target/OrderMicroservice-0.0.1-SNAPSHOT.jar'
                echo 'mvn deployed'
            }
        }
       
       
    }
}
