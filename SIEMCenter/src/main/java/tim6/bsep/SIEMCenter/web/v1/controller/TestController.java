package tim6.bsep.SIEMCenter.web.v1.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1/test")
public class TestController {

    @RequestMapping(method = RequestMethod.POST)
    public void testCommunication(@RequestBody  String message){
        System.out.println(message);
    }
}
