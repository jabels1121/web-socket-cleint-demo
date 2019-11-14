package com.jaybe.websocketcleintdemo.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class HttpService {

    private final RestTemplate restTemplate;

    public HttpService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity callWebSocketServer() {
        var uri = "http://localhost:8080/api/hello";

        HttpEntity entity = new HttpEntity(getHeadersWithBasicAuth("foo", "foo"));

        return restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
    }

    private String getBasicAuth(String login, String pass) {
        var plainCreds = login + ":" + pass;
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        return new String(base64CredsBytes);
    }

    public HttpHeaders getHeadersWithBasicAuth(String login, String pass) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Basic " + getBasicAuth(login, pass));
        return headers;
    }
}
