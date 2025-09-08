package report;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CsvReportWriter {

    private BufferedWriter writer;
    private String browser;
    private String environment;
    public static String Result = "FAILED"; // Default

    private static boolean allPassed = true; // Track if all tests passed

    // Constructor: create CSV file and write header
    public CsvReportWriter(String filePath, String browser, String environment) throws IOException {
        this.browser = browser;
        this.environment = environment;

        writer = new BufferedWriter(new FileWriter(filePath, false)); // overwrite old file
        writer.write("Browser/App,Environment,TCID,TEST RESULT,TIME TAKEN (in secs.),TEST DESCRIPTION,TIME STAMP\n");
        writer.flush();
    }

    // Write a single test result row
    public void writeRow(String tcID, String description, String result, double durationInSeconds) throws IOException {
        if (!"PASSED".equalsIgnoreCase(result)) {
            allPassed = false;
        }

        String timestamp = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH).format(new Date());

        String formattedDuration = durationInSeconds <= 0 ? "Less than a min" : String.format("%.2f", durationInSeconds);

        String row = String.format("%s,%s,%s,%s,%s,%s,%s\n",
                browser,
                environment,
                tcID,
                result,
                formattedDuration,
                description,
                timestamp
        );

        writer.write(row);
        writer.flush();
    }

    // Call at the end to finalize overall result
    public static void finalizeResult() {
        Result = allPassed ? "PASSED" : "FAILED";
    }

    public void close() throws IOException {
        if (writer != null) {
            writer.close();
        }
    }
}
