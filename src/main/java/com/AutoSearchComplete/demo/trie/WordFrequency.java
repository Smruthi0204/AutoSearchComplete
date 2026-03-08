package com.AutoSearchComplete.demo.trie;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WordFrequency {
    private String word;
    private int frequency;
}
