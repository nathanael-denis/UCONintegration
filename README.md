This project is an implementation of a usage control system (UCS) which interacts
with the IOTA technology. It is used to measure performance gains resulting from integration
in the context of micropayments in the IoT, using distributed ledgers.

To run the main code, use the following commands at root i.e., where pom.xml is located:

mvn compile
mvn exec:java -Dexec.mainClass="iotawucon.App" -e

All testing functions are located in the Testing.java class.

The full procedure to deploy a private IOTA network on AWS, install the IOTA node (Hornet)
and run the tests is detailed in the tutorial/tutorial.pdf file. As an exemple, a file registering all needed 
information for the peering of AWS nodes is also given in the tutorial directory.


