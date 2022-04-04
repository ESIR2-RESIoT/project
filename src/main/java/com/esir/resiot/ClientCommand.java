package com.esir.resiot;

public class ClientCommand {

    private String command;
    private String arg;
    public ClientCommand(String command, String arg){
        this.command = command;
        this.arg = arg;
    }

    public String getCommand() {
        return command;
    }

    public String getArg() {
        return arg;
    }
}
