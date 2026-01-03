package com.ben.jline0;

import org.jline.builtins.Completers;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.PrintAboveWriter;
import org.jline.reader.impl.completer.AggregateCompleter;
import org.jline.reader.impl.completer.ArgumentCompleter;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.jline.utils.Status;

import java.nio.file.Paths;
import java.util.Collections;

import static org.jline.builtins.Completers.TreeCompleter.node;


/**
 * how to test completer:
 * 1. cd into demo0
 * 2. run:
 * mvn compile exec:java -Dexec.mainClass="com.ben.jline0.Main"
 */

public class Main {


    public static void main(String[] args) {
//        Completer completer = new StringsCompleter("command1", "command2", "help", "quit");

//        Completers.FileNameCompleter completer = new Completers.FileNameCompleter();

//        Completer completer = new Completers.TreeCompleter(
//                node("help", node("commands"), node("syntax")),
//                node(
//                        "set",
//                        node("color", node("red", "green", "blue")),
//                        node("size", node("small", "medium", "large"))));


//        Completer completer = new AggregateCompleter(
//                new StringsCompleter("help", "exit"),
//                new ArgumentCompleter(
//                        new StringsCompleter("open"),
//                        new Completers.FilesCompleter(Paths.get(""))));



        try {
            Terminal terminal = TerminalBuilder.builder().system(true).build();
            LineReader reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .completer(new CustomCompleter())
                    .build();



            // Create a Status instance
            Status status = Status.getStatus(terminal);

            // Start a background thread to update the status
            new Thread(() -> {
                try {
                    int taskCount = 0;
                    while (true) {
                        Thread.sleep(2000);
                        taskCount = (taskCount + 1) % 10;

                        if (status != null) {
                            status.update(Collections.singletonList(new AttributedStringBuilder()
                                    .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE))
                                    .append("Connected to server | ")
                                    .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN))
                                    .append(Integer.toString(taskCount))
                                    .append(" tasks running")
                                    .toAttributedString()));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            })
                    .start();

            while (true) {
                String line = reader.readLine("> ");

                if ("exit".equalsIgnoreCase(line)) {
                    break;
                }

                terminal.writer().println("you entered: " + line);
                terminal.flush();
            }

            terminal.writer().println("bye");
            terminal.flush();
            terminal.close();
        } catch (Exception e) {
            System.err.println("Error creating terminal: " + e.getMessage());
        }
    }
}
