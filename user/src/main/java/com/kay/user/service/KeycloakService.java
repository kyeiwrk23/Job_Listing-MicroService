package com.kay.user.service;



import com.kay.user.dto.ApplicantRequestDto;
import com.kay.user.dto.ResetPassword;
import com.kay.user.dto.UserRequestDto;
import com.kay.user.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.lang.reflect.Array;
import java.net.URI;
import java.util.*;

@RequiredArgsConstructor
@Service
public class KeycloakService {

    private static final Logger log = LoggerFactory.getLogger(KeycloakService.class);
    @Value("${keycloak.username}")
    private String adminUsername;

    @Value("${keycloak.password}")
    private String adminPassword;

    @Value("${keycloak.clientId}")
    private String clientId;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.server_url}")
    private String server_url;

    @Value("${keycloak.clientUUId}")
    private String clientUUId;


    private final RestClient restClient;


    public String getAdminToken(){
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();

        params.add("client_id",clientId);
        params.add("username",adminUsername);
        params.add("password",adminPassword);
        params.add("grant_type", "password");


        String url = server_url + "/realms/" + realm + "/protocol/openid-connect/token";

       Map<String,Object> response =  restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(params)
                .retrieve()
               .body(new ParameterizedTypeReference<>(){});

        return response.get("access_token").toString();
    }

    public String createUser(String token, UserRequestDto requestDto){

        Map<String,Object> userPayload = new HashMap<>();
        userPayload.put("email",requestDto.getEmail());
        userPayload.put("firstName",requestDto.getFirstName());
        userPayload.put("lastName",requestDto.getLastName());
        userPayload.put("enabled",true);
        userPayload.put("username",requestDto.getUserName());

        Map<String,Object> credentials = new HashMap<>();
        credentials.put("type","password");
        credentials.put("value",requestDto.getPassword());
        credentials.put("temporary",false);

        userPayload.put("credentials", List.of(credentials));

        String url = server_url + "/admin/realms/" + realm + "/users";

        log.info("something in create!!!");
        ResponseEntity<String> response = restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h->h.setBearerAuth(token))
                .body(userPayload)
                .retrieve()
                .toEntity(String.class);

        if(!response.getStatusCode().is2xxSuccessful()){
            throw new ApiException("Unable to create User " + response.getBody());
        }

        //Extract KeyCloakId
        URI location = response.getHeaders().getLocation();
        if(location == null){
            throw new ApiException("Keycloak returned no Location Header " + response.getBody());
        }

        String path = location.getPath();
        return path.substring(path.lastIndexOf("/") + 1);

    }

    //Get client Role by Name
    public Map<String,Object> getClientRoleByName(String token, String roleName){

        log.info("getClientRoleByName ");

        String url = server_url + "/admin/realms/" + realm + "/clients/" +
                clientUUId + "/roles/" + roleName;
        log.info(url);

        return restClient
                .get()
                .uri(url)
                .headers(h->h.setBearerAuth(token))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }


    //Assign Client Role to User
    public void assignRoleToUserOrRealm(String userName, String roleName, String userId){
        String token = getAdminToken();

        Map<String,Object> roleMap = getClientRoleByName(token,roleName);
        roleMap.forEach( (k,v)->{
            log.info("Key: {} Value: {}",k,v);
        });

        String url = server_url + "/admin/realms/" + realm + "/users/" +
                userId + "/role-mappings/clients/" + clientUUId;

        log.info(url);

        ResponseEntity<String> response = restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h->h.setBearerAuth(token))
                .body(List.of(roleMap))
                .retrieve()
                .toEntity(String.class);

        if(!response.getStatusCode().is2xxSuccessful()){
            throw new ApiException("Unable to assign role '" + roleName + "' to " + userName);
        }

    }

    //Update User information
    public void UpdateUser(String userId, ApplicantRequestDto requestDto){
        String token = getAdminToken();

        Map<String,Object> userPayload = new HashMap<>();

        userPayload.put("email",requestDto.getEmail());
        userPayload.put("firstName",requestDto.getFirstName());
        userPayload.put("lastName",requestDto.getLastName());
        userPayload.put("enabled",true);


        String url = server_url + "/admin/realms/" + realm + "/users/" + userId;

        ResponseEntity<String> response = restClient.put()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h->h.setBearerAuth(token))
                .body(userPayload)
                .retrieve()
                .toEntity(String.class);

        if(!response.getStatusCode().is2xxSuccessful()){
            throw new ApiException("Unable to update User " + response.getBody());
        }
    }

    //Get User
    public Map<String,Object> getUser(String userId){
        String token = getAdminToken();

        String url = server_url + "/admin/realms/" + realm + "/users/" + userId;


        return restClient.get()
                 .uri(url)
                 .headers(h->h.setBearerAuth(token))
                 .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    // Deactivate User
    public void deactivateUser(String userId){

        ResponseEntity<String> deactivate = userStatusChange(userId, false);

        if(!deactivate.getStatusCode().is2xxSuccessful()){
            throw new ApiException("Unable to deactivate User " + deactivate.getBody());
        }
    }

    // Activate  User
    public void activateUser(String userId){

        ResponseEntity<String> activate = userStatusChange(userId, true);

        if(!activate.getStatusCode().is2xxSuccessful()){
            throw new ApiException("Unable to activate User " + activate.getBody());
        }
    }

    // Delete user permanently
    public void deleteUser(String userId){
        String token = getAdminToken();

        String url = server_url + "/admin/realms/" + realm + "/users/" +
                userId;

        ResponseEntity<String> response = restClient.delete()
                .uri(url)
                .headers(h-> h.setBearerAuth(token))
                .retrieve()
                .toEntity(String.class);

        if(!response.getStatusCode().is2xxSuccessful()){
            throw new ApiException("Unable to delete User " + response.getBody());
        }
    }

    //Change Password
    public boolean resetPassword(String userId, ResetPassword reset){
        String token = getAdminToken();


        Map<String,Object> credentials = new HashMap<>();
        credentials.put("type","password");
        credentials.put("value", reset.getNewPassword());
        credentials.put("temporary",false);



        String url = server_url + "/admin/realms/" + realm + "/users/" + userId +
                "/reset-password";

        ResponseEntity<String> pResponse = restClient.put()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h->h.setBearerAuth(token))
                .body(credentials)
                .retrieve()
                .toEntity(String.class);

        if(!pResponse.getStatusCode().is2xxSuccessful()){
            throw new ApiException("Unable to reset Password " + pResponse.getBody());
        }else {
            return true;
        }
    }



    private ResponseEntity<String> userStatusChange(String userId, boolean enable){
        String token = getAdminToken();

        Map<String,Object> payLoad = getUser(userId);
        payLoad.put("enabled",enable);

        String url = server_url + "/admin/realms/" + realm + "/users/" + userId;

        return restClient.put()
             .uri(url)
              .headers(h->h.setBearerAuth(token))
              .contentType(MediaType.APPLICATION_JSON)
             .body(payLoad)
             .retrieve()
             .toEntity(String.class);
    }

}
