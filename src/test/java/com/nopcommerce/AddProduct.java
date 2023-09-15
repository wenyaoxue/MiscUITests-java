package com.nopcommerce;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.time.Duration;
import java.util.List;

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

public class AddProduct {
	WebDriver driver;
	JavascriptExecutor js;
	
	@BeforeTest
	public void pre_condition() {
		//open page
		WebDriverManager.chromedriver().setup(); //download
		ChromeOptions opt = new ChromeOptions();
//		opt.setHeadless(true);
		driver = new ChromeDriver(opt); //launch
        js = (JavascriptExecutor) driver;
        
		driver.get("https://admin-demo.nopcommerce.com/"); //type in the url
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		//login
		driver.findElement(By.cssSelector("button")).click();
		//catalog > products
		driver.findElement(By.xpath("//*[normalize-space()='Catalog']")).click();
		driver.findElement(By.xpath("//*[normalize-space()='Products']")).click();
	}
	
	@Test
	public void adddelete() throws InterruptedException {
		Thread.sleep(2000);
		//find current total number of items - eg 45
		WebElement dispItems = driver.findElement(By.id("products-grid_info"));
		js.executeScript("arguments[0].scrollIntoView(true);", dispItems);
		int total = Integer.parseInt( dispItems.getText().split("of ")[1].split(" ")[0] );
		
		driver.findElement(By.xpath("//*[normalize-space()='Add new']")).click();
		String name = "CrystalProduct";
		String sku = "CW";
		String price = "1000";
		String quant = "2000";
		
		driver.findElement(By.id("Name")).sendKeys(name);
		driver.findElement(By.id("Sku")).sendKeys(sku);
//		driver.findElement(By.id("Published")).click();
//		driver.findElement(By.id("Price")).clear();
//		driver.findElement(By.id("Price")).sendKeys(price);
//		(new Select(driver.findElement(By.id("ManageInventoryMethodId")))).selectByVisibleText("Track inventory");
//		driver.findElement(By.id("StockQuantity")).clear();
//		driver.findElement(By.id("StockQuantity")).sendKeys(quant);

		js.executeScript("arguments[0].scrollIntoView(true);",driver.findElement(By.xpath("//*[normalize-space()='Save']")));
		driver.findElement(By.xpath("//*[normalize-space()='Save']")).click();
		Thread.sleep(2000);
		assertTrue(driver.findElement(By.xpath("//div[@class='content-wrapper']/div[1]")).getText().contains(
				"The new product has been added successfully."));

		Thread.sleep(3000);
		//find current total number of items - eg 46
		dispItems = driver.findElement(By.id("products-grid_info"));
		js.executeScript("arguments[0].scrollIntoView(true);", dispItems);
		assertEquals(Integer.parseInt( dispItems.getText().split("of ")[1].split(" ")[0] ),
				total+1
				);
		
		//to go to last page, and just search that
//		List<WebElement> pgbtns = driver.findElements(By.xpath("//*[@id='products-grid_next']/preceding-sibling::*"));
//		pgbtns.get(pgbtns.size()-1).click();
		//wait for table page to load
//		Thread.sleep(3000);
		
		//to search every page
		while(true) {
			List<WebElement> prdNames = driver.findElements(By.xpath("//tr/td[3]"));
			for (int i = 0; i < prdNames.size(); i++) {
				System.out.println(prdNames.get(i).getText());
				if (prdNames.get(i).getText().equals(name)) {
					//confirm item row is correct
					assertEquals(driver.findElement(By.xpath("//tr["+(i+1)+"]/td[4]")).getText(), sku);
//					assertEquals(driver.findElement(By.xpath("//tr["+(i+1)+"]/td[7]/i")).getAttribute("nop-value"), "false");
					
					//delete product
					driver.findElement(By.xpath("//tr["+(i+1)+"]/td[1]")).click(); //select item
					driver.findElement(By.id("delete-selected")).click(); //delete
					driver.findElement(By.id("delete-selected-action-confirmation-submit-button")).click(); //delete confirm
					
					Thread.sleep(2000); //wait for product to delete
					
					//find current total number of items - eg 45
					dispItems = driver.findElement(By.id("products-grid_info"));
					js.executeScript("arguments[0].scrollIntoView(true);", dispItems);
					assertEquals(Integer.parseInt( dispItems.getText().split("of ")[1].split(" ")[0] ),
							total
							);
					
					return;
				}
			}
			
			if (driver.findElement(By.xpath("//*[@id='products-grid_next']")).getAttribute("class").contains("disabled")) {
				assertEquals("PRODUCT NOT FOUND IN TABLE!", "");
			}
			else {
				driver.findElement(By.xpath("//*[@id='products-grid_next']")).click();		
				Thread.sleep(3000);
			}
		}

		

  }
}
