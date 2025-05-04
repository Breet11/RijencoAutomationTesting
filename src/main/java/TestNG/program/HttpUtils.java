package TestNG.program;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import http.program.dto.bookstore.BooksDTO;
import http.program.dto.github.Rate_Limit;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONObject;
import org.testng.annotations.DataProvider;
import http.program.PropertyReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

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
    public static HttpGet buildGetRequest(String url, String query) throws IOException {
        String property = PropertyReader.getProperty(url);
        String fullUrl = property + query;
        return new HttpGet(fullUrl);
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
    public static Object getValueFor(JSONObject jsonObject, String key){
        return jsonObject.get(key);
    }
    public static <T> T unmarshall(CloseableHttpResponse response, Class<T> clazz) throws IOException, ParseException {
        String jsonBody = EntityUtils.toString(response.getEntity());
        return new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValue(jsonBody, clazz);
    }
    @SuppressWarnings("unckecked")
    @JsonProperty("resources")
    public static Rate_Limit unmarshallNested(CloseableHttpResponse response) throws IOException, ParseException {
        String jsonBody = EntityUtils.toString(response.getEntity());
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.readValue(jsonBody, Map.class);

        Map<String, Object> resources = (Map<String, Object>) map.get("resources");
        Map<String, Object> core  = (Map<String, Object>) resources.get("core");
        Map<String, Object> search = (Map<String, Object>) resources.get("search");

        int coreLimit = (Integer) core.get("limit");
        int searchLimit = (Integer) search.get("limit");

        return new Rate_Limit(coreLimit, searchLimit);
    }
}
