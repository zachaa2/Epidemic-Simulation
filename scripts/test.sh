#!/bin/bash
#Bash script to run the Java Test Class
cd ../src/test/java
javac -cp "..\..\..\lib\jfree\*;.;..\..\..\bin" -d ../../../bin SimulationTest.java
java -cp "..\..\..\bin;..\..\..\lib\jfree\*" test.java.SimulationTest
cd ../../../scripts
$SHELL