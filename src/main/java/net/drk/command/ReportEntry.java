package net.drk.command;

public class ReportEntry {

    private final String playerName;
    private final String issue;

    public ReportEntry(String playerName, String issue) {
        this.playerName = playerName;
        this.issue = issue;
    }

    public String formattedMessage() {
        return "Player: " + playerName + " - Issue: " + issue;
    }
}
