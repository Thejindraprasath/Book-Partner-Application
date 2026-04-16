package com.sprint.Book_Partner_Application.publisher.repository;

import com.sprint.Book_Partner_Application.publisher.entity.Publisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class PublisherRepositoryTest {

    @Autowired
    private PublisherRepository publisherRepository;

    @Test
    void testSavePublisher() {

        // ✅ Create valid Publisher (matches @Pattern)
        Publisher publisher = Publisher.builder()
                .pubId("1389")   // MUST match pattern
                .pubName("O'Reilly Media")
                .city("New York")
                .state("NY")     // exactly 2 chars
                .country("USA")
                .build();

        // ✅ Save
        Publisher savedPublisher = publisherRepository.save(publisher);

        // ✅ Assertions
        assertNotNull(savedPublisher);
        assertEquals("1389", savedPublisher.getPubId());
        assertEquals("O'Reilly Media", savedPublisher.getPubName());
    }

    @Test
    void testFindPublisherById() {

        // Arrange
        Publisher publisher = Publisher.builder()
                .pubId("0736")
                .pubName("Pearson")
                .city("London")
                .state("LN")
                .country("UK")
                .build();

        publisherRepository.save(publisher);

        // Act
        Publisher found = publisherRepository.findById("0736").orElse(null);

        // Assert
        assertNotNull(found);
        assertEquals("Pearson", found.getPubName());
    }

    @Test
    void testUpdatePublisher() {

        // Arrange
        Publisher publisher = Publisher.builder()
                .pubId("0877")
                .pubName("McGraw Hill")
                .city("Chicago")
                .state("IL")
                .country("USA")
                .build();

        publisherRepository.save(publisher);

        // Act
        publisher.setCity("Boston");
        Publisher updated = publisherRepository.save(publisher);

        // Assert
        assertEquals("Boston", updated.getCity());
    }

    @Test
    void testDeletePublisher() {

        // Arrange
        Publisher publisher = Publisher.builder()
                .pubId("1622")
                .pubName("Springer")
                .city("Berlin")
                .state("BE")
                .country("Germany")
                .build();

        publisherRepository.save(publisher);

        // Act
        publisherRepository.deleteById("1622");

        // Assert
        boolean exists = publisherRepository.findById("1622").isPresent();
        assertFalse(exists);
    }
}