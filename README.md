# protobuf-function

Azure function to demonstrate receiving protobuf request message from Prometheus client using Snappy compression.
Written in Java using HttpTrigger.

## Pre-requesites

* [Maven](https://maven.apache.org/download.cgi)
* [JDK 17](https://learn.microsoft.com/en-us/java/openjdk/download#openjdk-17)
* [Azure Functions Core Tools](https://learn.microsoft.com/en-us/azure/azure-functions/functions-run-local?tabs=windows%2Cisolated-process%2Cnode-v4%2Cpython-v2%2Chttp-trigger%2Ccontainer-apps&pivots=programming-language-java#install-the-azure-functions-core-tools)
* Install [Go](https://go.dev/doc/install) locally in order to generate java classes for .proto files.

## Build locally

Run the following command:

```
mvn clean package
```

## Run locally

To run function locally use:

```
func start
```

## Integration tests

Trigger integration tests to verify connectivity against deployed azure function as follows:

```
mvn clean test -Pintegration-tests -Dfunction.url="http://localhost:7071/api/HttpTriggerJava"
```