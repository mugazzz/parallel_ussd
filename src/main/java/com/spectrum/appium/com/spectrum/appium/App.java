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
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
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
	public final String Data = Root + "\\Test_Execution_Input.xlsm";
	public String curtcid = "";
	public String Product_Name = "";
	public String Confirmation = "";
	
	public String curtcid1 = "";
	public static String udid;
	public String Message = "";
	private static AppiumDriverLocalService service;
	public static String port;

   //-----Main Method-------------//
	
//	public static void main (String[] args) {
//	App k = new App("device1");
//	}
	
	@Test
	public void Device_1() {
		App k = new App("device1");
	}
	
////	@Test
////	public void Device_2(){
////		App("device2");
////	}
	
	public String ReadMobileproperties(String fname, String propname) throws IOException {
		String fpath = Root + "\\src\\test\\resources\\config\\" + fname + ".properties";
		Properties prop = new Properties();
		FileInputStream input = new FileInputStream(fpath);

		prop.load(input);
		// prop.getProperty(propname);

		return prop.getProperty(propname);
	}
	
	public App(String deviceq) {
		try {
			
			Fillo fillo = new Fillo();
			Connection conn = fillo.getConnection(Data);
			
	//-----------Get input data------------//
			
			String inputQuery = "Select * from Execution_Sheet where Execution = 'YES' and Test_Device = '"+ deviceq + "'";
			System.out.println(inputQuery);
			Recordset inputs = conn.executeQuery(inputQuery);
			createtimestampfold();
			ExtentReports extent = new ExtentReports();
			ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(trfold + "\\Master.html");
			System.setProperty("logfilename", trfold + "\\Logs");
			DOMConfigurator.configure("log4j.xml");
			
			while (inputs.next()) {
				Runtime rt = Runtime.getRuntime();
				String device = inputs.getField("Test_Device");
				String MSISDN = inputs.getField("Parameter Value 1");
				String Test_Scenario = inputs.getField("Test_Scenario");
				String Test_Case = inputs.getField("Test_Case");
				//String Device_Name = inputs.getField("Test_Device");
				Product_Name = inputs.getField("Parameter Value 2");
				
				info("Starting execution at +: "+ Product_Name+ "->"+ Test_Scenario+ "->" + ExecutionStarttime);
				extent.attachReporter(htmlReporter);
			curtcid = inputs.getField("Test Case ID");
			startTestCase(inputs.getField("Test Case ID"));
			ExtentTest test = extent.createTest(inputs.getField("Parameter Value 2")+":- <br>"+inputs.getField("Test_Case"));
			
	//-------------Check the product list with respect to the given input-------//
			
			String strQuery = "Select * from Test_Data "
					+ "where Product_Name='"+Product_Name+ "' "
					+ "and Test_Scenario ='" + Test_Scenario+"' "
							+ "and Test_Case ='"+ Test_Case + "'";
			Recordset rs = conn.executeQuery(strQuery);
			//String Mobile = rs.getField("Device_Name");
			String basedir = System.getProperty("user.dir");
			String port_number = ReadMobileproperties(device, "appiumport");
			String device_name = ReadMobileproperties(device, "DeviceName");
			String package_name = ReadMobileproperties(device, "apppackage");
			String activity_name = ReadMobileproperties(device, "appactivity");
			String version = ReadMobileproperties(device, "version");
			String bsport = ReadMobileproperties(device, "bootstrapport");

			String execu1 = "java -jar" + basedir+"\\src\\test\\resources\\server\\selenium-server-standalone-3.14.0.jar -role hub -port 4444";
//			rt.exec("cmd /c start java -jar " + basedir
//					+ "\\src\\test\\resources\\server\\selenium-server-standalone-3.14.0.jar -role hub -port 4444");
			rt.exec(execu1);
	
//			rt.exec("cmd /c start appium -a 127.0.0.1 -p " + port_number
//			+ " --no-reset --bootstrap-port " + bsport + " --nodeconfig "
//			+ basedir + "\\server\\Node1-config_"
//			+ port_number + ".json");
			
			starter(port_number);
			
			while (rs.next()) {
				String ussdstr = rs.getField("USSD_Sequence");
				String startussd = URLEncoder.encode(rs.getField("USSD_Code"),"UTF-8");
				String hash = URLEncoder.encode("#", "UTF-8");
				DesiredCapabilities capabilities = new DesiredCapabilities();
				capabilities.setCapability("deviceName", device);
				capabilities.setCapability("platformVersion", version);
				capabilities.setCapability("platformName", "ANDROID");
				capabilities.setCapability("bootstrapPort", bsport); 
				capabilities.setCapability("appPackage", package_name);
				capabilities.setCapability("appActivity", activity_name);

				
	//-------------Start Appium server using terminal----------------//

				dr.set(new AndroidDriver(new URL("http://127.0.0.1:" + ReadMobileproperties(device, "appiumport") + "/wd/hub"), capabilities));
				Runtime run = Runtime.getRuntime();
	
				
	//--------- Recharges Validation -----------------//
				
				if ((Test_Scenario).equals("USSD_Recharge")) 
				{
					String Recharge_Voucher = inputs.getField("Parameter Value 3");
					System.out.println("New Recharge code: "+ Recharge_Voucher);
					String execu = "adb -s "+device_name+" shell am start -a android.intent.action.CALL -d tel:"+startussd+Recharge_Voucher+hash;
					System.out.println("Execution cmmand: "+execu);
					run.exec(execu);
					Thread.sleep(4000);
					Confirmation = dr.get().findElement(By.id("android:id/message")).getText();
					info("Confirmation alert : "+Confirmation);
					takeScreenShot("Confirmation Screen");
					dr.get().findElement(By.id("android:id/button1")).click();
					Thread.sleep(3000);
				}
			
				else 
				{
					run.exec("adb -s "+device_name+" shell am start -a android.intent.action.CALL -d tel:"+startussd);
					dr.get().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
					Thread.sleep(1000);
					if(ussdstr.length()>=1) {
					String[] spltussd = ussdstr.split(",");
					for (int currshortcode = 0; currshortcode < spltussd.length; currshortcode++) {
						String nxt = "fail";
						do {
							try {
								System.out.println("------------------------------");
								Thread.sleep(1000);
								By inputfield = By.id("com.android.phone:id/input_field");
								if (elementExists(inputfield)) {
									dr.get().findElement(By.id("com.android.phone:id/input_field"));
									nxt = "pass";
								}
								else {
									info("Error occured, please check with screenshot");
									takeScreenShot("Error appears");
									Confirmation = dr.get().findElement(By.id("android:id/message")).getText();
									info("Confirmation alert : "+Confirmation);
									dr.get().findElement(By.id("android:id/button1")).click();
									dr.get().quit();
									break;
								}
							} catch (Exception e) { // Thread.sleep(100); }
	
							}
						} while (nxt != "pass");
						System.out.println("------------------------------");
						info("Entering code : "+ spltussd[currshortcode]);
//						Thread.sleep(2000);
						dr.get().findElement(By.id("com.android.phone:id/input_field")).sendKeys(spltussd[currshortcode]);
						takeScreenShot("Entering code "+ spltussd[currshortcode]);
						dr.get().findElement(By.id("android:id/button1")).click();
					}
					}
					else {
						info("Menu options are not available");
					}
					Thread.sleep(3000);
					By messag = By.id("android:id/message");
					if(elementExists(messag)) {
					Confirmation = dr.get().findElement(By.id("android:id/message")).getText();
					info("Confirmation alert : "+Confirmation);
					takeScreenShot("Confirmation Screen");
					dr.get().findElement(By.id("android:id/button1")).click();
					}
					else {
						info("Error occured, please check with screenshot");
						takeScreenShot("Error appears");
					}
					Thread.sleep(3000);
				}
				
	//----------------	Notification Message handle	------------//
				
				dr.get().quit();
				dr.set(new AndroidDriver(new URL("http://127.0.0.1:" + ReadMobileproperties(device, "appiumport") + "/wd/hub"), capabilities));
				Thread.sleep(3000);
				By New_Message = By.id("com.samsung.android.messaging:id/list_unread_count");
				if (elementExists(New_Message)) {
				List<MobileElement> elements1 = dr.get().findElements(By.id("com.samsung.android.messaging:id/list_unread_count"));
				for(MobileElement link : elements1)
				{
				{
					dr.get().findElement(By.id("com.samsung.android.messaging:id/list_unread_count")).click();
					Message = dr.get().findElement(By.id("com.samsung.android.messaging:id/content_text_view")).getText();
					info("Message Received : "+ Message);
					takeScreenShot("Related SMS Notification");
					By visibile2 = By.xpath("//android.widget.Button[@text='Delete']");
					if(elementExists(visibile2)) {
						dr.get().findElement(By.xpath("//android.widget.Button[@text='Delete']")).click();
					}
					else {
						dr.get().findElement(By.id("com.samsung.android.messaging:id/composer_setting_button")).click();
						dr.get().findElement(By.id("com.samsung.android.messaging:id/composer_drawer_delete_conversation_text")).click();
					}
				By Delete = By.id("com.samsung.android.messaging:id/largeLabel");
					if (elementExists(Delete))
					{
						//delete button is displayed
						}
					else 
					{
						dr.get().findElement(By.id("com.samsung.android.messaging:id/bubble_all_select_checkbox")).click();
						}
					dr.get().findElement(By.id("com.samsung.android.messaging:id/largeLabel")).click();
					dr.get().findElement(By.id("android:id/button1")).click();
						
					}
				}
				}
				else
				{
					Message = "Message not received for the provided USSD";
					info("Message not received for the provided USSD");
					takeScreenShot("SMS not received");
				}
				String result = dr.get().stopRecordingScreen();
				
	//-------------	HTML Report Handle	-------------//
				
				test.pass("<b>Product Name: "+inputs.getField("Parameter Value 2")+"<br>Test Scenario: "+inputs.getField("Test_Scenario")+"<br> Test Case: " +inputs.getField("Test_Case") +
						"<br> Confirmation Alert Message: 	<i>"+ Confirmation + "</i></b>"+
						"<br> Message Status: 	<i>"+ Message +
						"</i></b><Br><a href='"+curtcid+"/ScreenShots.html' target='_blank'>ScreenShots</a>");
				extent.flush();
				endTestCase(inputs.getField("Test Case ID"));
			}
			}
		}
		 catch (Exception e) {
			e.printStackTrace();
		 }
	}
	
	public boolean elementExists(By locator) {
		dr.get().manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        try {
            dr.get().findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        } finally {
        	dr.get().manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
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
	
	public void stopServer() {
		service.stop();
	}

	}



