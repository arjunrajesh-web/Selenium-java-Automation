package com.register;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class TC_RF_016 {

    WebDriver driver;
    SoftAssert soft;

    @AfterMethod
    public void teardown() {
        if (soft != null)
            soft.assertAll();

        if (driver != null)
            driver.quit();
    }

    @Test
    public void verifyRegisteringAccountByEnteringOnlySpaces() {

        driver = new ChromeDriver();
        driver.manage().window().maximize();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.get("https://tutorialsninja.com/demo/");

        soft = new SoftAssert();

        // Title check
        soft.assertEquals(driver.getTitle(), "Your Store");

        // Navigate to register page
        driver.findElement(By.xpath("//span[text()='My Account']")).click();
        driver.findElement(By.linkText("Register")).click();

        // Enter spaces
        driver.findElement(By.id("input-firstname")).sendKeys("     ");
        driver.findElement(By.id("input-lastname")).sendKeys("     ");
        driver.findElement(By.id("input-email")).sendKeys("     ");
        driver.findElement(By.id("input-telephone")).sendKeys("     ");
        driver.findElement(By.id("input-password")).sendKeys("     ");
        driver.findElement(By.id("input-confirm")).sendKeys("     ");

        driver.findElement(By.name("agree")).click();
        driver.findElement(By.xpath("//input[@value='Continue']")).click();

        // Expected messages
        String expectedFirstNameWarning = "First Name must be between 1 and 32 characters!";
        String expectedLastNameWarning = "Last Name must be between 1 and 32 characters!";
        String expectedEmailWarning = "E-Mail Address does not appear to be valid!";
        String expectedTelephoneWarning = "Telephone does not appear to be valid!";

        // Wait + Validate
        WebElement firstNameError = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@id='input-firstname']/following-sibling::div")));

        soft.assertEquals(firstNameError.getText(), expectedFirstNameWarning);

        soft.assertEquals(driver.findElement(By.xpath("//input[@id='input-lastname']/following-sibling::div")).getText(),
                expectedLastNameWarning);

        soft.assertEquals(driver.findElement(By.xpath("//input[@id='input-email']/following-sibling::div")).getText(),
                expectedEmailWarning);

        soft.assertEquals(driver.findElement(By.xpath("//input[@id='input-telephone']/following-sibling::div")).getText(),
                expectedTelephoneWarning);
    }
}
