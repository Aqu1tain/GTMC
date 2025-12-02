#!/bin/bash

# Compile all Java sources
echo "Compiling SeasonalBundle plugin..."

# Create build directory
mkdir -p build/classes
mkdir -p build/jar

# Download Paper API if not present
if [ ! -f "lib/paper-api-1.21.1-R0.1-SNAPSHOT.jar" ]; then
    echo "Downloading Paper API..."
    mkdir -p lib
    cd lib
    # Using Maven central for Paper API
    # Note: You may need to manually download this from https://repo.papermc.io/repository/maven-public/io/papermc/paper/paper-api/1.21.1-R0.1-SNAPSHOT/
    cd ..
fi

# Compile sources
javac \
    -d build/classes \
    -encoding UTF-8 \
    src/main/java/com/gtmc/seasonalbundle/*.java \
    src/main/java/com/gtmc/seasonalbundle/listeners/*.java \
    src/main/java/com/gtmc/seasonalbundle/commands/*.java \
    src/main/java/com/gtmc/seasonalbundle/data/*.java \
    src/main/java/com/gtmc/seasonalbundle/util/*.java

if [ $? -eq 0 ]; then
    echo "✓ Compilation successful!"

    # Copy resources
    cp src/main/resources/* build/classes/

    # Create JAR
    cd build/classes
    jar cfm ../seasonal-bundle.jar manifest.txt .
    cd ../..

    echo "✓ JAR created at build/seasonal-bundle.jar"
else
    echo "✗ Compilation failed!"
    exit 1
fi
