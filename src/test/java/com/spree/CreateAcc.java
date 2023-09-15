package com.spree;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class CreateAcc {
	ChromeDriver driver;
	String email;
	
	@BeforeTest
	public void pre_condition() {
		WebDriverManager.chromedriver().setup(); //download
		driver = new ChromeDriver(); //launch
		driver.get("http://demo.spreecommerce.org"); //type in the url
		driver.manage().window().maximize();
		driver.findElement(By.id("account-button")).click();
		driver.findElement(By.linkText("SIGN UP")).click();
		
		Random rand = new Random();
		email = "cwtestem" + rand.nextInt(1000) + "@mail.com";
	}
	@AfterTest
	public void post_condition() {
//		driver.close();
	}
	
	public WebElement getEmailField() { return driver.findElement(By.id("spree_user_email")); }
	public WebElement getPwField() { return driver.findElement(By.name("spree_user[password]")); }
	public WebElement getPwcField() { return driver.findElement(By.name("spree_user[password_confirmation]")); }
	public WebElement getSignUpBtn() { return driver.findElement(By.name("commit")); }
	
	@Test
	public void pagecorrect() throws InterruptedException {
		assertEquals("http://demo.spreecommerce.org/signup", driver.getCurrentUrl() );
		Thread.sleep(1000);
		assertTrue(getEmailField().isDisplayed());
		assertTrue(getPwField().isDisplayed());
		assertTrue(getPwcField().isDisplayed());
		assertTrue(getSignUpBtn().isDisplayed());
	}
	
	@Test
	public void uniqueemail() throws InterruptedException {
		Thread.sleep(1000);
		getEmailField().clear();
		getEmailField().sendKeys("c@s.com");
		getPwField().sendKeys("123456");
		getPwcField().sendKeys("123456");
		getSignUpBtn().click();
		
		assertTrue(driver.findElement(By.id("errorExplanation")).isDisplayed());
		assertEquals("Email has already been taken", driver.findElement(By.id("errorExplanation")).getText());
	}
	@Test
	public void matchingpassword() throws InterruptedException {
		Thread.sleep(1000);
		getEmailField().clear();
		getEmailField().sendKeys(email);
		getPwField().sendKeys("123456");
		getPwcField().sendKeys("12345");
		getSignUpBtn().click();
		
		assertTrue(driver.findElement(By.id("errorExplanation")).isDisplayed());
		assertEquals("Password Confirmation doesn't match Password", driver.findElement(By.id("errorExplanation")).getText());
	}
	@Test
	public void passwordminlength() throws InterruptedException {
		Thread.sleep(1000);
		getEmailField().clear();
		getEmailField().sendKeys(email);
		getPwField().sendKeys("12345");
		getPwcField().sendKeys("12345");
		getSignUpBtn().click();
		
		assertTrue(driver.findElement(By.id("errorExplanation")).isDisplayed());
		assertEquals("Password is too short (minimum is 6 characters)", driver.findElement(By.id("errorExplanation")).getText());
	}
	
	@Test
	public void successfullogin() throws InterruptedException {
		Thread.sleep(1000);
		getEmailField().clear();
		getEmailField().sendKeys(email);
		getPwField().sendKeys("123456");
		getPwcField().sendKeys("123456");
		getSignUpBtn().click();
		
		assertEquals("http://demo.spreecommerce.org/account", driver.getCurrentUrl());
		
		//successful add
		Thread.sleep(1000);
		driver.findElement(By.linkText("Add new address")).click();
		assertEquals("http://demo.spreecommerce.org/addresses/new", driver.getCurrentUrl());
		driver.findElement(By.name("address[label]")).sendKeys("label");
		driver.findElement(By.name("address[firstname]")).sendKeys("firstname");
		driver.findElement(By.name("address[lastname]")).sendKeys("lastname");
		driver.findElement(By.name("address[address1]")).sendKeys("address1");
		driver.findElement(By.name("address[city]")).sendKeys("Dallas");
		(new Select(driver.findElement(By.name("address[state_id]")))).selectByVisibleText("Texas");
		driver.findElement(By.name("address[zipcode]")).sendKeys("75240");
		driver.findElement(By.name("address[phone]")).sendKeys("1234567890");
		driver.findElement(By.name("commit")).click();
		assertEquals("http://demo.spreecommerce.org/account", driver.getCurrentUrl());
		
		//unsuccessful add (duplicate label)
		Thread.sleep(1000);
		driver.findElement(By.linkText("Add new address")).click();
		assertEquals("http://demo.spreecommerce.org/addresses/new", driver.getCurrentUrl());
		driver.findElement(By.name("address[label]")).sendKeys("label");
		driver.findElement(By.name("address[firstname]")).sendKeys("firstname");
		driver.findElement(By.name("address[lastname]")).sendKeys("lastname");
		driver.findElement(By.name("address[address1]")).sendKeys("address1");
		driver.findElement(By.name("address[city]")).sendKeys("Dallas");
		(new Select(driver.findElement(By.name("address[state_id]")))).selectByVisibleText("Texas");
		driver.findElement(By.name("address[zipcode]")).sendKeys("75240");
		driver.findElement(By.name("address[phone]")).sendKeys("1234567890");
		driver.findElement(By.name("commit")).click();
		assertEquals("http://demo.spreecommerce.org/addresses", driver.getCurrentUrl());
		
		//change label, successful add
		Thread.sleep(1000);
		driver.findElement(By.name("address[label]")).clear();
		driver.findElement(By.name("address[label]")).sendKeys("label1");
		driver.findElement(By.name("commit")).click();
		assertEquals("http://demo.spreecommerce.org/account", driver.getCurrentUrl());
		driver.findElement(By.id("account-button")).click();
		Thread.sleep(1000);
		driver.findElement(By.linkText("LOGOUT")).click();
		driver.findElement(By.id("account-button")).click();
		driver.findElement(By.linkText("SIGN UP")).click();
	}
}
