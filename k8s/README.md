# Kubernetes Deployment for Movie Trailers Application

This directory contains Kubernetes manifests for deploying the Movie Trailers application and its PostgreSQL database.

## Prerequisites

- Kubernetes cluster (minikube, kind, or any cloud provider)
- kubectl configured to connect to your cluster
- Docker (for building the application image)

## Building the Application Image

Before deploying to Kubernetes, build the Docker image for the application:

```bash
# From the root directory of the project
docker build -t movie-trailers:latest .
```

If using minikube, you need to make the image available to minikube:

```bash
# For minikube
eval $(minikube docker-env)
docker build -t movie-trailers:latest .
```

## Deployment Steps

### Option 1: Using kubectl directly

1. Create the namespace:

```bash
kubectl apply -f namespace.yaml
```

2. Create the PostgreSQL resources:

```bash
kubectl apply -f postgres-secret.yaml
kubectl apply -f postgres-pvc.yaml
kubectl apply -f postgres-deployment.yaml
kubectl apply -f postgres-service.yaml
```

3. Create the application resources:

```bash
kubectl apply -f app-config.yaml
kubectl apply -f app-deployment.yaml
kubectl apply -f app-service.yaml
kubectl apply -f app-ingress.yaml
```

### Option 2: Using kustomize

Deploy all resources at once using kustomize:

```bash
kubectl apply -k .
```

## Verify the deployment:

```bash
kubectl get pods -n movie-trailers
kubectl get services -n movie-trailers
kubectl get ingress -n movie-trailers
```

## Accessing the Application

If you're using minikube, you can access the application using:

```bash
minikube service movie-trailers-app -n movie-trailers --url
```

If you're using the Ingress, add the following entry to your `/etc/hosts` file:

```
127.0.0.1 movie-trailers.local
```

Then access the application at: http://movie-trailers.local

## Cleanup

To remove all resources:

```bash
# Using kubectl directly
kubectl delete -f app-ingress.yaml
kubectl delete -f app-service.yaml
kubectl delete -f app-deployment.yaml
kubectl delete -f app-config.yaml
kubectl delete -f postgres-service.yaml
kubectl delete -f postgres-deployment.yaml
kubectl delete -f postgres-pvc.yaml
kubectl delete -f postgres-secret.yaml
kubectl delete -f namespace.yaml

# Or using kustomize
kubectl delete -k .
```