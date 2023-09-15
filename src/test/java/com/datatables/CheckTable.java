package com.datatables;

import static org.testng.Assert.assertEquals;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class CheckTable {
WebDriver driver;
	
	@BeforeTest
	public void pre_condition() {
		//open page
		WebDriverManager.chromedriver().setup(); //download
		ChromeOptions opt = new ChromeOptions();
		opt.setHeadless(true);
		driver = new ChromeDriver(opt); //launch
		driver.get("https://datatables.net/examples/data_sources/server_side"); //type in the url
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));

	}
	@Test
	public void SakuraSal() throws InterruptedException {
		driver.findElement(By.xpath("//label/input")).sendKeys("Sakura");
		assertEquals(driver.findElement(By.xpath("//td[text()='Sakura']/following-sibling::*[5]")).getText(), "$139,575");
		driver.findElement(By.xpath("//label/input")).clear();
		driver.findElement(By.xpath("//label/input")).sendKeys("\n");
		Thread.sleep(1000);
	}
	
	@Test
	public void firstlastnames() throws InterruptedException {
		driver.findElement(By.xpath("//thead/tr/th[1]")).click();
		Thread.sleep(1000);
		assertEquals(driver.findElement(By.xpath("//tbody[1]/tr[1]/td[1]")).getText(), "Zorita");
		
		driver.findElement(By.xpath("//thead/tr/th[1]")).click();
		Thread.sleep(1000);
		assertEquals(driver.findElement(By.xpath("//tbody[1]/tr[1]/td[1]")).getText(), "Airi");
	}
}
