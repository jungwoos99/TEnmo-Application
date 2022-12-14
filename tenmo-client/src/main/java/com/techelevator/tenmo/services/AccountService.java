package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

public class AccountService {

    private final String BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();

    public AccountService (String url) {this.BASE_URL = url;}

    public BigDecimal getAccountBalance(AuthenticatedUser authenticatedUser) {
        HttpEntity<AuthenticatedUser> entity = makeEntity(authenticatedUser);
        BigDecimal balance = null;
        try {
            ResponseEntity<BigDecimal> response =
                    restTemplate.exchange(BASE_URL+"/account/balance", HttpMethod.GET, entity, BigDecimal.class);
            balance = response.getBody();
        } catch (RestClientResponseException |ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }

    public Map<Integer, Integer> getAccountHolders(AuthenticatedUser authenticatedUser) {
        HttpEntity<AuthenticatedUser> entity = makeEntity(authenticatedUser);
        Map<Integer, Integer> ids = null;
        try {
            ResponseEntity<Map> response =
                    restTemplate.exchange(BASE_URL+"/account/account_holders", HttpMethod.GET, entity, Map.class);
            ids = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return ids;
    }

    private HttpEntity<AuthenticatedUser> makeEntity(AuthenticatedUser authenticatedUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(authenticatedUser, headers);
    }

}
