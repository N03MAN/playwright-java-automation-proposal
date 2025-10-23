#!/bin/bash
# Automated Maven Installation and Test Execution
# This script will install Maven and run your tests

set -e

echo "================================================"
echo "  Installing Maven and Running Tests"
echo "================================================"
echo ""

# Check if Homebrew is installed
if ! command -v brew &> /dev/null; then
    echo "📦 Installing Homebrew..."
    /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
    
    # Add Homebrew to PATH for Apple Silicon Macs
    if [[ $(uname -m) == 'arm64' ]]; then
        echo 'eval "$(/opt/homebrew/bin/brew shellenv)"' >> ~/.zprofile
        eval "$(/opt/homebrew/bin/brew shellenv)"
    fi
else
    echo "✅ Homebrew already installed"
fi

# Install Maven
if ! command -v mvn &> /dev/null; then
    echo ""
    echo "📦 Installing Maven..."
    brew install maven
    echo "✅ Maven installed successfully!"
else
    echo "✅ Maven already installed"
fi

# Verify installations
echo ""
echo "🔍 Verifying installation..."
java -version
echo ""
mvn -version

echo ""
echo "================================================"
echo "  Installing Playwright Browsers"
echo "================================================"
echo ""

mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install --with-deps chromium"

echo ""
echo "================================================"
echo "  Running Tests"
echo "================================================"
echo ""

mvn clean test

echo ""
echo "================================================"
echo "  ✅ ALL DONE!"
echo "================================================"
echo ""
echo "View results:"
echo "  - Console output above"
echo "  - TestNG reports: target/surefire-reports/index.html"
echo ""
echo "Generate Allure report:"
echo "  mvn allure:serve"
echo ""

