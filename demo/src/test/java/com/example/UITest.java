package com.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class UITest {
    WebDriver driver = new ChromeDriver();
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

    @BeforeEach
    public void setUp() {
        driver.get("http://uitestingplayground.com/sampleapp");
    }

    @AfterEach
    public void finish() {
        driver.close();
        driver.quit();
    }

    @Test
    void sampleApp_login() {
        driver.findElement(By.cssSelector("input[name='UserName']")).sendKeys("Evgeniy");
        driver.findElement(By.cssSelector("input[name='Password']")).sendKeys("pwd");
        driver.findElement(By.cssSelector("#login")).click();
        WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#loginstatus")));
        assertEquals("Welcome, Evgeniy!", message.getText());
    }

    @Test
    void sampleApp_logout() {
        driver.findElement(By.cssSelector("input[name='UserName']")).sendKeys("Evgeniy");
        driver.findElement(By.cssSelector("input[name='Password']")).sendKeys("pwd");
        driver.findElement(By.cssSelector("#login")).click();
        driver.findElement(By.cssSelector("#login")).click();
        WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#loginstatus")));
        assertEquals("User logged out.", message.getText());
    }

    @Test
    void dynamicId_clickButton() {
        WebElement button = driver.findElement(By.cssSelector("button.btn-primary"));
        button.click();
        assertDoesNotThrow(button::click);
    }

    @Test
    void classAttribute_alertPrimary() {
        driver.findElement(By.cssSelector("button.btn-primary")).click();
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        assertTrue(alert.getText().contains("Primary"));
        alert.accept();
    }

    @Test
    void hiddenLayers_noSecondClick() {
        WebElement greenButton = driver.findElement(By.cssSelector("#greenButton"));
        greenButton.click();
        assertThrows(ElementClickInterceptedException.class, greenButton::click);
    }

    @Test
    void loadDelay_buttonDisplayed() {
        WebElement button = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.btn-primary")));
        assertTrue(button.isDisplayed());
    }

    @Test
    void ajaxData_textLoaded() {
        driver.findElement(By.cssSelector("#ajaxButton")).click();
        WebElement text = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".bg-success")));
        assertEquals("Data loaded with AJAX get request.", text.getText());
    }

    @Test
    void textInput_buttonTextUpdated() {
        driver.findElement(By.cssSelector("#newButtonName")).sendKeys("Hello");
        driver.findElement(By.cssSelector("#updatingButton")).click();
        WebElement button = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#updatingButton")));
        assertEquals("Hello", button.getText());
    }

    @Test
    void scrollbars_clickHiddenButton() {
        WebElement button = driver.findElement(By.cssSelector("#hidingButton"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", button);
        button.click();
        assertDoesNotThrow(button::click);
    }

    @Test
    void overlappedElement_inputValue() {
        WebElement input = driver.findElement(By.cssSelector("#name"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", input);
        input.sendKeys("abc");
        assertEquals("abc", input.getAttribute("value"));
    }

@Test
    void visibility_checkHidden() {
        driver.findElement(By.cssSelector("#hideButton")).click();
        WebElement removedButton = driver.findElement(By.cssSelector("#removedButton"));
        WebElement zeroWidthButton = driver.findElement(By.cssSelector("#zeroWidthButton"));
        assertFalse(removedButton.isDisplayed());
        assertFalse(zeroWidthButton.isDisplayed());
    }

    @Test
    void click_buttonSuccessClass() {
        WebElement button = driver.findElement(By.cssSelector("#badButton"));
        button.click();
        assertTrue(button.getAttribute("class").contains("btn-success"));
    }

    @Test
    void progressBar_stopAt75() {
        driver.findElement(By.cssSelector("#startButton")).click();
        WebElement progress = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#progressBar")));
        wait.until(driver -> Integer.parseInt(progress.getAttribute("aria-valuenow")) >= 75);
        driver.findElement(By.cssSelector("#stopButton")).click();
        assertTrue(Integer.parseInt(progress.getAttribute("aria-valuenow")) >= 75);
    }

    @Test
    void mouseOver_clickCounter() {
        WebElement link = driver.findElement(By.cssSelector("a[title='Click me']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", link);
        WebElement counter = driver.findElement(By.cssSelector("#clickCount"));
        assertEquals("2", counter.getText());
    }

    @Test
    void shadowDom_textPresent() {
        WebElement shadowHost = driver.findElement(By.cssSelector("my-paragraph"));
        WebElement shadowRoot = (WebElement) ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].shadowRoot.querySelector('span')", shadowHost);
        assertNotNull(shadowRoot.getText());
    }
}