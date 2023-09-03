package com.ivan2003sr.microservice.currencyconversionservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;

@Configuration(proxyBeanMethods = false)
class RestTemplateConfiguration{
@Bean
    RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }

}

@RestController
public class CurrencyConversionServiceController {

    @Value("${currency.exchange.url}")
    private String currencyExchangeUrl;
@Autowired
    private CurrencyExchangeProxy proxy;

private Logger logger = LoggerFactory.getLogger(CurrencyConversionServiceController.class);

@Autowired
private  RestTemplate restTemplate;
@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversion(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity){

    logger.info("calculateCurrencyConversion called with {} to {} with {}",from,to,quantity);

    HashMap<String,String> uriVariables = new HashMap<>();
    uriVariables.put("from",from);
    uriVariables.put("to",to);
    ResponseEntity<CurrencyConversion> responseEntity = restTemplate.getForEntity(currencyExchangeUrl,CurrencyConversion.class,uriVariables);
    CurrencyConversion currencyConversion = responseEntity.getBody();

    return new CurrencyConversion(currencyConversion.getId(),from,to,quantity,currencyConversion.getConversionMultiple(), quantity.multiply(currencyConversion.getConversionMultiple()),currencyConversion.getEnviroment()+" "+" rest Template");

    }

    @GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversionFeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity){

        logger.info("calculateCurrencyConversion called with {} to {} with {}",from,to,quantity);
    CurrencyConversion currencyConversion = proxy.retrieveExchangeValue(from,to);

        return new CurrencyConversion(currencyConversion.getId(),from,to,quantity,currencyConversion.getConversionMultiple(), quantity.multiply(currencyConversion.getConversionMultiple()),currencyConversion.getEnviroment()+" "+"feign");

    }

    @GetMapping("/currency-conversion-feign-online/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversionFeignOnline(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity){

        logger.info("calculateCurrencyConversion called with {} to {} with {}",from,to,quantity);

        CurrencyConversion currencyConversion = proxy.retrieveExchangeValueTrue(from,to);
        return new CurrencyConversion(currencyConversion.getId(),from,to,quantity,currencyConversion.getConversionMultiple(), quantity.multiply(currencyConversion.getConversionMultiple()),currencyConversion.getEnviroment()+" "+"feign");

    }
}
