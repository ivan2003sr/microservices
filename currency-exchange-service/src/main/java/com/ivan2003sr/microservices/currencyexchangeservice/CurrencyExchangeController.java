package com.ivan2003sr.microservices.currencyexchangeservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@RestController
public class CurrencyExchangeController {

    private Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);
@Autowired
    private CurrencyExchangeRepository repository;

    @Autowired
    private Environment environment;

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyExchange retrieveExchangeValue(@PathVariable String from,@PathVariable String to){

        logger.info("RetrieveExchangeValue called with {} to {}", from, to);
        String port = environment.getProperty("local.server.port");

        CurrencyExchange currencyExchange = repository.findByFromAndTo(from, to);


        if(currencyExchange==null){
            throw new RuntimeException("Unable to Find data for " + from + " to "+to);
        }
        currencyExchange.setEnvironment(port);
return currencyExchange;
    }


    @GetMapping("/currency-exchange-online/from/{from}/to/{to}")
    public CurrencyExchange retrieveExchangeValueTrue(@PathVariable String from,@PathVariable String to){
        logger.info("RetrieveExchangeValue called with {} to {}", from, to);

        Double value;

if (from.equals("USD") && to.equals("ARS")) {
     value = DolarCotizacion.getBlue();
} else if (from.equals("ARS") && to.equals("USD")) {
    Double value2 = DolarCotizacion.getBlue();
    BigDecimal value3 = BigDecimal.ONE.divide(BigDecimal.valueOf(value2),new MathContext(5, RoundingMode.HALF_UP));
    value = value3.doubleValue();


} else {
     value = DolarCotizacion.getValue(from,to);
}

        CurrencyExchange currencyExchange = new CurrencyExchange(1000L,from,to, BigDecimal.valueOf(value));


        String port = environment.getProperty("local.server.port");




        if(currencyExchange==null){
            throw new RuntimeException("Unable to Find data for " + from + " to "+to);
        }
        currencyExchange.setEnvironment(port);
        return currencyExchange;
    }

}
