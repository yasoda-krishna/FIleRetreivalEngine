package org.yannapu;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
public class IndexStore {
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> globalIndex = new ConcurrentHashMap<>();

    public void updateIndex(String filePath, Map<String, Integer> localIndex) {
        localIndex.forEach((word, count) ->
                globalIndex.computeIfAbsent(word, k -> new ConcurrentHashMap<>()).merge(filePath, count, Integer::sum));
    }

    public List<String> searchANDQuery(String query) {
        long startTime = System.nanoTime();
        String[] terms = query.split(" and ");
        Map<String, Integer> fileScores = new HashMap<>();

        for (String term : terms) {
            if (globalIndex.containsKey(term)) {
                for (String file : globalIndex.get(term).keySet()) {
                    int termCount = globalIndex.get(term).get(file);
                    fileScores.merge(file, termCount, Integer::sum);
                }
            } else {
                fileScores.clear();
                break;
            }
        }

        Set<String> validFiles = new HashSet<>(fileScores.keySet());
        for (String term : terms) {
            validFiles.retainAll(globalIndex.getOrDefault(term, new ConcurrentHashMap<>()).keySet());
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;
        System.out.println("Search completed in " + duration + " milliseconds.");

        final Set<String> finalValidFiles = validFiles;
        return fileScores.entrySet().stream()
                .filter(entry -> finalValidFiles.contains(entry.getKey()))
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(10)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
