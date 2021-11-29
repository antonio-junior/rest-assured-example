package testcase;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

@RunWith(Parameterized.class)
public class SelTest {
	
	private int time;
	
	public SelTest(int time) {
		this.time = time;
	}
	
	@Parameterized.Parameters
	public static Integer[] params() {
		return new Integer[] {5000, 10000};
	}

	@Test
	public void test() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\user\\dev\\driver\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		driver.get("https://google.com");
		GooglePage g = new GooglePage(driver);		
		
		g.input.sendKeys("Chair");
		g.input.sendKeys(Keys.ENTER);
		assertEquals(driver.getTitle(), "Chair - Pesquisa Google");
		
		System.out.println(this.time);
		Thread.sleep(this.time);
		driver.quit();
	}
	
	@Ignore
	public void test2() {
		System.out.println(this.time);
	}
	
}
