@echo off
REM This is a script that updates the JRebel in a given location.
REM the default run updates jrebel in the current location, but it can be run with a custom jrebel location:
REM java -jar jrebel-activation.jar -update path/to/jrebel.jar
REM It will check the jrebel version of the current path provided and compare it with the latest version.
REM It the later is different then it downloads the latest jrebel-nosetup.zip and exctract it next to the jrebel.jar provided, overwriting the jar and other assosiated files.
REM URL's checked by this script are:
REM https://update.zeroturnaround.com/jrebel/jrebel-stable-version.txt for the latest version number
REM https://dl.zeroturnaround.com/jrebel/ to get the corresponding jrebel-nosetup.zip
java -jar "%~dp0\jrebel-activation.jar" -update "%~dp0\..\jrebel.jar"