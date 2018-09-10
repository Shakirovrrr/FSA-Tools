import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		FSAStreamParser parser = new FSAStreamParser(new FileInputStream("fsa.txt"));
		parser.read();
		parser.parse();

		FSABuilder builder = parser.constructBuilder();
		System.out.println(builder);
	}
}
