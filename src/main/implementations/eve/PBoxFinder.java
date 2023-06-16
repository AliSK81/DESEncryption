package main.implementations.eve;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PBoxFinder {

    public static List<Integer> charIndices(char c, String str) {
        return IntStream.range(0, str.length())
                .filter(i -> str.charAt(i) == c)
                .boxed()
                .collect(Collectors.toList());
    }

    public static List<Integer> intersect(List<Integer> list1, List<Integer> list2) {
        return list1.stream()
                .filter(list2::contains)
                .collect(Collectors.toList());
    }

    public static List<Integer> findPBox(Map<String, String> outputByInput) {
        int inputLength = outputByInput.keySet().iterator().next().length();

        // Create a map of possible positions for each bit
        Map<Integer, List<Integer>> possiblePositions = new HashMap<>();
        outputByInput.forEach((input, output) -> {
            for (int i = 0; i < inputLength; i++) {
                char currentChar = input.charAt(i);
                possiblePositions.merge(i, charIndices(currentChar, output), PBoxFinder::intersect);
            }
        });

        System.out.println(possiblePositions);

        // Find the real position of each bit
        List<Integer> pBox = new ArrayList<>(inputLength);
        for (int i = 0; i < inputLength; i++) {
            List<Integer> positions = possiblePositions.getOrDefault(i, new ArrayList<>());
            pBox.add(positions.size() == 1 ? positions.get(0) : -1);
        }

        return pBox;
    }
}