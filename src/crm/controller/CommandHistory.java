package crm.controller;

import crm.controller.command.Command;

import java.util.Stack;

public class CommandHistory implements Command {
    private Stack<Command> history = new Stack<Command>();


    @Override
    public void execute() {

    }

    @Override
    public void undo() {

    }
}
