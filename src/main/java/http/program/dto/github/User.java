package http.program.dto.github;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;

public class User {
    private String login;
    private Integer id;

    public String getLogin(){
        return login;
    }

    public Integer getId() {
        return id;
    }

}
