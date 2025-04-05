package com.thanhtan.groceryshop.service;

import com.nimbusds.jose.JOSEException;
import com.thanhtan.groceryshop.dto.request.AuthenticationRequest;
import com.thanhtan.groceryshop.dto.request.IntrospectRequest;
import com.thanhtan.groceryshop.dto.request.LogoutRequest;
import com.thanhtan.groceryshop.dto.request.RefreshRequest;
import com.thanhtan.groceryshop.dto.response.AuthenticationResponse;
import com.thanhtan.groceryshop.dto.response.IntrospectResponse;
import com.thanhtan.groceryshop.entity.User;

import java.text.ParseException;

public interface IAuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);

    String generateToken(User user, boolean isRefreshToken);

    IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException, ParseException;

    void logout(LogoutRequest request) throws ParseException, JOSEException;

    AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;

    AuthenticationResponse OutboundAuthenticate(String code);


}
