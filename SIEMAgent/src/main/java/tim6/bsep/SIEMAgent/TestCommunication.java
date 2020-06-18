package tim6.bsep.SIEMAgent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tim6.bsep.SIEMAgent.model.enums.FacilityType;
import tim6.bsep.SIEMAgent.model.enums.LogType;
import tim6.bsep.SIEMAgent.model.enums.SeverityLevel;
import tim6.bsep.SIEMAgent.utility.SignatureUtility;
import tim6.bsep.SIEMAgent.web.v1.dto.LogDTO;

import java.io.*;
import java.security.KeyStore;
import java.util.Date;

@Component
public class TestCommunication implements ApplicationRunner {

    private static final String keyStoreName = "keystore.jks";

    private static final String trustStoreName = "truststore.jks";

    private static final String password = "secret";

    public static final String serverCertificateAlias = "SIEMAgent";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        LogDTO logDTO = new LogDTO(new Date(), FacilityType.AUTH, SeverityLevel.ERROR, "localhost69", "Invalid credentials username: Djole|", LogType.SIMULATED);

//        String msg = "test";
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(new FileInputStream(new ClassPathResource(keyStoreName).getFile()),
                password.toCharArray());
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(new FileInputStream(new ClassPathResource(trustStoreName).getFile()),
                password.toCharArray());

        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                                                    new SSLContextBuilder()
                                                    .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
                                                    .loadKeyMaterial(keyStore, password.toCharArray()).build());

        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        byte[] signedData = SignatureUtility.signMessage(toJson(logDTO), keyStoreName, password, serverCertificateAlias);
        System.out.println(String.format("SIGNED DATA LEN %s", signedData.length));
        ResponseEntity<String> response = restTemplate.postForEntity("https://localhost:8044/api/v1/logs/receive", signedData, String.class);
    }

    private String toJson(LogDTO logDTO) {
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(logDTO);
            System.out.println("ResultingJSONstring = " + json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}
