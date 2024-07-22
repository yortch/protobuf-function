package com.demo.function;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.fail;

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
        HttpURLConnection connection = null;
        // Send the compressed data to Azure Function
        try {
            System.out.println("Function URL: " + FUNCTION_URL);
            URI uri = new URI(FUNCTION_URL);
            URL url = uri.toURL();
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-protobuf");
        }
        catch (Exception e) {
            fail("Failed to open connection: " + e.getMessage());
        }

        try (OutputStream outputStream = connection.getOutputStream()) {
            byte[] compressedData = getCompressedData();
            outputStream.write(compressedData);
        } catch (Exception e) {
            fail("Failed to send request: " + e.getMessage());
        }

        try {
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);
        } catch (Exception e) {
            fail("Failed to get response code: " + e.getMessage());
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
