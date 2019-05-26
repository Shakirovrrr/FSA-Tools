package FSATools.Build;

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

	public boolean readDone() {
		return readComplete;
	}

	public boolean parseDone() {
		return parseComplete;
	}

	public void parse() {
		if (!readComplete) {
			throw new IllegalStateException("File was not read yet.");
		}

		/* Use exception catching
		to stop further parsing */
		// TODO Replace exception catching with function returns
		try {
			parseStates(states);
			parseAlphabet(alphabet);
			parseInitialState(initSt);
			parseFinalStates(finSt);
			parseTransitions(trans);
		} catch (MalformedInputException e) {
			builder.invalidateInputFile();
		}

		parseComplete = true;
	}

	public void read() {
		Scanner scanner = new Scanner(stream);
		if (!scanner.hasNextLine()) {
			return;
		}
		states = scanner.nextLine();

		if (!scanner.hasNextLine()) {
			return;
		}
		alphabet = scanner.nextLine();

		if (!scanner.hasNextLine()) {
			return;
		}
		initSt = scanner.nextLine();

		if (!scanner.hasNextLine()) {
			return;
		}
		finSt = scanner.nextLine();

		if (!scanner.hasNextLine()) {
			return;
		}
		trans = scanner.nextLine();

		readComplete = true;
	}

	private void parseStates(String states) throws MalformedInputException {
		//language=RegExp
		validateFormat("states=\\{[A-z0-9,]*}", states);
		String[] parsed = states.substring(8).split("[,}]");
		for (String state : parsed) {
			builder.addState(state);
		}
	}

	private void parseAlphabet(String alphabet) throws MalformedInputException {
		//language=RegExp
		validateFormat("alpha=\\{[A-z0-9,_]*}", alphabet);
		String[] parsed = alphabet.substring(7).split("[,}]");
		for (String alpha : parsed) {
			builder.addAlpha(alpha);
		}
	}

	private void parseInitialState(String initSt) throws MalformedInputException {
		//language=RegExp
		validateFormat("init\\.st=\\{[A-z0-9]*}", initSt);
		String[] parsed = initSt.substring(9).split("[,}]");
		if (parsed.length > 0) {
			builder.setInitialState(parsed[0]);
		}
	}

	private void parseFinalStates(String finSt) throws MalformedInputException {
		//language=RegExp
		validateFormat("fin\\.st=\\{[A-z0-9,]*}", finSt);
		String[] parsed = finSt.substring(8).split("[,}]");
		for (String state : parsed) {
			builder.addFinalState(state);
		}
	}

	private void parseTransitions(String trans) throws MalformedInputException {
		//language=RegExp
		validateFormat("trans=\\{[A-z0-9,>_]*}", trans);
		String[] parsed = trans.substring(7).split("[,}]");
		for (String transition : parsed) {
			String[] t = transition.split(">");
			builder.addTransition(t[1], t[0], t[2]);
		}
	}

	private void validateFormat(String regex, String input) throws MalformedInputException {
		if (!input.matches(regex)) {
			throw new MalformedInputException();
		}
	}

	private class MalformedInputException extends Exception {
		MalformedInputException() {
			// TODO More exception constructors
		}
	}
}
