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
public class SeleniumHomePageTests {

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
    public void whengetToHomePage_verifyHomePageUIWithLogin() throws InterruptedException {

        WebDriverWait wait = new WebDriverWait(driver, 30, 1000);
        driver.get(serverUrl);
        assertEquals("Pet Shop - Home", driver.findElement(By.id("pet_shop_home")).getText());
        assertEquals("Login", driver.findElement(By.id("login_index")).getText());
        assertEquals("Home Page", driver.getTitle());

        //test buton detalii
        for(int i=1;i<=7;i++)
        {
            assertEquals("Details", driver.findElement(By.id("Details_"+i)).getText());
        }

        //test produs
        assertEquals("Minge - 17 RON", driver.findElement(By.id("2_Minge")).getText());

        //executa login
        By loginIndex = By.id("login_index");
        wait.until(presenceOfElementLocated(loginIndex));
        driver.findElement(loginIndex).click();

        //ne aflam pe pagina de login
        assertEquals("Are you a new user? Register here", driver.findElement(By.id("login_message")).getText());
        assertEquals("User Login", driver.getTitle());

        By login_email = By.id("username");
        wait.until(presenceOfElementLocated(login_email));
        driver.findElement(login_email).sendKeys("dan@yahoo.com");

        By login_passwd = By.id("password");
        wait.until(presenceOfElementLocated(login_passwd));
        driver.findElement(login_passwd).sendKeys("parola");

        By loginLogin = By.id("login-submit");
        wait.until(presenceOfElementLocated(loginLogin));
        driver.findElement(loginLogin).click();

        //s-a ajuns pe homepage cu succes
        assertEquals("dan@yahoo.com", driver.findElement(By.id("user_name_index")).getText());
        assertEquals("Logout", driver.findElement(By.id("logout_index")).getText());

        //test butoane add to cart
        for(int i=1;i<=7;i++)
        {
            assertEquals("Add to Cart", driver.findElement(By.id("AddToCart_"+i)).getText());
        }
    }

    @Test
    public void whenGetToHomePage_thenLoginAndClickDetailsAndVerifyUI() throws InterruptedException {

        WebDriverWait wait = new WebDriverWait(driver, 30, 1000);
        driver.get(serverUrl);

        //executa login
        By loginIndex = By.id("login_index");
        wait.until(presenceOfElementLocated(loginIndex));
        driver.findElement(loginIndex).click();

        //ne aflam pe pagina de login
        assertEquals("Are you a new user? Register here", driver.findElement(By.id("login_message")).getText());
        assertEquals("User Login", driver.getTitle());

        By login_email = By.id("username");
        wait.until(presenceOfElementLocated(login_email));
        driver.findElement(login_email).sendKeys("dan@yahoo.com");

        By login_passwd = By.id("password");
        wait.until(presenceOfElementLocated(login_passwd));
        driver.findElement(login_passwd).sendKeys("parola");

        By loginLogin = By.id("login-submit");
        wait.until(presenceOfElementLocated(loginLogin));
        driver.findElement(loginLogin).click();

        //s-a ajuns pe homepage cu succes
        assertEquals("dan@yahoo.com", driver.findElement(By.id("user_name_index")).getText());
        assertEquals("Logout", driver.findElement(By.id("logout_index")).getText());

        //click pe details 4
        By details_btn = By.id("Details_4");
        wait.until(presenceOfElementLocated(details_btn));
        driver.findElement(details_btn).click();

        //pagina corecta
        assertEquals("http://localhost:"+localPort+"/product/4", driver.getCurrentUrl());

        //teste pentru verificarea produsului
        assertEquals("Product Name: Hrana Pesti", driver.findElement(By.id("pname_4")).getText());
        assertEquals("Price: 14 RON", driver.findElement(By.id("pprice_4")).getText());
        assertEquals("Description: Hrana Pentru Pesti", driver.findElement(By.id("pdescr_4")).getText());
        assertEquals("Add to Cart", driver.findElement(By.id("AddToCartDetails_4")).getText());


    }
}
