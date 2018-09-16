import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {
		FSAStreamParser parser = new FSAStreamParser(new FileInputStream("fsa.txt"));
		parser.read();
		parser.parse();

		FSABuilder builder = parser.constructBuilder();

		FSABuildResult result = builder.build();
		PrintStream printer = new PrintStream(new FileOutputStream("result.txt"));
		if (result.successful()) {
			printer.print("FSA is ");
			printer.println(result.isComplete() ? "complete" : "incomplete");
			printErrors(result.getErrors(), printer);
		} else {
			printErrors(result.getErrors(), printer);
		}
		printer.close();
	}

	private static void printList(Collection list) {
		for (Object o : list) {
			System.out.print(o + " ");
		}
		System.out.println();
	}

	private static void printErrors(Collection<FSABuildResult.FSAError> errors, PrintStream printStream) {
		for (FSABuildResult.FSAError error : errors) {
			printError(error, printStream);
		}
	}

	private static void printError(FSABuildResult.FSAError error, PrintStream printer) {
		switch (error) {
			case E1:
				printer.println("Error:");
				printer.println("E1: A state s is not in set of states");
				break;
			case E2:
				printer.println("Error:");
				printer.println("E2: Some states are disjoint");
				break;
			case E3:
				printer.println("Error:");
				printer.println("E3: A transition a is not represented in the alphabet");
				break;
			case E4:
				printer.println("Error:");
				printer.println("E4: Initial state is not defined");
				break;
			case E5:
				printer.println("Error:");
				printer.println("E5: Input file is malformed");
				break;
			case W1:
				printer.println("Warning:");
				printer.println("W1: Accepting state is not defined");
				break;
			case W2:
				printer.println("Warning:");
				printer.println("W2: Some states are not reachable from initial state");
				break;
			case W3:
				printer.println("Warning:");
				printer.println("W3: FSA is nondeterministic");
				break;
		}
	}
}
