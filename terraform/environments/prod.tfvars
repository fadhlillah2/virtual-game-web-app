# Virtual Game Web App - Production Environment Configuration

# General
environment = "prod"
aws_region  = "us-west-2"

# VPC and Networking
vpc_cidr           = "10.2.0.0/16"
availability_zones = ["us-west-2a", "us-west-2b", "us-west-2c"]
public_subnets     = ["10.2.1.0/24", "10.2.2.0/24", "10.2.3.0/24"]
private_subnets    = ["10.2.4.0/24", "10.2.5.0/24", "10.2.6.0/24"]

# Database
db_name             = "virtual_casino"
db_username         = "dbadmin"  # Use AWS Secrets Manager in production
db_password         = "Password123!"  # Use AWS Secrets Manager in production
db_instance_class   = "db.m5.large"
db_allocated_storage = 100
db_multi_az         = true

# Redis
redis_node_type          = "cache.m5.large"
redis_num_cache_clusters = 3

# Kafka
kafka_instance_type   = "kafka.m5.large"
kafka_number_of_nodes = 3

# Application
app_image  = "virtual-game-web-app:latest"
app_count  = 4
app_port   = 8080
app_cpu    = 1024
app_memory = 2048
