package com.xperia.xpense_scheduler.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.xperia.client.AbstractHttpClient;
import org.xperia.models.SharedMailDetails;
import org.xperia.models.SharedUserSetting;
import org.xperia.models.UserOauthToken;
import org.xperia.models.response.SuccessResponse;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class InternalClient extends AbstractHttpClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(InternalClient.class);

    @Value("${xpense.internal.tracker.url}")
    private String trackerUrl;

    @Value("${xpense.internal.tracker.username}")
    private String username;

    @Value("${xpense.internal.tracker.password}")
    private String password;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;


    @Autowired
    public InternalClient(RestTemplate restTemplate){
        super(restTemplate);
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    //TODO Move the settingsType to common package and use the proper type here
    public SharedUserSetting findUserSettingByType(String email, String type){

        String url = trackerUrl + "/api/internal/user/settings"
                + "?email=" + URLEncoder.encode(email, StandardCharsets.UTF_8)
                + "&settingsType=mailLabelId";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(username, password);
        try{
            ResponseEntity<SuccessResponse> response = this.executeGet(url, httpHeaders, SuccessResponse.class);
            return this.objectMapper.convertValue(response.getBody().getData(), SharedUserSetting.class);
        }catch (Exception ex){
            LOGGER.error("Error fetching user settings from tracker service : {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    public SharedMailDetails findMailDetailsOfUser(String email) {

        String url = trackerUrl + "/api/internal/user/mailDetails"
                + "?email=" + URLEncoder.encode(email, StandardCharsets.UTF_8);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(username, password);
        try{
            ResponseEntity<SuccessResponse> response = this.executeGet(url, httpHeaders, SuccessResponse.class);
            return this.objectMapper.convertValue(response.getBody().getData(), SharedMailDetails.class);
        }catch (Exception ex){
            LOGGER.error("Error fetching user mail details from tracker services : {}", ex.getMessage(), ex);
            throw ex;
        }
    }



    public UserOauthToken refreshOauth2Token(String email){

        String url = trackerUrl + "/api/internal/refresh/token" + "?email=" + URLEncoder.encode(email, StandardCharsets.UTF_8);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(username, password);

        try{
            ResponseEntity<SuccessResponse> response = this.executePost(url, null, httpHeaders, SuccessResponse.class);
            return (UserOauthToken) response.getBody().getData();
        }catch (Exception ex){
            LOGGER.error("Error refreshing token using Internal Client : {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    public List<UserOauthToken> findAllAuthTokens(){

        String url = trackerUrl + "/api/internal/users/google";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth(username, password);
        HttpEntity<Void> request = new HttpEntity<>(httpHeaders);
        try{
            ResponseEntity<SuccessResponse<List<UserOauthToken>>> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            request,
                            new ParameterizedTypeReference<>() {}
                    );
            if (response.getBody().getStatus() == 0){
                LOGGER.error("InternalClient returns error for authtoken fetch : {}", response.getBody());
                return null;
            }
            return response.getBody().getData();
        }catch (Exception ex){
            LOGGER.error("Error fetching oauth tokens from tracker : {}", ex.getMessage(), ex);
            return null;
        }
    }
}
