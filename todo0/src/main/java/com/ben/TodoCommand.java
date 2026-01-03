package com.ben;


import com.ben.repository.TodoRepository;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "todo",
        description = "Todo list CLI",
        subcommands = {CommandLine.HelpCommand.class}
)
public class TodoCommand implements Callable<Integer> {

    private final TodoRepository repo;

    public TodoCommand(TodoRepository repo){
        this.repo = repo;
    }

    @CommandLine.Command(name = "add")
    public void add(@CommandLine.Parameters String title) {
        repo.add(title);
        System.out.println("新增成功！");
    }

    @CommandLine.Command(name = "list")
    public void list() {
        var todos = repo.findAll();
        todos.forEach(todo -> {
            String mark = todo.done() ? "[X]" : "[ ]";
            System.out.println(todo.id() + ". " + mark + " " + todo.title());
        });
    }

    @CommandLine.Command(name = "toggle", description = "toggle by #")
    public void done(@CommandLine.Parameters int id) {
        repo.toggleDone(id, true);
        System.out.println("完成任務！");
    }

    @Override
    public Integer call() {
        System.out.println("use help to check available command");
        return 0;
    }


}
