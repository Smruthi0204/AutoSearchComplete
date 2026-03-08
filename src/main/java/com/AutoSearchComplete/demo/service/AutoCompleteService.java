package com.AutoSearchComplete.demo.service;

import com.AutoSearchComplete.demo.Repository.SearchTermRepository;
import com.AutoSearchComplete.demo.entity.SearchTerm;
import com.AutoSearchComplete.demo.trie.Trie;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AutoCompleteService {

    private final SearchTermRepository repository;
    private final Trie trie;

    @PostConstruct
    public void loadTrieFromDB() {
        repository.findAll()
                .forEach(term ->
                        trie.insert(term.getTerm(), term.getFrequency()));
    }

    public List<String> autocomplete(String prefix) {
        return trie.getTopSuggestions(prefix.toLowerCase(), 5);
    }

    @Transactional
    public void recordSearch(String word) {

        word = word.toLowerCase();

        SearchTerm term = repository.findByTerm(word)
                .orElse(SearchTerm.builder()
                        .term(word)
                        .frequency(0)
                        .build());

        term.setFrequency(term.getFrequency() + 1);
        repository.save(term);

        trie.insert(word, term.getFrequency());
    }
}
