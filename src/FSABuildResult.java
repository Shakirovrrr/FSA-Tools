import java.util.LinkedList;
import java.util.List;

public class FSABuildResult {
	private FiniteStateAutomata automata;
	private List<FSAError> errors;
	private boolean success, complete;

	public FSABuildResult(boolean success, List<FSAError> errors, FiniteStateAutomata automata, boolean complete) {
		this.errors = errors;
		this.success = success;
		this.automata = automata;
		this.complete = complete;
	}

	public FSABuildResult(FSAError error) {
		success = false;
		automata = null;
		errors = new LinkedList<>();
		errors.add(error);
	}

	public boolean successful() {
		return success;
	}

	public boolean isComplete() {
		return complete;
	}

	public List<FSAError> getErrors() {
		return errors;
	}

	public FiniteStateAutomata getAutomata() {
		return automata;
	}

	public enum FSAError {
		E1, E2, E3, E4, E5,
		W1, W2, W3;
	}
}
