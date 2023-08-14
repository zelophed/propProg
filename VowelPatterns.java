import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class VowelPatterns {

	public static long[] patternCount = new long[32];

	public static void main(String[] args) {
		countPatterns();

		Map<Integer, Long> patternCountMap = new HashMap<>();

		for (int pattern = 0; pattern < patternCount.length; pattern++) {
			patternCountMap.put(pattern, patternCount[pattern]);
			System.out.println(patternToString(pattern, patternCount[pattern]));
		}

		System.out.println();
		System.out.println();

		patternCountMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEach(e -> System.out.println(patternToString(e.getKey(), e.getValue())));

	}


	private static String patternToString(int pattern, long count) {
		StringBuilder toPrint = new StringBuilder();

		for (int i = 0; i < 5; i++) {
			toPrint.append(((pattern >> i) & 0b1) == 0b1 ? "v " : "c ");
		}

		toPrint.append(": ").append(count);
		return toPrint.toString();
	}

	private static void countPatterns() {
		Path readPath = FileSystems.getDefault().getPath("dict", "wordlist1.txt");

		try {
			List<String> strings = Files.readAllLines(readPath);

			strings.stream().filter(NetworkData::onlyContainsValidChars).forEach(line -> {
				int patternIndex = 0;

				for (int i = 0; i < line.length(); i++) {
					char ch = line.charAt(i);

					if (!NetworkData.isVowel(ch))
						continue;

					patternIndex |= (0b1 << i);

				}

				patternCount[patternIndex] += 1;
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
