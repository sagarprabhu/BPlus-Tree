import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
/**
 * 
 * @author sagar prabhu
 *
 */
public class bplustree{
	
		private static final String SEARCH_FAILED = "Null";

		private static final String OUTPUT_FILENAME = "output_file.txt";

		/**
		 * Main method
		 * @param args Input file name
		 */
		public static void main(String args[]) {

			// Read name of input file from the command line argument
			File inputFile = new File(args[0]);
			try {
				Scanner sc = new Scanner(inputFile);
				
				// Creating a new file to write output to (output_file.txt)
				File file = new File(OUTPUT_FILENAME);
				BufferedWriter bw = new BufferedWriter(new FileWriter(file));

				
				String line = sc.nextLine();
				// splitting input file line based on regex
				String[] input = line.split("\\(|,|\\)");
				
				BPlusSearchTree tree = new BPlusSearchTree(Integer.parseInt(input[1]));
			
				while (sc.hasNextLine()) {
					line = sc.nextLine();
					input = line.split("\\(|,|\\)");
			//		System.out.println(input[0]);
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
							List<String> res;
							if (input.length == 2) 
								res = tree.search(Integer.parseInt(input[1].trim()));
							// Range Search
							else 
								res = tree.search(Integer.parseInt(input[1].trim()),Integer.parseInt(input[2].trim()));
							writeSearch(res, bw);
							
							break;
						}
					}

				}
				// closing buffered writer and scanner
				bw.close();
				sc.close();
			} catch (FileNotFoundException e) {
				System.out.println("Error: File with name: "+args[0]+" does not exist");
			} catch (IOException e) {
				System.out.println("Error: Failed to create new file");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/**
		 * Write search result to the output file.
		 *
		 * @param res the list of values to be written
		 * @param bw the BufferedWriter object
		 * @throws IOException 
		 */
		private static void writeSearch(List<String> result, BufferedWriter bw) throws IOException {
			String newLine = "";
			if (result != null){
				// if values are found, write to file in given format
				Iterator<String> itr = result.iterator();
				while (itr.hasNext()) {
					newLine = newLine + itr.next() + ",";
				}
				bw.write(newLine.substring(0, newLine.length() -1));
			}else{
				// if no values are found for key
				bw.write(SEARCH_FAILED);
			}
			bw.newLine();

		}
}
