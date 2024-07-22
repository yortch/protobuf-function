# protobuf-function

Azure function to demonstrate receiving protobuf request message from Prometheus client using Snappy compression.
Written in Java using HttpTrigger.

## Pre-requesites

Requires installing Go locally

## Build locally

Run the following command:

```
mvn clean package
```

## Integration tests

Trigger integration tests to verify connectivity against deployed azure function as follows:

```
mvn clean test -Pintegration-tests -Dfunction.url="http://localhost:7071/api/HttpTriggerJava"
```