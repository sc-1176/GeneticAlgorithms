package geneticsPackage;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

//This just outputs things to a file
//used this as a reference 
// https://www.homeandlearn.co.uk/java/write_to_textfile.html
public class WriteToFile {
	
	private String path;
	private boolean appendToFile = false;
	
	public WriteToFile(String filePath) {
		path = filePath;
	}
	
	public WriteToFile(String filePath, boolean appendValue) {
		path = filePath;
		appendToFile = appendValue;
	}
	
	public void writeFile(String textLine) throws IOException {
		FileWriter write = new FileWriter(path, appendToFile);
		PrintWriter print_line = new PrintWriter(write);
		
		print_line.printf("%s" + "%n", textLine);
		print_line.close();
	}

}
