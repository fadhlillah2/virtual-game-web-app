# Virtual Game Web App - Infrastructure as Code
# Outputs definition file

# VPC Outputs
output "vpc_id" {
  description = "The ID of the VPC"
  value       = module.vpc.vpc_id
}

output "public_subnet_ids" {
  description = "List of public subnet IDs"
  value       = module.vpc.public_subnet_ids
}

output "private_subnet_ids" {
  description = "List of private subnet IDs"
  value       = module.vpc.private_subnet_ids
}

# Database Outputs
output "db_endpoint" {
  description = "The connection endpoint for the RDS instance"
  value       = module.database.db_endpoint
}

output "db_instance_id" {
  description = "The ID of the RDS instance"
  value       = module.database.db_instance_id
}

output "db_connection_string" {
  description = "PostgreSQL connection string"
  value       = "jdbc:postgresql://${module.database.db_endpoint}/${var.db_name}"
  sensitive   = true
}

# Redis Outputs
output "redis_endpoint" {
  description = "The endpoint of the Redis cluster"
  value       = module.redis.redis_endpoint
}

output "redis_cluster_id" {
  description = "The ID of the Redis cluster"
  value       = module.redis.redis_cluster_id
}

# Kafka Outputs
output "kafka_bootstrap_brokers" {
  description = "The connection string to the Kafka brokers"
  value       = module.kafka.kafka_bootstrap_brokers
}

output "kafka_zookeeper_connect_string" {
  description = "The connection string to the Kafka ZooKeeper"
  value       = module.kafka.kafka_zookeeper_connect_string
}

# ECS Outputs
output "ecs_cluster_name" {
  description = "The name of the ECS cluster"
  value       = module.ecs.ecs_cluster_name
}

output "ecs_cluster_arn" {
  description = "The ARN of the ECS cluster"
  value       = module.ecs.ecs_cluster_arn
}

output "ecs_service_name" {
  description = "The name of the ECS service"
  value       = module.ecs.ecs_service_name
}

output "ecs_service_arn" {
  description = "The ARN of the ECS service"
  value       = module.ecs.ecs_service_arn
}

# Load Balancer Outputs
output "alb_dns_name" {
  description = "The DNS name of the load balancer"
  value       = module.ecs.alb_dns_name
}

output "alb_zone_id" {
  description = "The zone ID of the load balancer"
  value       = module.ecs.alb_zone_id
}

# Application URL
output "application_url" {
  description = "The URL to access the application"
  value       = "http://${module.ecs.alb_dns_name}"
}

# CloudWatch Dashboard
output "cloudwatch_dashboard_url" {
  description = "URL to the CloudWatch dashboard for the application"
  value       = module.monitoring.dashboard_url
}
