import TestNG.program.ExcelDataProvider;
import TestNG.program.HttpUtils;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import http.program.dto.bookstore.BooksDTO;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class TestNGTest {
    @Test(dataProvider = "RegistrationDataProvider" , dataProviderClass = ExcelDataProvider.class)
    public void test(Map<String, String> inputMatirx){
        System.out.println("Running test: "+ inputMatirx.get("TestCase"));
        System.out.println("Username: "+ inputMatirx.get("username"));
        System.out.println("Password: "+ inputMatirx.get("password"));
    }
    @Test
    public void testBooks() throws IOException {
        BooksDTO books = HttpUtils.getBooks();
        Assert.assertEquals(books.getBooks().get(0).getIsbn(), "9781449325862" );
    }
}
