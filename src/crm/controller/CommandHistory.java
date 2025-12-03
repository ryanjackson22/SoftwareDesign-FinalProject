package crm.controller;

import crm.controller.command.CRMCommand;

import java.util.Stack;

public class CommandHistory implements CRMCommand {
    private Stack<CRMCommand> history = new Stack<CRMCommand>();


    @Override
    public void execute() {

    }

    @Override
    public void undo() {

    }
}
