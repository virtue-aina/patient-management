# Patient Management System

This project consists of two microservices:
- Patient Service: Manages patient information
- Billing Service: Handles billing operations

## Running the Services with Docker Compose

To run both services using Docker Compose:

```bash
# Navigate to the project root directory
cd patient-management

# Build and start the containers
docker-compose up -d

# To see the running containers
docker ps
```

This will start:
- Patient Service: 
  - Container name: patient-service
  - HTTP port: 4000

- Billing Service:
  - Container name: billing-service
  - HTTP port: 8001
  - gRPC port: 9001

## Accessing the Services

- Patient Service API: http://localhost:4000
- Billing Service API: http://localhost:8001
- Billing Service gRPC: localhost:9001

## Stopping the Services

```bash
# Stop the containers
docker-compose down
```

## Development Resources

These documents should be consulted when working on the codebase to understand the current state and future direction of the project.
