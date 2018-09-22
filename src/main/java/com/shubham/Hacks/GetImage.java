package com.shubham.Hacks;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GetImage {
    private static final String url = "https://www.amazon.com/dp/B075TC7LBT";
    WebDriver driver;
    private static int DEFAULT_WAIT = 2;
    WebDriverWait wait;

    @Test
    public void checkStatus() throws Exception {
        setUp();
        openPage(url);
        WebElement element = driver.findElement(By.cssSelector("img[class='a-dynamic-image image-stretch-horizontal frontImage']"));
        System.out.println(element.getAttribute("src"));
        driver.quit();
    }

    private void setUp() {
        // System.setProperty("webdriver.chrome.driver",
        // "C:\\Users\\shubhgoe\\Downloads\\chromedriver_win32\\chromedriver.exe");

        ChromeOptions chromeOptions = new ChromeOptions();
        // chromeOptions.addArguments("--kiosk"); // for mac
        chromeOptions.addArguments("--start-maximized"); // for windows
        driver = new ChromeDriver(chromeOptions);
        initializeWait(DEFAULT_WAIT);

    }

    private void openPage(String url) {
        driver.get(url);
        sleep(2000);
    }

    private void initializeWait(int dEFAULT_WAIT) {
        wait = new WebDriverWait(driver, dEFAULT_WAIT);
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
        }
    }
}
