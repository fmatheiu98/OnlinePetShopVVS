package com.example.vvs.webapp.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.TimeUnit;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SeleniumLoginTests {

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
    public void whenGetToLoginPage_useValidCredentials() throws InterruptedException {

        String loginURL = "http://localhost:" + localPort + "/login";
        WebDriverWait wait = new WebDriverWait(driver, 30, 1000);
        driver.get(loginURL);

        //ne aflam pe pagina de login
        assertEquals("Are you a new user? Register here", driver.findElement(By.id("login_message")).getText());

        //executa login cu credentiale bune
        By login_email = By.id("username");
        wait.until(presenceOfElementLocated(login_email));
        driver.findElement(login_email).sendKeys("dan@yahoo.com");

        By login_passwd = By.id("password");
        wait.until(presenceOfElementLocated(login_passwd));
        driver.findElement(login_passwd).sendKeys("parola");

        By loginLogin = By.id("login-submit");
        wait.until(presenceOfElementLocated(loginLogin));
        driver.findElement(loginLogin).click();

        assertEquals("dan@yahoo.com", driver.findElement(By.id("user_name_index")).getText());
        assertEquals("Logout", driver.findElement(By.id("logout_index")).getText());
    }

    @Test
    public void whenGetToLoginPage_useINValidCredentials() throws InterruptedException {

        String loginURL = "http://localhost:" + localPort + "/login";
        WebDriverWait wait = new WebDriverWait(driver, 30, 1000);
        driver.get(loginURL);

        //ne aflam pe pagina de login
        assertEquals("Are you a new user? Register here", driver.findElement(By.id("login_message")).getText());

        //executa login cu credentiale gresite sau fara
        By login_email = By.id("username");
        wait.until(presenceOfElementLocated(login_email));
        driver.findElement(login_email).sendKeys("dan@yahoo.co");

        By login_passwd = By.id("password");
        wait.until(presenceOfElementLocated(login_passwd));
        driver.findElement(login_passwd).sendKeys("parol");

        By loginLogin = By.id("login-submit");
        wait.until(presenceOfElementLocated(loginLogin));
        driver.findElement(loginLogin).click();

        wait.until(presenceOfElementLocated(By.id("alert_invalid_login")));
        assertEquals("Invalid username or password!", driver.findElement(By.id("alert_invalid_login")).getText());
    }

    @Test
    public void whenGetToLoginPage_clickRegister() throws InterruptedException {

        String loginURL = "http://localhost:" + localPort + "/login";
        WebDriverWait wait = new WebDriverWait(driver, 30, 1000);
        driver.get(loginURL);

        By loginRegisterBtn = By.id("login_register_btn");
        wait.until(presenceOfElementLocated(loginRegisterBtn));
        driver.findElement(loginRegisterBtn).click();

        //pagina corecta, de registration
        assertEquals("http://localhost:"+localPort+"/registration", driver.getCurrentUrl());

        //apare textul dorit pe pagina registration
        assertEquals("Registration Page", driver.findElement(By.id("registration_page")).getText());
    }
}
