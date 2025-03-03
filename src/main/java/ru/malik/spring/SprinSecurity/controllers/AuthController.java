package ru.malik.spring.SprinSecurity.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.malik.spring.SprinSecurity.dto.AuthenticationDTO;
import ru.malik.spring.SprinSecurity.dto.PersonDTO;
import ru.malik.spring.SprinSecurity.models.Person;
import ru.malik.spring.SprinSecurity.security.JWTUtil;
import ru.malik.spring.SprinSecurity.service.RegistrationService;
import ru.malik.spring.SprinSecurity.util.PersonValidator;

import java.util.Map;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RegistrationService registrationService;
    private final PersonValidator personValidator;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(RegistrationService registrationService, PersonValidator personValidator, JWTUtil jwtUtil, ModelMapper modelMapper, AuthenticationManager authenticationManager) {
        this.registrationService = registrationService;
        this.personValidator = personValidator;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") Person person) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public Map<String, String> performRegistration(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult) {

        Person person = convertPersonDTOtoPerson(personDTO);

        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            return Map.of("message", "Error!");
        }

        registrationService.register(person);

        String token = jwtUtil.generateJWTToken(person.getUserName());
        return Map.of("token", token);
    }

    @PostMapping("login")
    public Map<String, String> performLogin(@RequestBody AuthenticationDTO authenticationDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(), authenticationDTO.getPassword());

        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            return Map.of("message", "Bad credentials!");
        }

        String token = jwtUtil.generateJWTToken(authenticationDTO.getUsername());
        return Map.of("token", token);
    }

    public Person convertPersonDTOtoPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }
}
