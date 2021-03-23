package br.com.mls.mltracking.util;

import java.net.URI;

/**
 * Created by manasses on 22/06/16.
 */

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import br.com.mls.mltracking.service.annotation.MeliAuthentication;

@Aspect
@Component
public class LoginInterceptor {
	
	@Autowired
	private LoginData loginData;

    @Pointcut("execution(* br.com.mls.mltracking.service.BuyerCollectorService.*(..))")
    public void meliIntegration() {
    	System.out.println("meliIntegration()");
    }

    @Pointcut("@annotation(br.com.mls.mltracking.service.annotation.MeliAuthentication)")
    public void meliAuthentication() {
    	System.out.println("meliAuthentication()");
    }

    @Before("meliIntegration() && meliAuthentication()")
    public void login() {
    	if (loginData.getAccessToken() == null || loginData.getRefreshToken() == null) {
    		URI uri = URI.create("https://api.mercadolibre.com/oauth/token?grant_type=client_credentials&client_id="+ loginData.getClientId() +"&client_secret="+ loginData.getClientSecret());
    		String authBody = new RestTemplate().postForObject(uri, null, String.class);
    		JsonParser jsonParser = new JsonParser();
    		JsonObject asJsonObject = jsonParser.parse(authBody).getAsJsonObject();
    		loginData.setAccessToken(asJsonObject.get("access_token").getAsString());
    		loginData.setRefreshToken(asJsonObject.get("refresh_token").getAsString());
    	}
    }
}
