package com.spectrum.appium.com.spectrum.appium;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;


public class App{
	@SuppressWarnings("rawtypes")
	public ThreadLocal<AndroidDriver> dr = new ThreadLocal<AndroidDriver>();
	public final String Result_FLD = System.getProperty("user.dir") + "\\Result";
	public final String Root = System.getProperty("user.dir");// .replace("\\", "/");
	public DateFormat For = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
	public Calendar cal = Calendar.getInstance();
	public File resfold = null;
	public String trfold = Root+ "\\trfold";
	public String timefold = "";
	public String ExecutionStarttime = For.format(cal.getTime()).toString();
	public final String Data = Root + "\\Test_Data.xlsx";
	public String curtcid = "";
	public static String udid;
	private static AppiumDriverLocalService service;
	public static String port;
	
	@Test
	public void Device_1() {
		App("device1");
	}
	
	@Test
	public void Device_2(){
		App("device2");
	}
	
	public String ReadMobileproperties(String fname, String propname) throws IOException {
		String fpath = Root + "\\src\\test\\resources\\config\\" + fname + ".properties";
		Properties prop = new Properties();
		FileInputStream input = new FileInputStream(fpath);

		prop.load(input);
		// prop.getProperty(propname);

		return prop.getProperty(propname);
	}
	
	public void App(String device) {
		try {
			String device_name = ReadMobileproperties(device, "DeviceName");
			String port_number = ReadMobileproperties(device, "appiumport");
			String package_name = ReadMobileproperties(device, "apppackage");
			String activity_name = ReadMobileproperties(device, "appactivity");
			String version = ReadMobileproperties(device, "version");
			String bsport = ReadMobileproperties(device, "bootstrapport");
			starter(port_number);
//			service = AppiumDriverLocalService.buildDefaultService();
//			service.start();
			createtimestampfold();
			ExtentReports extent = new ExtentReports();
			ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(trfold + "\\Master.html");
			System.setProperty("logfilename", trfold + "\\Logs");
			DOMConfigurator.configure("log4j.xml");
			info("Starting execution at +:"+ device_name + ExecutionStarttime);
			extent.attachReporter(htmlReporter);
			Fillo fillo = new Fillo();
			Connection conn = fillo.getConnection(Data);
			Recordset rs = conn.executeQuery("Select * from Test_Data where \"Execution Control\" = 'Yes'");
			
			while (rs.next()) {
				curtcid = rs.getField("Test Case ID");
				startTestCase(rs.getField("Test Case ID"));
				String Mobile = rs.getField("Device_Name");
				ExtentTest test = extent.createTest(rs.getField("Test Case ID"));
				String ussdstr = rs.getField("USSD_Sequence");
				String startussd = URLEncoder.encode(rs.getField("USSD_Code"),"UTF-8");
				DesiredCapabilities capabilities = new DesiredCapabilities();
				capabilities.setCapability("deviceName", device_name);
				//capabilities.setCapability("udid", udid);
				capabilities.setCapability("platformVersion", version);
				capabilities.setCapability("platformName", "ANDROID");
				capabilities.setCapability("appWaitDuration", "600000");
				capabilities.setCapability("bootstrapPort", bsport); 
				capabilities.setCapability("appPackage", package_name);
				capabilities.setCapability("appActivity", activity_name);
				
				//Start Appium server using terminal
//				service.stop();
//				Runtime rt = Runtime.getRuntime();
//				rt.exec("cmd /c start appium -a 127.0.0.1 -p " + port_number
//				+ " --no-reset --bootstrap-port " + bsport + " --nodeconfig "
//				+ Root + "\\server\\Node1-config_"
//				+ port_number + ".json");
//			     Thread.sleep(3000);
//				Activity activity = new Activity("io.appium.android.apis", ".ApiDemos");
//		        dr.startActivity(activity);
//		        dr.closeApp();
//				ap.dr.navigate().back();
//				ap.dr.findElement(By.xpath("//android.widget.ImageButton[@index='9']")).click();
//				ap.dr.findElement(By.id("dialer_input")).sendKeys(startussd);
//				ap.dr.findElement(By.id("single_call_button")).click();
				dr.set(new AndroidDriver(
						new URL("http://127.0.0.1:" + ReadMobileproperties(Mobile, "appiumport") + "/wd/hub"),
						capabilities));
				Runtime run = Runtime.getRuntime();
				run.exec("adb -s "+device_name+" shell am start -a android.intent.action.CALL -d tel:"+startussd);
				dr.get().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
//				WebDriverWait wait = new WebDriverWait(dr, 30);
//				wait.until(ExpectedConditions.visibilityOf(dr.findElement(By.id("android:id/button1"))));
				Thread.sleep(2000);
				dr.get().findElement(By.id("android:id/button1")).click();
				
				String[] spltussd = ussdstr.split(",");
				for (int currshortcode = 0; currshortcode < spltussd.length; currshortcode++) {
					String nxt = "fail";
					do {
						try {
							System.out.println("------------------------------");
							Thread.sleep(2000);
							dr.get().findElement(By.id("com.android.phone:id/input_field"));
							nxt = "pass";
						} catch (Exception e) { // Thread.sleep(100); }

						}
					} while (nxt != "pass");
					System.out.println("------------------------------");
					info("Entering code : "+ spltussd[currshortcode]);
					Thread.sleep(2000);
					dr.get().findElement(By.id("com.android.phone:id/input_field")).sendKeys(spltussd[currshortcode]);
					takeScreenShot("Entering code "+ spltussd[currshortcode]);
					dr.get().findElement(By.id("android:id/button1")).click();
				}
				dr.get().findElement(By.id("android:id/button2")).click();
				test.pass("<b>Test Case ID:"+rs.getField("Test Case ID")+"<br> Test Case Description: " +rs.getField("Test Case Description") +"</b><Br><a href='"+curtcid+"/ScreenShots.html' target='_blank'>ScreenShots</a>");
				extent.flush();
				endTestCase(rs.getField("Test Case ID"));
				 String result = dr.get().stopRecordingScreen();
			}
			Runtime rt = Runtime.getRuntime();
			rt.exec("cmd exit /n");
		}
		
			//        assertThat(result, is(not(emptyString())));
//				
//			}
		 catch (Exception e) {
			e.printStackTrace();
			}

	}

