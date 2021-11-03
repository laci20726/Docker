package com.example.springbootdocker.controller;

import com.example.springbootdocker.model.Person;
import com.example.springbootdocker.repositories.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class HomeController {
    private final List<String> lastNames = new ArrayList<>(Arrays.asList("Aaren","Aarika","Abagael","Abagail","Abbe"
            ,"Abbey","Abbi","Abbie","Abby","Abbye","Abigael","Abigail"));
    private final List<String> firstNames = new ArrayList<>(Arrays.asList("Betty","Snyder","Frances","Spence",
            "Kay", "McNulty","Marlyn", "Wescoff","Ruth", "Lichterman"));
    private final List<Person> people = new ArrayList<>();
    private Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    private PersonRepository personRepository;


    private void randomPeopleGenerate() {
        int max = 10;
        int min = 1;
        int range = max - min + 1;
        int maxLastNames = 12;
        int maxFirstNames = 10;
        int rangeOfLastNames = maxLastNames - min;
        int rangeOfFirstNames = maxFirstNames - min;

        for (int i = 0; i < (int)(Math.random() * range) + min; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(firstNames.get((int)(Math.random() * rangeOfFirstNames)));
            sb.append(" ");
            sb.append(lastNames.get((int)(Math.random() * rangeOfLastNames)));
            people.add(new Person(sb.toString(), (int)(Math.random() * 100)));
        }
    }

    @RequestMapping("/names")
    public String home() {
        StringBuilder sb = new StringBuilder();
        randomPeopleGenerate();
        people.forEach(person -> logger.info(person.toString()));
        people.forEach(personRepository::insert);

        people.clear();
        personRepository.findAll().forEach(sb::append);
        personRepository.deleteAll();
        return sb.toString();
    }
}
