import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Wordlist {

	public static void main(String[] args) {

		Path readPath = FileSystems.getDefault().getPath("dict", "german.dic");
		Path savePath = FileSystems.getDefault().getPath("dict", "wordlist1.txt");

		try (BufferedWriter writer = Files.newBufferedWriter(savePath, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
			try (BufferedReader reader = Files.newBufferedReader(readPath, StandardCharsets.ISO_8859_1)) {
				String line;

				while ((line = reader.readLine()) != null) {

					if (line.length() != 5)
						continue;

					writer.write(line + System.lineSeparator());

				}

			}


		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
