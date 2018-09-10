import java.io.InputStream;
import java.util.Scanner;

public class FSAStreamParser {
	private InputStream stream;
	private FSABuilder builder;

	private String states, alphabet, initSt, finSt, trans;

	private boolean readComplete, parseComplete;

	public FSAStreamParser(InputStream stream) {
		this.stream = stream;
		builder = new FSABuilder();

		readComplete = false;
		parseComplete = false;
	}

	public FSABuilder constructBuilder() {
		if (!parseComplete) {
			throw new IllegalStateException("File was not parsed yet.");
		}
		return builder;
	}

	public void parse() {
		if (!readComplete) {
			throw new IllegalStateException("Wile was not read yet.");
		}

		parseStates(states);
		parseAlphabet(alphabet);
		parseInitialState(initSt);
		parseFinalStates(finSt);
		parseTransitions(trans);

		parseComplete = true;
	}

	public void read() {
		Scanner scanner = new Scanner(stream);
		states = scanner.nextLine();
		alphabet = scanner.nextLine();
		initSt = scanner.nextLine();
		finSt = scanner.nextLine();
		trans = scanner.nextLine();
		scanner.close();

		readComplete = true;
	}

	private void parseStates(String states) {
		String[] parsed = states.substring(8).split("[,}]");
		for (String state : parsed) {
			builder.addState(state);
		}
	}

	private void parseAlphabet(String alphabet) {
		String[] parsed = alphabet.substring(7).split("[,}]");
		for (String alpha : parsed) {
			builder.addAlpha(alpha);
		}
	}

	private void parseInitialState(String initSt) {
		String parsed = initSt.substring(9).split("[,}]")[0];
		builder.setInitialState(parsed);
	}

	private void parseFinalStates(String finSt) {
		String[] parsed = finSt.substring(8).split("[,}]");
		for (String state : parsed) {
			builder.addFinalState(state);
		}
	}

	private void parseTransitions(String trans) {
		String[] parsed = trans.substring(7).split("[,}]");
		for (String transition : parsed) {
			String[] t = transition.split(">");
			builder.addTransition(t[1], t[0], t[2]);
		}
	}
}
