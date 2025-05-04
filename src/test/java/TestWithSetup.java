import TestNG.program.HttpUtils;
import http.program.dto.github.Rate_Limit;
import http.program.dto.github.User;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.HttpEntities;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.http.HttpClient;

public class TestWithSetup {
    private CloseableHttpClient client;
    private CloseableHttpResponse response;
    private final String LOGIN = "Breet11";

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
    @Test(dataProvider = "endpointProvider", dataProviderClass = HttpUtils.class)
    public void testMultipleStatusCodes(String url) throws IOException {
        HttpGet request = HttpUtils.buildGetRequest(url);
        response = client.execute(request);
        int statusCode = response.getCode();
        Assert.assertEquals(statusCode, 200);
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

}
