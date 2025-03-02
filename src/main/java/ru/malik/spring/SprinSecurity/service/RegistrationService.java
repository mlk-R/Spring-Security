package ru.malik.spring.SprinSecurity.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.malik.spring.SprinSecurity.models.Person;
import ru.malik.spring.SprinSecurity.repositories.PeopleRepository;

import java.net.PasswordAuthentication;

@Service
public class RegistrationService {

    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void register(Person person) {
        String password = passwordEncoder.encode(person.getPassword());
        person.setPassword(password);


        peopleRepository.save(person);
    }
}
