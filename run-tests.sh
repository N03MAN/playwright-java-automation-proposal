#!/bin/bash
# Quick Test Runner Script
# This script helps you run tests easily without remembering Maven commands

set -e

echo "================================================"
echo "  Playwright Java Test Automation Framework"
echo "================================================"
echo ""

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Check if Maven is installed
if command -v mvn &> /dev/null; then
    MVN_CMD="mvn"
    echo -e "${GREEN}✓ Maven found${NC}"
elif [ -f "./mvnw" ]; then
    MVN_CMD="./mvnw"
    echo -e "${GREEN}✓ Using Maven Wrapper${NC}"
else
    echo -e "${RED}✗ Maven not found${NC}"
    echo ""
    echo "Please install Maven or use the Maven wrapper:"
    echo "  brew install maven"
    echo "or"
    echo "  ./mvnw clean test"
    echo ""
    exit 1
fi

echo ""
echo "What would you like to do?"
echo ""
echo "1) Run all tests (API + UI)"
echo "2) Run API tests only"
echo "3) Run UI tests only"
echo "4) Run registration tests"
echo "5) Run login tests"
echo "6) Generate Allure report"
echo "7) Clean and run all tests"
echo "8) Install Playwright browsers"
echo "9) Exit"
echo ""
read -p "Enter your choice (1-9): " choice

case $choice in
    1)
        echo -e "${BLUE}Running all tests...${NC}"
        $MVN_CMD test
        ;;
    2)
        echo -e "${BLUE}Running API tests...${NC}"
        $MVN_CMD test -Dtest="*APITests"
        ;;
    3)
        echo -e "${BLUE}Running UI tests...${NC}"
        $MVN_CMD test -Dtest="*UITests"
        ;;
    4)
        echo -e "${BLUE}Running registration tests...${NC}"
        $MVN_CMD test -Dtest="*Registration*"
        ;;
    5)
        echo -e "${BLUE}Running login tests...${NC}"
        $MVN_CMD test -Dtest="*Login*"
        ;;
    6)
        echo -e "${BLUE}Generating Allure report...${NC}"
        $MVN_CMD allure:serve
        ;;
    7)
        echo -e "${BLUE}Cleaning and running all tests...${NC}"
        $MVN_CMD clean test
        ;;
    8)
        echo -e "${BLUE}Installing Playwright browsers...${NC}"
        $MVN_CMD exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install --with-deps chromium"
        ;;
    9)
        echo -e "${YELLOW}Goodbye!${NC}"
        exit 0
        ;;
    *)
        echo -e "${RED}Invalid choice${NC}"
        exit 1
        ;;
esac

echo ""
echo -e "${GREEN}Done!${NC}"
echo ""
echo "View results:"
echo "  - Console output above"
echo "  - TestNG reports: target/surefire-reports/index.html"
echo "  - Generate Allure: $MVN_CMD allure:serve"
echo ""

