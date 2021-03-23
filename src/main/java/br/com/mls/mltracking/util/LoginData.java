package br.com.mls.mltracking.util;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class LoginData {

	private String refreshToken;
	private String accessToken;
	private Long clientId;
	private String clientSecret;

}
