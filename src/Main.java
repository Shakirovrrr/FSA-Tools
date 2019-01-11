import FSATools.Build.FSABuildResult;
import FSATools.Build.FSABuilder;
import FSATools.Build.FSAStreamParser;
import FSATools.Foundation.FiniteStateAutomata;
import FSATools.Regex.FSARegexMaker;

import java.io.FileInputStream;
import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		FileInputStream inputStream = new FileInputStream("fsa.txt");
		FSAStreamParser parser = new FSAStreamParser(inputStream);
		FSABuilder builder;
		FiniteStateAutomata automata = null;
		if (parser.read()) {
			if (parser.parse()) {
				builder = parser.constructBuilder();

				FSABuildResult buildResult = builder.build();
				if (buildResult.successful()) {
					automata = buildResult.getAutomata();
				} else {
					buildResult.getErrors().forEach(System.out::println);
				}
			} else {
				System.err.println("Can't parse");
			}
		}

		if (automata != null) {
			System.out.println(FSARegexMaker.makeRegex(automata));
			String[] test = {"turn_off", "turn_off", "turn_on"};
			System.out.println(automata.compute(test));
		}
	}
}
