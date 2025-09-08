package testngExample;

import org.testng.Assert;
import org.testng.annotations.*;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;

import org.testng.annotations.Listeners;
import report.CsvReporter;

@Listeners(CsvReporter.class)
public class Main {
    private Playwright playwright;
    private Browser browser;
    private Page page;

    @BeforeClass
    public void setupClass() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
    }

    @BeforeMethod
    public void setupTest() {
        page = browser.newPage();
        page.navigate("https://winvinaya.com/");
    }

    @Test
    public void testPageTitle() {
        String title = page.title();
        System.out.println("Page Title: " + title);
        Assert.assertTrue(title.contains("WinVinaya InfoSystems"), "Title mismatch!");
    }

    @Test
    public void testRoleBasedLocator() {
        Locator btn = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Get Started"));
        Assert.assertNotNull(btn, "Button not found!");
        System.out.println("Found Button: " + btn);
    }

    @Test
    public void testTextBasedLocator() {
        page.getByText("CONTACT US").first().click();
        Assert.assertTrue(page.url().contains("contact"), "CONTACT US navigation failed!");
        System.out.println("CONTACT US clicked successfully");
        page.getByPlaceholder("Your name").fill("abc");
        page.getByPlaceholder("Your Email").fill("abc@mail.co");
        System.out.println("Filled name and email fields");
    }

    @Test
    public void testImageAltAttribute() {
        Locator img = page.locator(".css-1pduhan");
        String altText = img.getAttribute("alt");
        Assert.assertNotNull(altText, "Alt attribute missing!");
        System.out.println("Image alt text: " + altText);
    }

    @Test
    public void testCssSelectorBadge() {
        page.waitForSelector(".chakra-badge.css-d9gu5a");
        Locator badge = page.locator(".chakra-badge.css-d9gu5a");
        Assert.assertTrue(badge.textContent().contains("Trusted Technology Consulting Partner"),
                "Badge text mismatch!");
        System.out.println("Badge text: " + badge.textContent());
    }

    @Test
    public void testXPathLocator() {
        Locator textId = page.locator("//*[@id=\"root\"]/header/div/div/div[2]/a[2]");
        Assert.assertEquals(textId.textContent(), "Newsletters", "Newsletter link mismatch!");
        System.out.println("Found text by XPath: " + textId.textContent());
    }

    @Test
    public void testBlogsLinkNavigation() {
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Blogs")).click();
        Locator read = page.locator("//*[@id=\"root\"]/section[2]/div/div[2]/div/a");
        Assert.assertTrue(read.textContent().contains("Read"), "Blogs page not loaded!");
        System.out.println("Blogs page content: " + read.textContent());
    }

    @Test
    public void testPopupOrSameTabNavigation() {
        Locator visitSite = page.locator(".chakra-text.css-mws14h");

        Page popup = null;
        try {
            popup = page.waitForPopup(() -> {
                visitSite.click();
            });
        } catch (Exception e) {
            System.out.println("⚠ No popup, navigation happened in same tab.");
        }

        if (popup != null) {
            System.out.println("Opened in new tab: " + popup.url());
            Assert.assertTrue(popup.title().contains("NammAcademy"), "Popup site title mismatch!");
            popup.close();
        } else {
            System.out.println("Opened in same tab: " + page.url());
            page.navigate("https://winvinaya.com/");
        }

        Assert.assertTrue(page.url().contains("winvinaya"), "Did not return to test site!");
    }

    @AfterMethod
    public void tearDownTest() {
        if (page != null) {
            page.close();
        }
    }

    @AfterClass
    public void tearDownClass() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }
}
