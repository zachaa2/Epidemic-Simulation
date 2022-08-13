#!/bin/bash
#Batch script to generate the JavaDoc
cd ../src/main/java
printf "\nGenerating JavaDoc...\n"
javadoc -cp "..\..\..\lib\jfree\*;." -d ../../../docs *.java
cd ../../../scripts
$SHELL