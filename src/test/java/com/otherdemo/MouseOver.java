package com.otherdemo;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class MouseOver {
	ChromeDriver driver;
  	@Test(enabled=true)
	public void MouseHoverAmazon() throws InterruptedException
	{
		WebDriverManager.chromedriver().setup();
		// create Edge instance and maximize it
		ChromeDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get( "https://www.amazon.in/");
        
		WebElement signin = driver.findElement(By.id(("nav-link-accountList-nav-line-1")));
		Actions action = new Actions(driver);
        action.moveToElement(signin).perform();
        //driver.findElementById("signInBtn").click();
        //Thread.sleep(2000);
        driver.findElement(By.xpath(("//span[text()='Sign in']"))).click();
        String title = driver.getTitle();
        Assert.assertEquals("Amazon Sign In", title);
	}
}
