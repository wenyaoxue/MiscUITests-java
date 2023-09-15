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

public class AddToCart {
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
		
	}
	@AfterTest
	public void post_condition() {
//		driver.close();
	}
	@Test
	public void pagecorrect() throws InterruptedException {
		//go to the sportswear shopping page
		Thread.sleep(2000);
		assertEquals("http://demo.spreecommerce.org/cart", driver.getCurrentUrl() );
//		driver.findElement(By.linkText("Sportswear")).click();
		driver.findElement(By.xpath("//a[text()='Sportswear']")).click();
		
		//select an item
		Thread.sleep(2000);
		assertEquals("http://demo.spreecommerce.org/t/categories/sportswear", driver.getCurrentUrl() );
		String title = "Sports Bra Low Support";
		driver.findElement(By.cssSelector("div[title='"+title+"'")).click();
		
		//customize and add to cart
		Thread.sleep(2000);
		String price = driver.findElement(By.xpath("//span[contains(text(), '$')]")).getText();
		WebElement colortoclick = driver.findElements(By.xpath("//div[@id='product-price']//following-sibling::ul//div[@data-option-type-id='1']//label")).get(0);
		WebElement sizetoclick = driver.findElements(By.xpath("//div[@id='product-price']//following-sibling::ul//div[@data-option-type-id='3']//label")).get(1);
		assertEquals("http://demo.spreecommerce.org/products/sports-bra-low-support?taxon_id=4", driver.getCurrentUrl() );
		colortoclick.click();
		String color = colortoclick.getAttribute("data-original-title");
		sizetoclick.click();
		String size = sizetoclick.getAttribute("aria-label");
		driver.findElement(By.xpath("//button[text()='+']")).click();
		String quantity = driver.findElement(By.id("quantity")).getAttribute("value");
		//add to cart
		driver.findElement(By.id("add-to-cart-button")).click();

		//check success, go to cart
		Thread.sleep(2000);
		assertTrue(driver.findElement(By.xpath("//div[normalize-space()='Added to cart successfully!']")).isDisplayed());
		driver.findElement(By.xpath("//a[normalize-space()='View cart']")).click();
		
		//check cart
		Thread.sleep(1000);
		String pathToTitleCell = "//a[text()='"+title+"']/parent::*/parent::*";
		List<WebElement> itemDetails = driver.findElements(By.xpath(pathToTitleCell+"//li"));
		
		String youngerSibDataHookOpen = "/following-sibling::div[@data-hook='cart_item_";
		String cartprice = driver.findElement(By.xpath(pathToTitleCell+youngerSibDataHookOpen+"price']")).getText();
		String cartquantity= driver.findElement(By.xpath(pathToTitleCell+youngerSibDataHookOpen+"quantity']//input")).getAttribute("value");
		String carttotal=  driver.findElement(By.xpath(pathToTitleCell+youngerSibDataHookOpen+"total']")).getText() ;
		assertEquals("SIZE: "+size, itemDetails.get(0).getText());
		assertEquals("COLOR: "+color.toUpperCase(), itemDetails.get(1).getText());
		assertEquals(price, cartprice);
		assertEquals(quantity, cartquantity);
		assertEquals(costToDouble(cartprice) * Integer.parseInt(cartquantity), costToDouble(carttotal));
		
	}
	private double costToDouble(String cost) { return Double.parseDouble(cost.replace(",","").substring(1)); }
}
