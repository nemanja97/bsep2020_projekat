package tim6.bsep.SIEMCenter.web.v1.controller;

import org.bouncycastle.cms.CMSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tim6.bsep.SIEMCenter.mapper.LogMapper;
import tim6.bsep.SIEMCenter.service.LogService;
import tim6.bsep.SIEMCenter.utility.SignatureUtility;
import tim6.bsep.SIEMCenter.web.v1.dto.LogDTO;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping(value = "api/v1/logs")
@CrossOrigin()
public class LogsController {

    @Autowired
    LogService logService;

    @RequestMapping(value = "/receive", method = RequestMethod.POST, consumes = "application/octet-stream")
    public ResponseEntity<Object> receiveLog(@Valid @RequestBody byte[] signedLog) throws IOException, CMSException {
        if(SignatureUtility.checkMessage(signedLog)){
            LogDTO logDTO = SignatureUtility.extractMessage(signedLog);
            logService.save(LogMapper.LogFromDTO(logDTO));
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
