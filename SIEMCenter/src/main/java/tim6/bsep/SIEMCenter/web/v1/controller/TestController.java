package tim6.bsep.SIEMCenter.web.v1.controller;

import org.bouncycastle.cms.CMSException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tim6.bsep.SIEMCenter.keystore.KeyStoreReader;
import tim6.bsep.SIEMCenter.utility.SignatureUtility;

import java.io.IOException;
import java.security.cert.X509Certificate;

@RestController
@RequestMapping(value = "api/v1/test")
public class TestController {

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity testCommunication(@RequestBody  byte[] signedMessage) throws IOException, CMSException {
        if(SignatureUtility.checkMessage(signedMessage)){
            String msg = SignatureUtility.extractMessage(signedMessage);
            System.out.println(msg);
            return new ResponseEntity(HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }


}
