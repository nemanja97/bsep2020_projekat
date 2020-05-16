package tim6.bsep.SIEMAgent;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
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
import tim6.bsep.SIEMAgent.utility.SignatureUtility;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

@Component
public class TestCommunication implements ApplicationRunner {

    private static final String keyStoreName = "keystore.jks";

    private static final String trustStoreName = "truststore.jks";

    private static final String password = "secret";

    public static final String serverCertificateAlias = "SIEMAgent";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String msg = "test";
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
        byte[] signedData = SignatureUtility.signMessage(msg, keyStoreName, password, serverCertificateAlias);
        ResponseEntity<String> response = restTemplate.postForEntity("https://localhost:8044/api/v1/test", signedData, String.class);
    }
}
