package com.consume.consumeApi.services;

import com.consume.consumeApi.model.*;
import com.consume.consumeApi.repository.userRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class userIntImpl implements userInt{

    private final userRepo uRepo;
    private final String SECRET_KEY = "2646294A404E635266556A586E5A7234753778214125442A472D4B6150645367";
    private final PasswordEncoder passwordEncoder;
    private  final AuthenticationManager authenticationManager;


    @Override
    public RegisterResponse addUsers(RegisterRequest request) {
        var user = userModel.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .number(request.getNumber())
                .role(Role.USER)
                .build();
        var token = generatedToken(user);

        user.setIsVerified(false);
        uRepo.save(user);
        return  RegisterResponse.builder()
                .token(token).build();
    }

    @Override
    public  RegisterResponse logUsers(LoginRequest loginRequest) {
       authenticationManager.authenticate(new
               UsernamePasswordAuthenticationToken
               (loginRequest.getEmail(),loginRequest.getPassword()));
       var user = uRepo.findByEmail(loginRequest.getEmail());


        var token = generatedToken(user);


        return RegisterResponse.builder()
                .token(token).build();

    }


    @Override
    public userModel getUserByEmail(String email) {
       return uRepo.findByEmail(email);
    }

    @Override
    public List<userModel> FindAllUserEmails(String email) {
        return uRepo.findAll();
    }

    @Override
    public String generatedToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return generateToken(claims,userDetails);
    }

    @Override
    public userModel getUserByNumber(String number) {
        return uRepo.findByNumber(number);
    }

    //extract subject
    @Override
    public String ExtractUsername(String token) {
        return extractOtherClaims(token,Claims::getSubject);
    }



    //extra claims
    public <T> T extractOtherClaims(String token, Function<Claims,T> claimsResolver){
        final Claims claim = ExtractAllClaims(token);
        return  claimsResolver.apply(claim);
    }

//extract all claims
    public Claims ExtractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(signKey())
                .build()
                .parseClaimsJws(token)
                .getBody();


    }

    //is token valid
    public boolean isTokenValid(String token, UserDetails userDetails){
        String email = ExtractUsername(token);

        return (email.equals(userDetails.getUsername()) && !isTokenExpire(token));
    }


    private boolean isTokenExpire(String token) {

        return extractExpiration(token).before(new Date());
    }

    //date of expiration
    private Date extractExpiration(String token) {
        return extractOtherClaims(token,Claims::getExpiration);
    }


    private String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 120000))
                .signWith(SignatureAlgorithm.HS256, signKey())
                .compact();
    }

    private Key signKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }



}
