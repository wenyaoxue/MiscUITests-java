package com.nopcommerce;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class LinkTest {
	WebDriver driver;
	JavascriptExecutor js;
	Map<String, String> links = new HashMap<String, String>();
	String urlstem = "https://admin-demo.nopcommerce.com";
	
	@BeforeTest
	public void pre_condition() throws InterruptedException {
		//open page
		WebDriverManager.chromedriver().setup(); //download
		ChromeOptions opt = new ChromeOptions();
//		opt.setHeadless(true);
		driver = new ChromeDriver(opt); //launch
        js = (JavascriptExecutor) driver;
        
		driver.get("https://admin-demo.nopcommerce.com"); //type in the url
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		//login
		driver.findElement(By.cssSelector("button")).click();
		Thread.sleep(2000);
		
		List<WebElement> webeles = driver.findElements(By.cssSelector("a"));
		for (int i = 0; i < webeles.size(); i++) {
			String href = webeles.get(i).getAttribute("href");
			if (href != null && href.contains("/") && !href.contains("logout")) {
				String urltoadd = (href.charAt(0) == '/' ? urlstem : "") + href;
				if (links.get(urltoadd) == null)
					links.put(urltoadd, webeles.get(i).getText());
				
			}
		}
	}
	
	@Test
	public void linktest() throws InterruptedException {
		for (String link: links.keySet()) {
			System.out.print("\n"+link + "\t\t\t\t\t\t-- ");
			System.out.print(links.get(link) + "\t\t\t\t\t\t-- ");
			driver.get(link);
			System.out.print(driver.getTitle() + "\t\t\t\t\t\t-- ");
			List<WebElement> h1s = driver.findElements(By.cssSelector("h1"));
			if (h1s.size() == 0)
				System.out.println("no h1 tag");
			else {
				for (WebElement h1: h1s)
					System.out.print(h1.getText()+";");
			}
		}
	}
}
