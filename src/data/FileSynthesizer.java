
/**
 * Author: Austin Patel
 * Project: Handwritten Recognition
 * File Name: FileSynthesizer.java
 * Created: 01/26/17
 */

package data;

import java.io.File;
import java.util.ArrayList;

/**
 * Combines output data files into a single "*.csv" file to facilitate data
 * analysis. Combines the data by putting files together that follow the given parameters.
 */
public class FileSynthesizer {

	public static void main(String[] args) {
		new FileSynthesizer(FileManager.getAbsolutePath("\\OutputData\\"), "BP", "Accuracy");
	}

	public FileSynthesizer(String filePath, String... keywords) {
		File[] allFiles = new File(filePath).listFiles();
		ArrayList<File> desiredFiles = new ArrayList<File>();
		
		for (File file : allFiles) {
			String name = file.getName();
			
			boolean good = true;
			
			for (String keyword : keywords)
				if (!name.contains(keyword))
					good = false;
			
			if (good)
				desiredFiles.add(file);
		}
		
		File[] finalFiles = new File[desiredFiles.size()];
		
		for (int i = 0; i < finalFiles.length; i++)
			finalFiles[i] = desiredFiles.get(i);
		
		synthesizeFiles(finalFiles, filePath);
	}

	/** Combines a set of files into a single one. */
	private void synthesizeFiles(File[] files, String fileDirectory) {
		// For every set of different beginnings
		String fileNameBeginning = files[0].getName().substring(0, 2);

		// Load the data from the files
		String[][] fileContent = new String[files.length][];

		for (int i = 0; i < fileContent.length; i++) {
			File file = files[i];

			fileContent[i] = FileManager
					.readFileContent(file.getAbsolutePath());

			if (fileContent[i].length > 2605)
				System.out.println(files[0].getName());
		}

		// Create the combined content
		String content = "";

		// Add the file names to the content
		for (int i = 0; i < files.length; i++)
			content += files[i].getName().replace(".txt", "") + ',';

		content = content.substring(0, content.length() - 1) + '\n';

		// Find the max number of data values in the files
		int maxSize = 0;
		for (int i = 0; i < fileContent.length; i++) {
			maxSize = (fileContent[i].length > maxSize) ? fileContent[i].length
					: maxSize;
		}

		// Add the data values to the content
		for (int row = 0; row < maxSize; row++) {
			for (int col = 0; col < fileContent.length; col++)
				if (row < fileContent[col].length)
					content += fileContent[col][row] + ',';
				else
					content += ',';

			content = content.substring(0, content.length() - 1) + '\n';
		}

		System.out.println(fileNameBeginning + maxSize);

		FileManager.writeFileContent(
				fileDirectory + "All-" + fileNameBeginning + ".csv", content,
				false);
	}

}
