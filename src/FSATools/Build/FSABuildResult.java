package FSATools.Build;

import FSATools.Foundation.FiniteStateAutomata;

import java.util.LinkedList;
import java.util.List;

public class FSABuildResult {
	private FiniteStateAutomata automata;
	private List<FSAError> errors;
	private boolean success;
	private String notRepresentedE15;

	public FSABuildResult(boolean success, List<FSAError> errors, FiniteStateAutomata automata) {
		this.errors = errors;
		this.success = success;
		this.automata = automata;
	}

	public FSABuildResult(FSAError error) {
		success = false;
		automata = null;
		errors = new LinkedList<>();
		errors.add(error);
	}

	public FSABuildResult(FSAError error, String notRepresentedE15) {
		success = false;
		automata = null;
		errors = new LinkedList<>();
		errors.add(error);
		this.notRepresentedE15 = notRepresentedE15;
	}

	public boolean successful() {
		return success;
	}

	public List<FSAError> getErrors() {
		return errors;
	}

	public String getNotRepresented() {
		return notRepresentedE15;
	}

	public FiniteStateAutomata getAutomata() {
		return automata;
	}

	public enum FSAError {
		E1, E2, E3, E4, E5,
		W1, W2, W3;
	}
}
