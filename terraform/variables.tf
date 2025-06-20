# Virtual Game Web App - Infrastructure as Code
# Variables definition file

# General
variable "aws_region" {
  description = "The AWS region to deploy resources"
  type        = string
  default     = "us-west-2"
}

variable "environment" {
  description = "Environment name (dev, test, prod)"
  type        = string
}

# VPC and Networking
variable "vpc_cidr" {
  description = "CIDR block for the VPC"
  type        = string
  default     = "10.0.0.0/16"
}

variable "availability_zones" {
  description = "List of availability zones to use"
  type        = list(string)
  default     = ["us-west-2a", "us-west-2b", "us-west-2c"]
}

variable "public_subnets" {
  description = "CIDR blocks for public subnets"
  type        = list(string)
  default     = ["10.0.1.0/24", "10.0.2.0/24", "10.0.3.0/24"]
}

variable "private_subnets" {
  description = "CIDR blocks for private subnets"
  type        = list(string)
  default     = ["10.0.4.0/24", "10.0.5.0/24", "10.0.6.0/24"]
}

# Database
variable "db_name" {
  description = "Name of the PostgreSQL database"
  type        = string
  default     = "virtual_casino"
}

variable "db_username" {
  description = "Username for the PostgreSQL database"
  type        = string
  sensitive   = true
}

variable "db_password" {
  description = "Password for the PostgreSQL database"
  type        = string
  sensitive   = true
}

variable "db_instance_class" {
  description = "Instance class for the RDS instance"
  type        = string
  default     = "db.t3.medium"
}

variable "db_allocated_storage" {
  description = "Allocated storage for the RDS instance (in GB)"
  type        = number
  default     = 20
}

variable "db_multi_az" {
  description = "Whether to enable Multi-AZ for the RDS instance"
  type        = bool
  default     = false
}

# Redis
variable "redis_node_type" {
  description = "Node type for the ElastiCache Redis cluster"
  type        = string
  default     = "cache.t3.small"
}

variable "redis_num_cache_clusters" {
  description = "Number of cache clusters for the ElastiCache Redis replication group"
  type        = number
  default     = 2
}

# Kafka
variable "kafka_instance_type" {
  description = "Instance type for the MSK Kafka brokers"
  type        = string
  default     = "kafka.t3.small"
}

variable "kafka_number_of_nodes" {
  description = "Number of broker nodes in the MSK Kafka cluster"
  type        = number
  default     = 3
}

# Application
variable "app_image" {
  description = "Docker image for the application"
  type        = string
}

variable "app_count" {
  description = "Number of application instances to run"
  type        = number
  default     = 2
}

variable "app_port" {
  description = "Port on which the application listens"
  type        = number
  default     = 8080
}

variable "app_cpu" {
  description = "CPU units for the application task"
  type        = number
  default     = 256
}

variable "app_memory" {
  description = "Memory for the application task (in MiB)"
  type        = number
  default     = 512
}
