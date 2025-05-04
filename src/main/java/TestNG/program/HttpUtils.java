package TestNG.program;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import http.program.dto.bookstore.BooksDTO;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.Header;
import org.testng.annotations.DataProvider;
import http.program.PropertyReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class HttpUtils {
    @DataProvider(name = "endpointProvider")
    public static Object[][] endpointProvider(){
        Properties properties = PropertyReader.getAllProperties();

        Object[][] data = new Object[properties.size()][1];
        int i = 0;
        for (String key : properties.stringPropertyNames()){
            data[i++][0] = key;
        }
        return data;
    }
    public static BooksDTO getBooks() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://demoqa.com/BookStore/v1/Books");

        CloseableHttpResponse response = httpClient.execute(httpGet);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        String fullResponse = "";
        String line = "";

        while ((line = bufferedReader.readLine())!= null){
            fullResponse += line  + "\r\n";
        }

        ObjectMapper objectMapper = new ObjectMapper();
        BooksDTO books = objectMapper.readValue(fullResponse, new TypeReference<BooksDTO>(){});
        return books;
    }
    public static HttpGet buildGetRequest(String url) throws IOException {
        return new HttpGet(PropertyReader.getProperty(url));
    }
    public static String getHeader(CloseableHttpResponse response, String headerName){
        List<Header> httpHeaders = Arrays.asList(response.getHeaders());
        Header matchedHeader = httpHeaders.stream()
                .filter(header -> headerName.equalsIgnoreCase(header.getName()))
                .findFirst().orElseThrow(()-> new RuntimeException("Didn't find any headers"));
        return matchedHeader.getValue();
    }
    public static boolean headerIsPresent(CloseableHttpResponse response, String headerName){
        List<Header> httpHeaders  = Arrays.asList(response.getHeaders());
        return httpHeaders.stream().anyMatch(header -> header.getName().equalsIgnoreCase(headerName));
    }
}
