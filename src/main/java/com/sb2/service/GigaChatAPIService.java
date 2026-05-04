package com.sb2.service;

import com.sb2.client.GigaChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GigaChatAPIService {

    @Autowired
    private GigaChatClient gigaChatClient;

    public String textToTextRequest(String role, String text) {
        return gigaChatClient.gigaChatTextToTextRequest(role, text);
    }
}
