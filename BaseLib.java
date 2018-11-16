package com.tos.library;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.io.TemporaryFilesystem;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

/**
 * @author vanshraj.singh QAS
 *         <hr>
 *         <a href='http://192.168.1.135:81'>Time Office Software</a>
 * @since July 15, 2018
 * @version Time Office Release 9.x.1
 */
public class BaseLib {
	public static WebDriver driver;
	protected static ExtentReports extent;
	protected static ExtentTest test;
	DesiredCapabilities capabilities;

	@BeforeSuite
	public void preConditions() {
		extent = ReportManager.getReporter();

	}

	@BeforeTest
	@Parameters("browser")
	public void launchBrowserAndApplication(String browserName) {
		test = extent
				.startTest("Application Launch",
						"Browser and Application Under Test Launch script has been initialized.")
				.assignAuthor("Vanshraj Singh");
		if (browserName.equalsIgnoreCase("firefox")) {
			System.setProperty("webdriver.gecko.driver", ".\\exefiles\\geckodriver.exe");
			driver=new FirefoxDriver();
			test.log(LogStatus.INFO, "Browser Launch", "Firefox browser has been launched and running up.");
			Reporter.log("Firefox Browser has been launched successfully.", true);
			extent.setTestRunnerOutput("Firefox browser has been launched and running up.");
		} else if (browserName.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", ".\\exefiles\\chromedriver.exe");
			ChromeDriverService service = new ChromeDriverService.Builder()
					.usingDriverExecutable(new File(".\\exefiles\\chromedriver.exe")).usingAnyFreePort().build();
			try {
				service.start();
			} catch (IOException e) {
				e.printStackTrace();
			}

			test.log(LogStatus.INFO, "Browser Launch", "Chrome browser has been launched and running up.");
			extent.setTestRunnerOutput("Chrome browser has been launched and running up.");
			Reporter.log("Chrome browser has been launched successfully.", true);
		} else if (browserName.equalsIgnoreCase("ie")) {
			System.setProperty("webdriver.ie.driver", ".\\exefiles\\IEDriverServer.exe");
			driver = new InternetExplorerDriver();
			test.log(LogStatus.INFO, "Browser Launch", "Internet Explorer browser has been launched and running up.");
			extent.setTestRunnerOutput("Internet Explorer browser has been launched and running up.");
			Reporter.log("IE browser has been launched successfully.", true);
		}
		driver.manage().window().maximize();
		test.log(LogStatus.INFO, "Browser Maximized", "Browser has been maximized to full window for the testing.");
		driver.navigate().to(GetPropertyValues.getPropertyValue("TestURL"));

		test.log(LogStatus.PASS, "AUT Launch", "Application Under Test is launched and running up.");
		extent.endTest(test);
		extent.setTestRunnerOutput("Login successful, Now testing can be started.");
	}

	@AfterMethod
	public void resultAnalysis(ITestResult result) {
		String fileName = result.getMethod().getMethodName();
		if (result.isSuccess()) {
			test.log(LogStatus.PASS, "Result Analysis",
					"Test result of the executed method " + fileName + " is successful.");
			Reporter.log("Test Result of method " + fileName + " is Successful", true);
			extent.setTestRunnerOutput("Test Result of method : " + fileName + " is Successful.");
		} else if (result.getStatus() == ITestResult.FAILURE) {
			ScreenShotsLib.getScreenShot(driver, fileName);
			String path = System.getProperty("user.dir") + "\\screenshots\\" + fileName + ".png";
			test.log(LogStatus.FAIL, "Result Analysis", "Test result of executed method " + fileName
					+ " is found failed, Please check the below attached screenshot for the cause of failure.");
			test.log(LogStatus.INFO, "Attachment :", "SnapShot of failed method " + fileName
					+ " is attached below, please verify :" + test.addScreenCapture(path));
			Reporter.log("ScreenShot of failed script " + fileName + " has been taken successfully.", true);
			extent.setTestRunnerOutput("Test Result of method : " + fileName + " is unsuccessful.");
			extent.endTest(test);
		} else if (result.getStatus() == ITestResult.SKIP) {
			test.log(LogStatus.SKIP, "Result Analysis",
					"Test result of executed method " + fileName
							+ " is skipped, Please check the below attached screenshot for the bug/error."
							+ result.getThrowable());
			extent.setTestRunnerOutput("Test Result of method : " + fileName + " is skipped.");
		}
		extent.flush();
	}

	public String bn() {
		Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
		return cap.getBrowserName();
	}

	@AfterTest
	public void postConditions() {
		Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
		extent.addSystemInfo("Environment", "QA");
		String browser = cap.getBrowserName();
		String browserVersion = cap.getVersion();
		extent.addSystemInfo("Browser Name", browser);
		extent.addSystemInfo("Browser Version", browserVersion);
		System.getProperty("os.name");
		String osver = System.getProperty("os.version");
		String osarch = System.getProperty("os.arch");
		extent.addSystemInfo("OS Version", osver);
		extent.addSystemInfo("OS Architecture", osarch);
		Map<String, String> env = System.getenv();
		String pro = env.get("NUMBER_OF_PROCESSORS");
		String pro_id = env.get("PROCESSOR_IDENTIFIER");
		String userdomain = env.get("USERDOMAIN");
		extent.addSystemInfo("NUMBER_OF_PROCESSORS", pro);
		extent.addSystemInfo("PROCESSOR_IDENTIFIER", pro_id);
		extent.addSystemInfo("USERDOMAIN", userdomain);

		Reporter.log("After test execution browser has been closed successfully.", true);
		test.log(LogStatus.PASS, "Close Browser", "After test execution browser has been closed successfully.");
		extent.setTestRunnerOutput("After test execution browser has been closed successfully.");

	}

	@AfterSuite
	public void tearDown() {
		TemporaryFilesystem.getDefaultTmpFS().deleteTemporaryFiles();
		extent.endTest(test);
		extent.flush();
	}

}
