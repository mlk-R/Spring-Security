package ru.malik.spring.SprinSecurity.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.malik.spring.SprinSecurity.models.Person;
import ru.malik.spring.SprinSecurity.repositories.PeopleRepository;


import java.util.Optional;

@Service
public class PersonServiceValidator  {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonServiceValidator(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public Optional<Person> findByUserName(String userName) {
        return peopleRepository.findByUserName(userName);
    }
}
