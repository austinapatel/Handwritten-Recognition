
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: FileManager.java
 * Created: 01/02/17
 */

package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

/** Manages file input and output */
public class FileManager {

	public FileManager() {

	}

	public static String[] readFileContent(String filePath) {
		filePath = getAbsolutePath(filePath);
		File file = new File(filePath);
		
		if (!file.exists())
			return new String[0];

		try {
			Scanner fileInput = new Scanner(file);

			int numLines = 0;
			while (fileInput.hasNextLine()) {
				fileInput.nextLine();
				numLines++;
			}

			fileInput.close();
			fileInput = new Scanner(new File(filePath));

			String[] fileData = new String[numLines];
			int currentLine = 0;
			while (fileInput.hasNextLine())
				fileData[currentLine++] = fileInput.nextLine();

			fileInput.close();

			return fileData;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String getAbsolutePath(String filePath) {
		return System.getProperty("user.dir")
				+ filePath.replace(System.getProperty("user.dir"), "");
	}

	public static void writeFileContent(String filePath, String content,
			boolean append) {
		filePath = getAbsolutePath(filePath);

		int length = FileManager.readFileContent(filePath).length;

		if (length == 0)
			append = false;

		try {
			File file = new File(filePath);
			if (!file.exists())
				file.createNewFile();
			else if (file.exists() && !append) {
				file.delete();
				file.createNewFile();
			}
			else if (append)
				content = "\n" + content;

			StandardOpenOption writeOption = (append)
					? StandardOpenOption.APPEND : StandardOpenOption.CREATE;
			Files.write(Paths.get(filePath), content.getBytes(), writeOption);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void clearFile(String filePath) {
		FileManager.deleteFile(getAbsolutePath(filePath));
		FileManager.createFile(getAbsolutePath(filePath));
	}

	public static void deleteFile(String filePath) {
		File file = new File(getAbsolutePath(filePath));

		if (file.exists())
			file.delete();
		else
			new Exception("Tried to delete a file that doesn't exist.")
					.printStackTrace();
	}

	public static void createFile(String filePath) {
		File file = new File(getAbsolutePath(filePath));

		try {
			if (!file.exists())
				file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void deleteLine(String filePath, int line) {
		line -= 1;

		String[] fileContent = FileManager.readFileContent(filePath);

		FileManager.clearFile(filePath);

		for (int i = 0; i < fileContent.length; i++)
			if (i != line)
				FileManager.writeFileContent(filePath, fileContent[i], true);
	}

}
