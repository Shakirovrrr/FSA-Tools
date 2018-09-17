import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

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
			printer.print(result.isComplete() ? "complete" : "incomplete");
			printErrors(result, printer, "\n");
		} else {
			printErrors(result, printer, "");
		}
		printer.close();
	}

	private static void printErrors(FSABuildResult buildResult, PrintStream printStream, String prefix) {
		for (FSABuildResult.FSAError error : buildResult.getErrors()) {
			printError(error, printStream, prefix, buildResult.getNotRepresented());
		}
	}

	private static void printError(FSABuildResult.FSAError error, PrintStream printer, String prefix, String notRepresented) {
		switch (error) {
			case E1:
				printer.println(prefix + "Error:");
				printer.printf("E1: A state \'%s\' is not in set of states", notRepresented);
				break;
			case E2:
				printer.println(prefix + "Error:");
				printer.print("E2: Some states are disjoint");
				break;
			case E3:
				printer.println(prefix + "Error:");
				printer.printf("E3: A transition \'%s\' is not represented in the alphabet", notRepresented);
				break;
			case E4:
				printer.println(prefix + "Error:");
				printer.print("E4: Initial state is not defined");
				break;
			case E5:
				printer.println(prefix + "Error:");
				printer.print("E5: Input file is malformed");
				break;
			case W1:
				printer.println(prefix + "Warning:");
				printer.print("W1: Accepting state is not defined");
				break;
			case W2:
				printer.println(prefix + "Warning:");
				printer.print("W2: Some states are not reachable from initial state");
				break;
			case W3:
				printer.println(prefix + "Warning:");
				printer.print("W3: FSA is nondeterministic");
				break;
		}
	}
}
