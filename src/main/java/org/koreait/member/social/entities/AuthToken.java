package org.koreait.member.social.entities;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthToken {
    @JsonAlias("access_token")
    private String accessToken;

    @JsonAlias("token_type")
    private String tokenType;

    @JsonAlias("refresh_token")
    private String refreshToken;

    @JsonAlias("expires_in")
    private long expiresIn;

    @JsonAlias("refresh_token_expires_in")
    private long refreshTokenExpiresIn;
}
