package com.weborder;

import static org.junit.Assert.assertEquals;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.datafiles.ReadExcel;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebOrder_Login_TestNG_excel {
	
	@DataProvider(name = "exceldata")
	public Object[][] ReadDataFromExcel() throws Exception{
		ReadExcel excel = new ReadExcel();
		Object[][] testObjArray = excel.getExcelData("C:\\CrystalEclipse\\Selenium_Webdriver_Training\\testdatafiles\\weborderdata.xlsx","Login");
		return testObjArray;
	}
	
	@Test(dataProvider="exceldata")
	public void login_to_app(String un, String pw) {
		WebDriverManager.chromedriver().setup(); //download
		ChromeDriver driver = new ChromeDriver(); //launch
		driver.get("http://secure.smartbearsoftware.com/samples/TestComplete11/WebOrders/Login.aspx"); //type in the url
		driver.findElement(By.name("ctl00$MainContent$username")).sendKeys(un); //enter username
		driver.findElement(By.name("ctl00$MainContent$password")).sendKeys(pw); //enter password
		driver.findElement(By.name("ctl00$MainContent$login_button")).click(); //click login button
		assertEquals(true, driver.findElement(By.linkText("Logout")).isDisplayed());
		driver.findElement(By.linkText("Logout")).click();
		
		driver.close();
	}
}
