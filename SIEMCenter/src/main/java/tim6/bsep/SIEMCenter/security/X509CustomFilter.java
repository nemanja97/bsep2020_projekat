package tim6.bsep.SIEMCenter.security;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.cryptacular.util.CertUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class X509CustomFilter extends GenericFilterBean {
    public static final String X509 = "javax.servlet.request.X509Certificate";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest && response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

        if (request.getAttribute(X509) == null) {
            chain.doFilter(request, response);
            return;
        }

        X509Certificate[] certificates = (X509Certificate[]) request.getAttribute(X509);
        if (certificates.length > 0) {
            checkCertificate((HttpServletResponse) response, certificates[0]);
        }

        chain.doFilter(request, response);
    }

    private void checkCertificate(HttpServletResponse response, X509Certificate certificate) {
        RestTemplate restTemplate = null;
        try{
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(new FileInputStream(new File("src/main/resources/truststore.jks")),
                    "secret".toCharArray());

            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(new SSLContextBuilder()
                    .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build());

            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
            ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
            restTemplate = new RestTemplate(requestFactory);
        } catch (Exception e){
            e.printStackTrace();
        }
        String cn = CertUtil.subjectCN(certificate);

        ResponseEntity<Boolean> restResponse = restTemplate
                .getForEntity("https://localhost:8043/api/v1/certificates/isValid/" + cn, Boolean.class);
        if(restResponse.getBody() == null || restResponse.getBody().equals(Boolean.FALSE)){
            response.setStatus(403);
        }
    }

}
