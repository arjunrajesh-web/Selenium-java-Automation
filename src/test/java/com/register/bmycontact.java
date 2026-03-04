package com.register;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class bmycontact {
	
	WebDriver driver;
	@AfterMethod
	public void teardown() {
		driver.quit();
	}
	
	
	@Test(dataProvider="passwordSupplier")
	public void contactfor(String passwordText) {
		
	    driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
		driver.get("https://www.bmyholisticwellness.com/contact");
		
		
		driver.findElement(By.xpath("//input[contains(@placeholder,'First Name')]")).sendKeys("passwordText");
		driver.findElement(By.xpath("//input[contains(@placeholder,'Type your mail id Here')]")).sendKeys("test@gmail.com");
		driver.findElement(By.name("phone")).sendKeys("0424262010");
		
		WebElement dropdown = driver.findElement(By.xpath("//select[@name='age']"));
		Select select = new Select(dropdown);
		select.selectByValue("60+");    
		
		driver.findElement(By.xpath("//button[normalize-space()='Submit here']")).click();
	}
		
		@DataProvider(name="passwordSupplier")
		public Object[][] supplyPasswords() {
			
			Object[][] data = {{"12345"},{"abcdefghi"},{"abcd1234"},{"abcd123$"},{"ABCD456#"}};
			return data;
			
		}

	}


