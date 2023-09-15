package com.weborder;

import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;


public class WebOrder_Login_TestNG_xpath {
	ChromeDriver driver;
	String un;
	@Test(priority=1)
	public void login_to_app() {
		driver.findElement(By.xpath("//input[@name='ctl00$MainContent$username']")).sendKeys("Tester"); //enter username
		driver.findElement(By.cssSelector("input[name='ctl00$MainContent$password']")).sendKeys("test"); //enter password
		driver.findElement(By.xpath("//input[@name='ctl00$MainContent$login_button']")).click(); //click login button
		driver.findElement(By.cssSelector("a[id='ctl00_logout']")).isDisplayed();
		
		Assert.assertEquals("List of All Orders", 
				driver.findElement(By.xpath("//h2[normalize-space()='List of All Orders']")).getText()
			);
		Assert.assertEquals("Web Orders", 
				driver.getTitle()
			);
		Assert.assertEquals("http://secure.smartbearsoftware.com/samples/TestComplete11/WebOrders/default.aspx", 
				driver.getCurrentUrl()
			);
	}
	
	@Test(priority=2)
	public void create_Order() {
		driver.findElement(By.linkText("Order")).click();
		Select product = new Select(driver.findElement(By.name("ctl00$MainContent$fmwOrder$ddlProduct")));
		product.selectByVisibleText("FamilyAlbum");
		driver.findElement(By.name("ctl00$MainContent$fmwOrder$txtQuantity")).sendKeys("5");
		driver.findElement(By.name("ctl00$MainContent$fmwOrder$txtUnitPrice")).sendKeys("x");
		driver.findElement(By.name("ctl00$MainContent$fmwOrder$txtDiscount")).sendKeys("x");
		driver.findElement(By.name("ctl00$MainContent$fmwOrder$txtName")).sendKeys(un);
		driver.findElement(By.name("ctl00$MainContent$fmwOrder$TextBox2")).sendKeys("x");
		driver.findElement(By.name("ctl00$MainContent$fmwOrder$TextBox3")).sendKeys("x");
		driver.findElement(By.name("ctl00$MainContent$fmwOrder$TextBox5")).sendKeys("1");
		driver.findElement(By.id("ctl00_MainContent_fmwOrder_cardList_1")).click();
		driver.findElement(By.name("ctl00$MainContent$fmwOrder$TextBox6")).sendKeys("1");
		driver.findElement(By.name("ctl00$MainContent$fmwOrder$TextBox1")).sendKeys("12/23");
		driver.findElement(By.id("ctl00_MainContent_fmwOrder_InsertButton")).click();
		
		Assert.assertEquals("New order has been successfully added.", 
				driver.findElement(By.xpath("//strong[normalize-space()='New order has been successfully added.']")).getText()
			);
		
		driver.findElement(By.linkText("View all orders")).click();
		Assert.assertEquals(un, 
				driver.findElement(By.xpath("//td[text()='"+un+"']")).getText()
			);
	}
	
	@Test(priority=3)
	public void update() throws InterruptedException {
		driver.findElement(By.xpath("//td[text()='"+un+"']//following-sibling::td/input")).click();
		driver.findElement(By.xpath("//h2[normalize-space()='Edit Order']")).isDisplayed();
		driver.findElement(By.name("ctl00$MainContent$fmwOrder$TextBox4")).sendKeys("CA");
		driver.findElement(By.id("ctl00_MainContent_fmwOrder_UpdateButton")).click();
		Assert.assertEquals("CA",
				driver.findElement(By.xpath("//td[text()='"+un+"']//following-sibling::td[text()='CA']")).getText());
	}
	@Test(priority=4)
	public void delete() throws InterruptedException {
		//click check box
		driver.findElement(By.xpath("//td[text()='"+un+"']//preceding-sibling::td/input")).click();
		//click delete
		driver.findElement(By.name("ctl00$MainContent$btnDelete")).click();
		
		
		Assert.assertFalse(driver.getPageSource().contains(un));
		
	}
	
	
	@BeforeTest
	public void pre_condition() {
		WebDriverManager.chromedriver().setup(); //download
		driver = new ChromeDriver(); //launch
		driver.get("http://secure.smartbearsoftware.com/samples/TestComplete11/WebOrders/Login.aspx"); //type in the url
		driver.manage().window().maximize();
		

		Random randomgenerator = new Random();
		int randomInt = randomgenerator.nextInt(1000);
		un = "crisco"+randomInt;
	}
	@AfterTest
	public void post_condition() {
//		driver.findElement(By.linkText("Logout")).click();
//		driver.close();
	}
}
