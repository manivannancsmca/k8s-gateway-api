## 🎯 Overview

This project demonstrates a production-ready microservices architecture using modern cloud-native technologies:

### Key Features

- ✅ **Spring Boot REST API** - Robust backend service with health checks
- ✅ **Kubernetes Gateway API** - Modern ingress routing with CRDs
- ✅ **Envoy Proxy** - High-performance edge and service proxy
- ✅ **Kind** - Kubernetes in Docker for local development
- ✅ **Horizontal Pod Autoscaling** - Automatic scaling based on load
- ✅ **Health Checks & Readiness Probes** - Production-ready deployment
- ✅ **Resource Optimization** - Lightweight Alpine-based containers


### API Endpoints

| Endpoint | Method | Description | Response |
|----------|--------|-------------|----------|
| `/api/v1/status` | GET | Health check | `{"status":"UP","timestamp":"...","service":"gateway-demo","version":"1.0.0"}` |
| `/health` | GET | Liveness probe | `{"status":"UP"}` |
| `/info` | GET | Application info | Application metadata |

---

## 🔧 Prerequisites

### Required Software

| Tool | Version | Purpose | Installation Link |
|------|---------|---------|-------------------|
| Docker Desktop | Latest | Container runtime | [Download](https://www.docker.com/products/docker-desktop/) |
| Kind | v0.20.0+ | Kubernetes in Docker | [Installation Guide](https://kind.sigs.k8s.io/docs/user/quick-start/) |
| kubectl | v1.28+ | Kubernetes CLI | [Installation Guide](https://kubernetes.io/docs/tasks/tools/) |
| Java | 17+ | Spring Boot runtime | [Download](https://adoptium.net/) |
| Maven | 3.8+ | Build tool | [Download](https://maven.apache.org/download.cgi) |
| WSL 2 | Latest | Windows Subsystem for Linux | [Installation Guide](https://learn.microsoft.com/en-us/windows/wsl/install) |
| curl | Latest | API testing | [Download](https://curl.se/download.html) |
| jq | Latest | JSON processing | [Download](https://stedolan.github.io/jq/download/) |

### System Requirements

| Component | Minimum | Recommended |
|-----------|---------|-------------|
| **RAM** | 8GB | 16GB |
| **Disk Space** | 20GB | 50GB |
| **CPU** | 2 cores | 4+ cores |
| **OS** | Windows 10/11 with WSL 2 | Windows 11 Pro |
| **Network** | Internet connection | High-speed internet |

### WSL 2 Setup (Windows)

```powershell
# Enable WSL 2
wsl --install

# Set WSL 2 as default
wsl --set-default-version 2

# Install Ubuntu distribution
wsl --install -d Ubuntu

# Verify installation
wsl -l -v


📦 Installation Guide
Step 1: Clone the Repository

# Clone the repository
git clone https://github.com/yourusername/gateway-demo.git
cd gateway-demo

# Verify the repository structure
ls -la

Step 2: Install Docker Desktop
Windows Installation
Download Docker Desktop from docker.com

Run the installer with administrative privileges

Select "Use WSL 2 instead of Hyper-V" during installation

Complete the installation and restart if prompted

Post-Installation Configuration


# Open Docker Desktop
# Go to Settings > Resources > WSL Integration
# Enable WSL integration for your Ubuntu distribution
# Click Apply & Restart

# Verify Docker installation
docker --version
docker ps


Step 3: Install Kubernetes CLI Tools
Using Windows Package Manager (winget)

# Install Kind
winget install Kubernetes.kind

# Install kubectl
winget install Kubernetes.kubectl

Using Chocolatey (Alternative)

# Install Chocolatey if not already installed
Set-ExecutionPolicy Bypass -Scope Process -Force
[System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072
iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

# Install tools
choco install kind kubernetes-cli -y

Step 4: Verify Installations

# Check all installations
docker --version          # Docker version
kind version             # Kind version
kubectl version --client # kubectl version
java -version            # Java version
mvn -version            # Maven version


📁 Project Structure

gateway-demo/
│
├── 📂 src/
│   └── 📂 main/
│       ├── 📂 java/
│       │   └── 📂 com/
│       │       └── 📂 example/
│       │           └── 📂 demo/
│       │               ├── 📄 DemoApplication.java
│       │               ├── 📂 controller/
│       │               │   └── 📄 StatusController.java
│       │               ├── 📂 model/
│       │               │   └── 📄 StatusResponse.java
│       │               └── 📂 config/
│       │                   └── 📄 AppConfig.java
│       └── 📂 resources/
│           ├── 📄 application.properties
│           ├── 📄 application-dev.properties
│           └── 📄 application-prod.properties
│
├── 📂 target/                  # Build output (generated)
│   └── 📄 gateway-demo-1.0.0.jar
│
├── 📂 k8s/
│   ├── 📄 app-infrastructure.yaml
│   ├── 📄 kind-config.yaml
│   └── 📄 hpa.yaml
│
├── 📂 scripts/
│   ├── 📄 setup.sh
│   ├── 📄 deploy.sh
│   └── 📄 test.sh
│
├── 📄 pom.xml                   # Maven configuration
├── 📄 Dockerfile                # Container build definition
├── 📄 .dockerignore             # Docker ignore file
├── 📄 .gitignore                # Git ignore file
├── 📄 LICENSE                   # MIT License
├── 📄 CHANGELOG.md              # Version history
└── 📄 README.md                 # This file


Build the Application

# Clean and package the application
mvn clean package -DskipTests

# Run tests
mvn test

# Build without tests
mvn clean package -DskipTests

# Run the application locally
mvn spring-boot:run

# Verify the JAR
ls -la target/gateway-demo-*.jar

Build the Docker Image

# Build the image with multiple tags
docker build -t gateway-demo:1.0.0 -t gateway-demo:latest .

# Verify the image
docker images | grep gateway-demo

# Run the container locally
docker run -d -p 8080:8080 --name gateway-demo-container gateway-demo:1.0.0

# Test the container
curl http://localhost:8080/api/v1/status

# Stop and remove the container
docker stop gateway-demo-container
docker rm gateway-demo-container

# Scan for vulnerabilities
docker scan gateway-demo:1.0.0


☸️ Kubernetes Cluster Setup
Kind Cluster Configuration


# k8s/kind-config.yaml

kind: Cluster
apiVersion: kind.x-k8s.io/v1alpha4
name: gateway-api-cluster

# Network configuration
networking:
  apiServerAddress: "127.0.0.1"
  apiServerPort: 6443
  podSubnet: "10.244.0.0/16"
  serviceSubnet: "10.96.0.0/12"

# Node configuration
nodes:
- role: control-plane
  extraPortMappings:
  - containerPort: 80
    hostPort: 80
    protocol: TCP
  - containerPort: 443
    hostPort: 443
    protocol: TCP
  - containerPort: 8080
    hostPort: 8080
    protocol: TCP
  extraMounts:
  - hostPath: /var/run/docker.sock
    containerPath: /var/run/docker.sock

- role: worker
  extraMounts:
  - hostPath: /var/run/docker.sock
    containerPath: /var/run/docker.sock

- role: worker
  extraMounts:
  - hostPath: /var/run/docker.sock
    containerPath: /var/run/docker.sock


Deploy the Cluster

# Create the Kind cluster
kind create cluster --config k8s/kind-config.yaml --name gateway-api-cluster

# Verify cluster is running
kubectl cluster-info --context kind-gateway-api-cluster

# Check cluster status
kubectl get nodes -o wide
kubectl get pods -A

# Get cluster info
kubectl version



Load Application Image

# Load the local Docker image into Kind
kind load docker-image gateway-demo:1.0.0 --name gateway-api-cluster

# Verify image is loaded on all nodes
kubectl get nodes -o jsonpath='{.items[*].status.images}' | jq .

# Check if image exists on nodes
kubectl get nodes -o yaml | grep -A5 gateway-demo


🌐 Gateway API Installation

Step 1: Install Gateway API CRDs

# Install standard Gateway API CRDs
kubectl apply -f https://github.com/kubernetes-sigs/gateway-api/releases/download/v1.0.0/standard-install.yaml

# Verify CRDs are installed
kubectl get crds | grep gateway.networking.k8s.io

# Check CRD details
kubectl describe crd gateways.gateway.networking.k8s.io
kubectl describe crd httproutes.gateway.networking.k8s.io


Step 2: Install Envoy Gateway Controller

# Install Envoy Gateway controller
kubectl apply --server-side -f https://github.com/envoyproxy/gateway/releases/download/v1.0.0/install.yaml

# Wait for pods to be ready
kubectl wait --for=condition=ready pod -l app.kubernetes.io/name=envoy-gateway -n envoy-gateway-system --timeout=300s

# Check controller status
kubectl get pods -n envoy-gateway-system

# View logs
kubectl logs -n envoy-gateway-system deployment/envoy-gateway

# Check services
kubectl get svc -n envoy-gateway-system


Expected Output:
NAME                                READY   STATUS    RESTARTS   AGE
envoy-gateway-xxxxx-yyyyy           1/1     Running   0          2m
envoy-gateway-xxxxx-zzzzz           1/1     Running   0          2m



🔀 Traffic Routing Configuration
Complete Application Infrastructure


# k8s/app-infrastructure.yaml

---
# Deployment
apiVersion: apps/v1
kind: Deployment
metadata:
  name: demo-app
  labels:
    app: demo-app
    version: v1
spec:
  replicas: 2
  selector:
    matchLabels:
      app: demo-app
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 0
  template:
    metadata:
      labels:
        app: demo-app
        version: v1
    spec:
      containers:
      - name: app
        image: gateway-demo:1.0.0
        imagePullPolicy: IfNotPresent
        ports:
        - containerPort: 8080
          name: http
          protocol: TCP
        env:
        - name: JAVA_OPTS
          value: "-Xmx256m -Xms128m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        resources:
          requests:
            memory: "256Mi"
            cpu: "250m"
          limits:
            memory: "512Mi"
            cpu: "500m"
        livenessProbe:
          httpGet:
            path: /api/v1/status
            port: 8080
            scheme: HTTP
          initialDelaySeconds: 30
          periodSeconds: 10
          timeoutSeconds: 5
          failureThreshold: 3
        readinessProbe:
          httpGet:
            path: /api/v1/status
            port: 8080
            scheme: HTTP
          initialDelaySeconds: 20
          periodSeconds: 5
          timeoutSeconds: 3
          failureThreshold: 3
        startupProbe:
          httpGet:
            path: /api/v1/status
            port: 8080
          initialDelaySeconds: 10
          periodSeconds: 5
          failureThreshold: 30
---
# Service
apiVersion: v1
kind: Service
metadata:
  name: demo-app-service
  labels:
    app: demo-app
spec:
  selector:
    app: demo-app
  ports:
  - name: http
    port: 8080
    targetPort: 8080
    protocol: TCP
  type: ClusterIP
  sessionAffinity: None
---
# GatewayClass
apiVersion: gateway.networking.k8s.io/v1
kind: GatewayClass
metadata:
  name: my-gateway-class
  annotations:
    gateway.envoyproxy.io/disable-use-remote-address: "true"
    gateway.envoyproxy.io/enable-ipv6: "false"
spec:
  controllerName: gateway.envoyproxy.io/gatewayclass-controller
  parametersRef:
    group: gateway.envoyproxy.io
    kind: GatewayParameters
    name: my-gateway-params
    namespace: default
---
# Gateway
apiVersion: gateway.networking.k8s.io/v1
kind: Gateway
metadata:
  name: my-gateway
  namespace: default
  annotations:
    gateway.envoyproxy.io/enable-rate-limiting: "true"
spec:
  gatewayClassName: my-gateway-class
  listeners:
  - name: http
    protocol: HTTP
    port: 80
    allowedRoutes:
      namespaces:
        from: Same
    hostname: "*"
---
# HTTPRoute
apiVersion: gateway.networking.k8s.io/v1
kind: HTTPRoute
metadata:
  name: demo-route
  namespace: default
  annotations:
    gateway.envoyproxy.io/retry-on: "5xx,reset,connect-failure,refused-stream"
    gateway.envoyproxy.io/retry-status-codes: "503,502"
spec:
  parentRefs:
  - name: my-gateway
    sectionName: http
  rules:
  - matches:
    - path:
        type: PathPrefix
        value: /api/
    backendRefs:
    - name: demo-app-service
      port: 8080
      weight: 1
    filters:
    - type: ResponseHeaderModifier
      responseHeaderModifier:
        set:
        - name: X-Custom-Header
          value: "Gateway-Demo"
  - matches:
    - path:
        type: PathPrefix
        value: /
    backendRefs:
    - name: demo-app-service
      port: 8080
      weight: 1
---
# Gateway Parameters
apiVersion: gateway.envoyproxy.io/v1alpha1
kind: GatewayParameters
metadata:
  name: my-gateway-params
  namespace: default
spec:
  logging:
    level:
      default: info
  connection:
    bufferLimit: 32768
    enableClientTrafficPolicy: true
  telemetry:
    accessLog:
      text:
      - format: |
          [%START_TIME%] "%REQ(:METHOD)% %REQ(X-ENVOY-ORIGINAL-PATH?:PATH)% %PROTOCOL%"
          %RESPONSE_CODE% %RESPONSE_FLAGS% %BYTES_RECEIVED% %BYTES_SENT% %DURATION%
          "%REQ(X-FORWARDED-FOR)%" "%REQ(USER-AGENT)%" "%REQ(X-REQUEST-ID)%"
          "%REQ(AUTHORITY)%" "%UPSTREAM_HOST%"
          %DOWNSTREAM_LOCAL_ADDRESS% %DOWNSTREAM_REMOTE_ADDRESS%
          %UPSTREAM_CLUSTER%



Horizontal Pod Autoscaling (Optional)

# k8s/hpa.yaml

apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: demo-app-hpa
  namespace: default
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: demo-app
  minReplicas: 2
  maxReplicas: 10
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70
  - type: Resource
    resource:
      name: memory
      target:
        type: Utilization
        averageUtilization: 80
  behavior:
    scaleDown:
      stabilizationWindowSeconds: 300
      policies:
      - type: Percent
        value: 100
        periodSeconds: 15
    scaleUp:
      stabilizationWindowSeconds: 0
      policies:
      - type: Percent
        value: 100
        periodSeconds: 15


Apply the Configuration

# Apply all resources
kubectl apply -f k8s/app-infrastructure.yaml

# Apply HPA (optional)
kubectl apply -f k8s/hpa.yaml

# Verify deployments
kubectl get deployments
kubectl get pods -o wide
kubectl get services
kubectl get gatewayclass
kubectl get gateway
kubectl get httproute
kubectl get hpa

# Check resource status
kubectl describe gateway my-gateway
kubectl describe httproute demo-route


🧪 Testing & Verification
Step 1: Check Resource Status

# Comprehensive resource check
kubectl get all

# Gateway status
kubectl get gateway my-gateway -o yaml

# HTTPRoute status
kubectl get httproute demo-route -o yaml

# Check pod logs
kubectl logs deployment/demo-app

# Check events
kubectl get events --sort-by='.lastTimestamp'

# Port forward to check pod directly
kubectl port-forward deployment/demo-app 8080:8080

# Test pod directly (in another terminal)
curl http://localhost:8080/api/v1/status



Step 2: Port Forward to Envoy Proxy

# Find the Envoy proxy service
kubectl get svc -n envoy-gateway-system

# Example output:
# NAME                                   TYPE           CLUSTER-IP     EXTERNAL-IP   PORT(S)          AGE
# envoy-default-my-gateway-xxxxxxx       LoadBalancer   10.96.xxx.xxx   <pending>     80:32000/TCP     2m

# Port forward to Envoy (replace with your service name)
kubectl port-forward service/envoy-default-my-gateway-xxxxxxx -n envoy-gateway-system 80:80

# Alternative: Port forward to specific port
kubectl port-forward service/envoy-default-my-gateway-xxxxxxx -n envoy-gateway-system 8080:80



Step 3: Test the Endpoint

# Using curl
curl -v http://localhost/api/v1/status

# Using curl with headers
curl -H "User-Agent: TestAgent" http://localhost/api/v1/status

# Using PowerShell
Invoke-RestMethod -Uri http://localhost/api/v1/status -Method GET | ConvertTo-Json

# Using PowerShell with verbose output
Invoke-WebRequest -Uri http://localhost/api/v1/status -Verbose

# Open in browser
start http://localhost/api/v1/status


Expected Response:
{
  "status": "UP",
  "timestamp": "2026-06-17T14:30:00.123Z",
  "service": "gateway-demo",
  "version": "1.0.0",
  "uptime": 123456789
}


Step 4: Load Testing

# Install hey (load testing tool)
# Windows: scoop install hey
# Or download from: https://github.com/rakyll/hey/releases

# Basic load test with 1000 requests
hey -n 1000 -c 10 http://localhost/api/v1/status

# Advanced load test with different endpoints
hey -n 5000 -c 50 -m GET -H "User-Agent: LoadTest" http://localhost/api/v1/status

# Test with custom headers
hey -n 1000 -c 20 -H "X-Custom-Header: test" http://localhost/api/v1/status

# Check response times
hey -n 1000 -c 10 -o csv http://localhost/api/v1/status > results.csv