	public void takescreenshot()
	{
		// Capture screenshot.
		//Thread.sleep(100);\
		String destDir =trfold +"/MobileScreenshots";

		File scrFile = ((TakesScreenshot) dr).getScreenshotAs(OutputType.FILE);
		// Set date format to set It as screenshot file name.
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy__hh_mm_ssaa");
		// Create folder under project with name "screenshots" provided to destDir.
		new File(destDir).mkdirs();
		// Set file name using current date time.
		String destFile = dateFormat.format(new Date()) + ".png";

		try {
			// Copy paste file at destination folder location
			FileUtils.copyFile(scrFile, new File(destDir + "/" + destFile));
		//	Thread.sleep(100);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void createtimestampfold() {
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Calendar cal = Calendar.getInstance();
		try {
			resfold = new File(Result_FLD + "/" + dateFormat.format(cal.getTime()) + "/");
			if ((!resfold.exists()))
				resfold.mkdir();

			timefold = ExecutionStarttime.replace(":", "-").replace(" ", "_");
			File tresfold = new File(resfold + "/" + timefold + "/");
			if ((!tresfold.exists()))
				tresfold.mkdir();
			trfold = tresfold.toString();
		} catch (Exception e) {
			error(e.getMessage());
		}
	}

	private Logger Log = Logger.getLogger(App.class.getName());//

	// This is to print log for the beginning of the test case, as we usually run so
	// many test cases as a test suite

	public void startTestCase(String sTestCaseName) {

		Log.info("****************************************************************************************");

		Log.info("****************************************************************************************");

		Log.info("$$$$$$$$$$$$$$$$$$$$$ " + sTestCaseName + " $$$$$$$$$$$$$$$$$$$$$$$$$");

		Log.info("****************************************************************************************");

		Log.info("****************************************************************************************");

	}

	// This is to print log for the ending of the test case

	public void endTestCase(String sTestCaseName) {

		Log.info("XXXXXXXXXXXXXXXXXXXXXXX             " + "-E---N---D-" + "             XXXXXXXXXXXXXXXXXXXXXX");

	}

	// Need to create these methods, so that they can be called

	public void info(String message) {

		Log.info(message + "\r\n");

	}

	public void warn(String message) {

		Log.warn(message);

	}

	public void error(String message) {

		Log.error(message);

	}

	public void fatal(String message) {

		Log.fatal(message);

	}

	public void debug(String message) {

		Log.debug(message);

	}
	
	public void takeScreenShot(String scdesc) throws IOException, InterruptedException {
		// Set folder name to store screenshots.
		String destDir = trfold+"/"+curtcid+"/";
		// Capture screenshot.
		File scrFile = ((TakesScreenshot) dr.get()).getScreenshotAs(OutputType.FILE);
		// Set date format to set It as screenshot file name.
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy__hh_mm_ssaa");
		// Create folder under project with name "screenshots" provided to destDir.
		new File(destDir).mkdirs();
		// Set file name using current date time.
		String destFile = dateFormat.format(new Date()) + ".png";

		try {
			// Copy paste file at destination folder location
			FileUtils.copyFile(scrFile, new File(destDir + "/" + destFile));
			//Thread.sleep(5000);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		File file = new File(trfold + "\\" + curtcid + "\\ScreenShots.html");

		// Create the file
		if (file.createNewFile()) {
			//System.out.println("File is created!");
			//info("File is created!");
		} else {
			//System.out.println("File already exists.");
			//info("File already exists.");
		}

		// Write Content
		FileWriter writer = new FileWriter(file,true);
		writer.write(scdesc+"<br><img src = '"+destFile+"' height='500px'><br><br>");
		writer.close();


	}

	public void run() {
    }

	public void starter(String port) {

	    //Build the Appium service
	    AppiumServiceBuilder builder = new AppiumServiceBuilder();
	    builder.withIPAddress("127.0.0.1");
	    int port1 = Integer.parseInt(port);
	    builder.usingPort(port1);
	    builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
	    builder.withArgument(GeneralServerFlag.LOG_LEVEL,"error");

	    //Start the server with the builder
	    AppiumDriverLocalService service = AppiumDriverLocalService.buildService(builder);
	    service.start();
	}
//
//	public void stopServer() {
//		service.stop();
//	}

	}



