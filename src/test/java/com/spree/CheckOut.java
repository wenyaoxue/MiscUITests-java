package com.spree;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class CheckOut {
	//select item, check total, addtocart+message
	WebDriver driver;
	
	@BeforeTest
	public void pre_condition() throws InterruptedException {
		//get ready to log in
		WebDriverManager.edgedriver().setup(); //download
		driver = new EdgeDriver(); //launch
		driver.get("http://demo.spreecommerce.org"); //type in the url
		driver.manage().window().maximize();
		driver.findElement(By.id("account-button")).click();
		driver.findElement(By.linkText("LOGIN")).click();
		
		//log in
		Thread.sleep(2000);
		driver.findElement(By.name("spree_user[email]")).sendKeys("c@s.com");
		driver.findElement(By.name("spree_user[password]")).sendKeys("123456");
		driver.findElement(By.name("commit")).click();

		//clear the cart
		Thread.sleep(2000);
		assertEquals("http://demo.spreecommerce.org/account", driver.getCurrentUrl() );
		driver.findElement(By.id("link-to-cart")).click();
		Thread.sleep(2000);
		assertEquals("http://demo.spreecommerce.org/cart", driver.getCurrentUrl() );
		while (driver.findElements(By.xpath("//div[@data-hook='cart_item_image']/following-sibling::div[@data-hook='cart_item_delete']")).size() > 0)
			driver.findElement(By.xpath("//div[@data-hook='cart_item_image']/following-sibling::div[@data-hook='cart_item_delete']")).click();
		
		//go to the sportswear shopping page
		Thread.sleep(2000);
		driver.findElement(By.xpath("//a[text()='Sportswear']")).click();
		
		//select an item
		Thread.sleep(2000);
		String title = "Sports Bra Low Support";
		driver.findElement(By.cssSelector("div[title='"+title+"'")).click();
		
		//customize and add to cart
		Thread.sleep(2000);
		WebElement colortoclick = driver.findElements(By.xpath("//div[@id='product-price']//following-sibling::ul//div[@data-option-type-id='1']//label")).get(0);
		WebElement sizetoclick = driver.findElements(By.xpath("//div[@id='product-price']//following-sibling::ul//div[@data-option-type-id='3']//label")).get(1);
		colortoclick.click();
		sizetoclick.click();
		driver.findElement(By.xpath("//button[text()='+']")).click();
		//add to cart
		driver.findElement(By.id("add-to-cart-button")).click();

		//go to cart
		Thread.sleep(2000);
		driver.findElement(By.xpath("//a[normalize-space()='Checkout']")).click();
	}
	@AfterTest
	public void post_condition() {
//		driver.close();
	}
	@Test
	public void pagecorrect() throws InterruptedException {

		Thread.sleep(2000);
		//don't know why, url doesn't seem consistent
		assertTrue(driver.getCurrentUrl().equals("http://demo.spreecommerce.org/checkout") || driver.getCurrentUrl().equals("http://demo.spreecommerce.org/checkout/address") );
		
		//check addresses showing
		assertTrue(driver.findElement(By.xpath("//h4[text()='label1']")).isDisplayed());
		assertTrue(driver.findElement(By.xpath("//h4[text()='label']")).isDisplayed());
		driver.findElement(By.xpath("//h4[text()='label1']")).click();
		driver.findElement(By.name("commit")).click();
		
		//check delivery
		Thread.sleep(2000);
		assertEquals("http://demo.spreecommerce.org/checkout/delivery", driver.getCurrentUrl());
		//eg check address correct, shipping amount correct
		driver.findElement(By.name("commit")).click();

		//check payment
		Thread.sleep(2000);
		assertEquals("http://demo.spreecommerce.org/checkout/payment", driver.getCurrentUrl());
		//eg check all costs correct, payment forms
		driver.findElements(By.xpath("//input[@name='order[payments_attributes][][payment_method_id]']//following-sibling::*")).get(1).click();
		driver.findElement(By.name("commit")).click();

		//check worked
		Thread.sleep(2000);
		assertEquals("Order placed successfully", driver.findElement(By.xpath("//main/div/h5")).getText());
	}
	private double costToDouble(String cost) { return Double.parseDouble(cost.replace(",","").substring(1)); }
}
