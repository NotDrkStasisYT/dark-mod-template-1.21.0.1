package net.drk;

import net.drk.command.SupportCenterCommandFeedback;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackStorage {
    private static final String FILE_NAME = "feedback_messages.dat";

    public static void saveFeedbackMessages(List<SupportCenterCommandFeedback.FeedbackEntry> messages) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(messages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static List<SupportCenterCommandFeedback.FeedbackEntry> loadFeedbackMessages() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (List<SupportCenterCommandFeedback.FeedbackEntry>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }
}
