package com.otherdemo;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ScreenshottingTest {
	ChromeDriver driver;
	String filePath = System.getProperty("user.dir");

	String filepath_failure = filePath + "\\ScreenshotFailure";
	String filePath_Success = filePath + "\\ScreenshotSuccess";

	@Test
	public void success() {
		driver.findElement(By.xpath("//input[@name='ctl00$MainContent$username']")).sendKeys("Tester");
		driver.findElement(By.xpath("//input[@name='ctl00$MainContent$password']")).sendKeys("test");
		Assert.assertTrue(true);
	}
	@Test
	public void failure() {
		driver.findElement(By.xpath("//input[@name='ctl00$MainContent$username']")).sendKeys("Hahahahaha");
		driver.findElement(By.xpath("//input[@name='ctl00$MainContent$password']")).sendKeys("test");
		Assert.assertTrue(false);
	}
	

	@BeforeTest
	public void LaunchBrowser() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.navigate().to("http://secure.smartbearsoftware.com/samples/TestComplete11/WebOrders/Login.aspx");
	}

	@AfterMethod
	public void CaptureScreenShot(ITestResult result) throws IOException {
		if (ITestResult.FAILURE == result.getStatus()) {
			File Browserscreenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(Browserscreenshot,
					new File(filepath_failure + "\\" + result.getName() + "_" + System.currentTimeMillis() + ".png"));
		} else if (ITestResult.SUCCESS == result.getStatus()) {
			File Browserscreenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(Browserscreenshot,
					new File(filePath_Success + "\\" + result.getName() + "_" + System.nanoTime() + ".png"));
		}
	}

	@AfterTest
	public void CloseBrowser() {
		driver.quit();
	}
	
}
