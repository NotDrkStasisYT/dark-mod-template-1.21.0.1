package net.drk.discord;

import java.time.LocalDate;

public class PlayerStatus {
    private static int streak = 3;
    private static LocalDate lastJoinDate = LocalDate.now().minusDays(1); // Example: starts as yesterday

    public static int getStreak() {
        return streak;
    }

    public static void incrementStreak() {
        streak++;
    }

    public static void resetStreak() {
        streak = 0;
    }

    public static void updateStreak() {
        LocalDate currentDate = LocalDate.now();

        // Check if today is a new day compared to the last join date
        if (currentDate.isAfter(lastJoinDate)) {
            // If the player missed a day, reset the streak
            if (lastJoinDate.plusDays(1).isBefore(currentDate)) {
                resetStreak();
            }
            // Increment the streak since they joined on a new day
            incrementStreak();
            // Update last join date to today
            lastJoinDate = currentDate;
        }
    }
}
