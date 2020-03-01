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

    @Autowired
    private MyService service;

    @RequestMapping(path = "/db/{name}", method = RequestMethod.GET)
    public String dbEndpoint(@PathVariable("name") String name){    
        return service.storeInDatabase(name);
    }

    @RequestMapping(path = "/kafka/{name}", method = RequestMethod.GET)
    public String kafkaEndpoint(@PathVariable("name") String name){    
        return service.storeInKafkaTopic(name);
    }
    
}