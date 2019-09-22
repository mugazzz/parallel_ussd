package com.spectrum.appium.com.spectrum.appium;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

public class App {
	@SuppressWarnings("rawtypes")
	public ThreadLocal<AndroidDriver> dr = new ThreadLocal<AndroidDriver>();
	public final String Result_FLD = System.getProperty("user.dir") + "\\reports";
	public final String Root = System.getProperty("user.dir");// .replace("\\", "/");
	public DateFormat For = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
	public Calendar cal = Calendar.getInstance();
	public File resfold = null;
	public String trfold = Root + "\\trfold";
	public String timefold = "";
	public String ExecutionStarttime = For.format(cal.getTime()).toString();
	public final String Data = Root + "\\Input_sheet_V2.xlsx";
	public final String Reference_Data = Root + "\\server\\Reference_Sheet.xlsx";
	public static String cdrfiles = System.getProperty("user.dir") + "\\CDR";
	public String curtcid = "";
	public String Product_Name = "";
	public String Product_ID = "";
	public String Confirmation = "";
	public String Amount = "";
	public String To_Number = "";

	public String curtcid1 = "";
	public static String udid;
	public String Message = "";
	public String Prod_ID = "";
	private static AppiumDriverLocalService service;
	public static String port;
	public String strQuery = "";
	public String Test_Case_I = "";
	public String Test_Scenario_I = "";
	public String Recharge_Coupon = "";
	public String Balancemsg = "";
	public static String loginUser = "";
	public static String execution_status;

	Connection dbCon = null;
	Statement stmt = null;
	Statement stmt0 = null;
	ResultSet inputs = null;
	ResultSet inputs0 = null;
	Statement stmtu = null;
	Statement stmtip = null;
	ResultSet inputsip = null;
	Statement stmtuser = null;
	ResultSet inputsuser = null;

	public String updatef = "";

	// -----Main Method-------------//

//	public static void main (String[] args) throws SQLException {
//	App k = new App();
//	}

