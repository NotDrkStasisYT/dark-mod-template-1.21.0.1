package net.drk.discord;

public enum RichPresenceImage {
    IMAGE_ONE("image_one_key", "Image One Description"),
    IMAGE_TWO("image_two_key", "Image Two Description"),
    IMAGE_THREE("image_three_key", "Image Three Description");

    private final String key;
    private final String description;

    RichPresenceImage(String key, String description) {
        this.key = key;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }
}
