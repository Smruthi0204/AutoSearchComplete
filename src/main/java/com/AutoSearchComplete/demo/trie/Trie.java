package com.AutoSearchComplete.demo.trie;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Trie {

    private final TrieNode root = new TrieNode();

    public void insert(String word, int frequency) {
        TrieNode current = root;

        for (char c : word.toCharArray()) {
            current = current.getChildren()
                    .computeIfAbsent(c, k -> new TrieNode());
        }

        current.setEnd(true);
        current.setFrequency(frequency);
    }

    public List<String> getTopSuggestions(String prefix, int limit) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {
            node = node.getChildren().get(c);
            if (node == null) {
                return Collections.emptyList();
            }
        }

        PriorityQueue<WordFrequency> minHeap =
                new PriorityQueue<>(Comparator.comparingInt(WordFrequency::getFrequency));

        dfs(node, new StringBuilder(prefix), minHeap, limit);

        List<String> result = new ArrayList<>();
        while (!minHeap.isEmpty()) {
            result.add(minHeap.poll().getWord());
        }

        Collections.reverse(result);
        return result;
    }

    private void dfs(
            TrieNode node,
            StringBuilder word,
            PriorityQueue<WordFrequency> minHeap,
            int limit
    ) {

        if (node.isEnd()) {
            minHeap.offer(new WordFrequency(word.toString(), node.getFrequency()));
            if (minHeap.size() > limit) {
                minHeap.poll();
            }
        }

        for (Map.Entry<Character, TrieNode> entry : node.getChildren().entrySet()) {
            word.append(entry.getKey());
            dfs(entry.getValue(), word, minHeap, limit);
            word.deleteCharAt(word.length() - 1);
        }
    }
}
