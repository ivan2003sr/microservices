package com.ivan2003sr.microservices.currencyexchangeservice;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class DolarCotizacion {
    String apiKey="33AJU4AD81OPHZRC";


   // String symbol = "USDARS";

public String dolarValue(String symbol){

    try {
        URL url = new URL("https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + apiKey);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null){
    response.append(line);
        }
        reader.close();

        String jsonResponse = response.toString();

        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONObject globalQuote = jsonObject.getJSONObject("Global Quote");
        String cotizacion = globalQuote.getString("05. price");



   return cotizacion;

    } catch (Exception e){

        System.out.println("ERROR, NO SE ENCONRÓ "+symbol);
       // e.printStackTrace();
    }

return "0";

}

    public static Double getValue(String from, String to) {
        DolarCotizacion dolarCotizacion = new DolarCotizacion();
        Double value = Double.parseDouble(dolarCotizacion.dolarValue(from + to));
        return value;
    }

    public static Double getBlue(){
    try {

        URL url = new URL("https://api.bluelytics.com.ar/v2/latest");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null){
            response.append(line);
        }
        reader.close();

        String jsonResponse = response.toString();

        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONObject globalQuote = jsonObject.getJSONObject("blue");
        double valorBlue = globalQuote.getDouble("value_avg");

        return valorBlue;



    }catch (Exception e){
        e.printStackTrace();
    }

    return null;
    }



}

class Main{
    public static void main(String[] args) {

        try {

            URL url = new URL("https://api.bluelytics.com.ar/v2/latest");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null){
                response.append(line);
            }
            reader.close();

            String jsonResponse = response.toString();

            JSONObject jsonObject = new JSONObject(jsonResponse);
           JSONObject globalQuote = jsonObject.getJSONObject("blue");
            double avgQuote = globalQuote.getDouble("value_avg");

            System.out.println(avgQuote);

        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
