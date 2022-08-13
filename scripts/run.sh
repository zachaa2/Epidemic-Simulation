#!/bin/bash
#Bash script to compile and run the Battleship Application
cd ../src/main/java
javac -cp "..\..\..\lib\jfree\*;." -d ../../../bin *.java
printf "\nRunning Program...\n"
java -cp "..\..\..\bin;..\..\..\lib\jfree\*" main.java.Main
cd ../../../scripts
$SHELL