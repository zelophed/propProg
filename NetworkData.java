import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NetworkData {

	private static final long[] characterOccurrences = new long[26];

	public static void main(String[] args) {

		try {
			countCharacters();

		} catch (IOException e) {
			e.printStackTrace();
		}

		saveDataFile();

		AtomicLong charTotal = new AtomicLong();
		Map<Integer, Long> charOccurrences = new HashMap<>();
		for (int i = 0; i < characterOccurrences.length; i++) {
			charOccurrences.put(i, characterOccurrences[i]);
			//System.out.println("character: " + Character.toString(i + 'A') + "   occurrences: " + characterOccurrences[i]);
			//System.out.println(Character.toString(i + 'A') + "	" + characterOccurrences[i]);
		}

		charOccurrences.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(e -> {
			System.out.println(Character.toString(e.getKey() + 'A') + "	" + e.getValue());
			charTotal.addAndGet(e.getValue());
		});

		System.out.println("Total chars: " + charTotal.get());

	}


	private static void saveDataFile() {
		Path readPath = FileSystems.getDefault().getPath("dict", "wordlist_en.txt");
		Path savePath = FileSystems.getDefault().getPath("genie", "networkData4_en.txt");

		try (BufferedWriter writer = Files.newBufferedWriter(savePath, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
			List<String> strings = Files.readAllLines(readPath);

			//writer.write("letter1 letter2 letter3 letter4 letter5" + System.lineSeparator());
			//writer.write("letter1 isVowel1 letter2 isVowel2 letter3 isVowel3 letter4 isVowel4 letter5 isVowel5" + System.lineSeparator());
			writer.write("letter1 isVowel1 letter2 isVowel2 letter3 isVowel3 letter4 isVowel4 letter5 isVowel5 hasLow3Char hasLow5Char" + System.lineSeparator());

			strings.stream().filter(NetworkData::onlyContainsValidChars).forEach(line -> {
				StringBuilder toWrite = new StringBuilder();

				boolean hasLow3Char = false;
				boolean hasLow5Char = false;

				for (int i = 0; i < line.length(); i++) {
					char ch = line.charAt(i);
					toWrite.append(Character.toUpperCase(ch));

					toWrite.append(" ");
					toWrite.append(isVowel(ch) ? "yes" : "no");

					//if (i < line.length() - 1)
					toWrite.append(" ");

					if (isLow3Char(ch))
						hasLow3Char = true;

					if (isLow5Char(ch))
						hasLow5Char = true;

				}

				toWrite.append(hasLow3Char ? "yes" : "no");
				toWrite.append(" ");
				toWrite.append(hasLow5Char ? "yes" : "no");


				toWrite.append(System.lineSeparator());

				try {
					writer.write(toWrite.toString());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	private static void countCharacters() throws IOException {
		Path path = FileSystems.getDefault().getPath("dict", "wordlist_en.txt");

		List<String> strings = Files.readAllLines(path);

		strings.forEach(line -> {
			if (!onlyContainsValidChars(line))
				return;

			for (char c : line.toLowerCase().toCharArray()) {
				int index = c - 'a';
				characterOccurrences[index] += 1;
			}
		});

	}

	public static boolean onlyContainsValidChars(String line) {
		for (char c : line.toLowerCase().toCharArray()) {
			if (c < 'a' || c > 'z')
				return false;
		}
		return true;
	}

	public static boolean isVowel(char ch) {
		String str = "aeiouAEIOU";
		return str.indexOf(ch) != -1;
	}

	public static boolean isLow3Char(char ch) {
		String str = "qxjQXJ";
		return str.indexOf(ch) != -1;
	}

	public static boolean isLow5Char(char ch) {
		String str = "qxjvyQXJVY";
		return str.indexOf(ch) != -1;
	}



}
