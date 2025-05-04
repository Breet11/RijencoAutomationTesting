package http.program;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.Header;

import java.io.IOException;

public class TestStuff {
    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://api.github.com/");

        CloseableHttpResponse response = httpClient.execute(httpGet);
        for(Header header : response.getHeaders()){
            System.out.println(header.getName() + ": " + header.getValue());
        }
    }
}
