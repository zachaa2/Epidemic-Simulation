@ECHO OFF
:: Batch script to generate the JavaDoc
cd ../src/main/java
ECHO.
ECHO Generating JavaDoc...
ECHO.
javadoc -cp "..\..\..\lib\jfree\*;." -d ../../../docs *.java
cd ../../../scripts
PAUSE