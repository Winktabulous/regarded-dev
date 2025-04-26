#!/bin/bash
echo "Creating new game screen session..."
screen -dmS game
screen -S game -X stuff "cd /home/world/server-production/\n"
screen -S game -X stuff "./gradlew runBeta\n"
