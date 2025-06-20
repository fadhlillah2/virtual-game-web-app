# Virtual Game Web App - Development Environment Configuration

# General
environment = "dev"
aws_region  = "us-west-2"

# VPC and Networking
vpc_cidr           = "10.0.0.0/16"
availability_zones = ["us-west-2a", "us-west-2b"]
public_subnets     = ["10.0.1.0/24", "10.0.2.0/24"]
private_subnets    = ["10.0.4.0/24", "10.0.5.0/24"]

# Database
db_name             = "virtual_casino"
db_username         = "dbadmin"  # Use AWS Secrets Manager in production
db_password         = "Password123!"  # Use AWS Secrets Manager in production
db_instance_class   = "db.t3.small"
db_allocated_storage = 20
db_multi_az         = false

# Redis
redis_node_type          = "cache.t3.small"
redis_num_cache_clusters = 1

# Kafka
kafka_instance_type   = "kafka.t3.small"
kafka_number_of_nodes = 2

# Application
app_image  = "virtual-game-web-app:latest"
app_count  = 1
app_port   = 8080
app_cpu    = 256
app_memory = 512
