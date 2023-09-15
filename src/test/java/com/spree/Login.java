package com.spree;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Login {
	WebDriver driver;
	
  	@DataProvider(name = "WebOrder_LoginAll_TCs")
	public Object[][] getLogin_All_TCs_Scenarios() {
		// Multidimensional Object
		// 3X3 or 4X3 or 4X5
		return new Object[][] {

				{"Tester", "test","Logout" },
				{"Tester1", "test","Invalid Login or Password." },
				{"Tester", "test1","Invalid Login or Password." },
				{"", "test","Invalid Login or Password." },
				{"Tester", "","Invalid Login or Password." }
				};

	}
  	
	@Test(dataProvider="WebOrder_LoginAll_TCs")
	public void login_to_app(String uname, String pass, String Exp_Result) {
		driver.findElement(By.xpath("//input[@name='ctl00$MainContent$username']")).clear();
		driver.findElement(By.xpath("//input[@name='ctl00$MainContent$username']")).sendKeys(uname);
		driver.findElement(By.xpath("//input[@name='ctl00$MainContent$password']")).clear();
		driver.findElement(By.xpath("//input[@name='ctl00$MainContent$password']")).sendKeys(pass);
		driver.findElement(By.cssSelector("input[name='ctl00$MainContent$login_button']")).click();
		if(Exp_Result.equalsIgnoreCase("Logout"))
		{
		String Act_Msg = driver.findElement(By.linkText("Logout")).getText();
		Assert.assertEquals(Act_Msg, Exp_Result);
		driver.findElement(By.linkText("Logout")).click();
		}
		else
		{
			String Act_Error_Msg = driver.findElement(By.id("ctl00_MainContent_status")).getText();
			Assert.assertEquals(Act_Error_Msg, Exp_Result);
		}

	}
	@BeforeTest
	public void pre_condition() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.get("http://secure.smartbearsoftware.com/samples/TestComplete11/WebOrders/Login.aspx");
	}
	@AfterTest
	public void post_condition() {
		
		driver.close();
	}
}
