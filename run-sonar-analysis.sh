#!/bin/bash

# ==============================================
# SonarLint/SonarQube Analysis Script
# ==============================================

echo "========================================"
echo "🔍 SonarLint Code Quality Analysis"
echo "========================================"
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Check for Maven
if command -v mvn &> /dev/null; then
    MVN_CMD="mvn"
elif [ -f "./mvnw" ]; then
    MVN_CMD="./mvnw"
else
    echo -e "${RED}Error: Maven not found!${NC}"
    exit 1
fi

# Function to run analysis
run_analysis() {
    local analysis_type=$1
    
    case $analysis_type in
        1)
            echo -e "${BLUE}Running Quick Analysis (compile only)...${NC}"
            $MVN_CMD clean compile
            ;;
        2)
            echo -e "${BLUE}Running Full Analysis with Tests...${NC}"
            $MVN_CMD clean verify
            ;;
        3)
            echo -e "${BLUE}Running SonarQube Analysis (Local)...${NC}"
            echo -e "${YELLOW}Note: Requires local SonarQube server running on http://localhost:9000${NC}"
            $MVN_CMD clean verify sonar:sonar \
                -Dsonar.host.url=http://localhost:9000 \
                -Dsonar.login=admin \
                -Dsonar.password=admin
            ;;
        4)
            echo -e "${BLUE}Running Code Coverage Analysis with JaCoCo...${NC}"
            $MVN_CMD clean test jacoco:report
            echo ""
            echo -e "${GREEN}✓ Coverage report generated!${NC}"
            echo -e "View report at: ${BLUE}target/site/jacoco/index.html${NC}"
            ;;
        5)
            echo -e "${BLUE}Running Complete Analysis (Tests + Coverage + Quality)...${NC}"
            $MVN_CMD clean verify jacoco:report
            
            echo ""
            echo -e "${GREEN}✓ Analysis complete!${NC}"
            echo -e "Coverage report: ${BLUE}target/site/jacoco/index.html${NC}"
            echo -e "Surefire report: ${BLUE}target/surefire-reports/index.html${NC}"
            ;;
        6)
            echo -e "${BLUE}Opening JaCoCo Coverage Report...${NC}"
            if [ -f "target/site/jacoco/index.html" ]; then
                open target/site/jacoco/index.html 2>/dev/null || xdg-open target/site/jacoco/index.html 2>/dev/null || echo "Please open target/site/jacoco/index.html manually"
            else
                echo -e "${YELLOW}Report not found. Run analysis first (option 4 or 5)${NC}"
            fi
            ;;
        *)
            echo -e "${RED}Invalid option${NC}"
            ;;
    esac
}

# Main menu
while true; do
    echo ""
    echo "========================================"
    echo "Select Analysis Type:"
    echo "========================================"
    echo "1. Quick Analysis (compile only)"
    echo "2. Full Analysis with Tests"
    echo "3. SonarQube Analysis (requires local server)"
    echo "4. Code Coverage Analysis (JaCoCo)"
    echo "5. Complete Analysis (All checks)"
    echo "6. View Coverage Report"
    echo "7. Exit"
    echo ""
    read -p "Enter choice (1-7): " choice
    
    if [ "$choice" = "7" ]; then
        echo -e "${GREEN}Goodbye!${NC}"
        exit 0
    fi
    
    run_analysis $choice
    
    if [ "$choice" != "6" ]; then
        echo ""
        echo -e "${BLUE}Check for issues above ⬆️${NC}"
    fi
    
    echo ""
    read -p "Press Enter to continue..."
done
