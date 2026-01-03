package com.ben;

import com.ben.repository.TodoRepository;
import org.jline.reader.*;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.DefaultParser;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import picocli.CommandLine;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        TodoCommand todoCommand = new TodoCommand(new TodoRepository());
        CommandLine commandLine = new CommandLine(todoCommand);

        Terminal terminal = TerminalBuilder.terminal();
        LineReader lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .parser(new DefaultParser())
                .completer(new StringsCompleter("list", "add", "markDone", "help", "exit"))
                .build();

        System.out.println("📝 Todo List TUI - Picocli + JLine3");
        System.out.println("use help\n");


        while(true){
            String line;
            try {
                line = lineReader.readLine("> ");
            } catch (UserInterruptException | EndOfFileException e) {
                break;
            }

            if(line.trim().equals("exit")){
                System.out.println("bye!");
                break;
            }

            commandLine.execute(line.split(" "));
        }



    }
}