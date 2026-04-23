package com.sprint.Book_Partner_Application.publisher.repository;

import com.sprint.Book_Partner_Application.publisher.entity.Publisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PublisherRepositoryTest {

    @Autowired
    private PublisherRepository publisherRepository;

    @Test
    void testSavePublisher() {

        Publisher publisher = new Publisher();
        publisher.setPubId("1389");
        publisher.setPubName("O'Reilly Media");
        publisher.setCity("New York");
        publisher.setState("NY");
        publisher.setCountry("USA");

        Publisher savedPublisher = publisherRepository.save(publisher);

        assertNotNull(savedPublisher);
        assertEquals("1389", savedPublisher.getPubId());
        assertEquals("O'Reilly Media", savedPublisher.getPubName());
    }

    @Test
    void testFindPublisherById() {

        Publisher publisher = new Publisher();
        publisher.setPubId("0736");
        publisher.setPubName("Pearson");
        publisher.setCity("London");
        publisher.setState("LN");
        publisher.setCountry("UK");

        publisherRepository.save(publisher);

        Publisher found = publisherRepository.findById("0736").orElse(null);

        assertNotNull(found);
        assertEquals("Pearson", found.getPubName());
    }

    @Test
    void testUpdatePublisher() {

        Publisher publisher = new Publisher();
        publisher.setPubId("0877");
        publisher.setPubName("McGraw Hill");
        publisher.setCity("Chicago");
        publisher.setState("IL");
        publisher.setCountry("USA");

        publisherRepository.save(publisher);

        publisher.setCity("Boston");
        Publisher updated = publisherRepository.save(publisher);

        assertEquals("Boston", updated.getCity());
    }

    @Test
    void testDeletePublisher() {

        Publisher publisher = new Publisher();
        publisher.setPubId("1622");
        publisher.setPubName("Springer");
        publisher.setCity("Berlin");
        publisher.setState("BE");
        publisher.setCountry("Germany");

        publisherRepository.save(publisher);

        publisherRepository.deleteById("1622");

        boolean exists = publisherRepository.findById("1622").isPresent();
        assertFalse(exists);
    }
}