public class FSATransition {
	private FSAState from, to;
	private String alpha;

	public FSATransition(String alpha, FSAState from, FSAState to) {
		this.from = from;
		this.to = to;
		this.alpha = alpha;
	}

	public FSAState from() {
		return from;
	}

	public FSAState to() {
		return to;
	}

	public String alpha() {
		return alpha;
	}
}
