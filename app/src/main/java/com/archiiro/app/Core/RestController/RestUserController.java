package com.archiiro.app.Core.RestController;

import com.archiiro.app.Core.Constant;
import com.archiiro.app.Core.Dto.RoleDto;
import com.archiiro.app.Core.Dto.UserDto;
import com.archiiro.app.Core.Service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/user")
public class RestUserController {
    @Autowired
    private UserService userService;

    @Secured({Constant.ROLE_ADMIN})
    @RequestMapping(value = "/getAllDto", method = RequestMethod.GET)
    public List<UserDto> getAllDto() {
        return this.userService.getAllDto();
    }

    @RequestMapping(value = "/getUserByUsername/{username}", method = RequestMethod.GET)
    public UserDto getUserByUsername(@PathVariable String username) {
        return this.userService.getUserByUsername(username);
    }

    @RequestMapping(value = "/createDto", method = RequestMethod.POST)
    public UserDto createDto(@RequestBody UserDto userDto) {
        return this.userService.createDto(null, userDto);
    }

    @RequestMapping(value = "/updateDto/{id}", method = RequestMethod.PUT)
    public UserDto updateDto(@PathVariable Long id, @RequestBody UserDto userDto) {
        return this.userService.updateDto(id, userDto);
    }

    @RequestMapping(value = "/deleteDto/{id}", method = RequestMethod.DELETE)
    public UserDto deleteDto(@PathVariable Long id) {
        return this.userService.deleteDto(id);
    }


    @RequestMapping(value = "/refresh_token", method = RequestMethod.POST)
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bear ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bear ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                UserDto user = userService.getUserByUsername(username);
                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10*60*1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(RoleDto::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }
        else {
            throw new RuntimeException("Refesh token is missing");
        }

    }

}
