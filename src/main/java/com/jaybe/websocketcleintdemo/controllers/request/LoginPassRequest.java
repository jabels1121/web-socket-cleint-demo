package com.jaybe.websocketcleintdemo.controllers.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginPassRequest {

    private String login;
    private String password;
    private int countOfConnections;

}
