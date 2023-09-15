package com.orange;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.datafiles.ReadExcel;

import io.github.bonigarcia.wdm.WebDriverManager;

public class CreateOrder {
	WebDriver driver;
	
	@BeforeTest
	public void pre_condition() {
		//open page
		WebDriverManager.chromedriver().setup(); //download
		driver = new ChromeDriver(); //launch
		driver.get("http://secure.smartbearsoftware.com/samples/TestComplete11/WebOrders/Login.aspx"); //type in the url
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));

		//login and click order
		driver.findElement(By.name("ctl00$MainContent$username")).	sendKeys("Tester");
		driver.findElement(By.name("ctl00$MainContent$password")).	sendKeys("test");
		driver.findElement(By.name("ctl00$MainContent$login_button")).click();
		driver.findElement(By.linkText("Order")).click();
	}
	
	@DataProvider(name="orderdata")
	public Object[][] getOrderData() {
		return new Object[][] {
			{"MyMoney", true, "1", true, "name", true, "street", true, "city", true, "state", true, "123", true, true,
														"0", true, "123", true, true, "12/23", true, true},
			{"FamilyAlbum", true, "", false, "", false, "", false, "", false, "", false, "", false, true,
														"1", true, "", false, true, "12023", true, false},
			{"ScreenSaver", true, "0", false, "name", true, "street", true, "city", true, "state", true, "abc", true, false,
														"2", true, "abc", true, false, "1223", true, false},
			{"MyMoney", true, "a", false, "name", true, "street", true, "city", true, "state", true, "123", true, true,
														"", false, "123", true, true, "ab/cd", true, false},
			{"MyMoney", true, "1", true, "name", true, "street", true, "city", true, "state", true, "123", true, true,
														"0", true, "123", true, true, "", false, true},
			
		};
	}

	
	String productx = "//*[@name='ctl00$MainContent$fmwOrder$ddlProduct']";
	String quantityx = "//*[@name='ctl00$MainContent$fmwOrder$txtQuantity']";
	String namex = "//*[@name='ctl00$MainContent$fmwOrder$txtName']";
	String streetx = "//*[@name='ctl00$MainContent$fmwOrder$TextBox2']";
	String cityx = "//*[@name='ctl00$MainContent$fmwOrder$TextBox3']";
	String statex = "//*[@name='ctl00$MainContent$fmwOrder$TextBox4']";
	String zipx = "//*[@name='ctl00$MainContent$fmwOrder$TextBox5']";
	public String getCardX(String cr) {return "//*[@id='ctl00_MainContent_fmwOrder_cardList_"+cr+"']";}
	String cardnox = "//*[@name='ctl00$MainContent$fmwOrder$TextBox6']";
	String cardexpx = "//*[@name='ctl00$MainContent$fmwOrder$TextBox1']";
	
	public WebElement getWebEle(String xpath) {return driver.findElement(By.xpath(xpath));}
	public List<WebElement> getWebEles(String xpath) {return driver.findElements(By.xpath(xpath));}
	public List<WebElement> getNextWebEles(String xpath) {return getWebEles(xpath+"/following-sibling::*");}

	public String sayEmpty(String fieldnm) {return "Field '"+fieldnm+"' cannot be empty.";}
	
	@Test(dataProvider="orderdata")
	public void placeOrders(String pr, boolean prv, String qu, boolean quv,
			String nm, boolean nmv, String st, boolean stv, String ct, boolean ctv, String st8, boolean st8v, 
			String zp, boolean zpv, boolean zpnum, String cr, boolean crv, 
			String cn, boolean cnv, boolean cnnum, String ce, boolean cev, boolean ceformat) throws InterruptedException {

		driver.findElement(By.cssSelector("input[type='reset']")).click();
		Thread.sleep(1000);
		
		(new Select(getWebEle(productx))).selectByVisibleText(pr);
		getWebEle(quantityx).sendKeys(qu);
		getWebEle(namex).sendKeys(nm);
		getWebEle(streetx).sendKeys(st);
		getWebEle(cityx).sendKeys(ct);
		getWebEle(statex).sendKeys(st8);
		getWebEle(zipx).sendKeys(zp);
		if (cr != "")
			getWebEle(getCardX(cr)).click();
		getWebEle(cardnox).sendKeys(cn);
		getWebEle(cardexpx).sendKeys(ce);
		driver.findElement(By.linkText("Process")).click();
		
		Object[][] validator = {
				{quantityx, quv, "Quantity must be greater than zero.", 1},
				{namex, nmv, sayEmpty("Customer name"), 0},
				{streetx, stv, sayEmpty("Street"), 0},
				{cityx, ctv, sayEmpty("City"), 0},
				{zipx, zpv, sayEmpty("Zip"), 0},
				{zipx, zpnum, "Invalid format. Only digits allowed.", 1},
				{getCardX("0")+"/parent::*/parent::*/parent::*/parent::*", crv, "Select a card type.", 0},
				{cardnox, cnv, sayEmpty("Card Nr"), 0},
				{cardnox, cnnum, "Invalid format. Only digits allowed.", 1},
				{cardexpx, cev, sayEmpty("Expire date"), 0},
				{cardexpx, ceformat, "Invalid format. Required format is mm/yy.", 1}
		};
		for (int i = 0; i < validator.length; i++) {
			WebElement errMsg = getNextWebEles((String) validator[i][0]).get((int) validator[i][3]);
			if ((boolean) validator[i][1]) {
					assertEquals("color: red; display: none;", errMsg.getAttribute("style"));
			}
			else {
					assertEquals("color: red; display: inline;", errMsg.getAttribute("style"));
					assertTrue(errMsg.getText().contains((String) validator[i][2]));
			}
		}
	}
}


