#!/bin/bash

# Virtual Game Web App - API Testing Script
echo "🎮 Virtual Game Web App - API Testing Demo"
echo "=========================================="

BASE_URL="http://localhost:8080"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${BLUE}📊 Checking Application Health...${NC}"
curl -s $BASE_URL/actuator/health | jq '.'

echo -e "\n${YELLOW}🔐 Testing Authentication Endpoints...${NC}"

echo -e "\n${BLUE}1. Testing User Registration${NC}"
echo "Registering user: testuser@example.com"
REGISTER_RESPONSE=$(curl -s -X POST $BASE_URL/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "testuser@example.com",
    "password": "Password123",
    "birthDate": "1990-01-01"
  }')

echo "Registration Response:"
echo $REGISTER_RESPONSE | jq '.'

echo -e "\n${BLUE}2. Testing User Login${NC}"
echo "Logging in with: testuser@example.com"
LOGIN_RESPONSE=$(curl -s -X POST $BASE_URL/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "testuser@example.com",
    "password": "Password123"
  }')

echo "Login Response:"
echo $LOGIN_RESPONSE | jq '.'

# Extract JWT token if login successful
TOKEN=$(echo $LOGIN_RESPONSE | jq -r '.data.accessToken // empty')

if [ ! -z "$TOKEN" ] && [ "$TOKEN" != "null" ]; then
    echo -e "${GREEN}✅ Login successful! JWT Token obtained.${NC}"
    
    echo -e "\n${YELLOW}👤 Testing User Management Endpoints...${NC}"
    
    echo -e "\n${BLUE}3. Get Current User Profile${NC}"
    curl -s -X GET $BASE_URL/api/users/me \
      -H "Authorization: Bearer $TOKEN" \
      -H "Content-Type: application/json" | jq '.'
    
    echo -e "\n${YELLOW}🎮 Testing Game Endpoints...${NC}"
    
    echo -e "\n${BLUE}4. Get All Games${NC}"
    curl -s -X GET $BASE_URL/api/games \
      -H "Authorization: Bearer $TOKEN" \
      -H "Content-Type: application/json" | jq '.'
    
    echo -e "\n${BLUE}5. Testing Public Endpoints${NC}"
    echo "Health Check:"
    curl -s $BASE_URL/actuator/health | jq '.status'
    
    echo -e "\n${BLUE}6. Available Metrics:${NC}"
    curl -s $BASE_URL/actuator/metrics | jq '.names[]' | head -10
    
else
    echo -e "${RED}❌ Login failed. Cannot proceed with authenticated endpoints.${NC}"
    echo "Response: $LOGIN_RESPONSE"
fi

echo -e "\n${GREEN}🎯 API Testing Complete!${NC}"
echo -e "\nTo manually test other endpoints, use the JWT token in Authorization header:"
echo -e "${YELLOW}Authorization: Bearer \$TOKEN${NC}"

echo -e "\n${BLUE}📖 Available Endpoints Summary:${NC}"
echo "POST   /api/auth/register - Register new user"
echo "POST   /api/auth/login    - Login user"
echo "GET    /api/users/me      - Get current user (auth required)"
echo "GET    /api/games         - Get all games (auth required)"
echo "GET    /api/leaderboard   - View leaderboard (auth required)"
echo "GET    /actuator/health   - Application health check"
echo "GET    /actuator/metrics  - Application metrics"