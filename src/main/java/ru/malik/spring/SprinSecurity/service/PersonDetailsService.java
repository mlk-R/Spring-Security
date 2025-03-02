package ru.malik.spring.SprinSecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.malik.spring.SprinSecurity.models.Person;
import ru.malik.spring.SprinSecurity.repositories.PeopleRepository;
import ru.malik.spring.SprinSecurity.security.PersonDetails;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = peopleRepository.findByUserName(username);

        if (person.isEmpty()) {
            throw new UsernameNotFoundException(username + " not found");
        }

        return new PersonDetails(person.get());
    }
}
