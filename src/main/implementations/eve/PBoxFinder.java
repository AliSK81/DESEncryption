package main.implementations.eve;

import java.util.*;
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

    public static ArrayList<int[]> findPossiblePBoxes(Map<String, String> outputByInput) {
        int inputLength = outputByInput.keySet().iterator().next().length();

        // Create a map of possible positions for each bit
        Map<Integer, List<Integer>> possiblePositions = new HashMap<>();
        outputByInput.forEach((input, output) -> {
            for (int i = 0; i < inputLength; i++) {
                char currentChar = output.charAt(i);
                possiblePositions.merge(i, charIndices(currentChar, input), PBoxFinder::intersect);
            }
        });


        // Find all valid pBox options
        List<List<Integer>> pBoxOptions = new ArrayList<>();
        for (int i = 0; i < inputLength; i++) {
            List<Integer> positions = possiblePositions.getOrDefault(i, new ArrayList<>());
            if (positions.isEmpty()) {
                // If no positions were found, return an empty list to indicate that no valid pBox options can be generated
                return new ArrayList<>();
            }
            List<List<Integer>> newPBoxOptions = new ArrayList<>();
            for (Integer position : positions) {
                if (pBoxOptions.isEmpty()) {
                    // If this is the first bit, add a new option for each position
                    newPBoxOptions.add(new ArrayList<>(Collections.singletonList(position)));
                } else {
                    // Otherwise, add the position to each existing option
                    for (List<Integer> pBoxOption : pBoxOptions) {
                        if (!pBoxOption.contains(position)) {
                            List<Integer> newPBoxOption = new ArrayList<>(pBoxOption);
                            newPBoxOption.add(position);
                            newPBoxOptions.add(newPBoxOption);
                        }
                    }
                }
            }
            pBoxOptions = newPBoxOptions;
        }

        ArrayList<int[]> pBoxOptionsArray = new ArrayList<>();
        for (List<Integer> pBoxOption : pBoxOptions) {
            int[] pBoxOptionArray = new int[pBoxOption.size()];
            for (int i = 0; i < pBoxOption.size(); i++) {
                pBoxOptionArray[i] = pBoxOption.get(i) + 1;
            }
            pBoxOptionsArray.add(pBoxOptionArray);
        }

        return pBoxOptionsArray;
    }
}