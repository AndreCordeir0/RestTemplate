package com.rest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


public class RestTemplate {


    private final HttpClient httpClient;

    private String url;

    private Map<String, String> headers;

    Consumer<Exception> action;
    private RestTemplate(){
        httpClient = HttpClient.newHttpClient();
        headers = new HashMap<>();
    }

    public RestTemplate setUrl(String url){
        this.url = url;
        return this;
    }

    public RestTemplate setHeader(String chave, String valor){
        headers.put(chave, valor);
        return this;
    }

    public static RestTemplate builder(){
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate;
    }

    public RestTemplate exceptionally(Consumer<Exception> action){
        this.action = action;
        return this;
    }
    public String build() throws IOException, InterruptedException {
        try{
            HttpRequest.Builder val = HttpRequest.newBuilder(
                    URI.create(this.url)
            );
            if(!headers.isEmpty()){
                for(Map.Entry<String, String> headerEntry : headers.entrySet()){
                    val.header(headerEntry.getKey(),headerEntry.getValue());
                }
            }
            HttpResponse<String> res = httpClient.send(val.build(), HttpResponse.BodyHandlers.ofString());

//            return res.body();
            throw new RuntimeException("Erro pq sim");
        }catch (Exception e){
            this.action.accept(e);
        }
        return null;
    }

    public static void main(String[] args) {
        try{
            String resposta = RestTemplate.builder().setUrl("https://opentdb.com/api.php?amount=2").exceptionally(ex->{
                System.out.println(ex.toString());
                throw new RuntimeException(ex);
            }).build();
            System.out.println(resposta);

        }catch (Exception e){
            System.out.println(e);
        }
    }
}
