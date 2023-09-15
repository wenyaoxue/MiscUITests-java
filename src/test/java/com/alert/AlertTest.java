package com.alert;

import static org.testng.Assert.assertEquals;

import java.time.Duration;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class AlertTest {
	WebDriver driver;
	
	@BeforeTest
	public void pre_condition() {
		//open page
		WebDriverManager.chromedriver().setup(); //download
		ChromeOptions opt = new ChromeOptions();
//		opt.setHeadless(true);
		driver = new ChromeDriver(opt); //launch
		driver.get("https://netbanking.hdfcbank.com/netbanking/"); //type in the url
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	}
  @Test
  public void f() throws InterruptedException {
	  driver.switchTo().frame("login_page");
	  driver.findElement(By.xpath("//a[text()='CONTINUE']")).click();
	  
	  Alert x  = driver.switchTo().alert();
	  assertEquals(x.getText(), "Customer ID  cannot be left blank.");
	  Thread.sleep(2000);
	  x.accept();
	  
	  driver.switchTo().defaultContent();
	  
  }
}
