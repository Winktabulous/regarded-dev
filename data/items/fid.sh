#!/bin/bash

cat items.json| grep -B 1 -A 1 -i -E "$1"
