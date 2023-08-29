package com.ivan2003sr.microservices.limitsservice.controller;

import com.ivan2003sr.microservices.limitsservice.bean.Limits;
import com.ivan2003sr.microservices.limitsservice.configuration.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LimitsController {
@Autowired
    private Configuration configuration;
@GetMapping("/limits")
    public Limits retrieveLimits(){


return new Limits(configuration.getMinimun(),configuration.getMaximun());
   // return new Limits(1,1000);

    }
}
