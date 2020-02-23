package com.rafabene.microserviceB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * MicroservicesBController
 */
@RequestMapping("/")
@RestController
public class MicroservicesBController {

    private static int count = 0;

    @Autowired
    private MessageRepository messageRepository;

    @RequestMapping(path = "/db/{name}", method = RequestMethod.GET)
    public String database(@PathVariable("name") String name){
        String msg = "Microservice A (from frontend): " + name + " => Microservice B (save to DB): " + ++count;
        Message message = new Message(msg);
        messageRepository.save(message);
        return msg;
    }

    @RequestMapping(path = "/chain/{name}", method = RequestMethod.GET)
    public String microserviceC(@PathVariable("name") String name){
        return "MicroserviceC";
    }
    
}