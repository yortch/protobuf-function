package com.demo.function;

import java.util.Optional;

import org.xerial.snappy.Snappy;

import com.google.protobuf.InvalidProtocolBufferException;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import prometheus.Remote.WriteRequest;
import java.io.IOException;

public class HttpTriggerFunction {

    /**
     * This function listens at endpoint "/api/HttpTriggerJava". Invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpTriggerJava
     */
    @FunctionName("HttpTriggerJava")
    public HttpResponseMessage run(
        @HttpTrigger(name = "req", methods = {HttpMethod.POST}, dataType = "binary",
            authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<byte[]>> request,
        final ExecutionContext context) {

        context.getLogger().info("Java HTTP trigger processed a request.");
        context.getLogger().info("Content-Type header: " + request.getHeaders().get("Content-Type"));

        byte[] requestBody = request.getBody().orElse(null);
        if (requestBody == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Invalid input").build();
        }

        try {
            byte[] uncompressedData = Snappy.uncompress(requestBody);
            WriteRequest writeRequest = WriteRequest.parseFrom(uncompressedData);
            context.getLogger().info("Received prometheus request with " + writeRequest.getTimeseriesCount() + " timeseries");
            context.getLogger().info("First timeseries has " + writeRequest.getTimeseries(0).getSamplesCount() + " samples");
            // Process the message
            return request.createResponseBuilder(HttpStatus.OK).body("Received message: " + writeRequest.getTimeseriesCount()).build();
        } catch (InvalidProtocolBufferException e) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Failed to parse Protobuf message").build();
        }
        catch (IOException e) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Failed to uncompress Snappy message").build();
        } 
    }
}