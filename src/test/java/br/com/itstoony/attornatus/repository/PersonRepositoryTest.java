package br.com.itstoony.attornatus.repository;

import br.com.itstoony.attornatus.model.Person;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class PersonRepositoryTest {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    @DisplayName("Should return true when person by passed 'cpf' exists on database")
    public void existsByCpfTest() {
        // scenery
        Person person = createPerson();

        personRepository.save(person);

        // execution
        Boolean result = personRepository.existsByCpf(person.getCpf());

        // validation
        assertThat(person.getId()).isNotNull();
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Should return false when passed 'cpf' doesn't exist on database")
    public void doesntExistByCpfTest() {
        // scenery
        Person person = createPerson();

        // execution
        Boolean result = personRepository.existsByCpf(person.getCpf());

        // validation
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("Should return a page of person filtering by name")
    public void findByNameTest() {
        // scenery
        Person person = createPerson();
        String name = "fULan";

        Person savedPerson = personRepository.save(person);

        // execution
        Page<Person> result = personRepository.findByName(name, PageRequest.of(0, 10));

        // validation
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent()).contains(savedPerson);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }


    private static Person createPerson() {
        return Person.builder()
                .id(1L)
                .name("Fulano")
                .cpf("486.031.170-12")
                .birthDay(LocalDate.of(1998, 11, 25))
                .addressSet(new HashSet<>())
                .build();
    }


}
