package com.datatables;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class NextButton {
	ChromeDriver driver;

	@Test
	  public void Pagination_Example() throws InterruptedException {
		    WebDriverManager.chromedriver().setup();
		    WebDriver driver = new ChromeDriver();
		    driver.manage().window().maximize();
		    driver.get("https://datatables.net/examples/data_sources/server_side");
		    Thread.sleep(2000);
		    //Get all Name from Table
		    List<WebElement> FnameElements=driver.findElements(By.xpath("//table[@id='example']/tbody/tr/td[1]"));
		    List<String> names = new ArrayList<String>();
		    //Go through Each First Name, using foreach loop concept and getting text of each element and
		    //Storing in names object (which is List<String> class
		    //Below Foreach loop will add all first name in ArrayList from first page, before we click on Next Button
		    for (WebElement Firstnames : FnameElements) {
				names.add(Firstnames.getText());
			}
		    //------------Now will build logic to click on Next button--------
		    //Getting class attribute of Next button, which should contains disabled text
		    String nextButton = driver.findElement(By.id("example_next")).getAttribute("class");
		    while(!nextButton.contains("disabled"))
		    {
		    	//So, here we are checking, if Next button not disabled then click on Next button
		    	driver.findElement(By.id("example_next")).click();
		    	Thread.sleep(2000);
		    	//Post click, you will get next page. from that page you would like to capture the First Name
		    	FnameElements=driver.findElements(By.xpath("//table[@id='example']/tbody/tr/td[1]"));
		    	//Post finding the name, user need to add the all First name in names object
		    	for (WebElement Firstnames : FnameElements) {
					names.add(Firstnames.getText());
				}
		    	//After adding the First name in names object List, need to find the class of Next button
		    	nextButton = driver.findElement(By.id("example_next")).getAttribute("class");
		    	Thread.sleep(3000);
		    }
		    //As a user i would like to Print all the name stored in names List object
		    for (String name : names) {
		    	
				System.out.println(name);
			}
		    //User would like to count total number of First Name retured by names object
		    int totalnames = names.size();
		    System.out.println("Total number of First Names :"+totalnames);
		    //User would like to get the total number of Entries and verify with totalnames 
		    String totalcount = driver.findElement(By.id("example_info")).getText().split(" ")[5];
		    System.out.println("Total number of displayed first name count  :"+totalcount);
		    //Lets verify using Assert of TestNG function
		    //expected =totalcount getting stored as String and actual=totalnames getting stored as int, 
		    //As we can't compare String with Int, hence we need to convert int to string before verifying
		    Assert.assertEquals(totalcount, String.valueOf(totalnames));
		    driver.quit();
	  }
	 
}
