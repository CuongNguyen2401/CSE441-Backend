package com.cuongnguyen.cse441.service;

import com.nimbusds.jose.JOSEException;
import com.cuongnguyen.cse441.dto.request.AuthenticationRequest;
import com.cuongnguyen.cse441.dto.request.IntrospectRequest;
import com.cuongnguyen.cse441.dto.request.LogoutRequest;
import com.cuongnguyen.cse441.dto.request.RefreshRequest;
import com.cuongnguyen.cse441.dto.response.AuthenticationResponse;
import com.cuongnguyen.cse441.dto.response.IntrospectResponse;
import com.cuongnguyen.cse441.entity.User;

import java.text.ParseException;

public interface IAuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

    String generateToken(User user, boolean isRefreshToken);

    IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException, ParseException;

    void logout(LogoutRequest request) throws ParseException, JOSEException;

    AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;

    AuthenticationResponse OutboundAuthenticate(String code);


}
