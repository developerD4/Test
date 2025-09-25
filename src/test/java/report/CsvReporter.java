package report;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import report.CsvReportWriter;

public class CsvReporter implements ITestListener {

    private CsvReportWriter csvWriter;
    private long startTime;

    @Override
    public void onStart(ITestContext context) {
        try {
            // CSV file path can be changed as needed
            csvWriter = new CsvReportWriter("target/TestResults.csv", "Chromium", "QA");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        startTime = System.currentTimeMillis();
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        writeResult(result, "PASSED");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        writeResult(result, "FAILED");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        writeResult(result, "SKIPPED");
    }

    private void writeResult(ITestResult result, String status) {
        try {
            double duration = (System.currentTimeMillis() - startTime) / 1000.0;
            String tcID = result.getMethod().getMethodName();
            String desc = result.getMethod().getDescription();
            if (desc == null) desc = "No Description";

            csvWriter.writeRow(tcID, desc, status, duration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        try {
            CsvReportWriter.finalizeResult();
            csvWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
