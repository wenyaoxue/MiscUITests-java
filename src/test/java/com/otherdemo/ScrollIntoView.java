package com.otherdemo;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ScrollIntoView {
	  ChromeDriver driver;
  @Test
  public void f() throws InterruptedException {
	    	WebDriverManager.chromedriver().setup();
	        driver = new ChromeDriver();
	        
	        JavascriptExecutor js = (JavascriptExecutor) driver;

	        // Launch the application		
	        driver.get("https://stackoverflow.com/");
	        		
	        WebElement Element = driver.findElement(By.linkText(("Take a tour of Teams")));
	        Thread.sleep(2000);
	        //This will scroll the page Horizontally till the element is found		
	        js.executeScript("arguments[0].scrollIntoView();", Element);
	        Thread.sleep(5000);
	        Element.click();
	        Thread.sleep(2000);
	        driver.quit();
  }
}
