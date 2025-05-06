import TestNG.program.HttpUtils;
import http.program.PropertyReader;
import http.program.dto.github.Rate_Limit;
import http.program.dto.github.User;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpOptions;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

public class TestWithSetup {
    private CloseableHttpClient client;
    private CloseableHttpResponse response;
    private final String LOGIN = "Breet11";
    private final String GITHUB_TOKEN = "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";

    @BeforeMethod
    public void setup(){
        client = HttpClientBuilder.create().build();
    }
    @AfterMethod
    public void close() throws IOException {
        if(client!= null){
            client.close();
        }
        if(response!= null){
            EntityUtils.consumeQuietly(response.getEntity());
            response.close();
        }
    }
    @Test
    public void testStatusCode() throws IOException {
        HttpGet request = HttpUtils.buildGetRequest("base_url");
        response = client.execute(request);
        int statusCode = response.getCode();
        Assert.assertEquals(statusCode, 200);
    }
    @DataProvider(name = "existingEndpoints")
    public static Object[][] endpointProvider(){

        return new Object[][]{{"base_url", 200},
                {"rate_limit_url", 200},
                {"feeds_url", 200},
                {"repos_url", 404},
                {"current_user_repositories_url", 401}};
    }
    @Test(dataProvider = "existingEndpoints")
    public void testMultipleStatusCodes(String url, int expectedStatusCode) throws IOException {
        HttpGet request = HttpUtils.buildGetRequest(url);
        response = client.execute(request);
        int actualStatusCode = response.getCode();
        Assert.assertEquals(actualStatusCode, expectedStatusCode);
    }
    @Test
    public void testContentType() throws IOException {
        HttpGet request = HttpUtils.buildGetRequest("base_url");
        response = client.execute(request);
        String contentType = response.getEntity().getContentType();
        Assert.assertEquals(contentType, "application/json; charset=utf-8");
    }
    @Test
    public void testRateLimit() throws IOException {
        HttpGet request = HttpUtils.buildGetRequest("base_url");
        response = client.execute(request);
        String rateLimitValue = HttpUtils.getHeader(response, "X-RateLimit-Limit");
        Assert.assertEquals(rateLimitValue, "60");
    }
    @Test
    public void testHeaderPresence() throws IOException {
        HttpGet request = HttpUtils.buildGetRequest("base_url");
        response = client.execute(request);
        boolean headerPresence = HttpUtils.headerIsPresent(response, "ETag");
        Assert.assertEquals(headerPresence, true);
    }
    @Test
    public void testReturningId() throws IOException, ParseException {
        HttpGet request = HttpUtils.buildGetRequest("users_url", LOGIN);
        response = client.execute(request);
        User user = HttpUtils.unmarshall(response, User.class);
        Assert.assertEquals(user.getId(), Integer.valueOf(98190101));
    }
    @Test
    public void testCoreLimit() throws IOException, ParseException {
        HttpGet request = HttpUtils.buildGetRequest("rate_limit_url");
        response = client.execute(request);
        Rate_Limit rateLimit = HttpUtils.unmarshallNested(response);

        Assert.assertEquals(rateLimit.getSearchLimit(), 10);
        Assert.assertEquals(rateLimit.getCoreLimit(), 60);
    }
    @Test
    public void optionsReturnsCorrectMethodsList() throws IOException{

        String header = "Access-Control-Allow-Methods";
        String expectedReply = "GET, POST, PATCH, PUT, DELETE";

        HttpOptions request = new HttpOptions(PropertyReader.getProperty("base_url"));
        response = client.execute(request);

        String actualValue = HttpUtils.getHeader(response, header);
        Assert.assertEquals(actualValue, expectedReply);
    }

    @Test
    public void deleteIsSuccessful() throws IOException{
        HttpDelete request = new HttpDelete("https://api.github.com/repos/Breet11/to-delete");

        request.setHeader("Authorization", "Bearer " + GITHUB_TOKEN);
        response = client.execute(request);

        int actualStatusCode = response.getCode();
        Assert.assertEquals(actualStatusCode, 204);
    }
    @Test
    public void createRepoReturns201() throws IOException {
        HttpPost request = new HttpPost(PropertyReader.getProperty("current_user_repositories_url"));
        request.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + GITHUB_TOKEN);
        String json = "{\"name\": \"to-delete\"}";
        request.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        response = client.execute(request);
        int actualStatusCode = response.getCode();
        Assert.assertEquals(actualStatusCode, 201);
    }
}
