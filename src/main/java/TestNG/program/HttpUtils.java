package TestNG.program;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import http.program.dto.bookstore.BooksDTO;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.testng.annotations.DataProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class HttpUtils {
    @DataProvider(name ="HttpGet")
    public static Object[][] getResponse() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://demoqa.com/BookStore/v1/Books");

        CloseableHttpResponse response = httpClient.execute(httpGet);

        return new Object[][]{new Object[]{response}};
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
        //objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        //System.out.println(objectMapper.writeValueAsString(valCurs));

        return books;
    }
}
