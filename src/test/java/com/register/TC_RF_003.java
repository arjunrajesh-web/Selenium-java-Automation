package com.register;

import java.sql.Date;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import Utilies.common_utilies;

public class TC_RF_003 {

	@Test
	public void verifyregisteringAccountusingallfield(){
		
		 WebDriver driver = new ChromeDriver();
		 
		 driver.manage().window().maximize();
		 
		 driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
		 
		 driver.get("https://tutorialsninja.com/demo/");
		 
		 WebElement myAccountBtn = driver.findElement(
	                By.xpath("//span[normalize-space()='My Account']")
	        );
	        myAccountBtn.click();

	        WebElement registerButton = driver.findElement(By.linkText("Register"));
	        registerButton.click();
	        
	        driver.findElement(By.id("input-firstname")).sendKeys("Arjun");
	        driver.findElement(By.id("input-lastname")).sendKeys("Rajesh");
	        driver.findElement(By.id("input-email")).sendKeys(common_utilies.generatebrandnewemail());
	        driver.findElement(By.id("input-telephone")).sendKeys("8075126463");
	        driver.findElement(By.id("input-password")).sendKeys("Arjun@123");
	        driver.findElement(By.id("input-confirm")).sendKeys("Arjun@123");

	        driver.findElement(By.name("agree")).click();
	        driver.findElement(By.xpath("//input[@value='Continue']")).click();

	        Assert.assertTrue(
	                driver.findElement(
	                        By.xpath("//a[@class='list-group-item'][normalize-space()='Logout']")
	                ).isDisplayed(),
	                "Logout link is not displayed – registration failed"
	        );
	        Assert.assertTrue(
	                driver.findElement(
	                        By.xpath("//a[normalize-space()='Success']")
	                ).isDisplayed(),
	                "sucess link is not displayed – registration failed"
	        );
	        
	        String properdetailsone = "Your Account Has Been Created!";
	        String properdetailsTwo = "Congratulations! Your new account has been successfully created!";
	        String properdetailsThree = "You can now take advantage of member privileges to enhance your online shopping experience with us.";
	        String properdetailsfour = "If you have ANY questions about the operation of this online shop, please e-mail the store owner.";
	        String Properdetailsfive = "A confirmation has been sent to the provided e-mail address. If you have not received it within the hour, please contact us.";
	        
	        Assert.assertTrue(driver.findElement(By.id("content")).getText().contains(properdetailsone));
	        Assert.assertTrue(driver.findElement(By.id("content")).getText().contains(properdetailsTwo));
	        Assert.assertTrue(driver.findElement(By.id("content")).getText().contains(properdetailsThree));
	        Assert.assertTrue(driver.findElement(By.id("content")).getText().contains(properdetailsfour));
	        Assert.assertTrue(driver.findElement(By.id("content")).getText().contains(Properdetailsfive));
	        
	        
	        WebElement Continuebtn = driver.findElement(By.xpath("//a[normalize-space()='Continue']"));
	        Continuebtn.click();
	        
	        Assert.assertTrue(driver.findElement(By.linkText("Edit your account information")).isDisplayed());
	        
	        driver.quit();
	        

		
	}
	
}
