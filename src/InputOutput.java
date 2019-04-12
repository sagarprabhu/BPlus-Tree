import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class InputOutput {
	
		private static final String RESULT_NOT_FOUND = "Null";

		private static final String OUTPUT_FILENAME = "output_file.txt";

		/**
		 * Main method
		 * @param args Input file name
		 */
		public static void main(String args[]) {

			// Read name of input file from command line argument
			String fileName = args[0];
			File inputFile = new File(fileName);
			try {
				Scanner sc = new Scanner(inputFile);
				// For creating output file
				BufferedWriter bw = openNewFile();
				
				String newLine = sc.nextLine();
				// splitting input file line based on regex
				String[] input = newLine.split("\\(|,|\\)");
				
				BPlusTree tree = new BPlusTree(Integer.parseInt(input[1]));
			
				while (sc.hasNextLine()) {
					newLine = sc.nextLine();
					input = newLine.split("\\(|,|\\)");
					switch (input[0].trim()) {
						case "Insert": {
							tree.insert(Integer.parseInt(input[1].trim()), input[2].trim());
							break;
						}
						case "Delete": {
							tree.delete(Integer.parseInt(input[1].trim()));
							break;
						}
						case "Search": {
							if (input.length == 2) {
								List<String> res = tree.search(Integer.parseInt(input[1].trim()));
								writeSearch(res, bw);
							} 

							// Range Search
							else {
								List<Pair> res = tree.search(Integer.parseInt(input[1].trim()),Integer.parseInt(input[2].trim()));
								writeRangeSearch(res, bw);
							}
							break;
						}
					}

				}
				// closing scanner and buffered writer
				sc.close();
				bw.close();
			} catch (FileNotFoundException e) {
				// LOGGER.severe("File is not found");
				System.out.println("Error: File not found with name: " + fileName);
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("Error: Failed to create new file");
				e.printStackTrace();
			} catch (NumberFormatException e) {	
				System.out.println("Enter valid degree");
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * Open new file to which output has to be written to.
		 *
		 * @return the buffered writer
		 * @throws IOException
		 *             Signals that an I/O exception has occurred.
		 */
		private static BufferedWriter openNewFile() throws IOException {
			// Creating a new file to write output to (output_file.txt)
			File file = new File(OUTPUT_FILENAME);
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			return bw;
		}

		/**
		 * Write search by key result to the output file.
		 *
		 * @param res the list of values to be written
		 * @param bw the BufferedWriter object
		 * @throws IOExceptio Signals that an I/O exception has occurred.
		 */
		private static void writeSearch(List<String> res, BufferedWriter bw) throws IOException {
			String newLine = "";
			if (res == null) {
				// if no values are found for key
				bw.write(RESULT_NOT_FOUND);
			} else {
				// if values are found, write to file in given format
				Iterator<String> valueIterator = res.iterator();
				while (valueIterator.hasNext()) {
					newLine = newLine + valueIterator.next() + ", ";
				}
				bw.write(newLine.substring(0, newLine.length() - 2));
			}
			bw.newLine();

		}

		/**
		 * Write search by keys result to the output file.
		 *
		 * @param res
		 *            the list of key value pairs to be written
		 * @param bw
		 *            the BufferedWriter object
		 * @throws IOException
		 *             Signals that an I/O exception has occurred.
		 */
		private static void writeRangeSearch(List<Pair> res, BufferedWriter bw) throws IOException {
			String newLine = "";
			if (res.isEmpty()) {
				// if no values are found between given keys
				bw.write(RESULT_NOT_FOUND);
			} else {
				// if pairs are found, write to file in given format
				Iterator<Pair> keyIterator = res.iterator();
				Iterator<String> valueIterator;
				Pair key;
				while (keyIterator.hasNext()) {
					key = keyIterator.next();
					valueIterator = key.getValues().iterator();
					while (valueIterator.hasNext()) {
						newLine = newLine + "(" + key.key + "," + valueIterator.next() + "), ";
					}
				}
				bw.write(newLine.substring(0, newLine.length() - 2));
			}
			bw.newLine();

		}

}
