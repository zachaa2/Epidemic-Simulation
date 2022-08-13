@ECHO OFF
:: Batch Script to compile and run the custom test class
cd ../src/test/java
javac -cp "..\..\..\lib\jfree\*;.;..\..\..\bin" -d ../../../bin SimulationTest.java
java -cp "..\..\..\bin;..\..\..\lib\jfree\*" test.java.SimulationTest
cd ../../../scripts
PAUSE