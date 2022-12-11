package ru.alexksysx.coffeelang.repl;

import ru.alexksysx.coffeelang.exception.AnalyzeException;
import ru.alexksysx.coffeelang.exception.CoffeeRuntimeException;
import ru.alexksysx.coffeelang.operator.IOperator;
import ru.alexksysx.coffeelang.parser.Parser;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Repl {
    private final Parser parser;
    private static final String PROMPT = ">> ";

    public Repl() {
        parser = new Parser();
    }

    public void start(OutputStream out, InputStream in) {
        PrintStream stream = new PrintStream(out, true);
        try (Scanner scanner = new Scanner(in)) {
            while (true) {
                stream.print(PROMPT);
                if (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.equalsIgnoreCase("выход"))
                        break;
                    runCommand(line, stream);
                }
            }
        }
    }

    public void startFile(OutputStream out, InputStream in) {
        PrintStream stream = new PrintStream(out, true);
        try (Scanner scanner = new Scanner(in)) {
            while (scanner.hasNextLine()) {
                stream.print(PROMPT);
                String line = scanner.nextLine();
                runCommand(line, stream);
            }
        }
    }

    private void runCommand(String line, PrintStream out) {
        try {
            IOperator operator = parser.parseLine(line);
            if (operator != null)
                operator.run(out);
        } catch (AnalyzeException e) {
            out.println(e.getMessage());
            out.println("Строка с ошибкой: " + e.getCodeLine());
            out.println("Ошибка на токене: " + e.getToken().toString());
        } catch (CoffeeRuntimeException e) {
            out.println(e.getMessage());
        }
    }
}
