#!/bin/bash
./gradlew --console=plain --build-cache :app:runPluginScanner
./gradlew --console=plain --build-cache :app:installDist
