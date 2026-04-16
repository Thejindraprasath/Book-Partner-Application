package com.sprint.Book_Partner_Application.author.repository;



import com.sprint.Book_Partner_Application.author.entity.TitleAuthor;
import com.sprint.Book_Partner_Application.author.entity.TitleAuthor.TitleAuthorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TitleAuthorRepository extends JpaRepository<TitleAuthor, TitleAuthorId> {
    List<TitleAuthor> findByAuId(String auId);
    List<TitleAuthor> findByTitleId(String titleId);
    void deleteByAuId(String auId);
    void deleteByTitleId(String titleId);
}