class MessageEvent {
    private Client sender;
    private Command command;
    private String body;

    MessageEvent(Client sender, Command command, String body) {
        this.sender = sender;
        this.command = command;
        this.body = body;
    }

    Client getClient() {
        return sender;
    }

    Command getCommand() {
        return command;
    }

    String getBody() {
        return body;
    }
}
