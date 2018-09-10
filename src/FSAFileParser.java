import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FSAFileParser {
	private Scanner scanner;
	private String fileName;
	private FSABuilder builder;

	public FSAFileParser(String fileName){
		this.fileName = fileName;
		builder = new FSABuilder();
	}

	public FSABuilder constructBuilder() throws FileNotFoundException {
		scanner = new Scanner(new FileInputStream(fileName));
		String states = scanner.nextLine();
		String alphabet = scanner.nextLine();
		String initSt = scanner.nextLine();
		String finSt = scanner.nextLine();
		String trans = scanner.nextLine();
		scanner.close();

		return null;
	}

	private void parseStates(String states){
		String[] parsed = states.split("(states=\\{|\\,|\\})");
		for (String state : parsed) {
			builder.addState(state);
		}
	}

	private void parseAlphabet(String alphabet){
		String[] parsed = alphabet.split("(alpha=\\{|\\,|\\})");
		for (String alpha : parsed) {
			builder.addAlpha(alpha);
		}
	}
}
