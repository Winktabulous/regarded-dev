#!/bin/bash
sh -c "until ! nc -z localhost 43594; do sleep 0.1; done"
