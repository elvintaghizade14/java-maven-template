package module_01.lesson_11.warmup;

import java.util.*;
import java.util.stream.IntStream;

public class warmup_02 {
  public static void main(String[] args) {
    String origin = "Hello World";
    HashMap<Character, List<Integer>> map = new HashMap<>();

    IntStream.range(0, origin.length()).forEach(i -> {
      char c = origin.charAt(i);
      List<Integer> positions = map.getOrDefault(c, new ArrayList<>());
      positions.add(i);
      map.put(c, positions);
    });



    for (int indx = 0; indx < origin.length(); indx++) {
      int finalIndx = indx;
      map.forEach((c, n) -> {
      if (origin.charAt(finalIndx) == c)
        System.out.printf("Letter: '%c', Count: %d, Positions:%s\n", c, n.size(), n);
      });
    }
  }
}