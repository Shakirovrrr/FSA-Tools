import java.util.LinkedList;
import java.util.List;

public class FSABuildResult {
	private FiniteStateAutomata automata;
	private List<String> errors;
	private boolean success;

	public FSABuildResult() {
		errors = new LinkedList<>();
		success = false;
		automata = null;
	}

	public boolean successful() {
		return success;
	}

	public List<String> getErrors() {
		return errors;
	}

	public FiniteStateAutomata getAutomata() {
		return automata;
	}
}
