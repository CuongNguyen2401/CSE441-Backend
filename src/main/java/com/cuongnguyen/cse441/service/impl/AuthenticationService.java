package com.cuongnguyen.cse441.service.impl;

import com.cuongnguyen.cse441.dto.request.*;
import com.cuongnguyen.cse441.entity.Role;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.cuongnguyen.cse441.dto.response.AuthenticationResponse;
import com.cuongnguyen.cse441.dto.response.IntrospectResponse;
import com.cuongnguyen.cse441.entity.InvalidatedToken;
import com.cuongnguyen.cse441.entity.RefreshToken;
import com.cuongnguyen.cse441.entity.User;
import com.cuongnguyen.cse441.enums.Status;
import com.cuongnguyen.cse441.exception.AppException;
import com.cuongnguyen.cse441.exception.ErrorCode;
import com.cuongnguyen.cse441.repository.InvalidatedTokenRepository;
import com.cuongnguyen.cse441.repository.RefreshTokenRepository;
import com.cuongnguyen.cse441.repository.RoleRepository;
import com.cuongnguyen.cse441.repository.UserRepository;
import com.cuongnguyen.cse441.repository.httpclient.OutboundIdentityClient;
import com.cuongnguyen.cse441.repository.httpclient.OutboundUserClient;
import com.cuongnguyen.cse441.service.IAuthenticationService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService implements IAuthenticationService {

    UserRepository userRepository;

    RefreshTokenRepository refreshTokenRepository;

    InvalidatedTokenRepository invalidatedTokenRepository;

    OutboundIdentityClient outboundIdentityClient;

    OutboundUserClient outboundUserClient;

    RoleRepository roleRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-access-duration}")
    protected long VALID_ACCESS_DURATION;

    @NonFinal
    @Value("${jwt.valid-refresh-duration}")
    protected long VALID_REFRESH_DURATION;


    @NonFinal
    @Value("${outbound.identity.client-id}")
    protected String CLIENT_ID;

    @NonFinal
    @Value("${outbound.identity.client-secret}")
    protected String CLIENT_SECRET;

    @NonFinal
    @Value("${outbound.identity.redirect-uri}")
    protected String REDIRECT_URI;

    @NonFinal
    protected final String GRANT_TYPE = "authorization_code";


    @Override
    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        var user = userRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (user.getStatus() == Status.INACTIVE)
            throw new AppException(ErrorCode.USER_INACTIVE);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var accessToken = generateToken(user, false);
        var refreshToken = generateToken(user, true);


        RefreshToken refreshTokenEntity = new RefreshToken();

        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setUsername(user.getUsername());
        refreshTokenEntity.setExpiryTime(Date.from(Instant.now().plus(VALID_REFRESH_DURATION, ChronoUnit.SECONDS)));
        refreshTokenRepository.save(refreshTokenEntity);

        return AuthenticationResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .authenticated(true)
                .build();
    }


    @Override
    @Transactional
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(request.getRefreshToken());

        refreshTokenRepository.deleteByToken(request.getRefreshToken());

        SignedJWT accessSignedJWT = verifyToken(request.getAccessToken());

        String accessTokenId = accessSignedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = accessSignedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(accessTokenId)
                .expiryTime(expiryTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);
    }

    @Override
    public String generateToken(User user, boolean isRefreshToken) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        long duration = isRefreshToken ? VALID_REFRESH_DURATION : VALID_ACCESS_DURATION;

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("cuongnguyen.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(duration, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .claim("customClaim", "custom")
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());


        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("can not create token", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String resetRefreshToken(User user, long expirationTime, String jwtId) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);


        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("cuongnguyen.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        expirationTime
                ))
                .claim("customClaim", "custom")
                .jwtID(jwtId)
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());


        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("can not create token", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;
        try {

            verifyToken(token);
        } catch (AppException e) {
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> {
                        stringJoiner.add(permission.getName());
                    });
            });
        }
        System.out.println("roles ne " + stringJoiner.toString());
        return stringJoiner.toString();
    }


    @Override
    @Transactional
    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signedJWT = verifyToken(request.getRefreshToken());

        var jit = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        var username = signedJWT.getJWTClaimsSet().getSubject();

        var user = userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.UNAUTHENTICATED)
        );
        if (!refreshTokenRepository.existsByToken(request.getRefreshToken()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        refreshTokenRepository.deleteByToken(request.getRefreshToken());

        var refreshToken = resetRefreshToken(user, expiryTime.getTime(), jit);
        RefreshToken refreshTokenEntity = new RefreshToken();


        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setUsername(user.getUsername());
        refreshTokenEntity.setExpiryTime(expiryTime);
        refreshTokenRepository.save(refreshTokenEntity);


        var accesstoken = generateToken(user, false);

        return AuthenticationResponse.builder()
                .accessToken(accesstoken)
                .refreshToken(refreshToken)
                .authenticated(true)
                .build();
    }

    @Override
    public AuthenticationResponse OutboundAuthenticate(String code) {
        var response = outboundIdentityClient.exchangeToken(ExchangeTokenRequest.builder()
                .code(code)
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .redirectUri(REDIRECT_URI)
                .grantType(GRANT_TYPE)
                .build());

        log.info("TOKEN RESPONSE {}", response);

        var userInfo = outboundUserClient.getUserInfor("json", response.getAccessToken());

        log.info("USER INFO {}", userInfo);


        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(com.cuongnguyen.cse441.enums.Role.USER.name()).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));
        roles.add(userRole);

        var user = userRepository.findByUsername(userInfo.getEmail())
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .username(userInfo.getEmail())
                            .firstName(userInfo.getGivenName())
                            .lastName(userInfo.getFamilyName())
                            .password(UUID.randomUUID().toString())
                            .roles(roles)
                            .build();
                    return userRepository.save(newUser);
                });
        var accessToken = generateToken(user, false);
        var refreshToken = generateToken(user, true);


        RefreshToken refreshTokenEntity = new RefreshToken();

        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setUsername(user.getUsername());
        refreshTokenEntity.setExpiryTime(Date.from(Instant.now().plus(VALID_REFRESH_DURATION, ChronoUnit.SECONDS)));
        refreshTokenRepository.save(refreshTokenEntity);

        return AuthenticationResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .authenticated(true)
                .build();
    }


    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        String tokenId = signedJWT.getJWTClaimsSet().getJWTID();


        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);


        if (!(verified && expiryTime.after(new Date())))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (invalidatedTokenRepository.existsById(tokenId))
            throw new AppException(ErrorCode.UNAUTHENTICATED);


        return signedJWT;
    }
}
