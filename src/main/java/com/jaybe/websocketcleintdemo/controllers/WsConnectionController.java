package com.jaybe.websocketcleintdemo.controllers;

import com.jaybe.websocketcleintdemo.controllers.request.LoginPassRequest;
import com.jaybe.websocketcleintdemo.services.HttpService;
import com.jaybe.websocketcleintdemo.websocket.MySockJsWebSocketHandler;
import com.jaybe.websocketcleintdemo.websocket.MyWebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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

@RestController
@Slf4j
public class WsConnectionController {
    private final MyWebSocketHandler myWebSocketHandler;
    private final HttpService httpService;
    private final MySockJsWebSocketHandler mySockJsWebSocketHandler;

    public WsConnectionController(MyWebSocketHandler myWebSocketHandler, HttpService httpService, MySockJsWebSocketHandler mySockJsWebSocketHandler) {
        this.myWebSocketHandler = myWebSocketHandler;
        this.httpService = httpService;
        this.mySockJsWebSocketHandler = mySockJsWebSocketHandler;
    }

    @PostMapping(path = "/connect")
    public void connect(@RequestBody LoginPassRequest loginPassRequest) throws URISyntaxException {
        int countOfConnections = loginPassRequest.getCountOfConnections();
        for (int i = 0; i < countOfConnections; i++) {
            createConnections(loginPassRequest.getLogin(), loginPassRequest.getPassword());
        }
    }

    @PostMapping(path = "/connect/sockjs")
    public void connectSockJs(@RequestBody LoginPassRequest loginPassRequest) {
        int countOfConnections = loginPassRequest.getCountOfConnections();
        for (int i = 0; i < countOfConnections; i++) {
            createConnectionsSockJs(loginPassRequest.getLogin(), loginPassRequest.getPassword());
        }
    }

    private WebSocketHttpHeaders getHeadersWithBasicAuth(final String login, final String pass) {
        WebSocketHttpHeaders webSocketHttpHeaders = new WebSocketHttpHeaders();
        HttpHeaders headersWithBasicAuth = httpService.getHeadersWithBasicAuth(login, pass);
        webSocketHttpHeaders.put(HttpHeaders.AUTHORIZATION, headersWithBasicAuth.get(HttpHeaders.AUTHORIZATION));
        return webSocketHttpHeaders;
    }

    @Async
    public void createConnectionsSockJs(final String login, final String pass) {
        List<Transport> transports = new ArrayList<>(2);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        transports.add(new RestTemplateXhrTransport());
        SockJsClient sockJsClient = new SockJsClient(transports);
        try {
            sockJsClient.doHandshake(mySockJsWebSocketHandler, getHeadersWithBasicAuth(login, pass), new URI("ws://localhost:8080/ws-demo/sockjs"));
        } catch (URISyntaxException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Async
    public void createConnections(final String login, final String pass) {
        WebSocketClient wsClient = new StandardWebSocketClient();
        try {
            wsClient.doHandshake(myWebSocketHandler, getHeadersWithBasicAuth(login, pass), new URI("ws://localhost:8080/ws-demo"));
        } catch (URISyntaxException e) {
            log.error(e.getMessage(), e);
        }
    }
}
