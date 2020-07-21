package com.hindsightsoftware.hotelbooking.utils;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.json.JSONObject;

import java.io.IOException;

public class AuthorizationToken {
    public static final String HEADER_NAME = "Authorization";
    private String baseUrl;
    private String username;
    private String password;

    public AuthorizationToken(String baseUrl) {
        this.baseUrl = baseUrl;
        this.username = "admin";
        this.password = "password123";
    }

    public AuthorizationToken setUsername(String username) {
        this.username = username;
        return this;
    }

    public AuthorizationToken setPassword(String password) {
        this.password = password;
        return this;
    }

    public String build() throws IOException {
        JSONObject model = new JSONObject();
        model.put("username", "admin");
        model.put("password", "password123");

        String body = Request.Post(String.format("%s/login", baseUrl))
                .useExpectContinue()
                .bodyString(model.toString(), ContentType.APPLICATION_JSON)
                .execute()
                .returnContent()
                .asString();


        JSONObject token = new JSONObject(body);
        return String.format("Bearer %s", token.getString("token"));
    }
}