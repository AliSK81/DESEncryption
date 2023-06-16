package main.implementations.eve;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PBoxFinder {
    public static ArrayList<int[]> findPossiblePBoxes(Map<String, String> outputByInput) {
        int inputLength = outputByInput.keySet().iterator().next().length();

        Map<Integer, List<Integer>> possiblePositions = getPossiblePositions(outputByInput, inputLength);

        List<List<Integer>> pBoxOptions = getPBoxOptions(inputLength, possiblePositions);

        return convertPBoxOptionsToArray(pBoxOptions);
    }

    private static Map<Integer, List<Integer>> getPossiblePositions(Map<String, String> outputByInput, int inputLength) {
        Map<Integer, List<Integer>> possiblePositions = new HashMap<>();
        outputByInput.forEach((input, output) -> {
            for (int i = 0; i < inputLength; i++) {
                char currentChar = output.charAt(i);
                possiblePositions.merge(i, charIndices(currentChar, input), PBoxFinder::intersect);
            }
        });
        return possiblePositions;
    }

    private static List<Integer> charIndices(char c, String str) {
        return IntStream.range(0, str.length())
                .filter(i -> str.charAt(i) == c)
                .boxed()
                .collect(Collectors.toList());
    }

    private static List<Integer> intersect(List<Integer> list1, List<Integer> list2) {
        return list1.stream()
                .filter(list2::contains)
                .collect(Collectors.toList());
    }

    private static List<List<Integer>> getPBoxOptions(int inputLength, Map<Integer, List<Integer>> possiblePositions) {
        List<List<Integer>> pBoxOptions = new ArrayList<>();
        for (int i = 0; i < inputLength; i++) {
            List<Integer> positions = possiblePositions.getOrDefault(i, new ArrayList<>());
            if (positions.isEmpty()) {
                return new ArrayList<>();
            }
            List<List<Integer>> newPBoxOptions = new ArrayList<>();
            for (Integer position : positions) {
                if (pBoxOptions.isEmpty()) {
                    newPBoxOptions.add(new ArrayList<>(Collections.singletonList(position)));
                } else {
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
        return pBoxOptions;
    }

    private static ArrayList<int[]> convertPBoxOptionsToArray(List<List<Integer>> pBoxOptions) {
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