	@Test
	public void Device_1() throws SQLException {
		//App k = new App();
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
	
	public App() throws SQLException {
		try {
			Fillo fillo = new Fillo();
			com.codoid.products.fillo.Connection conn = fillo.getConnection(Data);
			
	//-----------Get input data------------//
			
			String inputQuery = "Select * from Execution_Sheet where Execution = 'YES'";
			System.out.println(inputQuery);
			Recordset inputs = conn.executeQuery(inputQuery);
			createtimestampfold();
			ExtentReports extent = new ExtentReports();
			ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(trfold + "\\Master.html");
			System.setProperty("logfilename", trfold + "\\Logs");
			DOMConfigurator.configure("log4j.xml");
			
			while (inputs.next()) {
				Runtime rt = Runtime.getRuntime();
//						String device = inputs.getField("Test_Device");
//						System.out.println(device);
						String MSISDN = inputs.getField("MSISDN");
						String Test_Scenario = inputs.getField("Test_Scenario");
//						String Test_Case = inputs.getField("Test_Case");
						String Test_Case_ID = inputs.getField("Test_Case_ID");
//						Prod_ID = inputs.getField("Product_Name");
//						Recharge_Coupon = inputs.getField("Recharge_Coupon");
//						String Call_To = inputs.getField("Call_TO_MSISDN");
//						String CALL_DURATION = inputs.getField("CALL_DURATION");
						
						
						info("Starting execution at +: " + Test_Case_ID + "->" + Test_Scenario + "->" + ExecutionStarttime);
						extent.attachReporter(htmlReporter);
						
//						 //String Mobile = inputs.getField("Test_Device");
//						String basedir = System.getProperty("user.dir");
//						String port_number = ReadMobileproperties(device, "appiumport");
//						String device_name = ReadMobileproperties(device, "DeviceName");
//						String package_name = ReadMobileproperties(device, "apppackage");
//						String activity_name = ReadMobileproperties(device, "appactivity");
//						String package_name1 = ReadMobileproperties(device, "apppackage1");
//						String activity_name1 = ReadMobileproperties(device, "appactivity1");
//						String version = ReadMobileproperties(device, "version");
//						String bsport = ReadMobileproperties(device, "bootstrapport");
//						
//
//						String execu1 = "java -jar" + basedir
//								+ "\\src\\test\\resources\\server\\selenium-server-standalone-3.14.0.jar -role hub -port 4444";
////			rt.exec("cmd /c start java -jar " + basedir
////					+ "\\src\\test\\resources\\server\\selenium-server-standalone-3.14.0.jar -role hub -port 4444");
//						rt.exec(execu1);
//
////			rt.exec("cmd /c start appium -a ipaddress -p " + port_number
////			+ " --no-reset --bootstrap-port " + bsport + " --nodeconfig "
////			+ basedir + "\\server\\Node1-config_"
////			+ port_number + ".json");
//
//						starter(port_number);

//						com.codoid.products.fillo.Connection conn1 = fillo.getConnection(Reference_Data);

						// -------------------------- OPT IN / OUT / Recharge  ------------------------------------//

//						if (Test_Scenario.equalsIgnoreCase("USSD_OPT_IN") || Test_Scenario.equalsIgnoreCase("OPT OUT")) {
//							DesiredCapabilities capabilities = new DesiredCapabilities();
//							capabilities.setCapability("deviceName", device);
//							capabilities.setCapability("platformVersion", version);
//							capabilities.setCapability("platformName", "ANDROID");
//							capabilities.setCapability("bootstrapPort", bsport);
//							capabilities.setCapability("appPackage", package_name);
//							capabilities.setCapability("appActivity", activity_name);
//
//							if (Test_Scenario.equals("All")) {
//								strQuery = "Select * from ussd_code_data " + "where Product_Name= '" + Prod_ID + "'"
//										+ " and Execution='Yes'";
//							} else {
//								strQuery = "Select * from ussd_code_data " + "where Product_Name= '" + Prod_ID + "'"
//										+ " and Test_Scenario ='" + Test_Scenario + "' " + "and Test_Case ='"
//										+ Test_Case + "'";
//							}
//
//							Recordset rs = conn1.executeQuery(strQuery);
//
//							while (rs.next()) {
//								String Product_Name1 = rs.getField("Product_Name");
//								String Product_Name2 = Product_Name1.replace(" ", "_");
//								String Product_Name3 = Product_Name2.replace("*", "_");
//								String Product_Name4 = Product_Name3.replace("(", "_");
//								String Product_Name5 = Product_Name4.replace("#", "_");
//								String Product_Name7 = Product_Name5.replace("%23", "_");
//								Product_Name = Product_Name7.replace(")", "_");
//								String ussdstr = rs.getField("USSD_Sequence");
//								Test_Case_I = rs.getField("Test_Case");
//								Test_Scenario_I = rs.getField("Test_Scenario");
//								String startussd = URLEncoder.encode(rs.getField("USSD_Code"), "UTF-8");
//								String hash = URLEncoder.encode("#", "UTF-8");
//
//								curtcid = inputs.getField("Test_Case_ID") + "--" +Product_Name;
//								startTestCase(curtcid);
//								ExtentTest test = extent.createTest(inputs.getField("Test_Case_ID") + ": <br>"
//										+ inputs.getField("Product_Name"));

//				// ------------- Start Appium server using terminal----------------//
//								APIHandler.API(curtcid, trfold, "Before_Execution", MSISDN);
//								
//								 dr.set(new AndroidDriver(new URL(
//								 "http://127.0.0.1:" + ReadMobileproperties(device, "appiumport") +
//								 "/wd/hub"), capabilities));
//								 
//				//-------------- Clear Usage usageThresholdValue	-------------------------------
//									String xml_path = trfold+"//"+curtcid;
//									APIHandler.Read_API(xml_path, "usageThresholdID", "UpdateUsageThresholdsAndCounters", curtcid, trfold, "Before_Execution", MSISDN);
//									
//								 
//								 dr.get().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
//								 Thread.sleep(1000);
//							
				//---------- Dial Number -------------//
//								 
//								 String startuss = startussd.replace("%23", "#");
//								 dr.get().findElement(By.id("com.samsung.android.dialer:id/star")).click();
//								 dr.get().findElement(By.id("com.samsung.android.dialer:id/digits")).sendKeys(startuss);
//								 dr.get().findElement(By.id("com.samsung.android.dialer:id/pound")).click();
//								 takeScreenShot("Dialed Number");
//								 dr.get().findElement(By.id("com.samsung.android.dialer:id/dialButtonImage")).click();
//								 Runtime run = Runtime.getRuntime();
//								run.exec("adb -s " + device_name
//										+ " shell am start -a android.intent.action.CALL -d tel:" + startussd);
								 
								
//								Thread.sleep(3000);
//								By inputfield = By.id("com.android.phone:id/input_field");
//								if (elementExists(inputfield)) {
//									dr.get().findElement(By.id("com.android.phone:id/input_field"));
//									if (ussdstr.length() >= 1) {
//										String[] spltussd = ussdstr.split(",");
//										for (int currshortcode = 0; currshortcode < spltussd.length; currshortcode++) {
//											String nxt = "fail";
//											do {
//												try {
//													System.out.println("------------------------------");
//													Thread.sleep(1000);
//													nxt = "pass";
//												} catch (Exception e) { // Thread.sleep(100); }
//
//												}
//											} while (nxt != "pass");
//											System.out.println("------------------------------");
//										try {
//											info("Entering code : " + spltussd[currshortcode]);
////						Thread.sleep(2000);
//											dr.get().findElement(By.id("com.android.phone:id/input_field"))
//													.sendKeys(spltussd[currshortcode]);
//											takeScreenShot("Entering code " + spltussd[currshortcode]);
//											dr.get().hideKeyboard();
//											dr.get().findElement(By.id("android:id/button1")).click();
//										}
//										catch (Exception e) { // Thread.sleep(100); }
//											takeScreenShot("Error in option selection");	
//										}
//									}
//									}
//										else {
//										info("Menu options are not available");
//									}
//									Thread.sleep(3000);
//									takeScreenShot("Menu option");
//									By messag = By.id("android:id/message");
//									if (elementExists(messag)) {
//										Confirmation = dr.get().findElement(By.id("android:id/message")).getText();
//										info("Confirmation alert : " + Confirmation);
//										takeScreenShot("Confirmation Screen");
//										dr.get().findElement(By.id("android:id/button1")).click();
//									} else {
//										info("Menu options are not available");
//										Thread.sleep(2000);
//										By Send = By.id("android:id/button1");
//										By Messa = By.id("android:id/message");
//										if(elementExists(Messa)) {
//											Confirmation = dr.get().findElement(By.id("android:id/message")).getText();
//											info("Confirmation alert : "+Confirmation);
//											takeScreenShot("Confirmation Screen");
//										}
//										if (elementExists(Send)){
//											dr.get().findElement(By.id("android:id/button1")).click();
//											//takeScreenShot("Error");
//										}
//									}
//								} else 
//									{
//									info("Menu options are not available");
//									takeScreenShot("Error appears");
//									Thread.sleep(2000);
//									By Send = By.id("android:id/button1");
//									By Messa = By.id("android:id/message");
//									if(elementExists(Messa)) {
//										Confirmation = dr.get().findElement(By.id("android:id/message")).getText();
//										info("Confirmation alert : "+Confirmation);
//										takeScreenShot("Confirmation Screen");
//									}
//									if (elementExists(Send)){
//										dr.get().findElement(By.id("android:id/button1")).click();
//										takeScreenShot("Error");
//									}
////									dr.get().findElement(By.id("android:id/button1")).click();
//									//dr.get().quit();
//								}
//								Thread.sleep(3000);

								// ---------------- Notification Message handle ------------//
//							try {
//								DesiredCapabilities capabilities1 = new DesiredCapabilities();
//								capabilities1.setCapability("deviceName", device);
//								capabilities1.setCapability("platformVersion", version);
//								capabilities1.setCapability("platformName", "ANDROID");
//								capabilities1.setCapability("bootstrapPort", bsport);
//								capabilities1.setCapability("appPackage", package_name1);
//								capabilities1.setCapability("appActivity", activity_name1);
//
//								dr.set(new AndroidDriver(new URL(
//										"http://127.0.0.1:" + ReadMobileproperties(device, "appiumport") + "/wd/hub"),
//										capabilities1));
//								Thread.sleep(3000);
//								By New_Message = By.id("com.samsung.android.messaging:id/list_unread_count");
//								if (elementExists(New_Message)) {
//									List<MobileElement> elements1 = dr.get()
//											.findElements(By.id("com.samsung.android.messaging:id/list_unread_count"));
//									for (MobileElement link : elements1) {
//										{
//											dr.get().findElement(
//													By.id("com.samsung.android.messaging:id/list_unread_count"))
//													.click();
//											Message = dr.get()
//													.findElement(
//															By.id("com.samsung.android.messaging:id/content_text_view"))
//													.getText();
//											info("Message Received : " + Message);
//											takeScreenShot("Related SMS Notification");
//											By visibile2 = By.xpath("//android.widget.Button[@text='Delete']");
//											if (elementExists(visibile2)) {
//												dr.get().findElement(
//														By.xpath("//android.widget.Button[@text='Delete']")).click();
//											} else {
//												dr.get().findElement(By
//														.id("com.samsung.android.messaging:id/composer_setting_button"))
//														.click();
//												dr.get().findElement(By.id(
//														"com.samsung.android.messaging:id/composer_drawer_delete_conversation_text"))
//														.click();
//											}
//											By Delete = By.id("com.samsung.android.messaging:id/largeLabel");
//											if (elementExists(Delete)) {
//												// delete button is displayed
//											} else {
//												dr.get().findElement(By.id(
//														"com.samsung.android.messaging:id/bubble_all_select_checkbox"))
//														.click();
//											}
//											dr.get().findElement(By.id("com.samsung.android.messaging:id/largeLabel"))
//													.click();
//											dr.get().findElement(By.id("android:id/button1")).click();
//
//										}
//									}
//								} else {
//									Message = "Message not received for the provided USSD";
//									info("Message not received for the provided USSD");
//									takeScreenShot("SMS not received");
//								}
//								String result = dr.get().stopRecordingScreen();
////								APIHandler.API(curtcid, trfold, "After_Execution", MSISDN);
//							}
//							catch (Exception e) {
//								//e.printStackTrace();
//								System.out.println("--------++++++---------");
//							 }	
//					//--------------------------	CIS DB	----------------------------------------------//				
//							String Result = Asnconvertor.cis_db("both", MSISDN);
//							
//							
//					//-------------------------- CDR Conversion ------------------------------------------//
//							Asnconvertor.nodeValidation(Test_Scenario, MSISDN);
//							
//							
//					//------------------------------ Report ----------------------------------------------//
//							String[] convertor = Asnconvertor.Result(trfold, MSISDN, Product_ID, Test_Scenario, Test_Case_ID, curtcid, Product_Name, Test_Scenario_I, Test_Case, Confirmation, Message, Recharge_Coupon,"", "", "", "", "", "", ExecutionStarttime, "", "");
//						
//							test.pass("</table><br><br><table><tr><th style= 'min-width: 168px'><b>Scenario</b></th>"
//									+ "<th style= 'min-width: 168px'><b>Test Scenario: </b></th>"
//									+ "<th style= 'min-width: 168px'><b>Test Case: </b></th>"
//									+ "<th style= 'min-width: 168px'><b>Confirmation Alert Message: </b></th>"
//									+"<th style= 'min-width: 168px'><b> ScreenShot</b></th></tr>" + 
									
									//Device Result
//									"<tr><td style= 'min-width: 168px'>"+Product_Name1+"</td><td style= 'min-width: 168px'>"+ "<a href='"+curtcid+"/ScreenShots.html' target='_blank'>Deviced_Execution_ScreenShots</a></td></tr></table><br>"
//									);
							
//								//CIS API
//								test.pass("<br><br><b>CS Get Account Details Response:</b>" 
//								+ "<br><b>Response before execution XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "\\CS_API_VALIDATION\\Before_Execution\\Response\\response.xml'>Click to View the Response</a>"
//								+ "<br><b>Response After execution XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "\\CS_API_VALIDATION\\After_Execution\\Response\\response.xml'>Click to View the Response</a><br>");
//								
//								//CIS DB
//								test.pass("<br><br><b>CIS DB Data:</b><table>" + Result + "</table> <br>");
//								
//						com.codoid.products.fillo.Connection co = fillo.getConnection(Reference_Data);
//						String strQuery = "Select * from node_xml_conversion " + "where Test_Scenario= '"+Test_Scenario+"' and Execution ='Yes'";
//						Recordset rsr = co.executeQuery(strQuery);
//						while (rsr.next()) {
//							String Node_Type = rsr.getField("Node_To_Validate");
//							
//								//CIS Result
//								if(Node_Type.equalsIgnoreCase("CIS")) {
//								test.pass("<br><br><b>CIS Data:</b>"
//								+ "<br><b>CIS EDR: </b><br>" + convertor[3] 
//								+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[5]+"/"+convertor[7] + "/Output.csv'>Click to View the EDR</a>"+"</table><br>");
//								}
//								
//								//SDP Result
//								if(Node_Type.equalsIgnoreCase("SDP")) {
//								test.pass("<br><br><b>SDP Data:</b>"
//								+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[4]+"/"+convertor[6] + "/Output.xml'>Click to View the SDP CDR</a><br>");
//								}
//								
//								//OCC Result
//								if(Node_Type.equalsIgnoreCase("OCC")) {
//								test.pass("<br><br><b>OCC Data:</b>"
//								+ "<br><b>OCC Table: </b><br>" + convertor[2] 
//								+ "<br><b>XML Link---> </b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[0]+"/"+convertor[1] + "/output.xml'>Click to View the OCC CDR</a>"+"</table><br>");
//								}
//								
//								//AIR
//								if(Node_Type.equalsIgnoreCase("AIR")) 
//								{
//								test.pass("<br><br><b>AIR Data:</b>"
//								+ "<br><b>AIR Table: </b><br>" + convertor[10] 
//								+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[11]+"/"+convertor[9] + "/Output.xml'>Click to View the AIR CDR</a>"+"</table><br>");
//								}
//								
//								//CCN
//								if(Node_Type.equalsIgnoreCase("CCN")) 
//								{
//								test.pass("<br><br><b>CCN Data:</b>"
//								+ "<br><b>CCN Table: </b><br>" + convertor[14] 
//								+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[13]+"/"+convertor[12] + "/Output.xml'>Click to View the CCN CDR</a>"+"</table><br>");
//								}
//							extent.flush();
//							endTestCase(curtcid);
//							}
//						}
////
//						
//						
//			// ----------------------------- Recharge Coupon ---------------------------------------------//
//
//						if (Test_Scenario.equals("RECHARGE")) {
//
//							strQuery = "Select * from recharge_data " + "where Test_Scenario ='" + Test_Scenario + "' "
//									+ "and Test_Case ='" + Test_Case + "'";
//
//							Recordset rs = conn1.executeQuery(strQuery);
//							while (rs.next()) {
//								Test_Case_I = rs.getField("Test_Case");
//								Test_Scenario_I = rs.getField("Test_Scenario");
//								String startussd = URLEncoder.encode(rs.getField("USSD_Code"), "UTF-8");
//								String hash = URLEncoder.encode("#", "UTF-8");
//
//								curtcid = inputs.getField("Test_Case_ID") + "--" + rs.getField("Test_Scenario") + "_"
//										+ rs.getField("Test_Case");
//								startTestCase(curtcid);
//								ExtentTest test = extent.createTest(inputs.getField("Test_Case_ID") + ": <br>"
//										+ inputs.getField("Test_Scenario") + "- <br>" + inputs.getField("Test_Case"));
//
//								DesiredCapabilities capabilities = new DesiredCapabilities();
//								capabilities.setCapability("deviceName", device);
//								capabilities.setCapability("platformVersion", version);
//								capabilities.setCapability("platformName", "ANDROID");
//								capabilities.setCapability("bootstrapPort", bsport);
//								capabilities.setCapability("appPackage", package_name);
//								capabilities.setCapability("appActivity", activity_name);
//								
//								APIHandler.API(curtcid, trfold, "Before_Execution", MSISDN);
//								dr.set(new AndroidDriver(new URL(
//										"http://127.0.0.1:" + ReadMobileproperties(device, "appiumport") + "/wd/hub"),
//										capabilities));
//								
//								 dr.get().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
//								 Thread.sleep(1000);
//								
//								//---------- Dial Number -------------//
//								 
//								 dr.get().findElement(By.id("com.samsung.android.dialer:id/star")).click();
//								 dr.get().findElement(By.id("com.samsung.android.dialer:id/digits")).sendKeys(startussd+Recharge_Coupon);
//								 dr.get().findElement(By.id("com.samsung.android.dialer:id/pound")).click();
//								 takeScreenShot("Dialed Number");
//								 dr.get().findElement(By.id("com.samsung.android.dialer:id/dialButtonImage")).click();
//								
////								Runtime run = Runtime.getRuntime();
////
////								System.out.println("New Recharge code: " + Recharge_Coupon);
////								String execu = "adb -s " + device_name
////										+ " shell am start -a android.intent.action.CALL -d tel:" + startussd
////										+ Recharge_Coupon + hash;
////								System.out.println("Execution cmmand: " + execu);
////								run.exec(execu);
//						try { 
//								Thread.sleep(4000);
//									By mes = By.id("android:id/message");
//								if (elementExists(mes)) {
//									Confirmation = dr.get().findElement(By.id("android:id/message")).getText();
//									info("Confirmation alert : " + Confirmation);
//									takeScreenShot("Confirmation Screen");
//									dr.get().findElement(By.id("android:id/button1")).click();
//									Thread.sleep(3000);
//									dr.get().quit();
//								} else {
//									info("Menu options are not available");
//									takeScreenShot("Error appears");
//									Thread.sleep(2000);
//									By Send = By.id("android:id/button1");
//									By Messa = By.id("android:id/message");
//									if(elementExists(Messa)) {
//										Confirmation = dr.get().findElement(By.id("android:id/message")).getText();
//										info("Confirmation alert : "+Confirmation);
//										takeScreenShot("Confirmation Screen");
//									}
//									if (elementExists(Send)){
//										dr.get().findElement(By.id("android:id/button1")).click();
//										takeScreenShot("Error");
//									}
//									dr.get().quit();	
//								}
//								
//								DesiredCapabilities capabilities1 = new DesiredCapabilities();
//								capabilities1.setCapability("deviceName", device);
//								capabilities1.setCapability("platformVersion", version);
//								capabilities1.setCapability("platformName", "ANDROID");
//								capabilities1.setCapability("bootstrapPort", bsport);
//								capabilities1.setCapability("appPackage", package_name1);
//								capabilities1.setCapability("appActivity", activity_name1);
//								dr.set(new AndroidDriver(new URL(
//										"http://127.0.0.1:" + ReadMobileproperties(device, "appiumport") + "/wd/hub"),
//										capabilities1));
//								Thread.sleep(3000);
//						try {
//								By New_Message = By.id("com.samsung.android.messaging:id/list_unread_count");
//								if (elementExists(New_Message)) {
//									List<MobileElement> elements1 = dr.get().findElements(By.id("com.samsung.android.messaging:id/list_unread_count"));
//									for (MobileElement link : elements1) {
//										{
//											dr.get().findElement(
//													By.id("com.samsung.android.messaging:id/list_unread_count"))
//													.click();
//											Message = dr.get()
//													.findElement(
//															By.id("com.samsung.android.messaging:id/content_text_view"))
//													.getText();
//											info("Message Received : " + Message);
//											takeScreenShot("Related SMS Notification");
//											By visibile2 = By.xpath("//android.widget.Button[@text='Delete']");
//											if (elementExists(visibile2)) {
//												dr.get().findElement(
//														By.xpath("//android.widget.Button[@text='Delete']")).click();
//											} else {
//												dr.get().findElement(By
//														.id("com.samsung.android.messaging:id/composer_setting_button"))
//														.click();
//												dr.get().findElement(By.id(
//														"com.samsung.android.messaging:id/composer_drawer_delete_conversation_text"))
//														.click();
//											}
//											By Delete = By.id("com.samsung.android.messaging:id/largeLabel");
//											if (elementExists(Delete)) {
//												// delete button is displayed
//											} else {
//												dr.get().findElement(By.id(
//														"com.samsung.android.messaging:id/bubble_all_select_checkbox"))
//														.click();
//											}
//											dr.get().findElement(By.id("com.samsung.android.messaging:id/largeLabel"))
//													.click();
//											dr.get().findElement(By.id("android:id/button1")).click();
//
//										}
//									}
//								} else {
//									Message = "Message not received for the provided USSD";
//									info("Message not received for the provided USSD");
//									takeScreenShot("SMS not received");
//								}
//								String result = dr.get().stopRecordingScreen();
//
//						}
//						catch (Exception e) {
//							//e.printStackTrace();
//							System.out.println("--------++++++---------");
//						 }
//						
//						APIHandler.API(curtcid, trfold, "After_Execution", MSISDN);
//						
//					//-------------------------- CDR Conversion -------------------------------------------//
//						
//						Asnconvertor.nodeValidation(Test_Scenario, MSISDN);
//						
//					//-------------------------- Report ----------------------------------------------//
//						String[] convertor = Asnconvertor.Result(trfold, MSISDN, "", Test_Scenario, Test_Case_ID, curtcid, "", Test_Scenario_I, Test_Case, Confirmation, Message,"", "", "", "", "", "", "", ExecutionStarttime, "", "");
//						
//						test.pass("</table><table><tr><th style= 'min-width: 168px'><b>Test Scenario: </b></th>"
//								+ "<th style= 'min-width: 168px'><b>Test Case: </b></th>"
//								+ "<th style= 'min-width: 168px'><b>Recharge Coupon: </b></th>"
//								+ "<th style= 'min-width: 168px'><b>Confirmation Alert Message: </b></th>"
//								+"<th style= 'min-width: 168px'><b> Message Status: </b></th>"
//								+"<th style= 'min-width: 168px'><b> ScreenShot</b></th></tr>" + 
//								
//								//Device Result
//								"<tr><td style= 'min-width: 168px'>"+Test_Scenario+"</td><td style= 'min-width: 168px'>"+Test_Case+"</td><td style= 'min-width: 168px'>"+Recharge_Coupon+"</td><td style= 'min-width: 168px'>"+ Confirmation +"</td><td style= 'min-width: 168px'>"+ Message+"</td><td style= 'min-width: 168px'>"+ "<a href='"+curtcid+"/ScreenShots.html' target='_blank'>Deviced_Execution_ScreenShots</a></td></tr></table><br>"
//								);
//						
//						//CIS API
//						test.pass("<br><br><b>CS Get Account Details Response:</b>" 
//						+ "<br><b>Response before execution XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "\\CS_API_VALIDATION\\Before_Execution\\Response\\response.xml'>Click to View the Response</a>"
//						+ "<br><b>Response After execution XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "\\CS_API_VALIDATION\\After_Execution\\Response\\response.xml'>Click to View the Response</a><br>");
//					
//					com.codoid.products.fillo.Connection co = fillo.getConnection(Reference_Data);
//					String strQuery = "Select * from node_xml_conversion " + "where Test_Scenario= '"+Test_Scenario+"' and Execution ='Yes'";
//					Recordset rsr = co.executeQuery(strQuery);
//					while (rsr.next()) {
//						String Node_Type = rsr.getField("Node_To_Validate");
//						
//							//CIS Result
//							if(Node_Type.equalsIgnoreCase("CIS")) {
//							test.pass("<br><br><b>CIS Data:</b>"
//							+ "<br><b>CIS EDR: </b><br>" + convertor[3] 
//							+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[5]+"/"+convertor[7] + "/Output.csv'>Click to View the EDR</a>"+"</table><br>");
//							}
//							
//							//SDP Result
//							if(Node_Type.equalsIgnoreCase("SDP")) {
//							test.pass("<br><br><b>SDP Data:</b>"
//							+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[4]+"/"+convertor[6] + "/Output.xml'>Click to View the SDP CDR</a><br>");
//							}
//							
//							//OCC Result
//							if(Node_Type.equalsIgnoreCase("OCC")) {
//							test.pass("<br><br><b>OCC Data:</b>"
//							+ "<br><b>OCC Table: </b><br>" + convertor[2] 
//							+ "<br><b>XML Link---> </b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[0]+"/"+convertor[1] + "/output.xml'>Click to View the OCC CDR</a>"+"</table><br>");
//							}
//							
//							//AIR
//							if(Node_Type.equalsIgnoreCase("AIR")) 
//							{
//							test.pass("<br><br><b>AIR Data:</b>"
//							+ "<br><b>AIR Table: </b><br>" + convertor[10] 
//							+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[11]+"/"+convertor[9] + "/Output.xml'>Click to View the AIR CDR</a>"+"</table><br>");
//							}
//							
//							//CCN
//							if(Node_Type.equalsIgnoreCase("CCN")) 
//							{
//							test.pass("<br><br><b>CCN Data:</b>"
//							+ "<br><b>CCN Table: </b><br>" + convertor[14] 
//							+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[13]+"/"+convertor[12] + "/Output.xml'>Click to View the CCN CDR</a>"+"</table><br>");
//							}
//
//						extent.flush();
//						endTestCase(curtcid);
//						}
//							}
//					catch (Exception e) { // Thread.sleep(100); }
//						info("Error occured in for the recharge process");
//						}
//					}
//					}
//						
//						
//						//----------------------	Voice Call	 --------------------------//
//						
//						else if(Test_Scenario.equals("LIVE USAGE VOICE")) {
//							String package_voice = ReadMobileproperties(Test_Scenario, "apppackage");
//							String activity_voice = ReadMobileproperties(Test_Scenario, "appactivity");
//							DesiredCapabilities capabilities = new DesiredCapabilities();
//							capabilities.setCapability("deviceName", device);
//							capabilities.setCapability("platformVersion", version);
//							capabilities.setCapability("platformName", "ANDROID");
//							capabilities.setCapability("bootstrapPort", bsport); 
//							capabilities.setCapability("appPackage", package_voice);
//							capabilities.setCapability("appActivity", activity_voice);
//							curtcid = Test_Case_ID+"--"+Test_Scenario+"_"+Test_Scenario;
//							startTestCase(curtcid);
//							ExtentTest test = extent.createTest(Test_Case_ID+": <br>"+Test_Scenario+"<br>"+Test_Scenario);
							
//							int secs = Integer.parseInt(CALL_DURATION);
//							APIHandler.API(curtcid, trfold, "Before_Execution", MSISDN);
//							dr.set(new AndroidDriver(new URL("http://127.0.0.1:" +  ReadMobileproperties(device, "appiumport") + "/wd/hub"), capabilities));
//							Thread.sleep(3000);
//							dr.get().findElement(By.id("com.samsung.android.dialer:id/zero")).click();
//							 //dr.get().findElement(By.id("com.samsung.android.dialer:id/digits")).clear();
//							 dr.get().findElement(By.id("com.samsung.android.dialer:id/digits")).sendKeys(Call_To);
//							 takeScreenShot("Dialed Number");
//							 dr.get().findElement(By.id("com.samsung.android.dialer:id/dialButtonImage")).click();
							
							
//							Runtime run = Runtime.getRuntime();
//							String execu = "adb -s "+device_name+" shell am start -a android.intent.action.CALL -d tel:"+Call_To;
//							System.out.println("Execution cmmand: "+execu);
//							run.exec(execu);
//							Thread.sleep(secs*1000);
//							takeScreenShot("Call process");
//							try {
//							dr.get().findElement(By.id("com.samsung.android.incallui:id/disconnect_button")).click();
//							}
//							catch (Exception e) {
//								//e.printStackTrace();
//								System.out.println("User didn't pick the call");
//							 }
//							//com.samsung.android.incallui:id/disconnect_button
//							String result = dr.get().stopRecordingScreen();
							
//							APIHandler.API(curtcid, trfold, "After_Execution", MSISDN);
//							//-------------------------- CDR Conversion -------------------------------------------//
//							
//							Asnconvertor.nodeValidation(Test_Scenario, MSISDN);
//							
//					//-------------------------- Report ----------------------------------------------//
//							String[] convertor = Asnconvertor.Result(trfold, MSISDN, "", Test_Scenario, Test_Case_ID, curtcid, "", Test_Scenario_I, Test_Case, "", "", "", Call_To, "", "", "", "", "", ExecutionStarttime, CALL_DURATION, "" );
//							
//							test.pass("</table><table><tr><th style= 'min-width: 168px'><b>MSISDN</b></th>"
//									+"<th style= 'min-width: 168px'><b>Test Scenario </b></th>"
//									+"<th style= 'min-width: 168px'><b>Test Case </b></th>"
//									+"<th style= 'min-width: 168px'><b>Called To </b></th>"
//									+"<th style= 'min-width: 168px'><b>Call Duration </b></th>"
//									+"<th style= 'min-width: 168px'><b> ScreenShot</b></th></tr>" + 
//									
//									//Device Result
//									"<tr><td style= 'min-width: 168px'>"+MSISDN+"</td><td style= 'min-width: 168px'>"+Test_Scenario+"</td><td style= 'min-width: 168px'>"+Test_Case+"</td><td style= 'min-width: 168px'>"+Call_To+"</td><td style= 'min-width: 168px'>"+ CALL_DURATION +"</td><td style= 'min-width: 168px'>"+ "<a href='"+curtcid+"/ScreenShots.html' target='_blank'>Deviced_Execution_ScreenShots</a></td></tr></table><br>");
//							
							//CIS API
//							test.pass("<br><br><b>CS Get Account Details Response:</b>" 
//							+ "<br><b>Response before execution XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "\\CS_API_VALIDATION\\Before_Execution\\Response\\response.xml'>Click to View the Response</a>"
//							+ "<br><b>Response After execution XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "\\CS_API_VALIDATION\\After_Execution\\Response\\response.xml'>Click to View the Response</a><br>");
//						
//					com.codoid.products.fillo.Connection co = fillo.getConnection(Reference_Data);
//					String strQuery = "Select * from node_xml_conversion " + "where Test_Scenario= '"+Test_Scenario+"' and Execution ='Yes'";
//					Recordset rsr = co.executeQuery(strQuery);
//					while (rsr.next()) {
//						String Node_Type = rsr.getField("Node_To_Validate");
//						
//							//CIS Result
//							if(Node_Type.equalsIgnoreCase("CIS")) {
//							test.pass("<br><br><b>CIS Data:</b>"
//							+ "<br><b>CIS EDR: </b><br>" + convertor[3] 
//							+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[5]+"/"+convertor[7] + "/Output.csv'>Click to View the EDR</a>"+"</table><br>");
//							}
//							
//							//SDP Result
//							if(Node_Type.equalsIgnoreCase("SDP")) {
//							test.pass("<br><br><b>SDP Data:</b>"
//							+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[4]+"/"+convertor[6] + "/Output.xml'>Click to View the SDP CDR</a><br>");
//							}
//							
//							//OCC Result
//							if(Node_Type.equalsIgnoreCase("OCC")) {
//							test.pass("<br><br><b>OCC Data:</b>"
//							+ "<br><b>OCC Table: </b><br>" + convertor[2] 
//							+ "<br><b>XML Link---> </b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[0]+"/"+convertor[1] + "/output.xml'>Click to View the OCC CDR</a>"+"</table><br>");
//							}
//							
//							//AIR
//							if(Node_Type.equalsIgnoreCase("AIR")) 
//							{
//							test.pass("<br><br><b>AIR Data:</b>"
//							+ "<br><b>AIR Table: </b><br>" + convertor[10] 
//							+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[11]+"/"+convertor[9] + "/Output.xml'>Click to View the AIR CDR</a>"+"</table><br>");
//							}
//							
//							//CCN
//							if(Node_Type.equalsIgnoreCase("CCN")) 
//							{
//							test.pass("<br><br><b>CCN Data:</b>"
//							+ "<br><b>CCN Table: </b><br>" + convertor[14] 
//							+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[13]+"/"+convertor[12] + "/Output.xml'>Click to View the CCN CDR</a>"+"</table><br>");
//							}
//
//							extent.flush();
//							endTestCase(curtcid);
//						}
//						
//						
//						//----------------------	Video Call	 --------------------------//
//						
//						else if(Test_Scenario.equals("LIVE USAGE VIDEO CALL")) {
//							String package_voice = ReadMobileproperties(Test_Scenario, "apppackage");
//							String activity_voice = ReadMobileproperties(Test_Scenario, "appactivity");
//							DesiredCapabilities capabilities = new DesiredCapabilities();
//							capabilities.setCapability("deviceName", device);
//							capabilities.setCapability("platformVersion", version);
//							capabilities.setCapability("platformName", "ANDROID");
//							capabilities.setCapability("bootstrapPort", bsport); 
//							capabilities.setCapability("appPackage", package_voice);
//							capabilities.setCapability("appActivity", activity_voice);
//							curtcid = Test_Case_ID+"--"+Test_Scenario+"_"+Test_Scenario;
//							startTestCase(curtcid);
//							ExtentTest test = extent.createTest(Test_Case_ID+": <br>"+Test_Scenario+"<br>"+Test_Scenario);
//							String cal_nuber = Call_To.substring(1, Call_To.length());
//							int secs = Integer.parseInt(CALL_DURATION);
//							APIHandler.API(curtcid, trfold, "Before_Execution", MSISDN);
//							dr.set(new AndroidDriver(new URL("http://127.0.0.1:" +  ReadMobileproperties(device, "appiumport") + "/wd/hub"), capabilities));
//							Thread.sleep(3000);
//							dr.get().findElement(By.id("com.samsung.android.dialer:id/zero")).click();
//							 //dr.get().findElement(By.id("com.samsung.android.dialer:id/digits")).clear();
//							 dr.get().findElement(By.id("com.samsung.android.dialer:id/digits")).sendKeys(cal_nuber);
//							 takeScreenShot("Dialed Number");
//							 dr.get().findElement(By.id("com.samsung.android.dialer:id/left_frame_image_view")).click();
//							
//							
////							Runtime run = Runtime.getRuntime();
////							String execu = "adb -s "+device_name+" shell am start -a android.intent.action.CALL -d tel:"+Call_To;
////							System.out.println("Execution cmmand: "+execu);
////							run.exec(execu);
//							Thread.sleep(secs*1000);
//							takeScreenShot("Call process");
//							try {
//							dr.get().findElement(By.id("com.samsung.android.incallui:id/display_texture_view")).click();
//							}
//							catch (Exception e) {
//								//e.printStackTrace();
//								System.out.println("End user didn't connect the call");
//							 }
//							 Thread.sleep(1000);
//							 dr.get().findElement(By.id("com.samsung.android.incallui:id/disconnect_button_icon")).click();
//				
//							//com.samsung.android.incallui:id/disconnect_button
//							String result = dr.get().stopRecordingScreen();
//							
//							APIHandler.API(curtcid, trfold, "After_Execution", MSISDN);
//							//-------------------------- CDR Conversion -------------------------------------------//
//							
//							Asnconvertor.nodeValidation(Test_Scenario, MSISDN);
//							
//					//-------------------------- Report ----------------------------------------------//
//							String[] convertor = Asnconvertor.Result(trfold, MSISDN, "", Test_Scenario, Test_Case_ID, curtcid, "", Test_Scenario_I, Test_Case, "", "", "", Call_To, "", "", "", "", "", ExecutionStarttime, CALL_DURATION, "" );
//							
//							test.pass("</table><table><tr><th style= 'min-width: 168px'><b>MSISDN</b></th>"
//									+"<th style= 'min-width: 168px'><b>Test Scenario </b></th>"
//									+"<th style= 'min-width: 168px'><b>Test Case </b></th>"
//									+"<th style= 'min-width: 168px'><b>Called To </b></th>"
//									+"<th style= 'min-width: 168px'><b>Call Duration </b></th>"
//									+"<th style= 'min-width: 168px'><b> ScreenShot</b></th></tr>" + 
//									
//									//Device Result
//									"<tr><td style= 'min-width: 168px'>"+MSISDN+"</td><td style= 'min-width: 168px'>"+Test_Scenario+"</td><td style= 'min-width: 168px'>"+Test_Case+"</td><td style= 'min-width: 168px'>"+Call_To+"</td><td style= 'min-width: 168px'>"+ CALL_DURATION +"</td><td style= 'min-width: 168px'>"+ "<a href='"+curtcid+"/ScreenShots.html' target='_blank'>Deviced_Execution_ScreenShots</a></td></tr></table><br>");
//							
//							//CIS API
//							test.pass("<br><br><b>CS Get Account Details Response:</b>" 
//							+ "<br><b>Response before execution XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "\\CS_API_VALIDATION\\Before_Execution\\Response\\response.xml'>Click to View the Response</a>"
//							+ "<br><b>Response After execution XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "\\CS_API_VALIDATION\\After_Execution\\Response\\response.xml'>Click to View the Response</a><br>");
//						
//					com.codoid.products.fillo.Connection co = fillo.getConnection(Reference_Data);
//					String strQuery = "Select * from node_xml_conversion " + "where Test_Scenario= '"+Test_Scenario+"' and Execution ='Yes'";
//					Recordset rsr = co.executeQuery(strQuery);
//					while (rsr.next()) {
//						String Node_Type = rsr.getField("Node_To_Validate");
//						
//							//CIS Result
//							if(Node_Type.equalsIgnoreCase("CIS")) {
//							test.pass("<br><br><b>CIS Data:</b>"
//							+ "<br><b>CIS EDR: </b><br>" + convertor[3] 
//							+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[5]+"/"+convertor[7] + "/Output.csv'>Click to View the EDR</a>"+"</table><br>");
//							}
//							
//							//SDP Result
//							if(Node_Type.equalsIgnoreCase("SDP")) {
//							test.pass("<br><br><b>SDP Data:</b>"
//							+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[4]+"/"+convertor[6] + "/Output.xml'>Click to View the SDP CDR</a><br>");
//							}
//							
//							//OCC Result
//							if(Node_Type.equalsIgnoreCase("OCC")) {
//							test.pass("<br><br><b>OCC Data:</b>"
//							+ "<br><b>OCC Table: </b><br>" + convertor[2] 
//							+ "<br><b>XML Link---> </b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[0]+"/"+convertor[1] + "/output.xml'>Click to View the OCC CDR</a>"+"</table><br>");
//							}
//							
//							//AIR
//							if(Node_Type.equalsIgnoreCase("AIR")) 
//							{
//							test.pass("<br><br><b>AIR Data:</b>"
//							+ "<br><b>AIR Table: </b><br>" + convertor[10] 
//							+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[11]+"/"+convertor[9] + "/Output.xml'>Click to View the AIR CDR</a>"+"</table><br>");
//							}
//							
//							//CCN
//							if(Node_Type.equalsIgnoreCase("CCN")) 
//							{
//							test.pass("<br><br><b>CCN Data:</b>"
//							+ "<br><b>CCN Table: </b><br>" + convertor[14] 
//							+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[13]+"/"+convertor[12] + "/Output.xml'>Click to View the CCN CDR</a>"+"</table><br>");
//							}
//
//							extent.flush();
//							endTestCase(curtcid);
//							}
//						}
//						
//
//						// --------------------------- SMS ----------------------------------------//
//
//						else if (Test_Scenario.equals("LIVE USAGE SMS")) {
//							curtcid = inputs.getField("Test_Case_ID") + "-" + inputs.getField("Test_Scenario") + "_"
//									+ inputs.getField("Test_Case");
//							startTestCase(curtcid);
//							ExtentTest test = extent.createTest(inputs.getField("Test_Case_ID") + ": <br>"
//									+ inputs.getField("Test_Scenario") + "-" + inputs.getField("Test_Case"));
//							String To_Receiver = inputs.getField("RECEIVER_MSISDN");
//							String Text_Message = inputs.getField("Message_To_Send");
//							String Count = inputs.getField("SMS_COUNT");
//							int sms_count = Integer.parseInt(Count);
//							DesiredCapabilities capabilities = new DesiredCapabilities();
//							capabilities.setCapability("deviceName", device);
//							capabilities.setCapability("platformVersion", version);
//							capabilities.setCapability("platformName", "ANDROID");
//							capabilities.setCapability("bootstrapPort", bsport);
//							capabilities.setCapability("appPackage", package_name1);
//							capabilities.setCapability("appActivity", activity_name1);
//							APIHandler.API(curtcid, trfold, "Before_Execution", MSISDN);
//							
//							dr.set(new AndroidDriver(new URL(
//									"http://127.0.0.1:" + ReadMobileproperties(device, "appiumport") + "/wd/hub"),
//									capabilities));
//						try {
//							for (int i = 1; i <= sms_count; i++) {
//								dr.get().findElement(By.id("com.samsung.android.messaging:id/fab")).click();
//								Thread.sleep(2000);
////								Runtime run = Runtime.getRuntime();
//								dr.get().findElement(By.id("com.samsung.android.messaging:id/recipients_editor_to")).sendKeys(To_Receiver);
////								run.exec("adb -s " + device_name + " shell input text " + To_Receiver);
//								// takeScreenShot("To Receiver");
//								 dr.get().findElement(By.id("message_edit_text")).click();
//
//								// run.exec("adb -s "+device_name+" shell input tap 170 1050");
////				Thread.sleep(000);
////				info("Started enter...");
//								// run.exec("adb -s "+device_name+" shell input text "+Text_Message);
//								dr.get().findElement(By.xpath("//android.widget.EditText[@text='Enter message']"))
//										.sendKeys(Text_Message);
//								takeScreenShot("Message Content");
//								dr.get().findElement(By.id("send_button1")).click();
//								takeScreenShot("SMS Status");
//								dr.get().hideKeyboard();
//								takeScreenShot("SMS Send");
//								dr.get().navigate().back();
//							}}
//						catch (Exception e) {
//							//e.printStackTrace();
//							System.out.println("--------++++++---------");
//						 }
//							String result = dr.get().stopRecordingScreen();
//							APIHandler.API(curtcid, trfold, "After_Execution", MSISDN);
//							
//				//-------------------------- CDR Conversion -------------------------------------------//
//							
//							Asnconvertor.nodeValidation(Test_Scenario, MSISDN);
//							//APIHandler.UpdateService(curtcid, trfold, "Service_Revert", MSISDN, "1001");
//							
//							
//							String[] convertor = Asnconvertor.Result(trfold, MSISDN, "", Test_Scenario, Test_Case_ID, curtcid, "", Test_Scenario_I, Test_Case, "", "", "", "", Text_Message, To_Receiver, "", "", "", ExecutionStarttime, "", Count);
//							
//							test.pass("</table><table><tr><th style= 'min-width: 168px'><b>Test Scenario </b></th>"
//									+ "<th style= 'min-width: 168px'><b>Test Case </b></th>"
//									+ "<th style= 'min-width: 168px'><b>Message </b></th>"
//									+ "<th style= 'min-width: 168px'><b>Receiver Number </b></th>"
//									+ "<th style= 'min-width: 168px'><b>Number of SMS sent </b></th>"
//									+"<th style= 'min-width: 168px'><b> ScreenShot</b></th></tr>" + 
//									
//									//Device Result
//									"<tr><td style= 'min-width: 168px'>"+Test_Scenario+"</td><td style= 'min-width: 168px'>"+Test_Case+"</td><td style= 'min-width: 168px'>"+Text_Message+"</td><td style= 'min-width: 168px'>"+To_Receiver+"</td><td style= 'min-width: 168px'>"+ Count +"</td><td style= 'min-width: 168px'>"+ "<a href='"+curtcid+"/ScreenShots.html' target='_blank'>Deviced_Execution_ScreenShots</a></td></tr></table><br>");
//							
//								//CIS API
//								test.pass("<br><br><b>CS Get Account Details Response:</b>" 
//								+ "<br><b>Response before execution XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "\\CS_API_VALIDATION\\Before_Execution\\Response\\response.xml'>Click to View the Response</a>"
//								+ "<br><b>Response After execution XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "\\CS_API_VALIDATION\\After_Execution\\Response\\response.xml'>Click to View the Response</a><br>");
//							
//						com.codoid.products.fillo.Connection co = fillo.getConnection(Reference_Data);
//						String strQuery = "Select * from node_xml_conversion " + "where Test_Scenario= '"+Test_Scenario+"' and Execution ='Yes'";
//						Recordset rsr = co.executeQuery(strQuery);
//						while (rsr.next()) {
//							String Node_Type = rsr.getField("Node_To_Validate");
//							
//								//CIS Result
//								if(Node_Type.equalsIgnoreCase("CIS")) {
//								test.pass("<br><br><b>CIS Data:</b>"
//								+ "<br><b>CIS EDR: </b><br>" + convertor[3] 
//								+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[5]+"/"+convertor[7] + "/Output.csv'>Click to View the EDR</a>"+"</table><br>");
//								}
//								
//								//SDP Result
//								if(Node_Type.equalsIgnoreCase("SDP")) {
//								test.pass("<br><br><b>SDP Data:</b>"
//								+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[4]+"/"+convertor[6] + "/Output.xml'>Click to View the SDP CDR</a><br>");
//								}
//								
//								//OCC Result
//								if(Node_Type.equalsIgnoreCase("OCC")) {
//								test.pass("<br><br><b>OCC Data:</b>"
//								+ "<br><b>OCC Table: </b><br>" + convertor[2] 
//								+ "<br><b>XML Link---> </b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[0]+"/"+convertor[1] + "/output.xml'>Click to View the OCC CDR</a>"+"</table><br>");
//								}
//								
//								//AIR
//								if(Node_Type.equalsIgnoreCase("AIR")) 
//								{
//								test.pass("<br><br><b>AIR Data:</b>"
//								+ "<br><b>AIR Table: </b><br>" + convertor[10] 
//								+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[11]+"/"+convertor[9] + "/Output.xml'>Click to View the AIR CDR</a>"+"</table><br>");
//								}
//								
//								//CCN
//								if(Node_Type.equalsIgnoreCase("CCN")) 
//								{
//								test.pass("<br><br><b>CCN Data:</b>"
//								+ "<br><b>CCN Table: </b><br>" + convertor[14] 
//								+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[13]+"/"+convertor[12] + "/Output.xml'>Click to View the CCN CDR</a>"+"</table><br>");
//								}
//
//							extent.flush();
//							endTestCase(curtcid);
//							}
//						
//						}
						
						
						//----------------------	API Call	 --------------------------//
						
						if(Test_Scenario.contains("API")) {
							if(Test_Scenario.contains("CIS")) {
								curtcid = inputs.getField("Test_Case_ID")+"--"+inputs.getField("Test_Scenario");
								startTestCase(curtcid);
								ExtentTest test = extent.createTest(inputs.getField("Test_Case_ID")+": <br>"+inputs.getField("Test_Scenario"));

								String [] Result = APIparam.CIS_API(Test_Scenario, ExecutionStarttime, inputs.getField("Test_Case_ID"));
								
								test.pass("&nbsp<b><a style = 'color:hotpink' target = '_blank' href = '" + Result[0]
										+ "'>Click to View the " + Result[1] + " Response file</a></b>");
								extent.flush();
							}
							else if (Test_Scenario.contains("USSD_API")){
								curtcid = inputs.getField("Test_Case_ID")+"--"+inputs.getField("Test_Scenario");
								startTestCase(curtcid);
								ExtentTest test = extent.createTest(inputs.getField("Test_Case_ID")+": <br>"+inputs.getField("Test_Scenario"));

								String USSD = inputs.getField("USSD_CODE");
								String Menu_Option = inputs.getField("Menu_Options");
								//String MSISDN = inputs.getField("MSISDN");
								String Env = inputs.getField("Environment");
								String[] Result = APIparam.USSDcontrol(Test_Scenario, ExecutionStarttime, inputs.getField("Test_Case_ID"), USSD, MSISDN, "First_USSD_", Env);
								if (Menu_Option.equals("")) {
									System.out.println("Menu option is empty, this might be Long code");
								}
								else {
										String[] spltussd = Menu_Option.split(",");
										for (int currshortcode = 0; currshortcode < spltussd.length; currshortcode++) {
											String nxt = "fail";
											do {
												try {
													System.out.println("------------------------------");
													Thread.sleep(1000);
													nxt = "pass";
												} catch (Exception e) { // Thread.sleep(100); }

												}
											} while (nxt != "pass");
											System.out.println("------------------------------");
										try {
											info("Entering code : " + spltussd[currshortcode]);
											String USSD1 = spltussd[currshortcode];
											String count = Integer.toString(currshortcode);
											String[] Result1 = APIparam.USSDcontrol(Test_Scenario, ExecutionStarttime, inputs.getField("Test_Case_ID"), USSD1, MSISDN, count, Env);
										}
										catch (Exception e) { // Thread.sleep(100); }
											takeScreenShot("Error in option selection");	
										}
									}
									}
								
								test.pass("&nbsp<b><a style = 'color:hotpink' target = '_blank' href = '" + Result[0]
										+ "'>Click to View the " + Result[1] + " Response file</a></b><br>"+ "</table>");
								extent.flush();
								}
							else {
							curtcid = inputs.getField("Test_Case_ID")+"--"+inputs.getField("Test_Scenario")+"_"+inputs.getField("Test_Case");
							startTestCase(curtcid);
							ExtentTest test = extent.createTest(inputs.getField("Test_Case_ID")+": <br>"+inputs.getField("Test_Scenario"));
							String[] Result = APIparam.APIcontrol(Test_Scenario, ExecutionStarttime, inputs.getField("Test_Case_ID"));
							
							test.pass("&nbsp<b><a style = 'color:hotpink' target = '_blank' href = '" + Result[0]
									+ "'>Click to View the " + Result[1] + " Response file</a></b><br>" + Result[2] + "</table>");
							extent.flush();

								}
							}
						else {
							System.out.println("Please check the entered scenario name");
						}
						

						// ---------------------- Balance Enquiry ---------------------------//
//
//						else if (Test_Scenario.equals("BALANCE ENQUIRES")) {
//							strQuery = "Select * from balance_enquires " + "where Test_Scenario ='" + Test_Scenario
//									+ "'";
//							Recordset rs = conn1.executeQuery(strQuery);
//							while (rs.next()) {
//								String ussdstr = rs.getField("USSD_Sequence");
//								Test_Scenario_I = rs.getField("Test_Scenario");
//								String startussd = URLEncoder.encode(rs.getField("USSD_Code"), "UTF-8");
//								String hash = URLEncoder.encode("#", "UTF-8");
//
//								curtcid = inputs.getField("Test_Case_ID") + "--" + rs.getField("Test_Scenario");
//								startTestCase(curtcid);
//								ExtentTest test = extent.createTest(inputs.getField("Test_Case_ID") + ": <br>"
//										+ inputs.getField("Test_Scenario"));
//
//								DesiredCapabilities capabilities = new DesiredCapabilities();
//								capabilities.setCapability("deviceName", device);
//								capabilities.setCapability("platformVersion", version);
//								capabilities.setCapability("platformName", "ANDROID");
//								capabilities.setCapability("bootstrapPort", bsport);
//								capabilities.setCapability("appPackage", package_name);
//								capabilities.setCapability("appActivity", activity_name);
//
//								dr.set(new AndroidDriver(new URL(
//										"http://127.0.0.1:" + ReadMobileproperties(device, "appiumport") + "/wd/hub"),
//										capabilities));
//								dr.get().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
//								Thread.sleep(2000);
//								
//								//---------- Dial Number -------------//
//								 
//								 dr.get().findElement(By.id("com.samsung.android.dialer:id/star")).click();
//								 dr.get().findElement(By.id("com.samsung.android.dialer:id/digits")).sendKeys(startussd);
//								 dr.get().findElement(By.id("com.samsung.android.dialer:id/pound")).click();
//								 takeScreenShot("Dialed Number");
//								 dr.get().findElement(By.id("com.samsung.android.dialer:id/dialButtonImage")).click();
//								
////								Runtime run = Runtime.getRuntime();
////								run.exec("adb -s " + device_name
////										+ " shell am start -a android.intent.action.CALL -d tel:" + startussd);
//								Thread.sleep(2000);
//								By inputfield = By.id("com.android.phone:id/input_field");
//								if (elementExists(inputfield)) {
//									String[] spltussd = ussdstr.split(",");
//									for (int currshortcode = 0; currshortcode < spltussd.length; currshortcode++) {
//										String nxt = "fail";
//										do {
//											try {
//												System.out.println("------------------------------");
//												Thread.sleep(1000);
//												dr.get().findElement(By.id("com.android.phone:id/input_field"));
//												nxt = "pass";
//											} catch (Exception e) { // Thread.sleep(100); }
//
//											}
//										} while (nxt != "pass");
//										System.out.println("------------------------------");
//										info("Entering code : " + spltussd[currshortcode]);
//										dr.get().findElement(By.id("com.android.phone:id/input_field"))
//												.sendKeys(spltussd[currshortcode]);
//										dr.get().hideKeyboard();
//										takeScreenShot("Entering code " + spltussd[currshortcode]);
//										dr.get().findElement(By.id("android:id/button1")).click();
//									}
//								} else {
//									info("Menu options are not available");
//									takeScreenShot("Error appears");
////				Confirmation = dr.get().findElement(By.id("android:id/message")).getText();
////				info("Confirmation alert : "+Confirmation);
////				dr.get().findElement(By.id("android:id/button1")).click();
//									dr.get().quit();
//								}
//								Thread.sleep(30000);
//								List<MobileElement> elements1 = dr.get().findElements(By.id("android:id/message"));
//								for (MobileElement link : elements1) {
//									Confirmation = dr.get().findElement(By.id("android:id/message")).getText();
//									Balancemsg = Balancemsg + "<br>" + Confirmation;
//									info("Confirmation alert : " + Balancemsg);
//								}
//								takeScreenShot("Balance Message");
//								dr.get().findElement(By.id("android:id/button1")).click();
//								String result = dr.get().stopRecordingScreen();
//								Asnconvertor.nodeValidation(Test_Scenario, MSISDN);
//							//-------------------Result------------------------------
//								String[] convertor = Asnconvertor.Result(trfold, MSISDN, "", Test_Scenario, Test_Case_ID, curtcid, "", Test_Scenario_I, Test_Case, "", "", "", "", "", "", Balancemsg, "", "", ExecutionStarttime, "", "");
//						
//								test.pass("</table><table><tr><th style= 'min-width: 168px'><b>Test Scenario </b></th>"
//										+ "<th style= 'min-width: 168px'><b>Balance Message </b></th>"
//										+"<th style= 'min-width: 168px'><b> ScreenShot</b></th></tr>" + 
//										
//										//Device Result
//										"<tr><td style= 'min-width: 168px'>"+Test_Scenario+"</td><td style= 'min-width: 168px'>"+Balancemsg +"</td><td style= 'min-width: 168px'>"+ "<a href='"+curtcid+"/ScreenShots.html' target='_blank'>Deviced_Execution_ScreenShots</a></td></tr></table><br>");
//								
//														
//						com.codoid.products.fillo.Connection co = fillo.getConnection(Reference_Data);
//						String strQuery = "Select * from node_xml_conversion " + "where Test_Scenario= '"+Test_Scenario+"' and Execution ='Yes'";
//						Recordset rsr = co.executeQuery(strQuery);
//						while (rsr.next()) {
//							String Node_Type = rsr.getField("Node_To_Validate");
//							
//								//CIS Result
//								if(Node_Type.equalsIgnoreCase("CIS")) {
//								test.pass("<br><br><b>CIS Data:</b>"
//								+ "<br><b>CIS EDR: </b><br>" + convertor[3] 
//								+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[5]+"/"+convertor[7] + "/Output.csv'>Click to View the EDR</a>"+"</table><br>");
//								}
//								
//								//SDP Result
//								if(Node_Type.equalsIgnoreCase("SDP")) {
//								test.pass("<br><br><b>SDP Data:</b>"
//								+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[4]+"/"+convertor[6] + "/Output.xml'>Click to View the SDP CDR</a><br>");
//								}
//								
//								//OCC Result
//								if(Node_Type.equalsIgnoreCase("OCC")) {
//								test.pass("<br><br><b>OCC Data:</b>"
//								+ "<br><b>OCC Table: </b><br>" + convertor[2] 
//								+ "<br><b>XML Link---> </b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[0]+"/"+convertor[1] + "/output.xml'>Click to View the OCC CDR</a>"+"</table><br>");
//								}
//								
//								//AIR
//								if(Node_Type.equalsIgnoreCase("AIR")) 
//								{
//								test.pass("<br><br><b>AIR Data:</b>"
//								+ "<br><b>AIR Table: </b><br>" + convertor[10] 
//								+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[11]+"/"+convertor[9] + "/Output.xml'>Click to View the AIR CDR</a>"+"</table><br>");
//								}
//								
//								//CCN
//								if(Node_Type.equalsIgnoreCase("CCN")) 
//								{
//								test.pass("<br><br><b>CCN Data:</b>"
//								+ "<br><b>CCN Table: </b><br>" + convertor[14] 
//								+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[13]+"/"+convertor[12] + "/Output.xml'>Click to View the CCN CDR</a>"+"</table><br>");
//								}
//								extent.flush();
//								endTestCase(curtcid);
//								}
//								}
//								}
//
//						// ------------------- P2P Transfer -----------------------------------//
//
//						else if (Test_Scenario.equals("P2P TRANSFER")) {
//							DesiredCapabilities capabilities = new DesiredCapabilities();
//							capabilities.setCapability("deviceName", device);
//							capabilities.setCapability("platformVersion", version);
//							capabilities.setCapability("platformName", "ANDROID");
//							capabilities.setCapability("bootstrapPort", bsport);
//							capabilities.setCapability("appPackage", package_name);
//							capabilities.setCapability("appActivity", activity_name);
//							strQuery = "Select * from p2p_transfer " + "where Test_Scenario ='" + Test_Scenario + "'";
//							Recordset rs = conn1.executeQuery(strQuery);
//							while (rs.next()) {
//								String ussdstr = rs.getField("USSD_Sequence");
//								Test_Scenario_I = rs.getField("Test_Scenario");
//								String startussd = URLEncoder.encode(rs.getField("USSD_Code"), "UTF-8");
//								String hash = URLEncoder.encode("#", "UTF-8");
//
//								curtcid = inputs.getField("Test_Case_ID") + "--" + rs.getField("Test_Scenario");
//								startTestCase(curtcid);
//								APIHandler.API(curtcid, trfold, "Before_Execution", MSISDN);
//								ExtentTest test = extent.createTest(inputs.getField("Test_Case_ID") + ": <br>"
//										+ inputs.getField("Test_Scenario"));
//								dr.set(new AndroidDriver(new URL(
//										"http://127.0.0.1:" + ReadMobileproperties(device, "appiumport") + "/wd/hub"),
//										capabilities));
//								dr.get().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
//								Thread.sleep(2000);
//								
//								//---------- Dial Number -------------//
//								 
//								 dr.get().findElement(By.id("com.samsung.android.dialer:id/star")).click();
//								 dr.get().findElement(By.id("com.samsung.android.dialer:id/digits")).sendKeys(startussd);
//								 dr.get().findElement(By.id("com.samsung.android.dialer:id/pound")).click();
//								 takeScreenShot("Dialed Number");
//								 dr.get().findElement(By.id("com.samsung.android.dialer:id/dialButtonImage")).click();
//								
////								Runtime run = Runtime.getRuntime();
////								run.exec("adb -s " + device_name
////										+ " shell am start -a android.intent.action.CALL -d tel:" + startussd);
//								
//								 Thread.sleep(2000);
//								By inputfield = By.id("com.android.phone:id/input_field");
//								if (elementExists(inputfield)) {
//									String[] spltussd = ussdstr.split(",");
//									for (int currshortcode = 0; currshortcode < spltussd.length; currshortcode++) {
//										String nxt = "fail";
//										do {
//											try {
//												System.out.println("------------------------------");
//												Thread.sleep(1000);
//												dr.get().findElement(By.id("com.android.phone:id/input_field"));
//												nxt = "pass";
//											} catch (Exception e) { // Thread.sleep(100); }
//
//											}
//										} while (nxt != "pass");
//										System.out.println("------------------------------");
//										info("Entering code : " + spltussd[currshortcode]);
////				Thread.sleep(2000);
//										dr.get().findElement(By.id("com.android.phone:id/input_field"))
//												.sendKeys(spltussd[currshortcode]);
//										dr.get().hideKeyboard();
//										takeScreenShot("Entering code " + spltussd[currshortcode]);
//										dr.get().findElement(By.id("android:id/button1")).click();
//									}
//									Thread.sleep(2000);
//								try {
//									Amount = inputs.getField("TRANSFER_AMOUNT");
//									dr.get().findElement(By.id("com.android.phone:id/input_field")).sendKeys(Amount);
//									dr.get().hideKeyboard();
//									takeScreenShot("Entering Transfer Amount: " + Amount);
//									dr.get().findElement(By.id("android:id/button1")).click();
//									To_Number = inputs.getField("TRANSFER_TO_MSISDN");
//									dr.get().findElement(By.id("com.android.phone:id/input_field")).sendKeys(To_Number);
//									dr.get().hideKeyboard();
//									takeScreenShot("Entering Mobile Number: " + To_Number);
//									dr.get().findElement(By.id("android:id/button1")).click();
//									if (elementExists(inputfield)) {
//									dr.get().findElement(By.id("com.android.phone:id/input_field")).sendKeys("1");
//									dr.get().hideKeyboard();
//									takeScreenShot("Enter Code to Confirm");
//									dr.get().findElement(By.id("android:id/button1")).click();
//									}
//									Thread.sleep(2000);
//									Confirmation = dr.get().findElement(By.id("android:id/message")).getText();
//									info("Confirmation alert : " + Confirmation);
//									takeScreenShot("Confirmation Screen");
//									dr.get().findElement(By.id("android:id/button1")).click();
//
//									// Notification Message handle
//									dr.get().quit();
//								} 
//								catch (Exception e) { // Thread.sleep(100); }
//									e.printStackTrace();
//								}
//								} else {
//									info("Menu options are not available");
//									takeScreenShot("Error appears");
////				Confirmation = dr.get().findElement(By.id("android:id/message")).getText();
////				info("Confirmation alert : "+Confirmation);
////				dr.get().findElement(By.id("android:id/button1")).click();
//									dr.get().quit();
//								}
//								DesiredCapabilities capabilitiese = new DesiredCapabilities();
//							capabilitiese.setCapability("deviceName", device);
//							capabilitiese.setCapability("platformVersion", version);
//							capabilitiese.setCapability("platformName", "ANDROID");
//							capabilitiese.setCapability("bootstrapPort", bsport);
//							capabilitiese.setCapability("appPackage", package_name1);
//							capabilitiese.setCapability("appActivity", activity_name1);
//								dr.set(new AndroidDriver(new URL(
//										"http://127.0.0.1:" + ReadMobileproperties(device, "appiumport") + "/wd/hub"),
//										capabilitiese));
//								Thread.sleep(3000);
//								By New_Message = By.id("com.samsung.android.messaging:id/list_unread_count");
//								if (elementExists(New_Message)) {
//									List<MobileElement> elements1 = dr.get().findElements(By.id("com.samsung.android.messaging:id/list_unread_count"));
//									for (MobileElement link : elements1) {
//										{
//											dr.get().findElement(
//													By.id("com.samsung.android.messaging:id/list_unread_count"))
//													.click();
//											List<MobileElement> elements2 = dr.get().findElements(By.id("com.samsung.android.messaging:id/content_text_view"));
//											for (MobileElement link1 : elements1) {
//												Message = dr.get()
//														.findElement(By.id(
//																"com.samsung.android.messaging:id/content_text_view"))
//														.getText();
//												Balancemsg = Balancemsg + "<br>" + Message;
//												info("Message Received : " + Balancemsg);
//											}
//											takeScreenShot("Related SMS Notification");
//											By visibile2 = By.xpath("//android.widget.Button[@text='Delete']");
//											if (elementExists(visibile2)) {
//												dr.get().findElement(
//														By.xpath("//android.widget.Button[@text='Delete']")).click();
//											} else {
//												dr.get().findElement(By
//														.id("com.samsung.android.messaging:id/composer_setting_button"))
//														.click();
//												dr.get().findElement(By.id(
//														"com.samsung.android.messaging:id/composer_drawer_delete_conversation_text"))
//														.click();
//											}
//											By Delete = By.id("com.samsung.android.messaging:id/largeLabel");
//											if (elementExists(Delete)) {
//												// delete button is displayed
//											} else {
//												dr.get().findElement(By.id(
//														"com.samsung.android.messaging:id/bubble_all_select_checkbox"))
//														.click();
//											}
//											dr.get().findElement(By.id("com.samsung.android.messaging:id/largeLabel"))
//													.click();
//											dr.get().findElement(By.id("android:id/button1")).click();
//
//										}
//									}
//								} else {
//									Message = "Message not received for the provided USSD";
//									info("Message not received for the provided USSD");
//									takeScreenShot("SMS not received");
//								}
//								String result = dr.get().stopRecordingScreen();
//								APIHandler.API(curtcid, trfold, "After_Execution", MSISDN);
//								
//						//-------------------------- CDR Conversion -------------------------------------------//
//								
//								Asnconvertor.nodeValidation(Test_Scenario, MSISDN);
//								
//						//-------------------------- Report ----------------------------------------------//
//								String[] convertor = Asnconvertor.Result(trfold, MSISDN, "", Test_Scenario, Test_Case_ID, curtcid, "", "", "", Confirmation, Message, "", "", "", "", "", To_Number, Amount, ExecutionStarttime, "", "");
//								
//								test.pass("</table><table><tr><th style= 'min-width: 168px'><b>Test Scenario </b></th>"
//										+ "<th style= 'min-width: 168px'><b>Test Case </b></th>"
//										+ "<th style= 'min-width: 168px'><b>Confirmation</b></th>"
//										+ "<th style= 'min-width: 168px'><b>Message</b></th>"
//										+ "<th style= 'min-width: 168px'><b>P2P To Number </b></th>"
//										+ "<th style= 'min-width: 168px'><b>P2P Amount </b></th>"
//										+"<th style= 'min-width: 168px'><b>ScreenShot</b></th></tr>" + 
//										
//										//Device Result
//										"<tr><td style= 'min-width: 168px'>"+Test_Scenario+"</td><td style= 'min-width: 168px'>"+Test_Case+"</td><td style= 'min-width: 168px'>"+Confirmation+"</td><td style= 'min-width: 168px'>"+Message+"</td><td style= 'min-width: 168px'>"+To_Number+"</td><td style= 'min-width: 168px'>"+Amount+"</td><td style= 'min-width: 168px'><a href='"+curtcid+"/ScreenShots.html' target='_blank'>Deviced_Execution_ScreenShots</a></td></tr></table><br>");
//								
//								//CIS API
//								test.pass("<br><br><b>CS Get Account Details Response:</b>" 
//								+ "<br><b>Response before execution XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "\\CS_API_VALIDATION\\Before_Execution\\Response\\response.xml'>Click to View the Response</a>"
//								+ "<br><b>Response After execution XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "\\CS_API_VALIDATION\\After_Execution\\Response\\response.xml'>Click to View the Response</a><br>");
//							
//						com.codoid.products.fillo.Connection co = fillo.getConnection(Reference_Data);
//						String strQuery = "Select * from node_xml_conversion " + "where Test_Scenario= '"+Test_Scenario+"' and Execution ='Yes'";
//						Recordset rsr = co.executeQuery(strQuery);
//						while (rsr.next()) {
//							String Node_Type = rsr.getField("Node_To_Validate");
//							
//								//CIS Result
//								if(Node_Type.equalsIgnoreCase("CIS")) {
//								test.pass("<br><br><b>CIS Data:</b>"
//								+ "<br><b>CIS EDR: </b><br>" + convertor[3] 
//								+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[5]+"/"+convertor[7] + "/Output.csv'>Click to View the EDR</a>"+"</table><br>");
//								}
//								
//								//SDP Result
//								if(Node_Type.equalsIgnoreCase("SDP")) {
//								test.pass("<br><br><b>SDP Data:</b>"
//								+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[4]+"/"+convertor[6] + "/Output.xml'>Click to View the SDP CDR</a><br>");
//								}
//								
//								//OCC Result
//								if(Node_Type.equalsIgnoreCase("OCC")) {
//								test.pass("<br><br><b>OCC Data:</b>"
//								+ "<br><b>OCC Table: </b><br>" + convertor[2] 
//								+ "<br><b>XML Link---> </b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[0]+"/"+convertor[1] + "/output.xml'>Click to View the OCC CDR</a>"+"</table><br>");
//								}
//								
//								//AIR
//								if(Node_Type.equalsIgnoreCase("AIR")) 
//								{
//								test.pass("<br><br><b>AIR Data:</b>"
//								+ "<br><b>AIR Table: </b><br>" + convertor[10] 
//								+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[11]+"/"+convertor[9] + "/Output.xml'>Click to View the AIR CDR</a>"+"</table><br>");
//								}
//								
//								//CCN
//								if(Node_Type.equalsIgnoreCase("CCN")) 
//								{
//								test.pass("<br><br><b>CCN Data:</b>"
//								+ "<br><b>CCN Table: </b><br>" + convertor[14] 
//								+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[13]+"/"+convertor[12] + "/Output.xml'>Click to View the CCN CDR</a>"+"</table><br>");
//								}
//							
//								extent.flush();
//								endTestCase(curtcid);
//								}
//							}
//							}
//
//						// ------------------------- Data ---------------------------//
//
//						else if (Test_Scenario.equals("LIVE USAGE DATA")) {
//							String package_Data = ReadMobileproperties(inputs.getField("Test_Case"), "apppackage");
//							String activity_Data = ReadMobileproperties(inputs.getField("Test_Case"), "appactivity");
//							String package_Data1 = ReadMobileproperties(inputs.getField("Test_Case"), "apppackage1");
//							String activity_Data1 = ReadMobileproperties(inputs.getField("Test_Case"), "appactivity1");
//							
//																			
//					//-------------- YouTube activity -------------------//
//							
//							DesiredCapabilities capabilities = new DesiredCapabilities();
//							capabilities.setCapability("deviceName", device);
//							capabilities.setCapability("platformVersion", version);
//							capabilities.setCapability("platformName", "ANDROID");
//							capabilities.setCapability("bootstrapPort", bsport);
//							capabilities.setCapability("appPackage", package_Data);
//							capabilities.setCapability("appActivity", activity_Data);
//							
//							DesiredCapabilities capabilities1 = new DesiredCapabilities();
//							capabilities1.setCapability("deviceName", device);
//							capabilities1.setCapability("platformVersion", version);
//							capabilities1.setCapability("platformName", "ANDROID");
//							capabilities1.setCapability("bootstrapPort", bsport);
//							capabilities1.setCapability("appPackage", package_Data1);
//							capabilities1.setCapability("appActivity", activity_Data1);
//							
//							curtcid = inputs.getField("Test_Case_ID") + "--" + inputs.getField("Test_Scenario") + "_"
//									+ inputs.getField("Test_Case");
//							startTestCase(curtcid);
//							ExtentTest test = extent.createTest(inputs.getField("Test_Case_ID") + ": <br>"
//									+ inputs.getField("Test_Scenario") + "<br>" + inputs.getField("Test_Case"));
//							if (Test_Case.equals("DATA_REGULAR")) {
//								
//								//-------------	Data Turn ON --------------------------//
//							try {
//								dr.set(new AndroidDriver(new URL(
//										"http://127.0.0.1:" + ReadMobileproperties(device, "appiumport") + "/wd/hub"),
//										capabilities1));
//								Thread.sleep(2000);
//								dr.get().findElement(By.xpath("//android.widget.TextView[@text='Connections']")).click();
//								Thread.sleep(2000);
//								dr.get().findElement(By.xpath("//android.widget.TextView[@text='Data usage']")).click();
//								Thread.sleep(2000);
//								dr.get().findElement(By.id("android:id/switch_widget")).click();
//								Thread.sleep(2000);
//								takeScreenShot("Data Turned On: " + timefold);
//								
////								Runtime run = Runtime.getRuntime();
////								run.exec("adb shell svc data enable");
////								Thread.sleep(2000);
//								dr.set(new AndroidDriver(new URL(
//										"http://127.0.0.1:" + ReadMobileproperties(device, "appiumport") + "/wd/hub"),
//										capabilities));
//								try {
//								Thread.sleep(3000);
//								dr.get().findElement(By.xpath("//android.widget.TextView[@text='Trending']")).click();
//								Thread.sleep(2000);
//								dr.get().findElement(By.xpath("//android.view.ViewGroup[@index='1']")).click();
//								Thread.sleep(15000);
//								takeScreenShot("Regular Network -- you tube");
//								}
//								catch (Exception e) {
//									//e.printStackTrace();
//									System.out.println("--------++++++---------");
//								 }
//							}
//							catch (Exception e) {
//								e.printStackTrace();
//							 }						
//						//-------------	Data Turn OFF --------------------------//
//																					
//								dr.set(new AndroidDriver(new URL(
//										"http://127.0.0.1:" + ReadMobileproperties(device, "appiumport") + "/wd/hub"),
//										capabilities1));
//								Thread.sleep(2000);
//								dr.get().findElement(By.xpath("//android.widget.TextView[@text='Connections']")).click();
//								Thread.sleep(2000);
//								dr.get().findElement(By.xpath("//android.widget.TextView[@text='Data usage']")).click();
//								Thread.sleep(2000);
//								dr.get().findElement(By.id("android:id/switch_widget")).click();
//								takeScreenShot("Data Truned OFF: " + timefold);
//																
////								run.exec("adb shell svc data disable");
////								Thread.sleep(2000);
////								takeScreenShot("Data Turned off: " + timefold);
//								
//								
//							} else if (Test_Case.equals("DATA_SOCIAL")) {
//								
//						//-------------	Data Turn ON --------------------------//
//								
//								dr.set(new AndroidDriver(new URL(
//										"http://127.0.0.1:" + ReadMobileproperties(device, "appiumport") + "/wd/hub"),
//										capabilities1));
//								Thread.sleep(2000);
//								dr.get().findElement(By.xpath("//android.widget.TextView[@text='Connections']")).click();
//								Thread.sleep(2000);
//								dr.get().findElement(By.xpath("//android.widget.TextView[@text='Data usage']")).click();
//								Thread.sleep(2000);
//								dr.get().findElement(By.id("android:id/switch_widget")).click();
//								Thread.sleep(2000);
//								takeScreenShot("Data Turned On: " + timefold);
//								
//								dr.set(new AndroidDriver(new URL(
//										"http://127.0.0.1:" + ReadMobileproperties(device, "appiumport") + "/wd/hub"),
//										capabilities));
//								try {
//								dr.get().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
//							//	Runtime run = Runtime.getRuntime();
//							//	run.exec("adb shell svc data enable");
//							//	Thread.sleep(2000);
//							//	takeScreenShot("Data Truned On: " + timefold);
//							//	Thread.sleep(5000);
//								dr.get().findElement(
//										By.xpath("//android.widget.EditText[@text='Phone number or email address']")).click();
//								dr.get().findElement(
//										By.xpath("//android.widget.EditText[@text='Phone number or email address']"))
//										.sendKeys("mugaz25@yahoo.com");
//								dr.get().findElement(By.xpath("//android.widget.EditText[@text='Password']"))
//										.sendKeys("Tester123!");
//								dr.get().findElement(By.xpath("//android.view.ViewGroup[@index=3]")).click();
//								Thread.sleep(5000);
//								takeScreenShot("Social Network -- Facebook");
//								}
//								catch (Exception e) {
//									//e.printStackTrace();
//									System.out.println("--------++++++---------");
//								 }
//								
//						//-------------	Data Turn OFF --------------------------//
//								
//								dr.set(new AndroidDriver(new URL(
//										"http://127.0.0.1:" + ReadMobileproperties(device, "appiumport") + "/wd/hub"),
//										capabilities1));
//								Thread.sleep(2000);
//								dr.get().findElement(By.xpath("//android.widget.TextView[@text='Connections']")).click();
//								Thread.sleep(2000);
//								dr.get().findElement(By.xpath("//android.widget.TextView[@text='Data usage']")).click();
//								Thread.sleep(2000);
//								dr.get().findElement(By.id("android:id/switch_widget")).click();
//								takeScreenShot("Data Truned OFF: " + timefold);
//								
////								run.exec("adb shell svc data disable");
////								Thread.sleep(2000);
////								takeScreenShot("Data Turned off: " + timefold);
//							}
//							String result = dr.get().stopRecordingScreen();
//							
//							APIHandler.API(curtcid, trfold, "After_Execution", MSISDN);
//							
//							//-------------------------- CDR Conversion -------------------------------------------//
//							
//							Asnconvertor.nodeValidation(Test_Scenario, MSISDN);
//							
//					//-------------------------- Report ----------------------------------------------//
//							String[] convertor = Asnconvertor.Result(trfold, MSISDN, "", Test_Scenario, Test_Case_ID, curtcid, "", "", Test_Case, "", "", "", "", "", "", "", "", "", ExecutionStarttime, "", "");
//							
//							test.pass("</table><table><tr><th style= 'min-width: 168px'><b>Test Scenario </b></th>"
//									+ "<th style= 'min-width: 168px'><b>Test Case </b></th>"
//									+"<th style= 'min-width: 168px'><b> ScreenShot</b></th></tr>" + 
//									
//									//Device Result
//									"<tr><td style= 'min-width: 168px'>"+Test_Scenario+"</td><td style= 'min-width: 168px'>"+Test_Case+"</td><td style= 'min-width: 168px'><a href='"+curtcid+"/ScreenShots.html' target='_blank'>Deviced_Execution_ScreenShots</a></td></tr></table><br>");
//							
//							//CIS API
//							test.pass("<br><br><b>CS Get Account Details Response:</b>" 
//							+ "<br><b>Response before execution XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "\\CS_API_VALIDATION\\Before_Execution\\Response\\response.xml'>Click to View the Response</a>"
//							+ "<br><b>Response After execution XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "\\CS_API_VALIDATION\\After_Execution\\Response\\response.xml'>Click to View the Response</a><br>");
//						
//						com.codoid.products.fillo.Connection co = fillo.getConnection(Reference_Data);
//						String strQuery = "Select * from node_xml_conversion " + "where Test_Scenario= '"+Test_Scenario+"' and Execution ='Yes'";
//						Recordset rsr = co.executeQuery(strQuery);
//						while (rsr.next()) {
//							String Node_Type = rsr.getField("Node_To_Validate");
//							
//								//CIS Result
//								if(Node_Type.equalsIgnoreCase("CIS")) {
//								test.pass("<br><br><b>CIS Data:</b>"
//								+ "<br><b>CIS EDR: </b><br>" + convertor[3] 
//								+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[5]+"/"+convertor[7] + "/Output.csv'>Click to View the EDR</a>"+"</table><br>");
//								}
//								
//								//SDP Result
//								if(Node_Type.equalsIgnoreCase("SDP")) {
//								test.pass("<br><br><b>SDP Data:</b>"
//								+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[4]+"/"+convertor[6] + "/Output.xml'>Click to View the SDP CDR</a><br>");
//								}
//								
//								//OCC Result
//								if(Node_Type.equalsIgnoreCase("OCC")) {
//								test.pass("<br><br><b>OCC Data:</b>"
//								+ "<br><b>OCC Table: </b><br>" + convertor[2] 
//								+ "<br><b>XML Link---> </b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[0]+"/"+convertor[1] + "/output.xml'>Click to View the OCC CDR</a>"+"</table><br>");
//								}
//								
//								//AIR
//								if(Node_Type.equalsIgnoreCase("AIR")) 
//								{
//								test.pass("<br><br><b>AIR Data:</b>"
//								+ "<br><b>AIR Table: </b><br>" + convertor[10] 
//								+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[11]+"/"+convertor[9] + "/Output.xml'>Click to View the AIR CDR</a>"+"</table><br>");
//								}
//								
//								//CCN
//								if(Node_Type.equalsIgnoreCase("CCN")) 
//								{
//								test.pass("<br><br><b>CCN Data:</b>"
//								+ "<br><b>CCN Table: </b><br>" + convertor[14] 
//								+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[13]+"/"+convertor[12] + "/Output.xml'>Click to View the CCN CDR</a>"+"</table><br>");
//								}
//
//							extent.flush();
//							endTestCase(curtcid);
//						}
//						}
//							
//			}
		} 
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
public static void copydir(String Source, String Destination) {
		
		File srcDir = new File(Source);

		
		File destDir = new File(Destination);

		try {
		    FileUtils.copyDirectory(srcDir, destDir);
		} catch (IOException e) {
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

	public void takescreenshot() {
		// Capture screenshot.
		// Thread.sleep(100);\
		String destDir = trfold + "/MobileScreenshots";

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
			// Thread.sleep(100);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void createtimestampfold() {
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		Calendar cal = Calendar.getInstance();
		try {
			resfold = new File(Result_FLD + "/" + loginUser + "/" + dateFormat.format(cal.getTime()) + "/");
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
		String destDir =trfold + "\\" + curtcid + "\\";
		System.out.println("Screenshot Folder: "+destDir);
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
			// Thread.sleep(5000);
		} catch (IOException e) {
			e.printStackTrace();
		}

		File file = new File(trfold + "\\" + curtcid + "\\ScreenShots.html");

		// Create the file
		if (file.createNewFile()) {
			// System.out.println("File is created!");
			// info("File is created!");
		} else {
			// System.out.println("File already exists.");
			// info("File already exists.");
		}

		// Write Content
		FileWriter writer = new FileWriter(file, true);
		writer.write(scdesc + "<br><img src = '" + destFile + "' height='500px'><br><br>");
		writer.close();
	}

	public void run() {

	}

	public void starter(String port) {

		// Build the Appium service
		AppiumServiceBuilder builder = new AppiumServiceBuilder();
		builder.withIPAddress("127.0.0.1");
		int port1 = Integer.parseInt(port);
		builder.usingPort(port1);
		builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
		builder.withArgument(GeneralServerFlag.LOG_LEVEL, "error");

		// Start the server with the builder
		AppiumDriverLocalService service = AppiumDriverLocalService.buildService(builder);
		service.start();
	}

	public void stopServer() {
		service.stop();
	}

}