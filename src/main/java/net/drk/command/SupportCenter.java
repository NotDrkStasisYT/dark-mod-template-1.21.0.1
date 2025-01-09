package net.drk.command;

import java.util.ArrayList;
import java.util.List;

public class SupportCenter {

    private static final List<ReportEntry> reports = new ArrayList<>();

    public static void addReport(ReportEntry report) {
        reports.add(report);
    }

    public static List<ReportEntry> getReports() {
        return new ArrayList<>(reports); // Return a copy to avoid external modification
    }
}
