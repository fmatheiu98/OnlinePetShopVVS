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
        assertEquals("User Login", driver.getTitle());

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

        //suntem pe URL-ul corect
        assertEquals("http://localhost:"+localPort+"/", driver.getCurrentUrl());

        assertEquals("dan@yahoo.com", driver.findElement(By.id("user_name_index")).getText());
        assertEquals("Logout", driver.findElement(By.id("logout_index")).getText());
    }

    @Test
    public void whenGetToLoginPage_useInvalidCredentials() throws InterruptedException {

        String loginURL = "http://localhost:" + localPort + "/login";
        WebDriverWait wait = new WebDriverWait(driver, 30, 1000);
        driver.get(loginURL);

        //ne aflam pe pagina de login
        assertEquals("Are you a new user? Register here", driver.findElement(By.id("login_message")).getText());
        assertEquals("User Login", driver.getTitle());

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

        //suntem pe URL-ul corect
        assertEquals("http://localhost:"+localPort+"/login?error", driver.getCurrentUrl());

        wait.until(presenceOfElementLocated(By.id("alert_invalid_login")));
        assertEquals("Invalid username or password!", driver.findElement(By.id("alert_invalid_login")).getText());
    }

    @Test
    public void whenGetToLoginPage_useINValidCredentials_thenUseCorrectCredentialsAndLoginAgain() throws InterruptedException {

        String loginURL = "http://localhost:" + localPort + "/login";
        WebDriverWait wait = new WebDriverWait(driver, 30, 1000);
        driver.get(loginURL);

        //ne aflam pe pagina de login
        assertEquals("Are you a new user? Register here", driver.findElement(By.id("login_message")).getText());
        assertEquals("User Login", driver.getTitle());

        //executa login cu credentiale gresite sau fara
        By login_email = By.id("username");
        wait.until(presenceOfElementLocated(login_email));
        driver.findElement(login_email).sendKeys("gf");

        By login_passwd = By.id("password");
        wait.until(presenceOfElementLocated(login_passwd));
        driver.findElement(login_passwd).sendKeys("8");

        By loginLogin = By.id("login-submit");
        wait.until(presenceOfElementLocated(loginLogin));
        driver.findElement(loginLogin).click();

        //suntem pe URL-ul corect
        assertEquals("http://localhost:"+localPort+"/login?error", driver.getCurrentUrl());

        wait.until(presenceOfElementLocated(By.id("alert_invalid_login")));
        assertEquals("Invalid username or password!", driver.findElement(By.id("alert_invalid_login")).getText());

        //executa login cu credentiale bune
        By login_emailG = By.id("username");
        wait.until(presenceOfElementLocated(login_emailG));
        driver.findElement(login_emailG).sendKeys("dan@yahoo.com");

        By login_passwdG = By.id("password");
        wait.until(presenceOfElementLocated(login_passwdG));
        driver.findElement(login_passwdG).sendKeys("parola");

        By loginLoginG = By.id("login-submit");
        wait.until(presenceOfElementLocated(loginLoginG));
        driver.findElement(loginLoginG).click();

        //suntem pe URL-ul corect
        assertEquals("http://localhost:"+localPort+"/", driver.getCurrentUrl());

        assertEquals("dan@yahoo.com", driver.findElement(By.id("user_name_index")).getText());
        assertEquals("Logout", driver.findElement(By.id("logout_index")).getText());
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
        assertEquals("User Registration", driver.getTitle());

        //apare textul dorit pe pagina registration
        assertEquals("Registration Page", driver.findElement(By.id("registration_page")).getText());
    }
}
