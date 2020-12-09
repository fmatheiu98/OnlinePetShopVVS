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
public class SeleniumCombinedTestsWithErrors {

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
        this.driver.manage().window().maximize();
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void appFlowTest_WithSomeErrors() throws InterruptedException {

        WebDriverWait wait = new WebDriverWait(driver, 30, 1000);
        driver.get(serverUrl);
        assertEquals("Pet Shop - Home", driver.findElement(By.id("pet_shop_home")).getText());
        assertEquals("Login", driver.findElement(By.id("login_index")).getText());

        //test existenta produs
        assertEquals("Minge - 17 RON", driver.findElement(By.id("2_Minge")).getText());

        Thread.sleep(2000);

        //mergi la login
        By loginIndex = By.id("login_index");
        wait.until(presenceOfElementLocated(loginIndex));
        driver.findElement(loginIndex).click();

        //pagina corecta
        assertEquals("http://localhost:"+localPort+"/login", driver.getCurrentUrl());

        Thread.sleep(2000);

        //mergi la registration
        By go_to_regstr = By.id("login_register_btn");
        wait.until(presenceOfElementLocated(go_to_regstr));
        driver.findElement(go_to_regstr).click();

        //pagina corecta
        assertEquals("http://localhost:"+localPort+"/registration", driver.getCurrentUrl());

        //executa registration cu credentiale care lipsesc(last-name,email si parola lipsesc)
        By first_nameErr = By.id("first_name");
        wait.until(presenceOfElementLocated(first_nameErr));
        driver.findElement(first_nameErr).sendKeys("regstr_test434");

        Thread.sleep(2000);

        By pressRegisterErr = By.id("register-submit");
        wait.until(presenceOfElementLocated(pressRegisterErr));
        driver.findElement(pressRegisterErr).click();

        Thread.sleep(2000);

        //trebuie sa primesc mesajul de eroare la campul last name
        assertEquals("Please fill out this field.", driver.findElement(By.id("last_name")).getAttribute("validationMessage"));

        driver.navigate().refresh();

        //executa registration cu un mail existent in BD.
        By first_nameErr2 = By.id("first_name");
        wait.until(presenceOfElementLocated(first_nameErr2));
        driver.findElement(first_nameErr2).sendKeys("regstr_test");
        Thread.sleep(1000);

        By last_nameErr2 = By.id("last_name");
        wait.until(presenceOfElementLocated(last_nameErr2));
        driver.findElement(last_nameErr2).sendKeys("regstr_test");
        Thread.sleep(1000);

        By regstr_emailErr2 = By.id("email");
        wait.until(presenceOfElementLocated(regstr_emailErr2));
        driver.findElement(regstr_emailErr2).sendKeys("dan@yahoo.com");
        Thread.sleep(1000);

        By regstr_passwdErr2 = By.id("passwd");
        wait.until(presenceOfElementLocated(regstr_passwdErr2));
        driver.findElement(regstr_passwdErr2).sendKeys("regstr_passwd_test");
        Thread.sleep(1000);

        By pressRegisterErr2 = By.id("register-submit");
        wait.until(presenceOfElementLocated(pressRegisterErr2));
        driver.findElement(pressRegisterErr2).click();
        Thread.sleep(1000);

        //eroare la inregistrare
        assertEquals("Email already registered!", driver.findElement(By.id("registration_failed")).getText());

        //suntem pe URL-ul corect
        assertEquals("http://localhost:"+localPort+"/registration?failure", driver.getCurrentUrl());


        driver.navigate().refresh();

        //executa registration cu credentiale bune
        By first_name = By.id("first_name");
        wait.until(presenceOfElementLocated(first_name));
        driver.findElement(first_name).sendKeys("regstr_test");
        Thread.sleep(2000);

        By last_name = By.id("last_name");
        wait.until(presenceOfElementLocated(last_name));
        driver.findElement(last_name).sendKeys("regstr_test");
        Thread.sleep(1000);

        By regstr_email = By.id("email");
        wait.until(presenceOfElementLocated(regstr_email));
        String reg_email = "regstr_email_test"+(int)(Math.random()*1000000000);
        driver.findElement(regstr_email).sendKeys(reg_email);
        Thread.sleep(1000);

        By regstr_passwd = By.id("passwd");
        wait.until(presenceOfElementLocated(regstr_passwd));
        driver.findElement(regstr_passwd).sendKeys("regstr_passwd_test");
        Thread.sleep(1000);

        By pressRegister = By.id("register-submit");
        wait.until(presenceOfElementLocated(pressRegister));
        driver.findElement(pressRegister).click();
        Thread.sleep(2000);

        assertEquals("Your registration was successful!", driver.findElement(By.id("registration_successful")).getText());

        By pressLogin = By.id("login_here_reg");
        wait.until(presenceOfElementLocated(pressLogin));
        driver.findElement(pressLogin).click();
        Thread.sleep(2000);

        //pagina corecta
        assertEquals("http://localhost:"+localPort+"/login", driver.getCurrentUrl());

        //executa login cu credentiale gresite sau fara
        By login_emailErr = By.id("username");
        wait.until(presenceOfElementLocated(login_emailErr));
        driver.findElement(login_emailErr).sendKeys("656");
        Thread.sleep(1000);

        By login_passwdErr = By.id("password");
        wait.until(presenceOfElementLocated(login_passwdErr));
        driver.findElement(login_passwdErr).sendKeys("8435357hj");
        Thread.sleep(1000);

        By loginLoginErr = By.id("login-submit");
        wait.until(presenceOfElementLocated(loginLoginErr));
        driver.findElement(loginLoginErr).click();
        Thread.sleep(2000);

        //suntem pe URL-ul corect
        assertEquals("http://localhost:"+localPort+"/login?error", driver.getCurrentUrl());

        wait.until(presenceOfElementLocated(By.id("alert_invalid_login")));
        assertEquals("Invalid username or password!", driver.findElement(By.id("alert_invalid_login")).getText());


        //executa login cu credentialele inregistrate mai sus, bune
        By login_email = By.id("username");
        wait.until(presenceOfElementLocated(login_email));
        driver.findElement(login_email).sendKeys(reg_email);
        Thread.sleep(1000);

        By login_passwd = By.id("password");
        wait.until(presenceOfElementLocated(login_passwd));
        driver.findElement(login_passwd).sendKeys("regstr_passwd_test");
        Thread.sleep(1000);

        //apasa login
        By loginLogin = By.id("login-submit");
        wait.until(presenceOfElementLocated(loginLogin));
        driver.findElement(loginLogin).click();
        Thread.sleep(2000);

        //am ajuns pe homepage, corect
        assertEquals(reg_email, driver.findElement(By.id("user_name_index")).getText());
        assertEquals("Logout", driver.findElement(By.id("logout_index")).getText());
        Thread.sleep(1000);

        //test butoane add to cart
        for(int i=1;i<=7;i++)
        {
            assertEquals("Add to Cart", driver.findElement(By.id("AddToCart_"+i)).getText());
        }

        //test buton detalii
        for(int i=1;i<=7;i++)
        {
            assertEquals("Details", driver.findElement(By.id("Details_"+i)).getText());
        }

        //click pe details 4(hrana pesti)
        By details_btn = By.id("Details_4");
        wait.until(presenceOfElementLocated(details_btn));
        driver.findElement(details_btn).click();
        Thread.sleep(1000);

        //pagina corecta(/product/4) cu detaliile este afisata
        assertEquals("http://localhost:"+localPort+"/product/4", driver.getCurrentUrl());

        //teste pentru verificarea produsului
        assertEquals("Product Name: Hrana Pesti", driver.findElement(By.id("pname_4")).getText());
        assertEquals("Price: 14 RON", driver.findElement(By.id("pprice_4")).getText());
        assertEquals("Description: Hrana Pentru Pesti", driver.findElement(By.id("pdescr_4")).getText());
        assertEquals("Add to Cart", driver.findElement(By.id("AddToCartDetails_4")).getText());
        Thread.sleep(1000);

        //click pe add to cart, din pagina de detalii(hrana pesti)
        By add_toCart_fromDetails = By.id("AddToCartDetails_4");
        wait.until(presenceOfElementLocated(add_toCart_fromDetails));
        driver.findElement(add_toCart_fromDetails).click();
        Thread.sleep(1000);

        //s-a ajuns pe pagina principala
        assertEquals("http://localhost:"+localPort+"/", driver.getCurrentUrl());

        //verificare nr. produse in cart
        assertEquals("No. of items in cart: 1", driver.findElement(By.id("no_items_cart")).getText());

        //adaug diferite produse in cos
        By addToCart_1 = By.id("AddToCart_1");
        wait.until(presenceOfElementLocated(addToCart_1));
        driver.findElement(addToCart_1).click();
        Thread.sleep(1000);

        By addToCart_2 = By.id("AddToCart_2");
        wait.until(presenceOfElementLocated(addToCart_2));
        driver.findElement(addToCart_2).click();
        Thread.sleep(1000);

        By addToCart_3 = By.id("AddToCart_3");
        wait.until(presenceOfElementLocated(addToCart_3));
        driver.findElement(addToCart_3).click();
        Thread.sleep(1000);

        By addToCart_4 = By.id("AddToCart_4");
        wait.until(presenceOfElementLocated(addToCart_4));
        driver.findElement(addToCart_4).click();
        Thread.sleep(1000);
        wait.until(presenceOfElementLocated(addToCart_4));
        driver.findElement(addToCart_4).click();
        Thread.sleep(1000);

        //verificare nr. produse in cart
        assertEquals("No. of items in cart: 6", driver.findElement(By.id("no_items_cart")).getText());

        //navighez spre cart
        By go_to_cart = By.id("go_to_cart");
        wait.until(presenceOfElementLocated(go_to_cart));
        driver.findElement(go_to_cart).click();
        Thread.sleep(2000);

        //am ajuns la pagina cart
        //pagina corecta, de cart
        assertEquals("http://localhost:"+localPort+"/cart", driver.getCurrentUrl());
        assertEquals("Your Cart", driver.findElement(By.id("your_cart")).getText());
        assertEquals("No. of items in cart: 6", driver.findElement(By.id("nr_items_cart")).getText());


        //verific ce este in cos
        assertEquals("Colivie", driver.findElement(By.id("prod_Colivie")).getText());
        assertEquals("Minge", driver.findElement(By.id("prod_Minge")).getText());
        assertEquals("Litiera", driver.findElement(By.id("prod_Litiera")).getText());
        assertEquals("Hrana pesti", driver.findElement(By.id("prod_Hrana pesti")).getText());

        assertEquals("1 PIECE(S) *", driver.findElement(By.id("pieces_Colivie")).getText());
        assertEquals("1 PIECE(S) *", driver.findElement(By.id("pieces_Minge")).getText());
        assertEquals("1 PIECE(S) *", driver.findElement(By.id("pieces_Litiera")).getText());
        assertEquals("3 PIECE(S) *", driver.findElement(By.id("pieces_Hrana pesti")).getText());

        assertEquals("100 RON EACH =", driver.findElement(By.id("price_Colivie")).getText());
        assertEquals("17 RON EACH =", driver.findElement(By.id("price_Minge")).getText());
        assertEquals("32 RON EACH =", driver.findElement(By.id("price_Litiera")).getText());
        assertEquals("14 RON EACH =", driver.findElement(By.id("price_Hrana pesti")).getText());

        assertEquals("100 RON", driver.findElement(By.id("total_Colivie")).getText());
        assertEquals("17 RON", driver.findElement(By.id("total_Minge")).getText());
        assertEquals("32 RON", driver.findElement(By.id("total_Litiera")).getText());
        assertEquals("42 RON", driver.findElement(By.id("total_Hrana pesti")).getText());

        //verificam totalul din cos
        assertEquals("Total : 191 RON", driver.findElement(By.id("total_cart")).getText());

        //adaugam/scoatem produse din cos cu +, -
        //adaug 2 colivii
        By add1ToCart_Colivie = By.id("plus_Colivie");
        wait.until(presenceOfElementLocated(add1ToCart_Colivie));
        driver.findElement(add1ToCart_Colivie).click();
        Thread.sleep(1000);
        wait.until(presenceOfElementLocated(add1ToCart_Colivie));
        driver.findElement(add1ToCart_Colivie).click();
        Thread.sleep(1000);

        //scad 1 hrana pesti
        By sub1_Pesti = By.id("minus_Hrana pesti");
        wait.until(presenceOfElementLocated(sub1_Pesti));
        driver.findElement(sub1_Pesti).click();
        Thread.sleep(1000);

        //delete total la minge(apas pe delete minge)
        By del_minge = By.id("delete_Minge");
        wait.until(presenceOfElementLocated(del_minge));
        driver.findElement(del_minge).click();
        Thread.sleep(2000);

        //pagina corecta
        assertEquals("http://localhost:"+localPort+"/cart", driver.getCurrentUrl());


        //verific ce se afla in cos dupa multiple schimbari
        //verific ce este in cos
        assertEquals("Colivie", driver.findElement(By.id("prod_Colivie")).getText());
        assertEquals("Litiera", driver.findElement(By.id("prod_Litiera")).getText());
        assertEquals("Hrana pesti", driver.findElement(By.id("prod_Hrana pesti")).getText());

        assertEquals("3 PIECE(S) *", driver.findElement(By.id("pieces_Colivie")).getText());
        assertEquals("1 PIECE(S) *", driver.findElement(By.id("pieces_Litiera")).getText());
        assertEquals("2 PIECE(S) *", driver.findElement(By.id("pieces_Hrana pesti")).getText());

        assertEquals("100 RON EACH =", driver.findElement(By.id("price_Colivie")).getText());
        assertEquals("32 RON EACH =", driver.findElement(By.id("price_Litiera")).getText());
        assertEquals("14 RON EACH =", driver.findElement(By.id("price_Hrana pesti")).getText());

        assertEquals("300 RON", driver.findElement(By.id("total_Colivie")).getText());
        assertEquals("32 RON", driver.findElement(By.id("total_Litiera")).getText());
        assertEquals("28 RON", driver.findElement(By.id("total_Hrana pesti")).getText());

        //verificam totalul din cos
        assertEquals("Total : 360 RON", driver.findElement(By.id("total_cart")).getText());



        //merg inapoi pe homepage
        By goBackHomePage = By.id("go_to_home");
        wait.until(presenceOfElementLocated(goBackHomePage));
        driver.findElement(goBackHomePage).click();
        Thread.sleep(2000);

        //pagina corecta
        assertEquals("http://localhost:"+localPort+"/", driver.getCurrentUrl());
        assertEquals("Home Page", driver.getTitle());

        //adaug in cart 2 produse Husa
        By addToCart_7 = By.id("AddToCart_7");
        wait.until(presenceOfElementLocated(addToCart_7));
        driver.findElement(addToCart_7).click();
        Thread.sleep(1000);
        wait.until(presenceOfElementLocated(addToCart_7));
        driver.findElement(addToCart_7).click();
        Thread.sleep(1000);

        //merg la cart, si verific totalul
        wait.until(presenceOfElementLocated(go_to_cart));
        driver.findElement(go_to_cart).click();
        Thread.sleep(1000);

        //pagina corecta
        assertEquals("http://localhost:"+localPort+"/cart", driver.getCurrentUrl());

        //verific ce se afla in cos dupa alte schimbari facute
        //verific ce este in cos
        assertEquals("Colivie", driver.findElement(By.id("prod_Colivie")).getText());
        assertEquals("Litiera", driver.findElement(By.id("prod_Litiera")).getText());
        assertEquals("Hrana pesti", driver.findElement(By.id("prod_Hrana pesti")).getText());
        assertEquals("Husa", driver.findElement(By.id("prod_Husa")).getText());

        assertEquals("3 PIECE(S) *", driver.findElement(By.id("pieces_Colivie")).getText());
        assertEquals("1 PIECE(S) *", driver.findElement(By.id("pieces_Litiera")).getText());
        assertEquals("2 PIECE(S) *", driver.findElement(By.id("pieces_Hrana pesti")).getText());
        assertEquals("2 PIECE(S) *", driver.findElement(By.id("pieces_Husa")).getText());

        assertEquals("100 RON EACH =", driver.findElement(By.id("price_Colivie")).getText());
        assertEquals("32 RON EACH =", driver.findElement(By.id("price_Litiera")).getText());
        assertEquals("14 RON EACH =", driver.findElement(By.id("price_Hrana pesti")).getText());
        assertEquals("100 RON EACH =", driver.findElement(By.id("price_Husa")).getText());

        assertEquals("300 RON", driver.findElement(By.id("total_Colivie")).getText());
        assertEquals("32 RON", driver.findElement(By.id("total_Litiera")).getText());
        assertEquals("28 RON", driver.findElement(By.id("total_Hrana pesti")).getText());
        assertEquals("200 RON", driver.findElement(By.id("total_Husa")).getText());

        assertEquals("Total : 560 RON", driver.findElement(By.id("total_cart")).getText());

        //apas pe checkout
        By go_to_checkout = By.id("go_to_chk");
        wait.until(presenceOfElementLocated(go_to_checkout));
        driver.findElement(go_to_checkout).click();
        Thread.sleep(1000);

        //s-a ajuns pe pagina de checkout
        assertEquals("http://localhost:"+localPort+"/checkout", driver.getCurrentUrl());
        assertEquals("Checkout", driver.findElement(By.id("checkout_page")).getText());

        //executa checkout cu credentiale care lipsesc
        By checkout_btnErr = By.id("checkout_btn");
        wait.until(presenceOfElementLocated(checkout_btnErr));
        driver.findElement(checkout_btnErr).click();

        //trebuie sa primesc mesajul de eroare la campul first name
        assertEquals("Please fill out this field.", driver.findElement(By.id("first_name")).getAttribute("validationMessage"));

        driver.navigate().refresh();

        Thread.sleep(2000);
        //executa checkout cu credentiale bune
        By first_name_chk = By.id("first_name");
        wait.until(presenceOfElementLocated(first_name_chk));
        driver.findElement(first_name_chk).sendKeys("chk_test");
        Thread.sleep(1000);

        By last_name_chk = By.id("last_name");
        wait.until(presenceOfElementLocated(last_name_chk));
        driver.findElement(last_name_chk).sendKeys("chk_test");
        Thread.sleep(1000);

        By chk_email = By.id("email");
        wait.until(presenceOfElementLocated(chk_email));
        String reg_email_chk = "chk_email_test" + (int) (Math.random() * 1000000000);
        driver.findElement(chk_email).sendKeys(reg_email_chk);
        Thread.sleep(1000);

        By chk_country = By.id("country");
        wait.until(presenceOfElementLocated(chk_country));
        driver.findElement(chk_country).sendKeys("chk_country_test");
        Thread.sleep(1000);

        By chk_address = By.id("address");
        wait.until(presenceOfElementLocated(chk_address));
        driver.findElement(chk_address).sendKeys("chk_address_test");
        Thread.sleep(1000);

        By checkout_btn = By.id("checkout_btn");
        wait.until(presenceOfElementLocated(checkout_btn));
        driver.findElement(checkout_btn).click();
        Thread.sleep(1000);

        assertEquals("Your order was successfully registered.", driver.findElement(By.id("checkout_successful")).getText());
    }
}
