package event;

public class UnmatchedCommandExceotion extends Exception{
    String notCorrectCommand;

    UnmatchedCommandExceotion(String notCorrectCommand){
        this.notCorrectCommand = notCorrectCommand;
    }
}
