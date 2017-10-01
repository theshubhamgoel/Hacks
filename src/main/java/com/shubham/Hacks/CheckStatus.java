package com.shubham.Hacks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CheckStatus {

    private static final String NAME_CSS = "span[class='emojitext ellipsify']";
    private static final String STATUS_CSS = "span[class='emojitext chat-subtitle-text']";
    private static final String HEADER_CSS = "header[class='pane-header pane-chat-header']";
    private static final String FOOTER_CSS = "footer[class='pane-footer pane-chat-footer']";
    private static final String CHAT_MESSAGE_CSS = "span[class='emojitext selectable-text']";
    private static final String SEARCH_CLASS = "input-search";

    private static int DEFAULT_WAIT = 10;
    private static final String WHATSAPP_WEB_URL = "https://web.whatsapp.com/";
    private static final String GOOGLE_TRANSLATE_URL = "https://translate.google.co.in/?hl=en#en/en/";

    String personToMonitor = "Arnab";
    private static final String STATUS_GROUP = "Status Group";
    private static final String DICTIONARY_GROUP = "Dictionary Group";
    WebDriver driver;
    WebDriverWait wait;
    DateFormat df = new SimpleDateFormat("hh:mm a");

    @Test
    public void checkStatus() throws Exception {
        setUp();
        openPage(WHATSAPP_WEB_URL);
        waitUntil(By.className(SEARCH_CLASS));

        openContactOrGroup(personToMonitor);

        System.out.println("Logging online status for " + personToMonitor);
        System.out.println("==========================");

        sleep(2000);

        String previousStatus = "";
        while (true) {
            try {
                WebElement headerElement = driver.findElement(By.cssSelector(HEADER_CSS));

                String name = getActiveChatTitleName(headerElement);
                String newstatus = getActiveChatOnlineStatus(headerElement);

                if ("online".equalsIgnoreCase(newstatus)) {
                    newstatus += " at " + df.format(new Date());
                }

                String message = name + "  :  " + newstatus;

                // send update to me
                if (!previousStatus.equals(newstatus)) {
                    previousStatus = newstatus;
                    openContactOrGroup(STATUS_GROUP);
                    sendMessageTO(message, STATUS_GROUP);
                    openContactOrGroup(name);
                }

                if (name == STATUS_GROUP)
                    name = personToMonitor;

                System.out.println(message);
            } catch (Exception e) {

            }
            sleep(5000);
        }

        // driver.quit();
    }

    @Test
    public void dictionaryGroup() {
        setUp();
        openPage(WHATSAPP_WEB_URL);

        waitUntil(By.className(SEARCH_CLASS));
        openContactOrGroup(DICTIONARY_GROUP);

        WebDriver localDriver = driver;
        while (true) {
            List<WebElement> chatMessages = driver.findElements(By.cssSelector(CHAT_MESSAGE_CSS));
            String word = chatMessages.get(chatMessages.size() - 1).getText();
            if (word.startsWith("#")) {
                String meaning = getMeaning(word.substring(1, word.length()));
                driver = localDriver;

                if (meaning.equals("*")) {
                    meaning = "No Defination found";
                } else {
                    meaning = "Defination : " + meaning;
                }
                sendMessageTO(meaning, DICTIONARY_GROUP);

            }
            sleep(5000);
        }
    }

    public String getMeaning(String word) {
        try {
            setUp();
            openPage(GOOGLE_TRANSLATE_URL + word);
            waitUntil(By.className("gt-def-row"));

            WebElement headerElement = driver.findElement(By.className("gt-def-row"));
            String meaning = headerElement.getText();
            System.out.println(meaning);
            return meaning;
        } catch (Exception e) {
            return "*";
        } finally {
            driver.close();
        }

    }

    private void waitUntil(By condition) {
        wait.until(ExpectedConditions.presenceOfElementLocated(condition));
    }

    private String getActiveChatTitleName(WebElement headerElement) {
        return getTextByCSS(headerElement, NAME_CSS);
    }

    private String getActiveChatOnlineStatus(WebElement headerElement) {
        return getTextByCSS(headerElement, STATUS_CSS);
    }

    private String getTextByCSS(WebElement root, String css) {
        return root.findElement(By.cssSelector(css)).getText();
    }

    private void sendMessageTO(String message, String personOrGroupName) {
        // first reverify person Name
        WebElement headerElement = driver.findElement(By.cssSelector(HEADER_CSS));
        String name = getActiveChatTitleName(headerElement);

        if (!name.equals(personOrGroupName)) {
            return;
        }

        WebElement footerElement = driver.findElement(By.cssSelector(FOOTER_CSS));
        WebElement input = footerElement.findElement(By.className("input"));
        input.clear();
        input.sendKeys(message);
        WebElement sendButton = footerElement.findElement(By.className("compose-btn-send"));
        sendButton.click();
    }

    private void openContactOrGroup(String contactName) {
        WebElement search = driver.findElement(By.className(SEARCH_CLASS));
        search.clear();
        search.sendKeys(contactName);
        sleep(4000);
        driver.findElements(By.className("chat-avatar")).get(0).click();
        sleep(1000);
    }

    private void setUp() {
        // System.setProperty("webdriver.chrome.driver",
        // "C:\\Users\\shubhgoe\\Downloads\\chromedriver_win32\\chromedriver.exe");

        ChromeOptions chromeOptions = new ChromeOptions();
        //chromeOptions.addArguments("--kiosk"); // for mac
        chromeOptions.addArguments("--start-maximized"); // for windows
        driver = new ChromeDriver(chromeOptions);
        initializeWait(DEFAULT_WAIT);

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

    private void openPage(String url) {
        driver.get(url);
        sleep(2000);
    }
}