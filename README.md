# protobuf-function

Azure function to demonstrate receiving protobuf request message from Prometheus client using Snappy compression.
Written in Java using HttpTrigger. NOTE: due to [lack of support](https://github.com/Azure/azure-functions-java-worker/issues/738) 
for `application/x-protobuf` Content Type in Azure function, the test client uses `application/octet-stream` instead as a workaround.

## Pre-requesites

* [Maven](https://maven.apache.org/download.cgi)
* [JDK 17](https://learn.microsoft.com/en-us/java/openjdk/download#openjdk-17)
* [Azure Functions Core Tools](https://learn.microsoft.com/en-us/azure/azure-functions/functions-run-local?tabs=windows%2Cisolated-process%2Cnode-v4%2Cpython-v2%2Chttp-trigger%2Ccontainer-apps&pivots=programming-language-java#install-the-azure-functions-core-tools)
* Install [Go](https://go.dev/doc/install) locally in order to generate java classes for .proto files.

Additionally to deploy to Azure using azd the following are required:

* [azd](https://learn.microsoft.com/en-us/azure/developer/azure-developer-cli/) cli
* [terraform] - required to provision infrastructure
* [Docker Desktop](https://docs.docker.com/desktop/install/windows-install/)
* [Node.js](https://nodejs.org/en/download/prebuilt-installer) - required by Azure Function Core Tools

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

## Deploy to Azure with azd

After installing pre-requisites above, run: `azd up`

Alternatively you can run these commands separately:
* `azd package` - builds and packages Azure Function
* `azd provision` - deploys infrastructure using terraform
* `azd deploy` - deploys function (using postdeploy script as a workaround for this [issue](https://github.com/Azure/azure-dev/issues/4155))
