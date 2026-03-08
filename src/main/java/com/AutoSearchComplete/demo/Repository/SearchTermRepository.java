package com.AutoSearchComplete.demo.Repository;

import com.AutoSearchComplete.demo.entity.SearchTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SearchTermRepository extends JpaRepository<SearchTerm, Long> {

    Optional<SearchTerm> findByTerm(String term);
}
