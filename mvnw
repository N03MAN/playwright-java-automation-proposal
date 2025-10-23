#!/bin/sh
# Maven Wrapper Script
# This script will download Maven if needed and run it

MAVEN_WRAPPER_VERSION="3.9.6"
MAVEN_WRAPPER_URL="https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/${MAVEN_WRAPPER_VERSION}/apache-maven-${MAVEN_WRAPPER_VERSION}-bin.tar.gz"
MAVEN_HOME="${HOME}/.m2/wrapper/maven-${MAVEN_WRAPPER_VERSION}"

# Check if Maven wrapper is already downloaded
if [ ! -d "${MAVEN_HOME}" ]; then
    echo "Downloading Maven ${MAVEN_WRAPPER_VERSION}..."
    mkdir -p "${HOME}/.m2/wrapper"
    curl -fsSL "${MAVEN_WRAPPER_URL}" -o "${HOME}/.m2/wrapper/maven.tar.gz"
    tar -xzf "${HOME}/.m2/wrapper/maven.tar.gz" -C "${HOME}/.m2/wrapper/"
    rm "${HOME}/.m2/wrapper/maven.tar.gz"
    echo "Maven downloaded successfully!"
fi

# Run Maven
"${MAVEN_HOME}/bin/mvn" "$@"

