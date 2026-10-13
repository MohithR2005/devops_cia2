# DevOps CIA — Course Registration System

## Project Overview

This project demonstrates DevOps practices using a Java-based Course Registration System. The lab covers Git version control, Maven multi-module builds, unit testing, Jenkins Continuous Integration (CI), Docker, and Kubernetes deployment using Minikube.

The application validates course registration rules, including course capacity, course activity, prerequisites, duplicate registrations, student credit limits, and valid course-credit ranges.

## Technology Stack

- **Programming Language:** Java
- **Build Automation:** Apache Maven
- **Unit Testing:** JUnit 5
- **Version Control:** Git and GitHub
- **Continuous Integration:** Jenkins Declarative Pipeline
- **Container Runtime:** Docker Desktop
- **Container Orchestration:** Kubernetes
- **Local Kubernetes Cluster:** Minikube
- **Web Server:** Nginx 1.27

## Project Structure

```text
Student-Starter/
├── pom.xml
├── Jenkinsfile
├── README.md
├── deployment.yaml
├── service.yaml
└── course-registration-app/
    ├── pom.xml
    ├── src/
    │   ├── main/java/com/university/
    │   │   ├── Course.java
    │   │   ├── RegistrationService.java
    │   │   └── Student.java
    │   └── test/java/com/university/
    │       ├── CourseTest.java
    │       └── RegistrationServiceTest.java
    └── target/
        ├── course-registration-app-1.0-SNAPSHOT.jar
        └── surefire-reports/
```

The root `pom.xml` defines the parent Maven project and includes `course-registration-app` as a child module. The child module contains the Java application source code and unit tests.

## Task 1 — Git Version Control

A feature branch named `feature/course-registration-ci` was created from the provided baseline commit.

The project was developed using separate commits for major changes, including:

- Refactoring the project into a multi-module Maven build.
- Fixing the course registration capacity boundary.
- Validating course-credit limits.
- Adding the Jenkins Declarative Pipeline.

Git commands used:

```bash
git branch
git switch feature/course-registration-ci
git status
git log --oneline
git add .
git commit -m "Describe the change"
git push -u origin feature/course-registration-ci
```

The feature branch and its commits were pushed to the GitHub repository:

https://github.com/MohithR2005/devops_cia2

## Task 2 — Multi-Module Maven Build

The original project structure was refactored into a parent Maven project and a child application module.

The parent POM manages the module structure and shared build configuration. The child POM defines the application module and its dependencies.

Build and test commands:

```bash
mvn validate
mvn clean test
mvn package
```

### Test Results

The final recorded Maven test run completed successfully.

| Test Class | Tests | Failures | Errors | Skipped |
|---|---:|---:|---:|---:|
| CourseTest | 3 | 0 | 0 | 0 |
| RegistrationServiceTest | 13 | 0 | 0 | 0 |
| **Total** | **16** | **0** | **0** | **0** |

Test reports are generated in:

`course-registration-app/target/surefire-reports/`

The packaged JAR is generated at:

`course-registration-app/target/course-registration-app-1.0-SNAPSHOT.jar`

## Task 3 — Course Capacity Boundary Fix

The registration service was updated to reject a registration when the course has reached its maximum capacity.

The capacity validation uses the condition:

```java
if (course.getCurrentEnrollment() >= course.getCapacity()) {
    return false;
}
```

This ensures that a course at full capacity does not accept another registration.

Tests were added to verify that:
- Registration is rejected when the course is full.
- Registration is accepted when exactly one seat remains.
- A rejected registration does not modify student registration details, total credits, or course enrollment.

## Task 4 — Course Credit Validation

The registration service was updated to accept only courses with credit values from 1 through 6, inclusive.

```java
if (course.getCredits() < 1 || course.getCredits() > 6) {
    return false;
}
```

Tests cover the valid boundary values of 1 and 6, as well as invalid values of 0, 7, and -1.

The registration workflow rejects invalid credit values before updating student registrations or course enrollment.

## Task 5 — Jenkins Continuous Integration Pipeline

A Jenkins Declarative Pipeline was configured in the root `Jenkinsfile`.

The pipeline contains the following stages:

1. **Checkout:** Retrieves the project source code from GitHub.
2. **Build and Test:** Runs `mvn clean test`.
3. **Package:** Runs `mvn package`.

The pipeline publishes JUnit test reports using:

`course-registration-app/target/surefire-reports/TEST-*.xml`

On successful builds, Jenkins archives the generated JAR using:

`course-registration-app/target/*.jar`

### Jenkins Verification

The project was successfully checked out from GitHub, and the Jenkins build completed with:

`Finished: SUCCESS`

The console output confirmed `BUILD SUCCESS`, test-result recording, and artifact archiving with fingerprinting.

The pipeline uses Windows `bat` commands and a configured Maven installation. It must run on a compatible Windows Jenkins agent.

## Task 6 — Kubernetes Deployment Using Minikube

Docker Desktop, Minikube, and kubectl were configured on Windows. A local Kubernetes cluster was started using the Docker driver.

```bash
minikube start --driver=docker
kubectl get nodes
```

The Minikube node reached the `Ready` state.

### Nginx Deployment

The `deployment.yaml` manifest defines an Nginx 1.27 Deployment with an initial desired replica count of two.

The deployment was applied using:

```bash
kubectl apply -f deployment.yaml
kubectl rollout status deployment/nginx-deployment
kubectl get deployments
kubectl get pods
```

### NodePort Service

The `service.yaml` manifest defines a NodePort Service named `nginx-service`.

- Service type: NodePort
- Service port: 80
- Target container port: 80
- NodePort: 30080

The service was created using:

```bash
kubectl apply -f service.yaml
kubectl get services
```

The Nginx application was accessed using:

```bash
minikube service nginx-service --url
```

The browser displayed the Nginx welcome page.

### Scaling the Deployment

The deployment was scaled from two replicas to three:

```bash
kubectl scale deployment nginx-deployment --replicas=3
kubectl rollout status deployment/nginx-deployment
kubectl get deployments
kubectl get pods
```

The verification output showed three desired, up-to-date, and available replicas.

### Self-Healing Verification

To demonstrate Kubernetes self-healing, one running Nginx pod was deleted:

```bash
kubectl get pods
kubectl delete pod <actual-pod-name>
kubectl get pods -w
```

After deletion, Kubernetes created a replacement pod. The deployment returned to three running replicas, demonstrating that the Deployment controller maintains the desired replica count.

## Evidence and Verification

The lab progress document contains screenshots documenting:

- Git branch creation and commit history.
- Maven multi-module build and test results.
- Course registration validation changes.
- Jenkins pipeline execution and console output.
- Minikube cluster startup and node readiness.
- Nginx deployment and NodePort service.
- Nginx welcome page in the browser.
- Scaling to three replicas.
- Pod deletion and replacement during self-healing.

## Conclusion

This lab demonstrates the use of Git for version control, Maven for multi-module Java builds, JUnit 5 for automated testing, Jenkins for continuous integration, and Kubernetes with Minikube for deployment, service exposure, scaling, and self-healing.

The recorded Maven tests and Jenkins pipeline completed successfully, and the Nginx Kubernetes deployment was verified with three ready replicas after scaling and pod replacement.