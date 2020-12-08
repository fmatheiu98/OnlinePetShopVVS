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
public class SeleniumCartTests {
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
    public void cartTestFunctionality() throws InterruptedException {

        WebDriverWait wait = new WebDriverWait(driver, 30, 1000);
        driver.get(serverUrl);

        //executa login
        By loginIndex = By.id("login_index");
        wait.until(presenceOfElementLocated(loginIndex));
        driver.findElement(loginIndex).click();

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

        By addToCart_1 = By.id("AddToCart_1");
        wait.until(presenceOfElementLocated(addToCart_1));
        driver.findElement(addToCart_1).click();

        By addToCart_2 = By.id("AddToCart_2");
        wait.until(presenceOfElementLocated(addToCart_2));
        driver.findElement(addToCart_2).click();

        By addToCart_3 = By.id("AddToCart_3");
        wait.until(presenceOfElementLocated(addToCart_3));
        driver.findElement(addToCart_3).click();

        By addToCart_4 = By.id("AddToCart_4");
        wait.until(presenceOfElementLocated(addToCart_4));
        driver.findElement(addToCart_4).click();
        wait.until(presenceOfElementLocated(addToCart_4));
        driver.findElement(addToCart_4).click();

        //pagina corecta
        assertEquals("http://localhost:"+localPort+"/", driver.getCurrentUrl());

        //verificare nr. produse in cart
        assertEquals("No. of items in cart: 5", driver.findElement(By.id("no_items_cart")).getText());

        //navigate to cart
        By go_to_cart = By.id("go_to_cart");
        wait.until(presenceOfElementLocated(go_to_cart));
        driver.findElement(go_to_cart).click();

        //am ajuns la pagina cart
        //pagina corecta, de cart
        assertEquals("http://localhost:"+localPort+"/cart", driver.getCurrentUrl());
        assertEquals("Your Cart", driver.findElement(By.id("your_cart")).getText());
        assertEquals("No. of items in cart: 5", driver.findElement(By.id("nr_items_cart")).getText());


        //verific ce este in cos
        assertEquals("Colivie", driver.findElement(By.id("prod_Colivie")).getText());
        assertEquals("Minge", driver.findElement(By.id("prod_Minge")).getText());
        assertEquals("Litiera", driver.findElement(By.id("prod_Litiera")).getText());
        assertEquals("Hrana pesti", driver.findElement(By.id("prod_Hrana pesti")).getText());

        assertEquals("1 PIECE(S) *", driver.findElement(By.id("pieces_Colivie")).getText());
        assertEquals("1 PIECE(S) *", driver.findElement(By.id("pieces_Minge")).getText());
        assertEquals("1 PIECE(S) *", driver.findElement(By.id("pieces_Litiera")).getText());
        assertEquals("2 PIECE(S) *", driver.findElement(By.id("pieces_Hrana pesti")).getText());

        assertEquals("100 RON EACH =", driver.findElement(By.id("price_Colivie")).getText());
        assertEquals("17 RON EACH =", driver.findElement(By.id("price_Minge")).getText());
        assertEquals("32 RON EACH =", driver.findElement(By.id("price_Litiera")).getText());
        assertEquals("14 RON EACH =", driver.findElement(By.id("price_Hrana pesti")).getText());

        assertEquals("100 RON", driver.findElement(By.id("total_Colivie")).getText());
        assertEquals("17 RON", driver.findElement(By.id("total_Minge")).getText());
        assertEquals("32 RON", driver.findElement(By.id("total_Litiera")).getText());
        assertEquals("28 RON", driver.findElement(By.id("total_Hrana pesti")).getText());

        //verificam totalul din cos
        assertEquals("Total : 177 RON", driver.findElement(By.id("total_cart")).getText());

        //adaugam/scoatem produse din cos cu +, -
        //adaug 2 colivii
        By add1ToCart_Colivie = By.id("plus_Colivie");
        wait.until(presenceOfElementLocated(add1ToCart_Colivie));
        driver.findElement(add1ToCart_Colivie).click();
        wait.until(presenceOfElementLocated(add1ToCart_Colivie));
        driver.findElement(add1ToCart_Colivie).click();

        //scad 1 hrana pesti
        By sub1_Pesti = By.id("minus_Hrana pesti");
        wait.until(presenceOfElementLocated(sub1_Pesti));
        driver.findElement(sub1_Pesti).click();

        //delete total la minge(apas pe delete minge)
        By del_minge = By.id("delete_Minge");
        wait.until(presenceOfElementLocated(del_minge));
        driver.findElement(del_minge).click();

        //pagina corecta
        assertEquals("http://localhost:"+localPort+"/cart", driver.getCurrentUrl());

        //verific ce se afla in cos dupa multiple schimbari
        //verific ce este in cos
        assertEquals("Colivie", driver.findElement(By.id("prod_Colivie")).getText());
        assertEquals("Litiera", driver.findElement(By.id("prod_Litiera")).getText());
        assertEquals("Hrana pesti", driver.findElement(By.id("prod_Hrana pesti")).getText());

        assertEquals("3 PIECE(S) *", driver.findElement(By.id("pieces_Colivie")).getText());
        assertEquals("1 PIECE(S) *", driver.findElement(By.id("pieces_Litiera")).getText());
        assertEquals("1 PIECE(S) *", driver.findElement(By.id("pieces_Hrana pesti")).getText());

        assertEquals("100 RON EACH =", driver.findElement(By.id("price_Colivie")).getText());
        assertEquals("32 RON EACH =", driver.findElement(By.id("price_Litiera")).getText());
        assertEquals("14 RON EACH =", driver.findElement(By.id("price_Hrana pesti")).getText());

        assertEquals("300 RON", driver.findElement(By.id("total_Colivie")).getText());
        assertEquals("32 RON", driver.findElement(By.id("total_Litiera")).getText());
        assertEquals("14 RON", driver.findElement(By.id("total_Hrana pesti")).getText());

        //verificam totalul din cos
        assertEquals("Total : 346 RON", driver.findElement(By.id("total_cart")).getText());

        //merg inapoi pe homepage
        By goBackHomePage = By.id("go_to_home");
        wait.until(presenceOfElementLocated(goBackHomePage));
        driver.findElement(goBackHomePage).click();

        //pagina corecta, home
        assertEquals("http://localhost:"+localPort+"/", driver.getCurrentUrl());

        //adaug in cart 2 produse Husa
        By addToCart_7 = By.id("AddToCart_7");
        wait.until(presenceOfElementLocated(addToCart_7));
        driver.findElement(addToCart_7).click();
        wait.until(presenceOfElementLocated(addToCart_7));
        driver.findElement(addToCart_7).click();

        //merg la cart, si verific totalul
        wait.until(presenceOfElementLocated(go_to_cart));
        driver.findElement(go_to_cart).click();

        //verific ce se afla in cos dupa alte schimbari facute
        //verific ce este in cos
        assertEquals("Colivie", driver.findElement(By.id("prod_Colivie")).getText());
        assertEquals("Litiera", driver.findElement(By.id("prod_Litiera")).getText());
        assertEquals("Hrana pesti", driver.findElement(By.id("prod_Hrana pesti")).getText());
        assertEquals("Husa", driver.findElement(By.id("prod_Husa")).getText());

        assertEquals("3 PIECE(S) *", driver.findElement(By.id("pieces_Colivie")).getText());
        assertEquals("1 PIECE(S) *", driver.findElement(By.id("pieces_Litiera")).getText());
        assertEquals("1 PIECE(S) *", driver.findElement(By.id("pieces_Hrana pesti")).getText());
        assertEquals("2 PIECE(S) *", driver.findElement(By.id("pieces_Husa")).getText());

        assertEquals("100 RON EACH =", driver.findElement(By.id("price_Colivie")).getText());
        assertEquals("32 RON EACH =", driver.findElement(By.id("price_Litiera")).getText());
        assertEquals("14 RON EACH =", driver.findElement(By.id("price_Hrana pesti")).getText());
        assertEquals("100 RON EACH =", driver.findElement(By.id("price_Husa")).getText());

        assertEquals("300 RON", driver.findElement(By.id("total_Colivie")).getText());
        assertEquals("32 RON", driver.findElement(By.id("total_Litiera")).getText());
        assertEquals("14 RON", driver.findElement(By.id("total_Hrana pesti")).getText());
        assertEquals("200 RON", driver.findElement(By.id("total_Husa")).getText());

        assertEquals("Total : 546 RON", driver.findElement(By.id("total_cart")).getText());

        //delete la tot ce se afla in cos
        By del_1 = By.id("delete_Colivie");
        wait.until(presenceOfElementLocated(del_1));
        driver.findElement(del_1).click();

        By del_2 = By.id("delete_Litiera");
        wait.until(presenceOfElementLocated(del_2));
        driver.findElement(del_2).click();

        By del_3 = By.id("delete_Hrana pesti");
        wait.until(presenceOfElementLocated(del_3));
        driver.findElement(del_3).click();

        By del_4 = By.id("delete_Husa");
        wait.until(presenceOfElementLocated(del_4));
        driver.findElement(del_4).click();

        //verific total = 0
        assertEquals("Total : 0 RON", driver.findElement(By.id("total_cart")).getText());
    }
}
