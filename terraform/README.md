# Virtual Game Web App - Infrastructure as Code

This directory contains Terraform configurations for deploying the Virtual Game Web App infrastructure to AWS. The infrastructure is defined as code, making it reproducible, version-controlled, and consistent across environments.

## Prerequisites

- [Terraform](https://www.terraform.io/downloads.html) (v1.0.0 or newer)
- AWS CLI configured with appropriate credentials
- Access to the AWS account where resources will be deployed

## Directory Structure

```
terraform/
├── main.tf              # Main Terraform configuration
├── variables.tf         # Variable definitions
├── outputs.tf           # Output definitions
├── environments/        # Environment-specific configurations
│   ├── dev.tfvars       # Development environment variables
│   ├── test.tfvars      # Test environment variables
│   └── prod.tfvars      # Production environment variables
└── modules/             # Terraform modules (created automatically)
    ├── vpc/             # VPC and networking module
    ├── security_groups/ # Security groups module
    ├── database/        # RDS PostgreSQL module
    ├── redis/           # ElastiCache Redis module
    ├── kafka/           # MSK Kafka module
    ├── ecs/             # ECS application module
    └── monitoring/      # CloudWatch monitoring module
```

## Usage

### Initialize Terraform

Before applying the configuration, initialize Terraform to download providers and set up the backend:

```bash
cd terraform
terraform init
```

If using remote state with S3 (recommended for production), uncomment the backend configuration in `main.tf` and run:

```bash
terraform init \
  -backend-config="bucket=your-terraform-state-bucket" \
  -backend-config="key=virtual-game-web-app/terraform.tfstate" \
  -backend-config="region=us-west-2" \
  -backend-config="dynamodb_table=terraform-state-lock"
```

### Plan and Apply

To deploy to a specific environment, use the corresponding `.tfvars` file:

#### Development Environment

```bash
# Preview changes
terraform plan -var-file=environments/dev.tfvars

# Apply changes
terraform apply -var-file=environments/dev.tfvars
```

#### Test Environment

```bash
terraform plan -var-file=environments/test.tfvars
terraform apply -var-file=environments/test.tfvars
```

#### Production Environment

```bash
terraform plan -var-file=environments/prod.tfvars
terraform apply -var-file=environments/prod.tfvars
```

### Destroy Infrastructure

To tear down the infrastructure (use with caution, especially in production):

```bash
terraform destroy -var-file=environments/dev.tfvars
```

## Sensitive Information

The configuration includes sensitive information like database credentials. In a real-world scenario:

1. **Never commit actual secrets to version control**
2. Use AWS Secrets Manager or Parameter Store for sensitive values
3. Pass sensitive values via environment variables or a secure vault

Example using environment variables:

```bash
export TF_VAR_db_username="your_username"
export TF_VAR_db_password="your_password"
terraform apply -var-file=environments/prod.tfvars
```

## CI/CD Integration

This Terraform configuration can be integrated with the CI/CD pipeline defined in `.github/workflows/ci-cd.yml`. The workflow can automatically apply infrastructure changes after successful builds.

To enable this:

1. Store Terraform state in a remote backend (S3)
2. Add appropriate AWS credentials as GitHub secrets
3. Uncomment the Terraform steps in the CI/CD workflow

## Best Practices

1. **State Management**: Use remote state with locking (S3 + DynamoDB)
2. **Workspaces**: Consider using Terraform workspaces for managing multiple environments
3. **Secrets**: Store sensitive information in AWS Secrets Manager or Parameter Store
4. **Modules**: Use modules for reusable components
5. **Versioning**: Pin module and provider versions for stability
6. **Documentation**: Keep this README updated with any changes to the infrastructure

## Monitoring and Maintenance

After deployment, you can access the CloudWatch dashboard using the `cloudwatch_dashboard_url` output to monitor the infrastructure. Regular maintenance tasks include:

1. Applying security updates
2. Reviewing and optimizing resource usage
3. Updating Terraform configurations as requirements change
4. Backing up the database regularly
