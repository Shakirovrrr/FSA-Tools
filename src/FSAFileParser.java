import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FSAFileParser {
	private String fileName;
	private FSABuilder builder;

	private String states, alphabet, initSt, finSt, trans;

	public FSAFileParser(String fileName) {
		this.fileName = fileName;
		builder = new FSABuilder();
	}

	public FSABuilder constructBuilder() {
		return builder;
	}

	public void parseFile() {
		parseStates(states);
		parseAlphabet(alphabet);
		parseInitialState(initSt);
		parseFinalStates(finSt);
		parseTransitions(trans);
	}

	public void readFile() throws FileNotFoundException {
		Scanner scanner = new Scanner(new FileInputStream(fileName));
		states = scanner.nextLine();
		alphabet = scanner.nextLine();
		initSt = scanner.nextLine();
		finSt = scanner.nextLine();
		trans = scanner.nextLine();
		scanner.close();
	}

	private void parseStates(String states) {
		String[] parsed = states.split("(states=\\{|,|})");
		for (String state : parsed) {
			builder.addState(state);
		}
	}

	private void parseAlphabet(String alphabet) {
		String[] parsed = alphabet.split("(alpha=\\{|,|})");
		for (String alpha : parsed) {
			builder.addAlpha(alpha);
		}
	}

	private void parseInitialState(String initSt) {
		String parsed = initSt.split("(init\\.st=\\{|,|})")[0];
		builder.setInitialState(parsed);
	}

	private void parseFinalStates(String finSt) {
		String[] parsed = finSt.split("(fin\\.st=\\{|,|})");
		for (String state : parsed) {
			builder.addFinalState(state);
		}
	}

	private void parseTransitions(String trans) {
		String[] parsed = trans.split("(trans=\\{|,|})");
		for (String transition : parsed) {
			String[] t = transition.split(">");
			builder.addTransition(t[1], t[0], t[2]);
		}
	}
}
