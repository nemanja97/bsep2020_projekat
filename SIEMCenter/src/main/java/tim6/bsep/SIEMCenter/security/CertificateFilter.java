package tim6.bsep.SIEMCenter.security;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.cryptacular.util.CertUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.GenericFilterBean;
import tim6.bsep.SIEMCenter.keystore.KeyStoreReader;
import tim6.bsep.SIEMCenter.utility.SignatureUtility;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;

import java.security.KeyStore;
import java.security.cert.X509Certificate;

public class CertificateFilter extends GenericFilterBean {

    public static final String X509 = "javax.servlet.request.X509Certificate";

    public static final String keyStoreName = "keystore.jks";

    public static final String trustStoreName = "truststore.jks";

    public static final String password = "secret";

    public static final String serverCertificateAlias = "SIEMCenter";

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
            if(checkCertificate((HttpServletResponse) response, certificates[0])){
                chain.doFilter(request, response);
            }
        }


    }

    private boolean checkCertificate(HttpServletResponse response, X509Certificate certificate) {
        RestTemplate restTemplate = null;
        try{
            KeyStoreReader keyStoreReader = new KeyStoreReader();
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(new FileInputStream(new ClassPathResource(trustStoreName).getFile()),
                    password.toCharArray());

            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(new SSLContextBuilder()
                    .loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build());

            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
            ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
            restTemplate = new RestTemplate(requestFactory);
            String msg = keyStoreReader.readCertificateAlias(trustStoreName, password, certificate);
            byte[] cms = SignatureUtility.signMessage(msg, keyStoreName, password, serverCertificateAlias);
            ResponseEntity<byte[]> restResponse = restTemplate
                    .postForEntity("https://localhost:8043/api/v1/certificates/isValid/", cms, byte[].class);
            byte[] signedResponse = restResponse.getBody();
            if(restResponse.getBody() == null || !SignatureUtility.checkMessage(signedResponse)){
                response.setStatus(500);
                return false;
            }
            if(SignatureUtility.extractMessage(signedResponse).equals("false")){
                response.setStatus(403);
                return false;
            }
        } catch (Exception e){
            response.setStatus(403);
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
