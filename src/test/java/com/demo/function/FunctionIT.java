package com.demo.function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.Optional;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.xerial.snappy.Snappy;

import prometheus.Remote.WriteRequest;
import prometheus.Types.Sample;
import prometheus.Types.TimeSeries;

/**
 * Integration test for the Function class.
 */
public class FunctionIT {

    // Define the Azure Function URL    
    private static String FUNCTION_URL = Optional.ofNullable(System.getProperty("function.url"))
                                                 .orElse("http://localhost:7071/api/HttpTriggerJava");

    // Define the Prometheus data
    private static final double SAMPLE_VALUE = 123.45;

    @Test
    public void testRequest() {
        System.out.println("Function URL: " + FUNCTION_URL);
        byte[] compressedData = null;
        try {
            compressedData = getCompressedData();
        } catch (IOException e) {
            fail("Failed to build request: " + e.getMessage());
        }
        // Send the compressed data to Azure Function
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost postRequest = new HttpPost(FUNCTION_URL);
            postRequest.setEntity(new ByteArrayEntity(compressedData));
            postRequest.setHeader("content-type", "application/octet-stream");

            CloseableHttpResponse response = httpClient.execute(postRequest);
            int responseCode = response.getStatusLine().getStatusCode();
            String msg = EntityUtils.toString(response.getEntity());
            System.out.println("Response Code: " + responseCode + ", Content: " + msg);
            assertEquals(200, responseCode);
        }
        catch (Exception e) {
            fail("Failed to open connection: " + e.getMessage());
        }

    }

    private static byte[] getCompressedData() throws IOException{
        // Create a sample WriteRequest
        WriteRequest writeRequest = WriteRequest.newBuilder()
            .addTimeseries(
                TimeSeries.newBuilder()
                    .addSamples(
                        Sample.newBuilder()
                            .setValue(SAMPLE_VALUE)
                            .setTimestamp(System.currentTimeMillis())
                        .build())
                .build())
            .build();

        // Serialize the WriteRequest to byte array
        byte[] protobufData = writeRequest.toByteArray();

        // Compress the byte array using Snappy
        byte[] compressedData = Snappy.compress(protobufData);
        return compressedData;
    }    
}
