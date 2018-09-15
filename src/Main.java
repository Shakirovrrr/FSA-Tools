import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		FSAStreamParser parser = new FSAStreamParser(new FileInputStream("fsa.txt"));
		parser.read();
		parser.parse();

		FSABuilder builder = parser.constructBuilder();

		FSABuildResult result = builder.build();
		if (result.successful()) {
			System.out.println("Success");
			System.out.println(result.isComplete());
			printList(result.getErrors());
		} else {
			System.out.println("Error");
			printList(result.getErrors());
		}
	}

	private static void printList(Collection list) {
		for (Object o : list) {
			System.out.print(o + " ");
		}
		System.out.println();
	}
}
