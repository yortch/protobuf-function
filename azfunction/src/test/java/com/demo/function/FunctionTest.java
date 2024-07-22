package com.demo.function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.xerial.snappy.Snappy;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;

import prometheus.Remote.WriteRequest;
import prometheus.Types.Sample;
import prometheus.Types.TimeSeries;


/**
 * Unit test for Function class.
 */
public class FunctionTest {
    /**
     * Unit test for HttpTriggerJava method.
     */
    @Test
    public void testHttpTriggerJava() throws Exception {
        // Setup
        @SuppressWarnings("unchecked")
        final HttpRequestMessage<Optional<byte[]>> req = mock(HttpRequestMessage.class);

        double sampleValue = 123.45;
        Optional<byte[]> queryBody = Optional.ofNullable((byte[])getCompressedData(sampleValue));
        doReturn(queryBody).when(req).getBody();

        doAnswer(new Answer<HttpResponseMessage.Builder>() {
            @Override
            public HttpResponseMessage.Builder answer(InvocationOnMock invocation) {
                HttpStatus status = (HttpStatus) invocation.getArguments()[0];
                return new HttpResponseMessageMock.HttpResponseMessageBuilderMock().status(status);
            }
        }).when(req).createResponseBuilder(any(HttpStatus.class));

        final ExecutionContext context = mock(ExecutionContext.class);
        doReturn(Logger.getGlobal()).when(context).getLogger();

        // Invoke
        final HttpResponseMessage ret = new HttpTriggerFunction().run(req, context);

        // Verify
        assertEquals(HttpStatus.OK, ret.getStatus());
    }

    /**
     * Returns the compressed data obtained by serializing and compressing a WriteRequest protobuf message.
     *
     * @return the compressed data as a byte array
     * @throws IOException if an I/O error occurs during serialization or compression
     */
    private static byte[] getCompressedData(double sampleValue) throws IOException {
        // Create a sample WriteRequest
        WriteRequest writeRequest = WriteRequest.newBuilder()
            .addTimeseries(
                TimeSeries.newBuilder()
                    .addSamples(
                        Sample.newBuilder()
                            .setValue(sampleValue)
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
