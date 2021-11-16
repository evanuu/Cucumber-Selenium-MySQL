package SQLtest;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import other.SQLpass;

import java.sql.*;
import java.util.Properties;

public class SunflowerHouse {

    //connection object
    static Connection connection = null;
    private static Statement statement;
    private static ResultSet result = null;
    public static String dbURL = "jdbc:mysql://localhost:3306/archdaily";

    public static String dbUser = "root";
    static SQLpass p = new SQLpass();
    public static String dbPassword = p.pass;
    public static String dbdriver = "com.mysql.jdbc.Driver";

    public static WebDriver driver;

    @BeforeClass
    public void beforeClass() {
        System.setProperty("webdriver.chrome.driver", "webdrivers/chromedriver95.exe");

        ChromeOptions option =  new ChromeOptions();
        option.setPageLoadStrategy(PageLoadStrategy.NONE);
        driver = new ChromeDriver(option);

        Properties props = new Properties();
        props.setProperty("user", dbUser);
        props.setProperty("password", dbPassword);

        try {
            //register jdbc driver
            Class.forName(dbdriver).newInstance();
            //get connection to db (can use either statement)
            connection = DriverManager.getConnection(dbURL, props);
            System.out.println("connected to database");

            //statement object to send the SQL statement to the database
            statement = connection.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(priority = 1)
    public void checkProjectTitle() {
        driver.get("https://www.archdaily.com/638145/sunflower-house-cadaval-and-sola-morales");
        WebDriverWait wait = new WebDriverWait(driver, 10);

        //put this all in a pageFactory class...
        //found this loop + "(PageLoadStrategy.NONE)" together loads faster than NORMAL
        String pageLoadStatus = null;
        do {
            JavascriptExecutor jse = (JavascriptExecutor) driver;
            pageLoadStatus = (String)jse.executeScript("return document.readyState");
        } while (!pageLoadStatus.equals("complete")); {
            System.out.println("Page Loaded");
        }

        //close popup
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"gdpr-consent\"]"))).click();
        //click btn to show all details
        WebElement moreBtn = driver.findElement(By.xpath("//*[@id=\"js-toggle-extra-specs\"]"));
        Actions action = new Actions(driver);
        action.moveToElement(moreBtn).build().perform();
        moreBtn.click();

        WebElement n = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"content\"]/div/div[2]/header[2]/h1")));
        String name = n.getText();

        WebElement a = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"single-content\"]/div[2]/ul/li[1]/div/span[2]/a")));
        String architect = a.getText();

        WebElement y = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"single-content\"]/div[2]/ul/li[3]/span[2]/a")));
        String year = y.getText();

        WebElement c = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"single-content\"]/div[3]/ul/li[5]/div/span[2]")));
        String city = c.getText();

        WebElement co = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"single-content\"]/div[3]/ul/li[6]/div/span[2]/a")));
        String country = co.getText();

        String url = driver.getCurrentUrl();

        //name is pk
        boolean namePresent = false;
        try {
            String query = "select * from projectpages where name = " + "'" + name + "'";
            result = statement.executeQuery(query);

            if (result.next()) {
                System.out.println("Name - " + name + " - exists in db");
                namePresent = true;
            } else {
                System.out.println("-----");
                System.out.println("Name - " + name + " - not in db");
                String queryAdd = "insert into projectpages (name) values (" + "'" + name + "'" + ")";
                statement.executeUpdate(queryAdd);
                System.out.println("db updated with - name: " + name);
                namePresent = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String query = "select * from projectpages where architect = " + "'" + architect + "'";
            result = statement.executeQuery(query);

            if (result.next() && namePresent) {
                System.out.println("Architect - " + architect + " - exists in db");
            } else {
                System.out.println("-----");
                System.out.println("Architect - " + architect + " - not in db");
                String queryAdd = "update projectpages set architect = " + "'" + architect + "'" + "where name = " + "'" + name + "'";
                statement.executeUpdate(queryAdd);
                System.out.println("db updated with - architect: " + architect);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String query = "select * from projectpages where year = " + year + "";
            result = statement.executeQuery(query);

            if (result.next() && namePresent) {
                System.out.println("Year - " + year + " - exists in db");
            } else {
                System.out.println("-----");
                System.out.println("Year - " + year + " - not in db");
                String queryAdd = "update projectpages set year = " + year + " where name = " + "'" + name + "'";
                statement.executeUpdate(queryAdd);
                System.out.println("db updated with - year: " + year);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String query = "select * from projectpages where city = " + "'" + city + "'";
            result = statement.executeQuery(query);

            if (result.next() && namePresent) {
                System.out.println("City - " + city + " - exists in db");
            } else {
                System.out.println("-----");
                System.out.println("City - " + city + " - not in db");
                String queryAdd = "update projectpages set city = " + "'" + city + "'" + "where name = " + "'" + name + "'";
                statement.executeUpdate(queryAdd);
                System.out.println("db updated with - city: " + city);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String query = "select * from projectpages where country = " + "'" + country + "'";
            result = statement.executeQuery(query);

            if (result.next() && namePresent) {
                System.out.println("Country - " + country + " - exists in db");
            } else {
                System.out.println("-----");
                System.out.println("Country - " + country + " - not in db");
                String queryAdd = "update projectpages set country = " + "'" + country + "'" + "where name = " + "'" + name + "'";
                statement.executeUpdate(queryAdd);
                System.out.println("db updated with - country: " + country);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String query = "select * from projectpages where url = " + "'" + url + "'";
            result = statement.executeQuery(query);

            if (result.next() && namePresent) {
                System.out.println("url - " + url + " - exists in db");
            } else {
                System.out.println("-----");
                System.out.println("url - " + url + " - not in db");
                String queryAdd = "update projectpages set url = " + "'" + url + "'" + "where name = " + "'" + name + "'";
                statement.executeUpdate(queryAdd);
                System.out.println("db updated with - url: " + url);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @AfterClass
    public void afterClass() {
        //close db connection
        try {
            if (result != null)
                result.close();
            if (statement != null)
                connection.close();
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        driver.close();
    }

}
