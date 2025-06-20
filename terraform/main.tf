# Virtual Game Web App - Infrastructure as Code
# Main Terraform configuration file

terraform {
  required_version = ">= 1.0.0"
  
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.0"
    }
  }
  
  # Uncomment to use remote state with S3 backend
  # backend "s3" {
  #   bucket         = "virtual-game-web-app-terraform-state"
  #   key            = "terraform.tfstate"
  #   region         = "us-west-2"
  #   dynamodb_table = "terraform-state-lock"
  #   encrypt        = true
  # }
}

provider "aws" {
  region = var.aws_region
  
  default_tags {
    tags = {
      Environment = var.environment
      Project     = "virtual-game-web-app"
      ManagedBy   = "terraform"
    }
  }
}

# VPC and Networking
module "vpc" {
  source = "./modules/vpc"
  
  environment        = var.environment
  vpc_cidr           = var.vpc_cidr
  availability_zones = var.availability_zones
  public_subnets     = var.public_subnets
  private_subnets    = var.private_subnets
}

# Security Groups
module "security_groups" {
  source = "./modules/security_groups"
  
  vpc_id      = module.vpc.vpc_id
  environment = var.environment
}

# RDS PostgreSQL Database
module "database" {
  source = "./modules/database"
  
  environment                = var.environment
  vpc_id                     = module.vpc.vpc_id
  subnet_ids                 = module.vpc.private_subnet_ids
  postgres_security_group_id = module.security_groups.postgres_security_group_id
  db_name                    = var.db_name
  db_username                = var.db_username
  db_password                = var.db_password
  db_instance_class          = var.db_instance_class
  db_allocated_storage       = var.db_allocated_storage
  db_multi_az                = var.db_multi_az
}

# ElastiCache Redis
module "redis" {
  source = "./modules/redis"
  
  environment              = var.environment
  vpc_id                   = module.vpc.vpc_id
  subnet_ids               = module.vpc.private_subnet_ids
  redis_security_group_id  = module.security_groups.redis_security_group_id
  redis_node_type          = var.redis_node_type
  redis_num_cache_clusters = var.redis_num_cache_clusters
}

# MSK Kafka Cluster
module "kafka" {
  source = "./modules/kafka"
  
  environment             = var.environment
  vpc_id                  = module.vpc.vpc_id
  subnet_ids              = module.vpc.private_subnet_ids
  kafka_security_group_id = module.security_groups.kafka_security_group_id
  kafka_instance_type     = var.kafka_instance_type
  kafka_number_of_nodes   = var.kafka_number_of_nodes
}

# ECS Cluster for Application
module "ecs" {
  source = "./modules/ecs"
  
  environment           = var.environment
  vpc_id                = module.vpc.vpc_id
  public_subnet_ids     = module.vpc.public_subnet_ids
  app_security_group_id = module.security_groups.app_security_group_id
  app_image             = var.app_image
  app_count             = var.app_count
  app_port              = var.app_port
  app_cpu               = var.app_cpu
  app_memory            = var.app_memory
  
  # Environment variables for the application
  app_environment = [
    { name = "SPRING_PROFILES_ACTIVE", value = var.environment },
    { name = "SPRING_DATASOURCE_URL", value = "jdbc:postgresql://${module.database.db_endpoint}/${var.db_name}" },
    { name = "SPRING_DATASOURCE_USERNAME", value = var.db_username },
    { name = "SPRING_DATASOURCE_PASSWORD", value = var.db_password },
    { name = "SPRING_DATA_REDIS_HOST", value = module.redis.redis_endpoint },
    { name = "SPRING_DATA_REDIS_PORT", value = "6379" },
    { name = "SPRING_KAFKA_BOOTSTRAP_SERVERS", value = module.kafka.kafka_bootstrap_brokers }
  ]
}

# CloudWatch Alarms and Monitoring
module "monitoring" {
  source = "./modules/monitoring"
  
  environment     = var.environment
  ecs_cluster_arn = module.ecs.ecs_cluster_arn
  ecs_service_arn = module.ecs.ecs_service_arn
  db_instance_id  = module.database.db_instance_id
  redis_cluster_id = module.redis.redis_cluster_id
}
