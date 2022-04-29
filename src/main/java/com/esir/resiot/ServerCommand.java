package com.esir.resiot;

public class ServerCommand {
    private String command;
    private Object args;

    public ServerCommand(String command, Object args){
        this.command = command;
        this.args = args;
    }
}
