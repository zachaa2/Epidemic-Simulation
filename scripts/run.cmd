@ECHO OFF
:: Batch script to compile and run the Simulator
cd ../src/main/java
javac -cp "..\..\..\lib\jfree\*;." -d ../../../bin *.java
ECHO.
ECHO Starting Application
ECHO.
java -cp "..\..\..\bin;..\..\..\lib\jfree\*" main.java.Main
cd ../../../scripts
PAUSE