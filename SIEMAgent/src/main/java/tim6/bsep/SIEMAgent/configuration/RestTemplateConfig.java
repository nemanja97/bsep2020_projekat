package tim6.bsep.SIEMAgent.configuration;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

@Configuration
public class RestTemplateConfig {

    @Value("${app.keystore-name}")
    private String keyStoreName;

    @Value("${app.truststore-name}")
    private String trustStoreName;

    @Value("${app.keystore-pass}")
    private String password ;

    @Bean
    RestTemplate restTemplate() throws KeyStoreException, IOException, NoSuchAlgorithmException,
            UnrecoverableKeyException, KeyManagementException, CertificateException {
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
        return new RestTemplate(requestFactory);
    }
}
