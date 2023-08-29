package com.ivan2003sr.microservices.currencyexchangeservice;

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
@Autowired
    private CurrencyExchangeRepository repository;

    @Autowired
    private Environment environment;

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyExchange retrieveExchangeValue(@PathVariable String from,@PathVariable String to){

/*
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

*/
        String port = environment.getProperty("local.server.port");

        CurrencyExchange currencyExchange = repository.findByFromAndTo(from, to);


        if(currencyExchange==null){
            throw new RuntimeException("Unable to Find data for " + from + " to "+to);
        }
        currencyExchange.setEnvironment(port);
return currencyExchange;
    }

}
