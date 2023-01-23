package com.kezbek.transaction.external;

import com.kezbek.transaction.model.request.ContextRequest;
import com.kezbek.transaction.model.response.SessionResponse;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;

@Component
@Slf4j
public class CognitoInfo {

    @Value("${integration.aws-cognito.jwks}")
    private String jwks;

    public SessionResponse execute(ContextRequest request) {
        JWKSet jwkSet;
        try {
            ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor();
            jwkSet = JWKSet.parse(jwks);
            JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(jwkSet);
            JWSAlgorithm jwsAlgorithm = JWSAlgorithm.RS256;
            JWSKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector(jwsAlgorithm, jwkSource);
            jwtProcessor.setJWSKeySelector(keySelector);
            JWTClaimsSet claimsSet = jwtProcessor.process(request.getToken(), null);
            log.info("jwt parser claimsSet value [{}]", claimsSet);
            return SessionResponse.builder()
                    .subId(claimsSet.getStringClaim("sub"))
                    .companyCode(claimsSet.getStringClaim("cognito:username"))
                    .company(claimsSet.getStringClaim("name"))
                    .build();
        } catch (ParseException e) {
            log.error("jwk parse failure on parse [{}, {}] ", request.getToken(), ExceptionUtils.getStackTrace(e));
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        } catch (BadJOSEException e) {
            log.error("jwk parse failure on bad jose lib [{}, {}] ", request.getToken(), ExceptionUtils.getStackTrace(e));
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        } catch (JOSEException e) {
            log.error("jwk parse failure on jose ex [{}, {}] ", request.getToken(), ExceptionUtils.getStackTrace(e));
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

}
