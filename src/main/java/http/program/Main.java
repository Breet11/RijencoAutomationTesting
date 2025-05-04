package http.program;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.thoughtworks.xstream.XStream;
import http.program.dto.bnm.ValCurs;
import http.program.dto.bnm.Valute;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(PropertyReader.getProperty("bnmUrl"));

        CloseableHttpResponse response = httpClient.execute(httpGet);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        String fullResponse = "";
        String line = "";

        while ((line = bufferedReader.readLine())!= null){
            fullResponse += line  + "\r\n";
        }
        XStream xStream = new XStream();

        xStream.allowTypesByWildcard(new String[]{"http.program.dto.*"});
        xStream.autodetectAnnotations(true);

        xStream.processAnnotations(ValCurs.class);
        xStream.processAnnotations(Valute.class);

        xStream.addImplicitCollection(ValCurs.class, "valutes", Valute.class);
        ValCurs valCurs = (ValCurs) xStream.fromXML(fullResponse);

        //System.out.print(xStream.toXML(valCurs));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);

        System.out.println(objectMapper.writeValueAsString(valCurs));
    }
}
