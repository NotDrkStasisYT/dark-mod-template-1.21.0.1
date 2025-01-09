package net.drk.command.supportcenter;

public class FeedbackEntry {
    private final String playerName;
    private final String playerMessage;
    private String adminResponse;
    private final long timestamp;

    public FeedbackEntry(String playerName, String playerMessage, long timestamp) {
        this.playerName = playerName;
        this.playerMessage = playerMessage;
        this.timestamp = timestamp;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getPlayerMessage() {
        return playerMessage;
    }

    public String getAdminResponse() {
        return adminResponse;
    }

    public void setAdminResponse(String adminResponse) {
        this.adminResponse = adminResponse;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String formattedMessage() {
        String response = adminResponse != null ? " Admin response: " + adminResponse : "";
        return "Your message: " + playerMessage + response;
    }
}
