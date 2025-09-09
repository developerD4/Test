package testngExample;

import org.testng.Assert;
import org.testng.annotations.*;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainWithCsvReport {

    private Playwright playwright;
    private Browser browser;
    private Page page;
    private BufferedWriter writer;
    private long startTime;

    @BeforeClass
    public void setupClass() throws IOException {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));

        // Initialize CSV file
        writer = new BufferedWriter(new FileWriter("target/TestResults.csv", false));
        writer.write("TCID,TEST RESULT,TIME TAKEN (in secs.),TEST DESCRIPTION,TIME STAMP\n");
        writer.flush();
    }

    @BeforeMethod
    public void setupTest() {
        page = browser.newPage();
        page.navigate("https://winvinaya.com/");
        startTime = System.currentTimeMillis();
    }

    private void writeCsv(String tcID, String result, String description) {
        try {
            double duration = (System.currentTimeMillis() - startTime) / 1000.0;
            String timestamp = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH).format(new Date());
            writer.write(String.format("%s,%s,%.2f,%s,%s\n", tcID, result, duration, description, timestamp));
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPageTitle() {
        String title = page.title();
        boolean status = title.contains("WinVinaya InfoSystems");
        Assert.assertTrue(status, "Title mismatch!");
        writeCsv("TC001", status ? "PASSED" : "FAILED", "Verify Page Title contains 'WinVinaya InfoSystems'");
    }

    @Test
    public void testRoleBasedLocator() {
        Locator btn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Get Stared"));
        boolean status = btn != null;
        Assert.assertTrue(status, "Button not found!");
        writeCsv("TC002", status ? "PASSED" : "FAILED", "Verify 'Get Started' button is present");
    }

    @AfterMethod
    public void tearDownTest() {
        if (page != null) page.close();
    }

    @AfterClass
    public void tearDownClass() throws IOException {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
        if (writer != null) writer.close();
    }
}
