package com.register;

import java.time.Duration;
import java.util.Properties;
import java.util.Set;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC_RF_002 {

    @Test
    public void verifyThankyouConfirmationonSucessFullRegisteration() {

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get("https://www.amazon.com/");

        String parentWindow = driver.getWindowHandle();

        driver.findElement(By.id("nav-link-accountList-nav-line-1")).click();
        driver.findElement(By.xpath("//a[normalize-space()='Need help?']")).click();

        // Switch to new tab
        for (String window : driver.getWindowHandles()) {
            if (!window.equals(parentWindow)) {
                driver.switchTo().window(window);
                break;
            }
        }

        // Select dropdown option
        WebElement chooseTopic = driver.findElement(By.id("cu-select-firstNode"));
        Select select = new Select(chooseTopic);
        select.selectByVisibleText("I forgot my password");

        driver.findElement(By.linkText("Password Reset")).click();

        String email = "arjun.rajesh@spiderworks.in";
        String appPassword = "cazc utrn yick uugh";

        driver.findElement(By.id("ap_email")).sendKeys(email);
        driver.findElement(By.id("continue")).click();

        // Gmail IMAP configuration
        String host = "imap.gmail.com";
        String expectedSubject = "Amazon password assistance";
        String expectedBodyContent = "reset";

        String link = null;

        try {
            Properties properties = new Properties();
            properties.put("mail.store.protocol", "imaps");

            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("imaps");
            store.connect(host, email, appPassword);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.search(
                    new FlagTerm(new Flags(Flags.Flag.SEEN), false));

            for (int i = messages.length - 1; i >= 0; i--) {
                Message message = messages[i];

                if (message.getSubject().contains(expectedSubject)) {
                    String body = getTextFromMessage(message);
                    Assert.assertTrue(body.contains(expectedBodyContent));

                    String[] ar = body.split("href=\"");
                    String[] arr = ar[1].split("\"");
                    link = arr[0];
                    break;
                }
            }

            inbox.close(false);
            store.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(link, "Reset link not found in email");

        driver.navigate().to(link);
        Assert.assertTrue(
                driver.findElement(By.name("customerResponseDenyButton")).isDisplayed()
        );

        driver.quit();
    }

    // ---------- Utility Methods ----------

	 private String getTextFromMessage(Message message) throws Exception {
	        String result = "";
	        if (message.isMimeType("text/plain")) {
	            result = message.getContent().toString();
	        } else if (message.isMimeType("text/html")) {
	            result = message.getContent().toString();
	        } else if (message.isMimeType("multipart/*")) {
	            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
	            result = getTextFromMimeMultipart(mimeMultipart);
	        }
	        return result;
}
    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws Exception {
        StringBuilder result = new StringBuilder();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result.append(bodyPart.getContent());
            } else if (bodyPart.isMimeType("text/html")) {
                result.append(bodyPart.getContent());
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }
        return result.toString();
 }

}
