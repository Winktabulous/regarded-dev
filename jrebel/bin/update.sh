#!/bin/sh
# This is a script that updates the JRebel in a given location
# the default run updates jrebel in the current location, but it can be run with a custom jrebel location:
# java -jar jrebel-activation.jar -update path/to/jrebel.jar
# It will check the jrebel version of the current path provided and compare it with the latest version.
# It the later is different then it downloads the latest jrebel-nosetup.zip and exctract it next to the jrebel.jar provided, overwriting the jar and other assosiated files.
# URL's checked by this script are:
# https://update.zeroturnaround.com/jrebel/jrebel-stable-version.txt for the latest version number
# https://dl.zeroturnaround.com/jrebel/ to get the corresponding jrebel-nosetup.zip
java -jar `dirname $0`/jrebel-activation.jar -update `dirname $0`/../jrebel.jar
