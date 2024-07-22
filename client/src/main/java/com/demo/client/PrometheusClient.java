import com.google.protobuf.InvalidProtocolBufferException;
import org.xerial.snappy.Snappy;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.io.OutputStream;
import prometheus.Remote.WriteRequest;
import prometheus.Types.TimeSeries;
import prometheus.Types.Sample;

public class PrometheusClient {

    public static void main(String[] args) throws IOException, URISyntaxException {
        byte[] compressedData = getCompressedData();

        // Send the compressed data to Azure Function
        URI uri = new URI("https://<your-azure-function-url>/api/PrometheusReceiver");
        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-protobuf");

        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(compressedData);
        }

        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);
    }

    private static byte[] getCompressedData() throws IOException {
        // Create a sample WriteRequest
        WriteRequest writeRequest = WriteRequest.newBuilder()
            .addTimeseries(
                TimeSeries.newBuilder()
                    .addSamples(
                        Sample.newBuilder()
                            .setValue(123.45)
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
