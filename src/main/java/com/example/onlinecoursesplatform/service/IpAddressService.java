package com.example.onlinecoursesplatform.service;

import com.example.onlinecoursesplatform.exceptions.ipAddress.IpAddressFetchException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class IpAddressService {
    @Value("${ipify.api.url}")
    private String ipifyApiUrl;

    public String fetchUserIpAddress() throws IpAddressFetchException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(ipifyApiUrl))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new IpAddressFetchException("An error occurred. Failed to fetch user IP address");
            }
        } catch (URISyntaxException | InterruptedException | IpAddressFetchException | IOException e) {
            throw new IpAddressFetchException("An error occurred. Failed to fetch user IP address");
        }
    }

}