package com.example.vvs.webapp.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SeleniumCheckoutTests {
    @LocalServerPort
    private int localPort;

    private String serverUrl;
    private WebDriver driver;

    @BeforeAll
    public static void init() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void initServerUrl() {
        this.serverUrl = "http://localhost:" + localPort;
        this.driver = new ChromeDriver();
        this.driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void whenGetToCheckoutPage_fillWithValidCredentialsAndPlaceOrder() throws InterruptedException {

        String registrationURL = "http://localhost:" + localPort + "/checkout";
        WebDriverWait wait = new WebDriverWait(driver, 30, 1000);
        driver.get(registrationURL);

        //ne aflam pe pagina de checkout
        assertEquals("Checkout", driver.findElement(By.id("checkout_page")).getText());

        //executa checkout cu credentiale bune
        By first_name = By.id("first_name");
        wait.until(presenceOfElementLocated(first_name));
        driver.findElement(first_name).sendKeys("chk_test");

        By last_name = By.id("last_name");
        wait.until(presenceOfElementLocated(last_name));
        driver.findElement(last_name).sendKeys("chk_test");

        By chk_email = By.id("email");
        wait.until(presenceOfElementLocated(chk_email));
        String reg_email = "chk_email_test" + (int) (Math.random() * 1000000000);
        driver.findElement(chk_email).sendKeys(reg_email);

        By chk_country = By.id("country");
        wait.until(presenceOfElementLocated(chk_country));
        driver.findElement(chk_country).sendKeys("chk_country_test");

        By chk_address = By.id("address");
        wait.until(presenceOfElementLocated(chk_address));
        driver.findElement(chk_address).sendKeys("chk_address_test");

        By checkout_btn = By.id("checkout_btn");
        wait.until(presenceOfElementLocated(checkout_btn));
        driver.findElement(checkout_btn).click();

        assertEquals("Your order was successfully registered.", driver.findElement(By.id("checkout_successful")).getText());

    }

    @Test
    public void whenGetToCheckoutPage_pressCheckoutWithNoInfoInserted() throws InterruptedException {

        String registrationURL = "http://localhost:" + localPort + "/checkout";
        WebDriverWait wait = new WebDriverWait(driver, 30, 1000);
        driver.get(registrationURL);

        //ne aflam pe pagina de checkout
        assertEquals("Checkout", driver.findElement(By.id("checkout_page")).getText());

        //executa checkout cu credentiale care lipsesc
        By checkout_btn = By.id("checkout_btn");
        wait.until(presenceOfElementLocated(checkout_btn));
        driver.findElement(checkout_btn).click();

        //trebuie sa primesc mesajul de eroare la campul first name
        assertEquals("Please fill out this field.", driver.findElement(By.id("first_name")).getAttribute("validationMessage"));
    }
}