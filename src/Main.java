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
		if (result.successful() && !result.getErrors().contains(FSABuildResult.FSAError.W3)) {
			FSARegexMaker regexMaker = new FSARegexMaker(result.getAutomata());
			printer.print(regexMaker.makeRegex());
		} else {
			printErrors(result, printer);
		}
		printer.close();
	}

	private static void printErrors(FSABuildResult buildResult, PrintStream printStream) {
		for (FSABuildResult.FSAError error : buildResult.getErrors()) {
			printError(error, printStream, buildResult.getNotRepresented());
		}
	}

	private static void printError(FSABuildResult.FSAError error, PrintStream printer, String notRepresented) {
		printer.println("Error:");
		switch (error) {
			case E1:
				printer.printf("E1: A state \'%s\' is not in set of states", notRepresented);
				break;
			case E2:
				printer.print("E2: Some states are disjoint");
				break;
			case E3:
				printer.printf("E3: A transition \'%s\' is not represented in the alphabet", notRepresented);
				break;
			case E4:
				printer.print("E4: Initial state is not defined");
				break;
			case E5:
				printer.print("E5: Input file is malformed");
				break;
			case W3:
				printer.print("\nE6: FSA is nondeterministic");
				break;
		}
	}

	private static void printWarnings(Collection<FSABuildResult.FSAError> warnings, PrintStream printer) {
		printer.print("\nWarning:");
		for (FSABuildResult.FSAError warning : warnings) {
			switch (warning) {
				case W1:
					printer.print("\nW1: Accepting state is not defined");
					break;
				case W2:
					printer.print("\nW2: Some states are not reachable from initial state");
					break;
			}
		}
	}
}
