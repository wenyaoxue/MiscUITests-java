package com.spree;

import static org.testng.Assert.assertEquals;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class PriceFilter {
	WebDriver driver;
	JavascriptExecutor js;
	WebDriverWait wait ;
	
	@BeforeTest
	public void pre_condition() throws InterruptedException {
		
		//open browser
		WebDriverManager.chromedriver().setup(); //download
		driver = new ChromeDriver(); //launch
		js = (JavascriptExecutor) driver;
		driver.get("http://demo.spreecommerce.org"); //type in the url
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		driver.manage().window().maximize();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Women']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Tops and T-Shirts']"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("filtersPrice"))).click();
				
	}
	
	@DataProvider(name="pricedata")
	public Object[][] getPriceData() {
		return new Object[][] {
			{"//div[text()='Less than $50']", 3},
			{"//div[text()='$50 - $100']", 5},
			{"//div[text()='$101 - $150']", 0},
			{"//div[text()='$151 - $200']", 0},
			{"//div[text()='$201 - $300']", 0},
			{"//div[text()='More than $300']", 0}
		};
	}
	
	private int checkNumProducts() {
		return driver.findElements(By.xpath("//div[contains(@id, 'product_')]")).size();
	}
	@Test(dataProvider="pricedata")
	public void checkPriceFilter(String xpath, int numProducts) throws InterruptedException {
		WebElement filteropt = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
		js.executeScript("arguments[0].scrollIntoView();", filteropt);
		filteropt.click();
		Thread.sleep(5000);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
		assertEquals(numProducts, checkNumProducts());
	}
}
