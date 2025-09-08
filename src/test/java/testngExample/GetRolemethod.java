package testngExample;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;

public class GetRolemethod {
	Playwright play;
	Browser brows;
	Page pg;
	
	@BeforeClass
	public void beforeClass() {
		play = Playwright.create();
		brows = play.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
	}
	
	@BeforeMethod
	public void beforeMethod() {
		pg = brows.newPage();
	}
	
	@Test
	public void mainTest() throws InterruptedException {
		pg.navigate("https://winvinaya.com/");
		String title = pg.title();
		System.out.println("Title :" + title);
		Assert.assertTrue(title.contains("WinVinaya InfoSystems"), "Title mismatch!");
		
//		Locators
//		1. Role-based
		pg.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Explore Our Services"));
		Locator btn = pg.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Get Started"));
		System.out.println("Test Case passed ---- Button Clicked: " + btn);
		
// Accessible Rich Internet Applications
//Many modern websites use dynamic content (e.g., dropdowns, sliders, popups).
//Sometimes these elements are not accessible to screen readers.
//ARIA attributes provide roles, states, and properties to make them understandable.
//Developers add special attributes like role, aria-label, aria-expanded, aria-checked, etc., to HTML tags.
//These attributes do not affect how the page looks, but they help assistive technologies describe the UI.
		
		//2. Text-based
		pg.getByText("CONTACT US").first().click();
		System.out.println("TestCase ---- passed contact us clicked ");
		
		//3. getbylabel,getbyplaceholder
		pg.getByPlaceholder("Your name").fill("abc");
		pg.getByPlaceholder("Your Email").fill("abc@mail.co");
		pg.waitForTimeout(2000);
		System.out.println("Passed ------ test filled name email ");
		
		pg.goBack();
		
		//4. Alt/title attributes		
//		Locator alttext = pg.getByAltText("Illustration representing IT services");
		Locator imgcls = pg.locator(".css-1pduhan");
		System.out.println("TestCase ---- passed: " + imgcls.getAttribute("alt"));

//		Locator textId = pg.locator("a.chakra-link css-1klkme5");//newsletter
		Locator textId = pg.locator("//*[@id=\"root\"]/header/div/div/div[2]/a[2]"); //using xpath
		System.out.println("TestCase ---- passed: " + textId.textContent());
		
//		<span class="chakra-badge css-d9gu5a">Trusted Technology Consulting Partner</span>
		//6. CSS-selectors
		pg.waitForSelector(".chakra-badge.css-d9gu5a"); 
		//waitForSelector - Playwright runs very fast. Sometimes your script tries to find an element before it is actually rendered in the DOM 
		//(due to slow page load, animations, or network delay).
		Locator cls = pg.locator(".chakra-badge.css-d9gu5a");
		System.out.println("TestCase ---- passed: " + cls.textContent());
		
		pg.locator("div.css-0");
		System.out.println("TestCase ---- passed div found");

		//7.XPath
//		<a class="chakra-link css-1klkme5" aria-label="Navigate to Blogs page" href="/blogs">Blogs</a>
		pg.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Blogs")).click();

		Locator read = pg.locator("//*[@id=\"root\"]/section[2]/div/div[2]/div/a");
		System.out.println("TestCase ---- passed: " + read.textContent());
		read.click();
		pg.waitForTimeout(5000);
		pg.navigate("https://winvinaya.com/");
		
		//8. Handle Popup window
		Locator visitsite = pg.locator(".chakra-text.css-mws14h"); //nammacademy
		
		Page popupg = null;
		try {
			popupg = pg.waitForPopup(() -> {
				visitsite.click();
			});
		}catch(Exception e) {
			System.out.println("site not found");
		}
		if(popupg != null) {
			System.out.println("Navigate to new tab: " + popupg.url());
			System.out.println("New site title: " + popupg.title());
		}else {
			System.out.println("Navigate to same tab: " + pg.url());
			pg.navigate("https://winvinaya.com/");
		}
		pg.navigate("https://winvinaya.com/");
		pg.waitForTimeout(5000);
		System.out.println("Back to testing site" + pg.url());

	}
//	
//	@Test
//	public void subTest() {
//		pg.navigate("https://winvinayafoundation.org/");
//		String title = pg.title();
//		System.out.println("Title :" +title);
//	
//		pg.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Careers")).click();
//		System.out.println("Link clicked");
//	}
	
	@AfterMethod
	public void closePage() {
		pg.close();
	}
	
    @AfterClass
    public void close() {
    	brows.close();
    	play.close();
    }
}
