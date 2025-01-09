package net.drk.discord;

public class PlayerRichPresenceConfig {
    private String largeImageKey;
    private String smallImageKey;

    public PlayerRichPresenceConfig(String largeImageKey, String smallImageKey) {
        this.largeImageKey = largeImageKey;
        this.smallImageKey = smallImageKey;
    }

    public String getLargeImageKey() {
        return largeImageKey;
    }

    public String getSmallImageKey() {
        return smallImageKey;
    }

    public void setLargeImageKey(String largeImageKey) {
        this.largeImageKey = largeImageKey;
    }

    public void setSmallImageKey(String smallImageKey) {
        this.smallImageKey = smallImageKey;
    }
}
