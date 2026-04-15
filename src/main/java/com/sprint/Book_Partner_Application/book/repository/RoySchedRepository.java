package com.sprint.Book_Partner_Application.book.repository;

import com.sprint.Book_Partner_Application.book.entity.RoySched;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoySchedRepository extends JpaRepository<RoySched, Long> {
    List<RoySched> findByTitle_TitleId(String titleId);
    void deleteByTitle_TitleId(String titleId);
}
