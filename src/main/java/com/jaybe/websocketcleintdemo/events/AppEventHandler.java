package com.jaybe.websocketcleintdemo.events;

import com.jaybe.websocketcleintdemo.services.HttpService;
import com.jaybe.websocketcleintdemo.websocket.MyWebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class AppEventHandler {

    private final HttpService httpService;
    private final MyWebSocketHandler myWebSocketHandler;

    public AppEventHandler(HttpService httpService, MyWebSocketHandler myWebSocketHandler) {
        this.httpService = httpService;
        this.myWebSocketHandler = myWebSocketHandler;
    }

    @EventListener
    public void handleContextRefreshedEvent(ContextRefreshedEvent event) throws URISyntaxException, InterruptedException {
        log.info("In context refreshed event.");

        //ResponseEntity responseEntity = httpService.callWebSocketServer();
        //log.info("ResponseEntity - {}", responseEntity);

        List<Transport> transports = new ArrayList<>(2);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        transports.add(new RestTemplateXhrTransport());

        WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();
        HttpHeaders headersWithBasicAuth = httpService.getHeadersWithBasicAuth("foo", "foo");
        webSocketHttpHeaders.put(HttpHeaders.AUTHORIZATION, headersWithBasicAuth.get(HttpHeaders.AUTHORIZATION));

        SockJsClient sockJsClient = new SockJsClient(transports);

        WebSocketClient wsClient = new StandardWebSocketClient();
        Thread.sleep(5000L);
        //sockJsClient.doHandshake(myWebSocketHandler, webSocketHttpHeaders, new URI("ws://localhost:8080/ws-demo/sockjs"));
        wsClient.doHandshake(myWebSocketHandler, webSocketHttpHeaders, new URI("ws://localhost:8080/ws-demo"));
    }


}
