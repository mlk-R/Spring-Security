package ru.malik.spring.SprinSecurity.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.malik.spring.SprinSecurity.models.Person;
import ru.malik.spring.SprinSecurity.service.PersonServiceValidator;

import java.util.Optional;


@Component
public class PersonValidator implements Validator {

    private final PersonServiceValidator personServiceValidator;

    @Autowired
    public PersonValidator(PersonServiceValidator personServiceValidator) {
        this.personServiceValidator = personServiceValidator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        Optional<Person> existingPerson = personServiceValidator.findByUserName(person.getUserName());
        if (existingPerson.isPresent()) {
            errors.rejectValue("userName", "userName.invalid", "Пользователь с таким именем уже существует");
        }
    }
}
