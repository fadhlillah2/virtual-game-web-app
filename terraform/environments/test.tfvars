# Virtual Game Web App - Test Environment Configuration

# General
environment = "test"
aws_region  = "us-west-2"

# VPC and Networking
vpc_cidr           = "10.1.0.0/16"
availability_zones = ["us-west-2a", "us-west-2b", "us-west-2c"]
public_subnets     = ["10.1.1.0/24", "10.1.2.0/24", "10.1.3.0/24"]
private_subnets    = ["10.1.4.0/24", "10.1.5.0/24", "10.1.6.0/24"]

# Database
db_name             = "virtual_casino"
db_username         = "dbadmin"  # Use AWS Secrets Manager in production
db_password         = "Password123!"  # Use AWS Secrets Manager in production
db_instance_class   = "db.t3.medium"
db_allocated_storage = 50
db_multi_az         = true

# Redis
redis_node_type          = "cache.t3.medium"
redis_num_cache_clusters = 2

# Kafka
kafka_instance_type   = "kafka.t3.small"
kafka_number_of_nodes = 3

# Application
app_image  = "virtual-game-web-app:latest"
app_count  = 2
app_port   = 8080
app_cpu    = 512
app_memory = 1024
