package org.toj.d6g.dicedistribution;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class D6CombinationDistribution {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Map<String, List<String>> results = new HashMap<String, List<String>>();
        for (int i = 3; i < 7; i++) {
            int numberOfDice = i;

            List<List<String>> resultSet = new LinkedList<List<String>>();
            initialize(resultSet);
            while (resultSet.get(0).size() < numberOfDice) {
                resultSet = add1MoreDiceToResultSet(resultSet);
            }
            System.out.println("calculating distribution for " + numberOfDice
                    + " dice, total " + resultSet.size()
                    + " results genereted.");
            System.out.println("Chance for 1 hit(5/6): "
                    + chanceForNumberOfHits(createClone(resultSet), 1) * 100
                    + "%");
            System.out.println("Chance for 2 hit(5/6 + 5/6): "
                    + chanceForNumberOfHits(createClone(resultSet), 2) * 100
                    + "%");
            System.out.println("Chance for 3 hit(5/6 + 5/6 + 5/6): "
                    + chanceForNumberOfHits(createClone(resultSet), 3) * 100
                    + "%");
            System.out.println("Chance for any pairs(n+n): "
                    + chanceForAnyNumberOccurance(createClone(resultSet), 2)
                    * 100 + "%");
            System.out.println("Chance for any triples(n+n+n): "
                    + chanceForAnyNumberOccurance(createClone(resultSet), 3)
                    * 100 + "%");
            System.out.println("Chance for specific number(6): "
                    + chanceForSpecificNumberOccurance(createClone(resultSet),
                            1) * 100 + "%");
            System.out.println("Chance for specific pairs(6+6): "
                    + chanceForSpecificNumberOccurance(createClone(resultSet),
                            2) * 100 + "%");
            System.out.println("Chance for specific triples(6+6+6): "
                    + chanceForSpecificNumberOccurance(createClone(resultSet),
                            3) * 100 + "%");
            System.out.println("Chance for 2 specific numbers(5+6): "
                    + chanceForSpecificNumberSeries(createClone(resultSet), 2)
                    * 100 + "%");
            System.out.println("Chance for 3 specific numbers(4+5+6): "
                    + chanceForSpecificNumberSeries(createClone(resultSet), 3)
                    * 100 + "%");
            System.out
                    .println("Chance for 2 consecutive numbers(1+2/2+3/...): "
                            + chanceForConsecutiveNumbers(
                                    createClone(resultSet), 2) * 100 + "%");
            System.out
                    .println("Chance for 3 consecutive numbers(1+2+3/2+3+4/...): "
                            + chanceForConsecutiveNumbers(
                                    createClone(resultSet), 3) * 100 + "%");
            System.out
                    .println("Chance for a pair plus a specific number(n+n+6): "
                            + chanceForAnyPairPlusSpecificNumber(createClone(resultSet))
                            * 100 + "%");
            // System.out.println("Chance for k3 total equal or greater than 14: "
            // + chanceForTotalGreaterThan(createClone(resultSet), 14)
            // * 100 + "%");
            // System.out.println("Chance for k3 total equal or greater than 15: "
            // + chanceForTotalGreaterThan(createClone(resultSet), 15)
            // * 100 + "%");
            // System.out.println("Chance for k3 total equal or greater than 16: "
            // + chanceForTotalGreaterThan(createClone(resultSet), 16)
            // * 100 + "%");
            // System.out.println("Chance for k3 total equal or greater than 17: "
            // + chanceForTotalGreaterThan(createClone(resultSet), 17)
            // * 100 + "%");
            // System.out.println("Chance for k3 total equal or greater than 18: "
            // + chanceForTotalGreaterThan(createClone(resultSet), 18)
            // * 100 + "%");
            System.out.println();

            getList(results, "Chance for 1 hit(5/6): ").add(
                    MessageFormat.format("{0,number,#.##%}",
                            chanceForNumberOfHits(createClone(resultSet), 1)));
            getList(results, "Chance for 2 hit(5/6 + 5/6): ").add(
                    MessageFormat.format("{0,number,#.##%}",
                            chanceForNumberOfHits(createClone(resultSet), 2)));
            getList(results, "Chance for 3 hit(5/6 + 5/6 + 5/6): ").add(
                    MessageFormat.format("{0,number,#.##%}",
                            chanceForNumberOfHits(createClone(resultSet), 3)));
            getList(results, "Chance for any pairs(n+n): ").add(
                    MessageFormat.format(
                            "{0,number,#.##%}",
                            chanceForAnyNumberOccurance(createClone(resultSet),
                                    2)));
            getList(results, "Chance for any triples(n+n+n): ").add(
                    MessageFormat.format(
                            "{0,number,#.##%}",
                            chanceForAnyNumberOccurance(createClone(resultSet),
                                    3)));
            getList(results, "Chance for specific number(6): ").add(
                    MessageFormat.format(
                            "{0,number,#.##%}",
                            chanceForSpecificNumberOccurance(
                                    createClone(resultSet), 1)));
            getList(results, "Chance for specific pairs(6+6): ").add(
                    MessageFormat.format(
                            "{0,number,#.##%}",
                            chanceForSpecificNumberOccurance(
                                    createClone(resultSet), 2)));
            getList(results, "Chance for specific triples(6+6+6): ").add(
                    MessageFormat.format(
                            "{0,number,#.##%}",
                            chanceForSpecificNumberOccurance(
                                    createClone(resultSet), 3)));
            getList(results, "Chance for 2 specific numbers(5+6): ").add(
                    MessageFormat.format(
                            "{0,number,#.##%}",
                            chanceForSpecificNumberSeries(
                                    createClone(resultSet), 2)));
            getList(results, "Chance for 3 specific numbers(4+5+6): ").add(
                    MessageFormat.format(
                            "{0,number,#.##%}",
                            chanceForSpecificNumberSeries(
                                    createClone(resultSet), 3)));
            getList(results, "Chance for 2 consecutive numbers(1+2/2+3/...): ")
                    .add(MessageFormat.format(
                            "{0,number,#.##%}",
                            chanceForConsecutiveNumbers(createClone(resultSet),
                                    2)));
            getList(results,
                    "Chance for 3 consecutive numbers(1+2+3/2+3+4/...): ").add(
                    MessageFormat.format(
                            "{0,number,#.##%}",
                            chanceForConsecutiveNumbers(createClone(resultSet),
                                    3)));
            getList(results,
                    "Chance for a pair plus a specific number(n+n+6): ")
                    .add(MessageFormat
                            .format("{0,number,#.##%}",
                                    chanceForAnyPairPlusSpecificNumber(createClone(resultSet))));
        }

        System.out.println(printAsTable(results));
    }

    private static double chanceForConsecutiveNumbers(
            List<List<String>> resultSet, int consecutiveThreshold) {
        int count = 0;
        int total = resultSet.size();

        for (List<String> r : resultSet) {
            Set<String> set = new HashSet<String>(r);
            List<String> result = new ArrayList<String>(set);
            Collections.sort(result);

            for (int i = 0; i <= result.size() - consecutiveThreshold; i++) {
                int base = 0;
                boolean hit = true;
                for (int j = 0; j < consecutiveThreshold; j++) {
                    if (j == 0) {
                        base = toInt(result.get(i));
                    } else {
                        if (base + j != toInt(result.get(i + j))) {
                            hit = false;
                        }
                    }
                }
                if (hit) {
                    count++;
                    break;
                }
            }
        }

        return ((double) count) / total;
    }

    private static double chanceForSpecificNumberSeries(
            List<List<String>> resultSet, int lengthOfSeries) {
        int count = 0;
        int total = resultSet.size();

        String[] numbers = { "6", "5", "4", "3", "2", "1" };

        for (List<String> result : resultSet) {
            boolean hit = true;
            for (int i = 0; i < lengthOfSeries; i++) {
                if (!result.contains(numbers[i])) {
                    hit = false;
                }
            }
            if (hit) {
                count++;
            }
        }

        return ((double) count) / total;
    }

    private static double chanceForNumberOfHits(List<List<String>> resultSet,
            int hitsThreshold) {
        int count = 0;
        int total = resultSet.size();

        for (List<String> result : resultSet) {
            int hits = 0;
            for (String die : result) {
                if (toInt(die) > 4) {
                    hits++;
                }
            }
            if (hits >= hitsThreshold) {
                count++;
            }
        }

        return ((double) count) / total;
    }

    private static int toInt(String die) {
        return Integer.parseInt(die);
    }

    private static double chanceForSpecificNumberOccurance(
            List<List<String>> resultSet, int Occurance) {
        int count = 0;
        int total = resultSet.size();

        for (List<String> result : resultSet) {
            if (Collections.frequency(result, "6") >= Occurance) {
                count++;
            }
        }

        return ((double) count) / total;
    }

    private static double chanceForAnyNumberOccurance(
            List<List<String>> resultSet, int Occurance) {
        int count = 0;
        int total = resultSet.size();

        for (List<String> result : resultSet) {
            for (String die : result) {
                if (Collections.frequency(result, die) >= Occurance) {
                    count++;
                    break;
                }
            }
        }

        return ((double) count) / total;
    }

    private static double chanceForAnyPairPlusSpecificNumber(
            List<List<String>> resultSet) {
        int count = 0;
        int total = resultSet.size();

        for (List<String> oriResult : resultSet) {
            List<String> result = createClone(oriResult);
            for (String die : result) {
                if (Collections.frequency(result, die) >= 2) {
                    result.remove(die);
                    result.remove(die);
                    break;
                }
            }
            if (result.contains("6")) {
                count++;
            }
        }

        return ((double) count) / total;
    }

    private static List<List<String>> add1MoreDiceToResultSet(
            List<List<String>> resultSet) {
        List<List<String>> newResultSet = new LinkedList<List<String>>();
        for (List<String> prevResult : resultSet) {
            newResultSet.add(createCloneWithAdditionalElement(prevResult, "1"));
            newResultSet.add(createCloneWithAdditionalElement(prevResult, "2"));
            newResultSet.add(createCloneWithAdditionalElement(prevResult, "3"));
            newResultSet.add(createCloneWithAdditionalElement(prevResult, "4"));
            newResultSet.add(createCloneWithAdditionalElement(prevResult, "5"));
            newResultSet.add(createCloneWithAdditionalElement(prevResult, "6"));
        }
        return newResultSet;
    }

    private static <T> List<T> createCloneWithAdditionalElement(List<T> list,
            T newElement) {
        List<T> newList = createClone(list);
        newList.add(newElement);
        return newList;
    }

    private static <T> List<T> createClone(List<T> list) {
        List<T> newList = new LinkedList<T>();
        newList.addAll(list);
        return newList;
    }

    private static void initialize(List<List<String>> resultSet) {
        resultSet.add(Arrays.asList("1"));
        resultSet.add(Arrays.asList("2"));
        resultSet.add(Arrays.asList("3"));
        resultSet.add(Arrays.asList("4"));
        resultSet.add(Arrays.asList("5"));
        resultSet.add(Arrays.asList("6"));
    }

    private static String printAsTable(Map<String, List<String>> results) {
        StringBuilder sb = new StringBuilder(wrapWith(
                "[td][/td][td]3d6[/td][td]4d6[/td][td]5d6[/td][td]6d6[/td]",
                "tr"));
        for (String key : results.keySet()) {
            sb.append(buildRow(key, results.get(key)));
        }
        return wrapWith(sb.toString(), "table");
    }

    private static String buildRow(String key, List<String> results) {
        StringBuilder sb = new StringBuilder(wrapWith(key + "　", "td"));
        for (String value : results) {
            sb.append(wrapWith(value + "　", "td"));
        }
        return wrapWith(sb.toString(), "tr") + "\n";
    }

    private static String wrapWith(String content, String tag) {
        return "[" + tag + "]" + content + "[/" + tag + "]";
    }

    private static List<String> getList(Map<String, List<String>> map,
            String key) {
        if (!map.containsKey(key)) {
            map.put(key, new ArrayList<String>(5));
        }
        return map.get(key);
    }
}
