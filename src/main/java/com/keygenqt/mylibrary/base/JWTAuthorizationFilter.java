/*
 * Copyright 2020 Vitaliy Zarubin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.keygenqt.mylibrary.base;

import com.keygenqt.mylibrary.config.WebSecurityConfig;
import com.keygenqt.mylibrary.models.User;
import com.keygenqt.mylibrary.models.UserToken;
import com.keygenqt.mylibrary.models.repositories.UserRepository;
import com.keygenqt.mylibrary.models.repositories.UserTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    public static final String HEADER = "Authorization";

    private static final String PREFIX = "Bearer ";

    private final UserRepository repositoryUser;
    private final UserTokenRepository repositoryUserToken;

    public JWTAuthorizationFilter(UserRepository repositoryUser, UserTokenRepository repositoryUserToken) {
        this.repositoryUser = repositoryUser;
        this.repositoryUserToken = repositoryUserToken;
    }

    public static String getJWTToken(String login, String role) {
        return JWTAuthorizationFilter.PREFIX + Jwts
                .builder()
                .setId("JWT")
                .setSubject(login)
                .claim("authorities", new ArrayList<SimpleGrantedAuthority>() {{
                    add(new SimpleGrantedAuthority(role));
                }})
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 2592000000L /* month */))
                .signWith(SignatureAlgorithm.HS512, WebSecurityConfig.SECRET_KEY.getBytes())
                .compact();
    }

    @Override
    public void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain chain) {

        String authorization = request.getHeader(HEADER);
        String language = request.getHeader(ACCEPT_LANGUAGE);

        try {
            if (authorization != null && language != null && authorization.startsWith(PREFIX)) {
                Claims claims = validateToken(authorization, language);
                if (claims != null && claims.get("authorities") != null) {
                    setUpSpringAuthentication(claims);
                    chain.doFilter(request, response);
                    return;
                }
            }
            SecurityContextHolder.clearContext();
            chain.doFilter(request, response);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private Claims validateToken(String authorization, String language) {
        UserToken token = repositoryUserToken.findByToken(authorization);
        if (token == null) {
            return null;
        }

        User user = repositoryUser.findByIdActive(token.getUserId());
        if (user == null || !user.getEnabled()) {
            return null;
        }

        update(token, language);

        return Jwts.parser()
                .setSigningKey(WebSecurityConfig.SECRET_KEY.getBytes())
                .parseClaimsJws(authorization.replace(PREFIX, ""))
                .getBody();
    }

    private void setUpSpringAuthentication(Claims claims) {
        var authorities = claims.get("authorities");
        if (authorities instanceof ArrayList && !((ArrayList<?>) authorities).isEmpty()) {
            var item = ((ArrayList<?>) authorities).get(0);
            if (item instanceof LinkedHashMap && ((LinkedHashMap<?, ?>) item).containsKey("authority")) {
                var authority = ((LinkedHashMap<?, ?>) item).get("authority");
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(claims.getSubject(), null, new ArrayList<SimpleGrantedAuthority>() {{
                            add(new SimpleGrantedAuthority((String) authority));
                        }})
                );
            }
        }
    }

    private void update(UserToken userToken, String language) {
        var isUpdate = false;
        if (!userToken.getLanguage().equals(language)) {
            userToken.setLanguage(language);
            isUpdate = true;
        }
        if (userToken.getUpdatedAt().getTime() < (new Date().getTime() - 86400000)) {
            userToken.setUpdatedAt(new Date());
            isUpdate = true;
        }
        if (isUpdate) {
            repositoryUserToken.save(userToken);
        }
    }
}
