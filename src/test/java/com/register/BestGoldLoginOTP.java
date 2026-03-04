package com.register;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

public class BestGoldLoginOTP {

    @Test
    public void loginWithOtp() throws Exception {

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        driver.get("https://web.bestgold.ae/en/login");

        String email = "arjunrajesh517@gmail.com";
        String appPassword = "bajnsqxsskkbonvr"; // Gmail App Password

        //  ENTER EMAIL
        driver.findElement(By.xpath("//input[@placeholder='Email']")).sendKeys(email);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        //  SELECT COUNTRY CODE 
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class,'flag-dropdown')]"))).click();

        WebElement searchBox = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@class='search-box']")));
        searchBox.sendKeys("India");

        WebElement india = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//li[contains(.,'India') and contains(.,'+91')]")));

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", india);

        //  ENTER MOBILE NUMBER 
        driver.findElement(By.xpath("//input[contains(@placeholder,'702')]"))
                .sendKeys("7034189424");

      
        driver.findElement(By.xpath("//button[normalize-space()='Continue']")).click();

    
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//input[@type='number']")));

        System.out.println("✅ OTP PAGE LOADED");

        //  FETCH OTP FROM EMAIL 
        String otp = fetchLatestOtp(email, appPassword);

        if (otp != null) {
            System.out.println("🎯 OTP RECEIVED: " + otp);
            enterOtp(driver, otp);
        } else {
            System.out.println("❌ OTP NOT FOUND");
        }

        // driver.quit(); // enable after verification
    }

   
    // FETCH LATEST OTP EMAIL
    
    private static String fetchLatestOtp(String username, String appPassword) throws Exception {

        System.out.println("⏳ Waiting for OTP email...");
        Thread.sleep(30000); 

        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");

        Session session = Session.getDefaultInstance(props);
        Store store = session.getStore("imaps");
        store.connect("imap.gmail.com", username, appPassword);

        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        Message message = inbox.getMessage(inbox.getMessageCount());

        System.out.println("📌 SUBJECT: " + message.getSubject());

        String body = getTextFromMessage(message);

        // Remove HTML tags
        String cleanText = body.replaceAll("<[^>]*>", " ");

        System.out.println("📄 EMAIL BODY: " + cleanText);

        Matcher matcher = Pattern.compile("\\b(\\d{6})\\b").matcher(cleanText);

        if (matcher.find()) {
            inbox.close(false);
            store.close();
            return matcher.group(1);
        }

        inbox.close(false);
        store.close();
        return null;
    }

   
    // READ EMAIL BODY (HTML + TEXT)

    private static String getTextFromMessage(Message message) throws Exception {

        if (message.isMimeType("text/plain")) {
            return message.getContent().toString();
        }

        if (message.isMimeType("text/html")) {
            return message.getContent().toString();
        }

        if (message.isMimeType("multipart/*")) {
            return getTextFromMimeMultipart((MimeMultipart) message.getContent());
        }

        return "";
    }

    private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws Exception {

        for (int i = 0; i < mimeMultipart.getCount(); i++) {

            BodyPart bodyPart = mimeMultipart.getBodyPart(i);

            if (bodyPart.isMimeType("text/html")) {
                return bodyPart.getContent().toString();
            }

            if (bodyPart.isMimeType("text/plain")) {
                return bodyPart.getContent().toString();
            }
        }
        return "";
    }


    // ENTER OTP INTO UI
   
    private static void enterOtp(WebDriver driver, String otp) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        List<WebElement> otpInputs = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(
                        By.xpath("//div[contains(@class,'items-center')]//input[@type='number']")));

        char[] digits = otp.toCharArray();

        for (int i = 0; i < digits.length; i++) {
            otpInputs.get(i).clear();
            otpInputs.get(i).sendKeys(String.valueOf(digits[i]));
        }

        System.out.println("✅ OTP ENTERED SUCCESSFULLY");
    }
}