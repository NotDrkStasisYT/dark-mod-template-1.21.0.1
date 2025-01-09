package net.drk.discord;

import java.util.Timer;
import java.util.TimerTask;

public class DiscordPresenceTicker {

    private Timer timer;
    private String playerId;

    public DiscordPresenceTicker(String playerId) {
        this.playerId = playerId;
    }

    // Start the timer that ticks every second
    public void start() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                tick();
            }
        }, 0, 1000); // 1000 milliseconds = 1 second
    }

    // Stop the timer when no longer needed
    public void stop() {
        if (timer != null) {
            timer.cancel();
        }
    }

    // This method gets called every second
    private void tick() {
        DiscordRichPresenceManager manager = DiscordRichPresenceManager.getManagerForPlayer(playerId);
        if (manager != null) {
            // Example: Update presence with placeholder data
            int currentSize = 1;               // Current number of party members
            int maxSize = 5;
            int streak = PlayerStatus.getStreak(); // Retrieve the current streak dynamically


            String state = currentSize + " of " + maxSize + "\t" + "⚡ " + streak + "x Streak "; // This would dynamically reflect player status
            String details = getCurrentDetails();      // This could be updated based on the player's activity
            String partyId = "party123";       // Placeholder party information
                // Maximum party size

            manager.updatePresence(state, details, partyId, currentSize, maxSize);
        } else {
            System.out.println("No manager found for player: " + playerId);

        }


    }

    private String getCurrentDetails() {
        // Implement logic to determine current details (e.g., activity)
        return "Debugging"; // Replace with actual logic
    }

}
