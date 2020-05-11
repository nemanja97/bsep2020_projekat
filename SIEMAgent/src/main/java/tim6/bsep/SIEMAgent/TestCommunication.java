package tim6.bsep.SIEMAgent;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

@Component
public class TestCommunication implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(new FileInputStream(new File("src/main/resources/keystore.jks")),
                "secret".toCharArray());
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(new FileInputStream(new File("src/main/resources/truststore.jks")),
                "secret".toCharArray());

        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                                                    new SSLContextBuilder()
                                                    .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
                                                    .loadKeyMaterial(keyStore, "secret".toCharArray()).build());

        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        ResponseEntity<String> response = restTemplate.postForEntity("https://localhost:8044/api/v1/test", "Pozz", String.class);
    }
}
