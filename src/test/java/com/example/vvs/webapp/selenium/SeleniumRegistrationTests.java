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
public class SeleniumRegistrationTests {
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
    public void whenGetToRegistrationPage_registerWithValidCredentials_andThenLogin() throws InterruptedException {

        String registrationURL = "http://localhost:" + localPort + "/registration";
        WebDriverWait wait = new WebDriverWait(driver, 30, 1000);
        driver.get(registrationURL);

        //ne aflam pe pagina de registration
        assertEquals("Already registered? Login here", driver.findElement(By.id("already_registered")).getText());

        //executa registration cu credentiale bune
        By first_name = By.id("first_name");
        wait.until(presenceOfElementLocated(first_name));
        driver.findElement(first_name).sendKeys("regstr_test");

        By last_name = By.id("last_name");
        wait.until(presenceOfElementLocated(last_name));
        driver.findElement(last_name).sendKeys("regstr_test");

        By regstr_email = By.id("email");
        wait.until(presenceOfElementLocated(regstr_email));
        String reg_email = "regstr_email_test"+(int)(Math.random()*1000000000);
        driver.findElement(regstr_email).sendKeys(reg_email);

        By regstr_passwd = By.id("passwd");
        wait.until(presenceOfElementLocated(regstr_passwd));
        driver.findElement(regstr_passwd).sendKeys("regstr_passwd_test");

        By pressRegister = By.id("register-submit");
        wait.until(presenceOfElementLocated(pressRegister));
        driver.findElement(pressRegister).click();

        assertEquals("Your registration was successful!", driver.findElement(By.id("registration_successful")).getText());

        By pressLogin = By.id("login_here_reg");
        wait.until(presenceOfElementLocated(pressLogin));
        driver.findElement(pressLogin).click();

        //executa login cu credentialele inregistrate mai sus
        By login_email = By.id("username");
        wait.until(presenceOfElementLocated(login_email));
        driver.findElement(login_email).sendKeys(reg_email);

        By login_passwd = By.id("password");
        wait.until(presenceOfElementLocated(login_passwd));
        driver.findElement(login_passwd).sendKeys("regstr_passwd_test");

        By loginLogin = By.id("login-submit");
        wait.until(presenceOfElementLocated(loginLogin));
        driver.findElement(loginLogin).click();

        assertEquals(reg_email, driver.findElement(By.id("user_name_index")).getText());
        assertEquals("Logout", driver.findElement(By.id("logout_index")).getText());

    }

    @Test
    public void whenGetToRegistrationPage_registerWithEmptyFields() throws InterruptedException {

        String registrationURL = "http://localhost:" + localPort + "/registration";
        WebDriverWait wait = new WebDriverWait(driver, 30, 1000);
        driver.get(registrationURL);

        //ne aflam pe pagina de registration
        assertEquals("Already registered? Login here", driver.findElement(By.id("already_registered")).getText());

        //executa registration cu credentiale care lipsesc(last-name,email si parola lipsesc)
        By first_name = By.id("first_name");
        wait.until(presenceOfElementLocated(first_name));
        driver.findElement(first_name).sendKeys("regstr_test");

        By pressRegister = By.id("register-submit");
        wait.until(presenceOfElementLocated(pressRegister));
        driver.findElement(pressRegister).click();

        //trebuie sa primesc mesajul de eroare la campul last name
        assertEquals("Please fill out this field.", driver.findElement(By.id("last_name")).getAttribute("validationMessage"));
    }
}
