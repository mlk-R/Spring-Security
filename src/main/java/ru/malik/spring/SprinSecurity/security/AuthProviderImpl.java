//package ru.malik.spring.SprinSecurity.security;
//
//import org.apache.coyote.BadRequestException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import ru.malik.spring.SprinSecurity.service.PersonDetailsService;
//
//import javax.security.auth.Subject;
//import javax.security.auth.callback.CallbackHandler;
//import javax.security.auth.login.LoginException;
//import java.security.AuthProvider;
//import java.util.Collections;
//
//@Component
//public class AuthProviderImpl implements AuthenticationProvider {
//
//    private final PersonDetailsService personDetailsService;
//
//    @Autowired
//    public AuthProviderImpl(PersonDetailsService personDetailsService) {
//        this.personDetailsService = personDetailsService;
//    }
//
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        String username = authentication.getName();
//        UserDetails personDetails = personDetailsService.loadUserByUsername(username);
//
//        String password = authentication.getCredentials().toString();
//
//        password.equals(personDetails.getPassword());
//        if (!password.equals(personDetails.getPassword())) {
//            throw new BadCredentialsException("incorrect password");
//        }
//        return new UsernamePasswordAuthenticationToken(personDetails, password, Collections.emptyList());
//    }
//
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return true;
//    }
//}
