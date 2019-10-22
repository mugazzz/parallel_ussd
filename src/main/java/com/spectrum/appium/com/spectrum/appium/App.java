package com.spectrum.appium.com.spectrum.appium;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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
	public static String datecis;

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
	
	String Call_To = "";
	String CALL_DURATION = "";
	String Productofferv = "";
	String ProductofferID = "";
	String offerStatev = "";
	String offerState = "";
	String ExpireDatev = "";
	String ExpireDate = "";
	String attributeNamev = "";
	String attributeName ="";
	String attributeValuev = "";
	String attributeValue = "";
	String pamClassIDv = "";
	String pamClassID = "";
	String pamServiceIDv = "";
	String pamServiceID = "";
	String pamServicePriorityv = "";
	String pamServicePriority = "";
	String scheduleIDv = "";
	String scheduleID = "";
	String lastEvaluationDatev = "";
	String lastEvaluationDate = "";
	String currentPamPeriodv = "";
	String currentPamPeriod ="";
	String dedicatedAccountIDv = "";
	String dedicatedAccountID = "";
	String dedicatedAccountActiveValue1v = "";
	String dedicatedAccountActiveValue1 = "";
	String dedicatedAccountUnitTypev = "";
	String dedicatedAccountUnitType = "";
	String dedicatedAccountValue1v = "";
	String dedicatedAccountValue1 = "";
	String expiryDatev = "";
	String expiryDate = "";
	String startDatev = "";
	String startDate ="";
	String CIS_type ="";
	String Statu = "";
	String Sta = "";
	int g = 0;
	int l=0;

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
						String device = "device1";
//						System.out.println(device);
						String MSISDN = inputs.getField("MSISDN");
						String Test_Scenario = inputs.getField("Test_Scenario");
						String Test_Case = inputs.getField("Test_Case");
						String Test_Case_ID = inputs.getField("Test_Case_ID");
						String table_type = inputs.getField("Table");
//						Prod_ID = inputs.getField("Product_Name");
//						Recharge_Coupon = inputs.getField("Recharge_Coupon");

						
						for (int Iterator = 1; Iterator <= 150; Iterator++) {
							String val[]  = new String[50];
							String para[] = new String[50];
							if (inputs.getField("Parameter" + Iterator).isEmpty() == false) {
								para[Iterator] = inputs.getField("Parameter" + Iterator);
								val[Iterator] = inputs.getField("Value" + Iterator);
								if(para[Iterator].contains("offerID")) {
									Productofferv = para[Iterator];
									ProductofferID = val[Iterator];
								}
								else if(para[Iterator].contains("offerState")) {
									offerStatev = para[Iterator];
									offerState = val[Iterator];
								}
								else if(para[Iterator].contains("expiryDateTime")) {
									ExpireDatev = para[Iterator];
									ExpireDate = val[Iterator];
								}
								else if(para[Iterator].contains("attributeName")) {
									attributeNamev = para[Iterator];
									attributeName = val[Iterator];
								}
								else if(para[Iterator].contains("attributeValueString")) {
									attributeValuev = para[Iterator];
									attributeValue = val[Iterator];
								}
								else if(para[Iterator].contains("pamClassID")) {
									pamClassIDv = para[Iterator];
									pamClassID = val[Iterator];
								}
								else if(para[Iterator].contains("pamServiceID")) {
									pamServiceIDv = para[Iterator];
									pamServiceID = val[Iterator];
								}
								else if(para[Iterator].contains("pamServicePriority")) {
									pamServicePriorityv = para[Iterator];
									pamServicePriority = val[Iterator];
								}
								else if(para[Iterator].contains("scheduleID")) {
									scheduleIDv = para[Iterator];
									scheduleID = val[Iterator];
								}
								else if(para[Iterator].contains("lastEvaluationDate")) {
									lastEvaluationDatev = para[Iterator];
									lastEvaluationDate = val[Iterator];
								}
								else if(para[Iterator].contains("currentPamPeriod")) {
									currentPamPeriodv = para[Iterator];
									currentPamPeriod = val[Iterator];
								}
								else if(para[Iterator].contains("dedicatedAccountID")) {
									dedicatedAccountIDv = para[Iterator];
									dedicatedAccountID = val[Iterator];
								}
								else if(para[Iterator].contains("dedicatedAccountActiveValue1")) {
									dedicatedAccountActiveValue1v = para[Iterator];
									dedicatedAccountActiveValue1 = val[Iterator];
								}
								else if(para[Iterator].contains("dedicatedAccountUnitType")) {
									dedicatedAccountUnitTypev = para[Iterator];
									dedicatedAccountUnitType = val[Iterator];
								}
								else if(para[Iterator].contains("dedicatedAccountValue1")) {
									dedicatedAccountValue1v = para[Iterator];
									dedicatedAccountValue1 = val[Iterator];
								}
								else if(para[Iterator].contains("expiryDate")) {
									expiryDatev = para[Iterator];
									expiryDate = val[Iterator];
								}
								else if(para[Iterator].contains("startDate")) {
									startDatev = para[Iterator];
									startDate = val[Iterator];
								}
								else if(para[Iterator].contains("Call_To")) {
									//Call_To = para[Iterator];
									Call_To = val[Iterator];
								}
								else if(para[Iterator].contains("CALL_DURATION")) {
									//CALL_DURATION = para[Iterator];
									CALL_DURATION = val[Iterator];
								}
								
							} else
								break;
						}
						
						
						info("Starting execution at +: " + Test_Case_ID + "->" + Test_Scenario + "->" + ExecutionStarttime);
						extent.attachReporter(htmlReporter);
						
						 //String Mobile = inputs.getField("Test_Device");
						String basedir = System.getProperty("user.dir");
						String port_number = ReadMobileproperties(device, "appiumport");
						String device_name = ReadMobileproperties(device, "DeviceName");
						String package_name = ReadMobileproperties(device, "apppackage");
						String activity_name = ReadMobileproperties(device, "appactivity");
						String package_name1 = ReadMobileproperties(device, "apppackage1");
						String activity_name1 = ReadMobileproperties(device, "appactivity1");
						String version = ReadMobileproperties(device, "version");
						String bsport = ReadMobileproperties(device, "bootstrapport");
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
						//----------------------	Voice Call	 --------------------------//
						
						if(Test_Scenario.equals("LIVE USAGE VOICE")) {
							starter(port_number);
							String package_voice = ReadMobileproperties(Test_Scenario, "apppackage");
							String activity_voice = ReadMobileproperties(Test_Scenario, "appactivity");
							DesiredCapabilities capabilities = new DesiredCapabilities();
							capabilities.setCapability("deviceName", device);
							capabilities.setCapability("platformVersion", version);
							capabilities.setCapability("platformName", "ANDROID");
							capabilities.setCapability("bootstrapPort", bsport); 
							capabilities.setCapability("appPackage", package_voice);
							capabilities.setCapability("appActivity", activity_voice);
							curtcid = Test_Case_ID+"--"+Test_Scenario+"_"+Test_Scenario;
							startTestCase(curtcid);
							ExtentTest test = extent.createTest(Test_Case_ID+": <br>"+Test_Scenario+"<br>"+Test_Scenario);
							
							int secs = Integer.parseInt(CALL_DURATION);
							//APIHandler.API(curtcid, trfold, "Before_Execution", MSISDN);
							dr.set(new AndroidDriver(new URL("http://127.0.0.1:" +  ReadMobileproperties(device, "appiumport") + "/wd/hub"), capabilities));
							Thread.sleep(3000);
							dr.get().findElement(By.id("com.samsung.android.dialer:id/zero")).click();
							 //dr.get().findElement(By.id("com.samsung.android.dialer:id/digits")).clear();
							 dr.get().findElement(By.id("com.samsung.android.dialer:id/digits")).sendKeys(Call_To);
							 takeScreenShot("Dialed Number");
							 dr.get().findElement(By.id("com.samsung.android.dialer:id/dialButtonImage")).click();
							
							
							Runtime run = Runtime.getRuntime();
							String execu = "adb -s "+device_name+" shell am start -a android.intent.action.CALL -d tel:"+Call_To;
							System.out.println("Execution cmmand: "+execu);
							run.exec(execu);
							Thread.sleep(secs*1000);
							takeScreenShot("Call process");
							try {
							dr.get().findElement(By.id("com.samsung.android.incallui:id/disconnect_button")).click();
							}
							catch (Exception e) {
								//e.printStackTrace();
								System.out.println("User didn't pick the call");
							 }
							//com.samsung.android.incallui:id/disconnect_button
							String result = dr.get().stopRecordingScreen();
							
							//APIHandler.API(curtcid, trfold, "After_Execution", MSISDN);
							//-------------------------- CDR Conversion -------------------------------------------//
							
							//Asnconvertor.nodeValidation(Test_Scenario, MSISDN);
							
					//-------------------------- Report ----------------------------------------------//
							//String[] convertor = Asnconvertor.Result(trfold, MSISDN, "", Test_Scenario, Test_Case_ID, curtcid, "", Test_Scenario_I, Test_Case, "", "", "", Call_To, "", "", "", "", "", ExecutionStarttime, CALL_DURATION, "" );
							
							test.pass("</table><table><tr><th style= 'min-width: 168px'><b>MSISDN</b></th>"
									+"<th style= 'min-width: 168px'><b>Test Scenario </b></th>"
									+"<th style= 'min-width: 168px'><b>Test Case </b></th>"
									+"<th style= 'min-width: 168px'><b>Called To </b></th>"
									+"<th style= 'min-width: 168px'><b>Call Duration </b></th>"
									+"<th style= 'min-width: 168px'><b> ScreenShot</b></th></tr>" + 
									
									//Device Result
									"<tr><td style= 'min-width: 168px'>"+MSISDN+"</td><td style= 'min-width: 168px'>"+Test_Scenario+"</td><td style= 'min-width: 168px'>"+Test_Case+"</td><td style= 'min-width: 168px'>"+Call_To+"</td><td style= 'min-width: 168px'>"+ CALL_DURATION +"</td><td style= 'min-width: 168px'>"+ "<a href='"+curtcid+"/ScreenShots.html' target='_blank'>Deviced_Execution_ScreenShots</a></td></tr></table><br>");
							
							//CIS API
//							test.pass("<br><br><b>CS Get Account Details Response:</b>" 
//							+ "<br><b>Response before execution XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "\\CS_API_VALIDATION\\Before_Execution\\Response\\response.xml'>Click to View the Response</a>"
//							+ "<br><b>Response After execution XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "\\CS_API_VALIDATION\\After_Execution\\Response\\response.xml'>Click to View the Response</a><br>");
						
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

							extent.flush();
							endTestCase(curtcid);
						}
						
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
//						Recordset rsr = co.execut,eQuery(strQuery);
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

							else if (Test_Scenario.contains("CS_API_")){
								curtcid = inputs.getField("Test_Case_ID")+"--"+inputs.getField("Test_Scenario")+"_"+inputs.getField("Test_Case");
								startTestCase(curtcid);
								ExtentTest test = extent.createTest(inputs.getField("Test_Case_ID")+": <br>"+inputs.getField("Test_Scenario"));
								String[] Result = APIparam.APIcontrol(Test_Scenario, ExecutionStarttime, inputs.getField("Test_Case_ID"));
								
								test.pass("&nbsp<b><a style = 'color:hotpink' target = '_blank' href = '" + Result[0]
										+ "'>Click to View the " + Result[1] + " Response file</a></b><br>" + Result[2] + "</table>");
								extent.flush();

									}
							
							else if (Test_Scenario.contains("Product_Subscription")) {
								curtcid = inputs.getField("Test_Case_ID")+"--"+Test_Case+"--"+inputs.getField("Test_Scenario")+"_"+inputs.getField("Test_Case");
								startTestCase(curtcid);
								String state = "Pass";
								String sta = "null";
								String[] report = new String[20];
								String[] report1 = new String[20];
								ExtentTest test = extent.createTest(inputs.getField("Test_Case_ID")+": <br>"+inputs.getField("Test_Case"));
								try {
								String [] Result = APIparam.CIS_API(Test_Scenario, ExecutionStarttime, inputs.getField("Test_Case_ID"));
								String val[]  = new String[10];
								String param[] = new String[10];
								for (int Iterator = 1; Iterator <= 150; Iterator++) {
									if (inputs.getField("Parameter" + Iterator).isEmpty() == false) {
										param[Iterator] = inputs.getField("Parameter" + Iterator);
										val[Iterator] = inputs.getField("Value" + Iterator);
										String Actual = APIparam.WebService3(Result[0], param[Iterator]);
										if(Actual.equalsIgnoreCase(val[Iterator])) {
											sta = "Pass";
											info("Parameter : " + param[Iterator]+ "is validated");
											report1[Iterator] = "<li>For Parameter: <b>"+param[Iterator]+"</b> actual value is <b>: " + Actual+ "</b> and the expected Value is <b>"+val[Iterator]+"</b></li>";
										}
										else {
											state = "Failed";
											report[Iterator] = "<li>For Parameter: <b>"+param[Iterator]+"</b> actual value is <b>: " + Actual+ "</b> and the expected Value is <b>"+val[Iterator]+"</b></li>";
										}
									} else
										break;
								}
								
								info("Status -----> "+state);
								//System.out.println("Service Tab---->"+tab);
								String reports = Arrays.toString(report).replace(", null", "").replace("null", "").replace("null,", "").replace("[", "").replace("]", "").replace(",", "");
								String reports1 = Arrays.toString(report1).replace(", null", "").replace("null", "").replace("null,", "").replace("[", "").replace("]", "").replace(",", "");

								
								test.pass("&nbsp<b><a style = 'color:hotpink' target = '_blank' href = '" + Result[0]
										+ "'>Click to View the " + Result[1] + " Response file</a></b><br>");
								if(sta.equals("Pass")) {
									test.pass("<b><u>Validation passed items:</u></b><br><ol>"+reports1);
								}
								if(state.equals("Failed")) {
									test.fail("<b><u>Failed due to following mismatch items:</u></b><br><ol>"+reports+ "</table>");
								}

								extent.flush();
								reports = null;
								reports1 = null;
								}
								catch (Exception e) { // Thread.sleep(100); }
									System.out.println("Process Failed!!!");	
								}
							}
							
							else {
						curtcid = inputs.getField("Test_Case_ID")+"--"+Test_Case+"--"+inputs.getField("Test_Scenario")+"_"+inputs.getField("Test_Case");
							startTestCase(curtcid);
							ExtentTest test = extent.createTest(inputs.getField("Test_Case_ID")+": <br>"+MSISDN+" : "+inputs.getField("Test_Case"));
							String[] Result = APIparam.APIcontrol(Test_Scenario, ExecutionStarttime, inputs.getField("Test_Case_ID"));
							
					//Product ID validation
							
							String[] ProductofferID1 = ProductofferID.trim().split(",");
							String[] offerState1 = offerState.split(",");
							String[] ExpireDate1 = ExpireDate.split(",");
							String[] attributeName1 = attributeName.split(",");
							String[] attributeValue1 = attributeValue.split(",");
							
							String[] pamClassID1 = pamClassID.split(",");
							String[] pamServiceID1 = pamServiceID.split(",");
							String[] pamServicePriority1 = pamServicePriority.split(",");
							String[] scheduleID1 = scheduleID.split(",");
							String[] lastEvaluationDate1 = lastEvaluationDate.split(",");
							String[] currentPamPeriod1 = currentPamPeriod.split(",");
							
							String[] dedicatedAccountID1 = dedicatedAccountID.split(",");
							String[] dedicatedAccountActiveValue11 = dedicatedAccountActiveValue1.split(",");
							String[] dedicatedAccountUnitType1 = dedicatedAccountUnitType.split(",");
							String[] dedicatedAccountValue11 = dedicatedAccountValue1.split(",");
							String[] expiryDate1 = expiryDate.split(",");
							String[] startDate1 = startDate.split(",");
						
							String[] report = new String[100000];
							String state = "Pass";
							String[] report1 = new String[100000];
							String state1 = "null";
																					
							for (g = 0; g< ProductofferID1.length; g++) {
								for (int w=0; w<pamClassID1.length;w++) {
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
								info("For Product ID : " + ProductofferID1[g]);
								info("Status is : " + offerState1[g]);
								info("ExpireDate is : " + ExpireDate1[g]);
								info("Attribute Name is : " + attributeName1[g]);
								System.out.println("Enter the Webservice loop--------------> "+ProductofferID1[g]);
								info("Attribute Value is : " + attributeValue1[g]);
								String ResultAPI[] = APIparam.WebService2(Result[0], ProductofferID1[g].trim(), attributeName1[g].trim(), pamClassID1[w].trim(), "");
								//String ResultAP = ResultAPI[0];
								String ResultAP2 = ResultAPI[2];
								String ResultAP5 = ResultAPI[5];
								String ResultAP7 = ResultAPI[7];
								System.out.println("ResultAP2---------> "+ResultAP2);
								System.out.println("ResultAP5---------> "+ResultAP5);
								try {
									if(ResultAP2.equals("Failed")) {
										if(!table_type.contains("delete_offer")){
										state= "Failed";
										report[98+g] = "<li>Given Offer ID: <b>"+ProductofferID1[g]+"</b> itself is not available</li>";
									}}
									
								}
									catch (Exception e) { // Thread.sleep(100); }
										
									}
								try {
									if(ResultAP5.equals("Failed")) {
										if(!attributeName1[g].equals("")) {
										state= "Failed";
										report[97+g] = "<li>Given Attribute Name <b>"+attributeName1[g]+"</b> itself is not available</li>";
									}
									}
									}
									catch (Exception e) { // Thread.sleep(100); }
										
									}
								System.out.println("ResultAP7---------> "+ResultAP7);
									try {
									if(ResultAP7.equals("Failed")) {
										state= "Failed";
										report[95+g] = "<li>Given PAM: <b>"+pamClassID1[w]+ "</b> is not available.</li>";
									}
									}
									catch (Exception e) { // Thread.sleep(100); }
										
									}
								try {
								String resultap = ResultAPI[1].replace(", null", "").replace("][", ", ").replace(" = ", ", ").replace("[", "").replace("]", "");
								System.out.println("Result List----> "+resultap);
								String[] Firstwrap = resultap.split(",");
								for(l=0; l<=Firstwrap.length; l++) {
									double y = Math.random()*100;
									int b = (int)y;
									System.out.println(Firstwrap[l]+"----->"+Firstwrap[l+1]);
									if(Firstwrap[l].trim().equalsIgnoreCase(Productofferv.trim())) {
										if(Firstwrap[l+1].trim().equalsIgnoreCase(ProductofferID1[g].trim())) {
											if(table_type.contains("delete_offer")) {
													state= "Failed";
													report[90+g] = "<li>Given Offer ID: <b>"+ProductofferID1[g]+"</b> is still available</li>";
											}
											info("Product ID : " + ProductofferID1[g]+ "is validated");
										}
										else {
//											state = "Failed";
//											report[0+b] = "<li>For Offer ID: <b>"+ProductofferID1[g]+"</b> ---> Product ID <b>: " + ProductofferID1[g]+ "</b> is not correct and the actual Value is "+Firstwrap[l+1]+"</li>";
										}
										
									}
									if(Firstwrap[l].trim().equalsIgnoreCase(offerStatev.trim())) {
										if(Firstwrap[l+1].trim().equalsIgnoreCase(offerState1[g].trim())) {
											state1 = "Pass";
											info("OfferState : " + offerState1[g]+ "is validated");
											report1[1+b] = "<li>For Offer ID: <b>"+ProductofferID1[g]+"</b> ---> OfferState : <b>" + offerState1[g]+ "</b> is correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";

										}
										else {
											state = "Failed";
											report[1+b] = "<li>For Offer ID: <b>"+ProductofferID1[g]+"</b> ---> OfferState : <b>" + offerState1[g]+ "</b> is not correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";
										}
									}
									if(Firstwrap[l].trim().equalsIgnoreCase(ExpireDatev.trim())) {
										if(Firstwrap[l+1].trim().equalsIgnoreCase(ExpireDate1[g].trim())) {
											info("Expire Date : " + ExpireDate1[g]+ "is validated");
											state1 = "Pass";
											report1[2+l] = "<li>For Offer ID: <b>"+ProductofferID1[g]+"</b> ---> Expire Date <b>: " + ExpireDate1[g]+ "</b> is correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";

										}
										else {
											state = "Failed";
											report[2+l] = "<li>For Offer ID: <b>"+ProductofferID1[g]+"</b> ---> Expire Date <b>: " + ExpireDate1[g]+ "</b> is not correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";
										}
									}
									if(Firstwrap[l].trim().equalsIgnoreCase(attributeNamev.trim())) {
										if(Firstwrap[l+1].trim().equalsIgnoreCase(attributeName1[g].trim())) {
											info("<br>For Offer ID: <b>"+ProductofferID1[g]+"</b> ----> Attribute Name : <b>" + attributeName1[g]+ "</b> is validated");
											state1 = "Pass";
											report1[3+b]="<li>For Offer ID: <b>"+ProductofferID1[g]+"</b> Attribute Name : <b>" + attributeName1[g]+ "</b> is correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";
										}
										else {
											state = "Failed";
											report[3+b]="<li>For Offer ID: <b>"+ProductofferID1[g]+"</b> Attribute Name : <b>" + attributeName1[g]+ "</b> is not correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";
										}
									}
									if(Firstwrap[l].trim().equalsIgnoreCase(attributeValuev.trim())) {
										if(Firstwrap[l+1].trim().equalsIgnoreCase(attributeValue1[g].trim())) {
											info("Attribute Value : " + attributeValue1[g]+ " is validated");
											state1 = "Pass";
											report1[4+b] = "<li>For Offer ID: <b>"+ProductofferID1[g]+"</b> -----> Attribute Value of <b>" + attributeName1[g]+ " = " + attributeValue1[g]+ "</b> is correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";
										}
										else {
											state = "Failed";
											report[4+b] = "<li>For Offer ID: <b>"+ProductofferID1[g]+"</b> -----> Attribute Value of <b>" + attributeName1[g]+ " = " + attributeValue1[g]+ "</b> is not correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";
										}
									}
					
									if(Firstwrap[l].trim().equalsIgnoreCase(pamClassIDv.trim())) {
										if(Firstwrap[l+1].trim().equalsIgnoreCase(pamClassID1[w].trim())) {
											info("pamClassID Value : " + pamClassID1[w]+ " is validated");
											state1 = "Pass";
											report1[5+b+w] = "<li>pamClassID Value: <b>" + pamClassID1[w]+ "</b> is correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";
										}
										else {
											state = "Failed";
											report[5+b+w] = "<li>pamClassID Value: <b>" + pamClassID1[w]+ "</b> is not correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";
										}
									}
									if(Firstwrap[l].trim().equalsIgnoreCase(pamServiceIDv.trim())) {
										if(Firstwrap[l+1].trim().equalsIgnoreCase(pamServiceID1[w].trim())) {
											info("pamServiceID Value : " + pamServiceID1[w]+ " is validated");
											state1 = "Pass";
											report1[6+b+w] = "<li>For pamClassID ID: <b>"+pamClassID1[w]+"</b> -----> pamServiceID Value: <b>" + pamServiceID1[w]+ "</b> is correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";
										}
										else {
											state = "Failed";
											report[6+b+w] = "<li>For pamClassID ID: <b>"+pamClassID1[w]+"</b> -----> pamServiceID Value: <b>" + pamServiceID1[w]+ "</b> is not correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";
										}
									}
									if(Firstwrap[l].trim().equalsIgnoreCase(pamServicePriorityv.trim())) {
										if(Firstwrap[l+1].trim().equalsIgnoreCase(pamServicePriority1[w].trim())) {
											info("pamServicePriority Value : " + pamServicePriority1[w]+ " is validated");
											state1 = "Pass";
											report1[7+b+w] = "<li>For pamClassID ID: <b>"+pamClassID1[w]+"</b> -----> pamServiceID Value: <b>" + pamServicePriority1[w]+ "</b> is correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";
										}
										else {
											state = "Failed";
											report[7+b+w] = "<li>For pamClassID ID: <b>"+pamClassID1[w]+"</b> -----> pamServiceID Value: <b>" + pamServicePriority1[w]+ "</b> is not correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";
										}
									}
									if(Firstwrap[l].trim().equalsIgnoreCase(scheduleIDv.trim())) {
										if(Firstwrap[l+1].trim().equalsIgnoreCase(scheduleID1[w].trim())) {
											info("scheduleID Value : " + scheduleID1[w]+ " is validated");
											state1 = "Pass";
											report1[8+b+w] = "<li>For pamClassID ID: <b>"+pamClassID1[w]+"</b> -----> scheduleID Value: <b>" + scheduleID1[w]+ "</b> is correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";
										}
										else {
											state = "Failed";
											report[8+b+w] = "<li>For pamClassID ID: <b>"+pamClassID1[w]+"</b> -----> scheduleID Value: <b>" + scheduleID1[w]+ "</b> is not correct and the actual Value is "+Firstwrap[l+1]+"</li>";
										}
									}
									if(Firstwrap[l].trim().equalsIgnoreCase(lastEvaluationDatev.trim())) {
										if(Firstwrap[l+1].trim().equalsIgnoreCase(lastEvaluationDate1[w].trim())) {
											info("lastEvaluationDate Value : " + lastEvaluationDate1[w]+ " is validated");
											state1 = "Pass";
											report1[9+b+w] = "<li>For pamClassID ID: <b>"+pamClassID1[w]+"</b> -----> lastEvaluationDate Value: <b>" + lastEvaluationDate1[w]+ "</b> is correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";
										}
										else {
											state = "Failed";
											report[9+b+w] = "<li>For pamClassID ID: <b>"+pamClassID1[w]+"</b> -----> lastEvaluationDate Value: <b>" + lastEvaluationDate1[w]+ "</b> is not correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";
										}
									}
									if(Firstwrap[l].trim().equalsIgnoreCase(currentPamPeriodv.trim())) {
										if(Firstwrap[l+1].trim().equalsIgnoreCase(currentPamPeriod1[w].trim())) {
											info("currentPamPeriod Value : " + currentPamPeriod1[w]+ " is validated");
											state1 = "Pass";
											report1[10+b+w] = "<li>For pamClassID ID: <b>"+pamClassID1[w]+"</b> -----> currentPamPeriod Value: <b>" + currentPamPeriod1[w]+ "</b> is correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";
										}
										else {
											state = "Failed";
											report[10+b+w] = "<li>For pamClassID ID: <b>"+pamClassID1[w]+"</b> -----> currentPamPeriod Value: <b>" + currentPamPeriod1[w]+ "</b> is not correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";
										}
									}
									}
								l = 0;
								}
								catch (Exception e) { // Thread.sleep(100); }
									System.out.println("######### Product, Attribute and PAM loop gets failed ##########");
								}
							}
								
							catch (Exception e) { // Thread.sleep(100); }
							
							}
								}
						}	
							g = 0;
							
							
							for (int i = 0; i < dedicatedAccountID1.length; i++) {
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
								String ResultAPI[] = APIparam.WebService2(Result[0], "", "", "", dedicatedAccountID1[i].trim());
								String ResultAP4 = ResultAPI[4];
								System.out.println("ResultAP4---------> "+ResultAP4);
//								try {
//									if(ResultAP4.equals("Failed")) {
//										state= "Failed";
//										report[98] = "<li>Given Dedicated Account ID <b>"+dedicatedAccountID1[i]+"</b> itself is not available</li>";
//									}
//									}
//									catch (Exception e) { // Thread.sleep(100); }
//										
//									}
								String resultap = ResultAPI[3].replace(", null", "").replace("][", ", ").replace(" = ", ", ").replace("[", "").replace("]", "");
								System.out.println("Result List----> "+resultap);
								String[] Firstwrap = resultap.split(",");
								for(int l=0; l<=Firstwrap.length; l++) {
									double y = Math.random()*100;
									int p = (int)y;
									
									System.out.println(Firstwrap[l]+"----->"+Firstwrap[l+1]);
									if(Firstwrap[l].trim().equalsIgnoreCase(dedicatedAccountIDv.trim())) {
										if(Firstwrap[l+1].trim().equalsIgnoreCase(dedicatedAccountID1[i].trim())) {
											info("Dedicated Account ID : " + dedicatedAccountID1[i]+ "is validated");
											state1 = "Pass";
											report1[11+p] = "<li>For Dedicated Account ID : <b>"+dedicatedAccountID1[i]+"</b> is correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";
										}
										else {
											state = "Failed";
											report[11+p] = "<li>For Dedicated Account ID : <b>"+dedicatedAccountID1[i]+"</b> is not correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";
										}
										
									}
									
									if(Firstwrap[l].trim().equalsIgnoreCase(dedicatedAccountActiveValue1v.trim())) {
										if(Firstwrap[l+1].trim().equalsIgnoreCase(dedicatedAccountActiveValue11[i].trim())) {
											if(table_type.contains("usage")) {
												state= "Failed";
												report[90+g] = "<li>Given DA active value: <b>"+dedicatedAccountActiveValue11[i]+"</b> is still equal and usage is not charged</li>";
										}
											info("Dedicated Account Active Value1 : " + dedicatedAccountActiveValue11[i]+ "is validated");
											state1 = "Pass";
											report1[12+p] = "<li>For Dedicated Account ID : <b>"+dedicatedAccountID1[i]+"</b> --> Dedicated Account Active Value1 <b>: " + dedicatedAccountActiveValue11[i]+ "</b> is correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";
										}
										else {
											if(!table_type.contains("usage")) {
											state = "Failed";
											report[12+p] = "<li>For Dedicated Account ID : <b>"+dedicatedAccountID1[i]+"</b> --> Dedicated Account Active Value1 <b>: " + dedicatedAccountActiveValue11[i]+ "</b> is not correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";
											}
											else {
												state1 = "Pass";
												report1[12+p] = "<li>For Dedicated Account ID : <b>"+dedicatedAccountID1[i]+"</b> --> Dedicated Account Active Value1 <b>: " + dedicatedAccountActiveValue11[i]+ "</b> and the actual Value after usage is <b>"+Firstwrap[l+1]+"</b></li>";
											}
											}
										
									}
									
									if(Firstwrap[l].trim().equalsIgnoreCase(dedicatedAccountUnitTypev.trim())) {
										if(Firstwrap[l+1].trim().equalsIgnoreCase(dedicatedAccountUnitType1[i].trim())) {
											info("Dedicated Account unit Type : " + dedicatedAccountUnitType1[i]+ "is validated");
											state1 = "Pass";
											report1[13+p] = "<li>For Dedicated Account ID : <b>"+dedicatedAccountID1[i]+"</b> --> Dedicated Account Unit Type <b>: " + dedicatedAccountUnitType1[i]+ "</b> is correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";
										}
										else {
											state = "Failed";
											report[13+p] = "<li>For Dedicated Account ID : <b>"+dedicatedAccountID1[i]+"</b> --> Dedicated Account Unit Type <b>: " + dedicatedAccountUnitType1[i]+ "</b> is not correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";
										}
										
									}
									
									if(Firstwrap[l].trim().equalsIgnoreCase(dedicatedAccountValue1v.trim())) {
										if(Firstwrap[l+1].trim().equalsIgnoreCase(dedicatedAccountValue11[i].trim())) {
											if(table_type.contains("usage")) {
												state= "Failed";
												report[90+g] = "<li>Given DA value: <b>"+ dedicatedAccountValue11[i]+"</b> is still equal and usage is not charged</li>";
										}
											info("Dedicated Account Value1 : " + dedicatedAccountValue11[i]+ "is validated");
											state1 = "Pass";
											report1[14+p] = "<li>For Dedicated Account ID : <b>"+dedicatedAccountID1[i]+"</b> --> Dedicated Account Value <b>: " + dedicatedAccountValue11[i]+ "</b> is correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";
										}
										else {
											if(!table_type.contains("usage")) {
											state = "Failed";
											report[14+p] = "<li>For Dedicated Account ID : <b>"+dedicatedAccountID1[i]+"</b> --> Dedicated Account Value <b>: " + dedicatedAccountValue11[i]+ "</b> is not correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";
											}
											else {
												report1[14+p] = "<li>For Dedicated Account ID : <b>"+dedicatedAccountID1[i]+"</b> --> Dedicated Account Value <b>: " + dedicatedAccountValue11[i]+ "</b> and the actual Value after usage is <b>"+Firstwrap[l+1]+"</b></li>";
											}
											}
										
									}
									
									if(Firstwrap[l].trim().equalsIgnoreCase(expiryDatev.trim())) {
										if(Firstwrap[l+1].trim().equalsIgnoreCase(expiryDate1[i].trim())) {
											info("Expire Date : " + expiryDate1[i]+ "is validated");
											state1 = "Pass";
											report1[15+p] = "<li>For Dedicated Account ID : <b>"+dedicatedAccountID1[i]+"</b> --> Expire Date <b>: " + expiryDate1[i]+ "</b> is correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";
										}
										else {
											state = "Failed";
											report[15+p] = "<li>For Dedicated Account ID : <b>"+dedicatedAccountID1[i]+"</b> --> Expire Date <b>: " + expiryDate1[i]+ "</b> is not correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";
										}
										
									}
									
									if(Firstwrap[l].trim().equalsIgnoreCase(startDatev.trim())) {
										if(Firstwrap[l+1].trim().equalsIgnoreCase(startDate1[i].trim())) {
											info("Start Date : " + startDate1[i]+ "is validated");
											state1 = "Pass";
											report1[16+p] = "<li>For Dedicated Account ID : <b>"+dedicatedAccountID1[i]+"</b> --> Start Date <b>: " + startDate1[i]+ "</b> is correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";
										}
										else {
											state = "Failed";
											report[16+p] = "<li>For Dedicated Account ID : <b>"+dedicatedAccountID1[i]+"</b> --> Start Date <b>: " + startDate1[i]+ "</b> is not correct and the actual Value is <b>"+Firstwrap[l+1]+"</b></li>";
										}
										
									}
									
									}
							}
								
							catch (Exception e) { // Thread.sleep(100); }
							
							}
								}
							
							info("Status -----> "+state);
							//System.out.println("Service Tab---->"+tab);
							String reports = Arrays.toString(report).replace(", null", "").replace("null", "").replace("null,", "").replace("[", "").replace("]", "").replace(",", "");
							String reports1 = Arrays.toString(report1).replace(", null", "").replace("null", "").replace("null,", "").replace("[", "").replace("]", "").replace(",", "");

							test.pass("&nbsp<b><a style = 'color:hotpink' target = '_blank' href = '" + Result[0]
									+ "'>Click to View the " + Result[1] + " Response file</a></b><br>");
							if(state1.equals("Pass")) {
								test.pass("<b><u>Validation passed items:</u></b><br><ol>"+reports1);
							}
							if(state.equals("Failed")) {
								test.fail("<b><u>Failed due to following mismatch items:</u></b><br><ol>"+reports+ "</table>");
							}

							extent.flush();
							report1 = null;
							report = null;
							
								}
							
							}
						
						else if(Test_Scenario.contains("CIS_DB"))
						{
							String validate = "null";
							String Statu = "null";
							String[] Reason = new String[100];
							String Statu1 = "null";
							String[] Reason1 = new String[100];
							String producid = "";
							String statusid = "";
							int kk = 0;
							curtcid = inputs.getField("Test_Case_ID")+"--"+Test_Case+"--"+inputs.getField("Test_Scenario")+"_"+inputs.getField("Test_Case");
							startTestCase(curtcid);
							ExtentTest test = extent.createTest(inputs.getField("Test_Case_ID")+": <br>"+MSISDN+" : "+Test_Case+" : "+inputs.getField("Test_Scenario"));
							String val[]  = new String[100000];
							String para[] = new String[100000];
							for (int Iterator = 1; Iterator <= 10000; Iterator++) {
								if (inputs.getField("Parameter" + Iterator).isEmpty() == false) {
									val[Iterator] = inputs.getField("Parameter" + Iterator);
									para[Iterator] = inputs.getField("Value" + Iterator);
									if(val[Iterator].equalsIgnoreCase("product_id")) {
										producid = para[Iterator];
										System.out.println("Product ID is ::::::::: "+producid);
									}
									if(val[Iterator].equalsIgnoreCase("status")) {
										statusid = para[Iterator];
										System.out.println("status is ::::::::: "+statusid);
									}
									
								} else
									break;
							}
							String vl = Arrays.toString(val);
							String k = vl.replace("null, ", "");
							String v = k.replace(", null", "");
							String strArray[] = v. split(",");
							String w = v.replace("[", "");
							String x = w.replace("]", "").trim();
							System.out.println("Value:-----> "+(x));
							
							if(table_type.equalsIgnoreCase("adhoc")) {
								System.out.println("Product ID is ::::::::: "+producid);
								//validate = "select * from rs_adhoc_products where product_id="+MSISDN+" order by last_action_date desc limit 1";
								validate = "select "+x+" from rs_adhoc_products where msisdn="+MSISDN+" and product_id ="+producid+" and status ="+statusid;
								//validate = "update renewal set last_renewal_date = '10-SEP-19 00:21:01' where msisdn=971520001714 and last_renewal_date = '04-SEP-19 00:21:01'";
								//validate = "select "+x+" from rs_adhoc_products where msisdn="+MSISDN+" order by last_action_date desc limit 1";
								System.out.println("Expected Query----->:"+validate);

							}
							else if(table_type.equalsIgnoreCase("renewal")) {
							 
								//validate = "select * from renewal where product_id= 925";
								validate = "select "+x+" from renewal where msisdn="+MSISDN+" and product_id ="+producid;
								//validate = "select "+x+" from renewal where msisdn="+MSISDN+" order by last_action_date desc limit 1";
							}
							System.out.println(validate);
							String dbURL = "jdbc:postgresql://10.95.214.136:5444/scs";
							Properties parameters = new Properties();
							parameters.put("user", "mugazmaveric1");
							parameters.put("password", "maverick");

							java.sql.Connection conn11 = DriverManager.getConnection(dbURL, parameters);
							System.out.println("Opened database successfully");
							Statement st = conn11.createStatement();
							ResultSet rs = st.executeQuery(validate);
							System.out.println("ESS:----"+rs.toString());
							int len = strArray.length;
							System.out.println("Length of string: "+len);
							String valueq = null;
			
							while (rs.next()) {
							for (int i =1; i<=len; i++) {
								String colname = val[i];
								try{
								valueq = rs.getObject(i).toString();
								if(valueq.equals(null)) {
									valueq = "Null value";
									System.out.println("Emmmptttyyy Value");
								}else {
									if(valueq.length()>19 && !(para[i].length()>19)) {
										//para[i] = para[i].substring(0, 18);
										valueq = valueq.substring(0, 19);
										
									}
									if (valueq.equalsIgnoreCase(para[i].trim())) {
										Statu1 = "Pass";
										Reason1[i] = "<br><li> Actual value of <b>"+colname+"</b> is <b>"+valueq+"</b> and the expected value is <b>"+para[i]+"</b></li>";
										System.out.println(colname+" is equal: "+valueq);
									}
									else {
										Statu = "Fail";
										Reason[i] = "<br><li> DB validation failed as the actual value of <b>"+colname+"</b> is <b>"+valueq+"</b> and the expected value is <b>"+para[i]+"</b></li>";
										System.out.println(Arrays.toString(Reason));
									}
								}
								}
								catch (Exception e){
									Statu = "Fail";
									info("Problem with the provided pramater "+colname+" in DB, either its 'null' or empty");
									Reason[i] = "<li>Problem with the provided pramater "+colname+" in DB, either its 'null' or empty</li>";
								}
								
							}
							}							
							String Result = Asnconvertor.cis_db(table_type, MSISDN, producid, "");
							System.out.println(Result);
							String Reasons = Arrays.toString(Reason).replace(", null", "").replace("null", "").replace("null,", "").replace("[", "").replace("]", "").replace(",", "");
							String Reasons1 = Arrays.toString(Reason1).replace(", null", "").replace("null", "").replace("null,", "").replace("[", "").replace("]", "").replace(",", "");

							test.pass(Result+"</table>");
							if(Statu1.contains("Pass")) {
								test.pass("<b><u>Validation passed items:</u></b><ol>"+Reasons1);
							}
							if(Statu.contains("Fail")) {
								test.fail("<b><u>Failed due to following mismatch items:</u></b><ol>"+Reasons+"</table>");
							}
							extent.flush();
							Reason = null;
							Reason1 = null;
						}
						
						else if (Test_Scenario.contains("DB_Simulation_")){

							String validate = "null";
							String validate1 = "null";
							String Statu = "null";
							String[] Reason = new String[100];
							String Statu1 = "null";
							String[] Reason1 = new String[100];
							String status = "";
							String product_id = "";
							String producid = "915";
							String statusid = "";
							String renewal_date = "";
							String grace_date = "";
							int kk = 0;
							curtcid = inputs.getField("Test_Case_ID")+"--"+Test_Case+"--"+inputs.getField("Test_Scenario")+"_"+inputs.getField("Test_Case");
							startTestCase(curtcid);
							ExtentTest test = extent.createTest(inputs.getField("Test_Case_ID")+": <br>"+MSISDN+" : "+Test_Case+" : "+inputs.getField("Test_Scenario"));
							String val[]  = new String[100000];
							String para[] = new String[100000];
						
							
							for (int Iterator = 1; Iterator <= 10000; Iterator++) {
								if (inputs.getField("Parameter" + Iterator).isEmpty() == false) {
									val[Iterator] = inputs.getField("Parameter" + Iterator);
									para[Iterator] = inputs.getField("Value" + Iterator);
									if(val[Iterator].equalsIgnoreCase("renewal_date")) {
										renewal_date = para[Iterator];
										System.out.println("renewal_date is ::::::::: "+renewal_date);
									}
									if(val[Iterator].equalsIgnoreCase("status")) {
										status = para[Iterator];
										System.out.println("status is ::::::::: "+status);
									}
									if(val[Iterator].equalsIgnoreCase("product_id")) {
										product_id = para[Iterator];
										System.out.println("product_id is ::::::::: "+product_id);
									}
									if(val[Iterator].equalsIgnoreCase("grace_date")) {
										grace_date = para[Iterator];
										System.out.println("grace_date is ::::::::: "+grace_date);
									}
								} else
									break;
							}
							String vl = Arrays.toString(val);
							String k = vl.replace("null, ", "");
							String v = k.replace(", null", "");
							String strArray[] = v. split(",");
							String w = v.replace("[", "");
							String x = w.replace("]", "").trim();
							System.out.println("Value:-----> "+(x));
							
							DateTimeFormatter dtf = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
							LocalDateTime now = LocalDateTime.now();
							if(Test_Scenario.contains("DB_Simulation_Renewal")){
								now = now.minusMinutes(16);
							}
							else if (Test_Scenario.contains("DB_Simulation_Grace")){
								now = now.minusMinutes(31);
							}
							String conf_time = now.toString();
							conf_time = conf_time.substring(0, 19);
							conf_time = conf_time.replace("T", " ");
							System.out.println("First Date: "+conf_time);
							
							if(Test_Scenario.contains("DB_Simulation_Renewal")) {
							validate = "update renewal set renewal_date= '"+conf_time+"' where msisdn="+MSISDN+" and product_id = "+product_id+" and status ="+status;
							}
							else if(Test_Scenario.contains("DB_Simulation_Grace")) {
								validate = "update renewal set grace_date= '"+conf_time+"' where msisdn="+MSISDN+" and product_id = "+product_id+" and status ="+status;
							}
							else if(Test_Scenario.contains("DB_Simulation_Delete")) {
								validate = "delete from renewal where msisdn="+MSISDN+" and product_id = "+product_id;
							}
							//validate = "select * from renewal where msisdn = 971520001714";
								validate1 = "select "+x+" from renewal where msisdn="+MSISDN+" and product_id = "+product_id+" and status ="+status+" order by last_action_date desc limit 1";
								//validate = "select "+x+" from renewal where msisdn="+MSISDN+" order by last_action_date desc limit 1";
							
							
								System.out.println(validate);
							String dbURL = "jdbc:postgresql://10.95.214.136:5444/scs";
							Properties parameters = new Properties();
							parameters.put("user", "mugazmaveric1");
							parameters.put("password", "maverick");

							java.sql.Connection conn11 = DriverManager.getConnection(dbURL, parameters);
							System.out.println("Opened database successfully");
							Statement st = conn11.createStatement();
							try {
							ResultSet rs1 = st.executeQuery(validate);
							}catch(Exception e){
								System.out.println("No results were returned by the query.");
							}
							ResultSet rs = st.executeQuery(validate1);
							System.out.println("ESS:----"+rs.toString());
							int len = strArray.length;
							System.out.println("Length of string: "+len);
							String valueq = null;
			
							while (rs.next()) {
							for (int i =1; i<=len; i++) {
								String colname = val[i];
								try{
								valueq = rs.getObject(i).toString();
								if(valueq.equals(null)) {
									valueq = "Null value";
									System.out.println("Emmmptttyyy Value");
								}else {
									if(valueq.length()>19 && !(para[i].length()>19)) {
										//para[i] = para[i].substring(0, 18);
										valueq = valueq.substring(0, 19);
										
									}
									if (valueq.equalsIgnoreCase(para[i].trim())) {
										Statu1 = "Pass";
										Reason1[i] = "<br><li> Actual value of <b>"+colname+"</b> is <b>"+valueq+"</b> and the expected value is <b>"+para[i]+"</b></li>";
										System.out.println(colname+" is equal: "+valueq);
									}
									else {
										Statu = "Fail";
										Reason[i] = "<br><li> DB validation failed as the actual value of <b>"+colname+"</b> is <b>"+valueq+"</b> and the expected value is <b>"+para[i]+"</b></li>";
										System.out.println(Arrays.toString(Reason));
									}
								}
								}
								catch (Exception e){
									Statu = "Fail";
									info("Problem with the provided pramater "+colname+" in DB, either its 'null' or empty");
									Reason[i] = "<li>Problem with the provided pramater "+colname+" in DB, either its 'null' or empty</li>";
								}
								
							}
							}							
							String Result = Asnconvertor.cis_db(table_type, MSISDN, product_id, status);
							System.out.println(Result);
							String Reasons = Arrays.toString(Reason).replace(", null", "").replace("null", "").replace("null,", "").replace("[", "").replace("]", "").replace(",", "");
							String Reasons1 = Arrays.toString(Reason1).replace(", null", "").replace("null", "").replace("null,", "").replace("[", "").replace("]", "").replace(",", "");

							test.pass(Result+"</table>");
							if(Statu1.contains("Pass")) {
								test.pass("<b><u>Validation passed items:</u></b><ol>"+Reasons1);
							}
							if(Statu.contains("Fail")) {
								test.fail("<b><u>Failed due to following mismatch items:</u></b><ol>"+Reasons+"</table>");
							}
							extent.flush();
							Reason = null;
							Reason1 = null;
							Thread.sleep(240000);
						}
						
						else if(Test_Scenario.contains("CIS_EDR_Validation")) {
							String datetoday;
							DateTimeFormatter dtf = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
							LocalDateTime now = LocalDateTime.now();
							System.out.println("First Date: "+now.toString());
							datetoday = (now.toString()).replaceAll("T", "_").replaceAll(":", ".").substring(0, 19);
							String finaldate=datetoday.substring(0, datetoday.length()-1);
							datecis = datetoday;
							String cur_date = finaldate.substring(0, 2);
							String cur_time = finaldate.substring(11, 13);
							int cur_date1  = Integer.parseInt(cur_date);
							int cur_time1 = Integer.parseInt(cur_time);
							System.out.println("Final Date: "+datecis);
							String curtcid = inputs.getField("Test_Case_ID")+Test_Case+inputs.getField("Test_Scenario")+inputs.getField("Test_Case");
							startTestCase(curtcid);
							ExtentTest test = extent.createTest(inputs.getField("Test_Case_ID")+": <br>"+" : "+Test_Case+" : "+inputs.getField("Test_Scenario"));
							String[] Reason = new String[50];
							String[] Reason1 = new String[50];
							String val[]  = new String[50];
							String para[] = new String[50];
							String transdate = "";
							String event_type = "";
							int given = 0;
							String Statu = "null";
							String Sta = "null";
							for (int Iterator = 1; Iterator <= 150; Iterator++) {
								if (inputs.getField("Parameter" + Iterator).isEmpty() == false) {
									para[Iterator] = inputs.getField("Parameter" + Iterator);
									val[Iterator] = inputs.getField("Value" + Iterator);
									if(para[Iterator].trim().equalsIgnoreCase("transaction_time")) {
										transdate = val[Iterator];
										System.out.println("Transaction date is ::::::::: "+transdate);
									}
									if(para[Iterator].trim().equalsIgnoreCase("event_type")) {
										event_type = val[Iterator].trim();
										System.out.println("event_type is ::::::::: "+event_type);
									}
									given++;
									
								} else
									break;
							}
							String valdat = transdate.replace(" ", "_").replace("/", "-").replace(":", ".");
							String valda_date = valdat.substring(0, 2);
							String Valda_time_h = valdat.substring(11, 13);
							String valid_date = valdat.substring(0, 13);
							String valid_min = valdat.substring(14, 16);
							
							String Fildate = valid_date;
							Date date1=new SimpleDateFormat("dd-mm-yyy_HH").parse(valid_date);
							SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd_HH");  
							String check_date = formatter.format(date1);
							System.out.println("Run time hour and date: "+valda_date+"___"+Valda_time_h);
							int act_date = Integer.parseInt(valda_date);
							int act_time = Integer.parseInt(Valda_time_h);
							
							if((act_date >= cur_date1)){
								CIS_type = "different";
							}
							try {
							Asnconvertor.nodeValidation(Test_Scenario, Test_Case_ID, MSISDN, CIS_type, valid_date, valid_min, Test_Case_ID);
							}
							catch(Exception e){
								//e.printStackTrace();
								Statu="fail";
								Reason[50] = "<li>No CIS EDR found for the provided MSISDSN <b>"+MSISDN+"</b> </li>";
								
							}
							String[] convertor = Asnconvertor.Result(trfold, MSISDN, "", Test_Scenario, Test_Case_ID, curtcid, "", Test_Scenario_I, Test_Case, "", "", "", "", "", "", "", "", "", ExecutionStarttime, "", "", transdate, check_date, event_type);
							System.out.println("EDR PATH-------> "+convertor[49]);							
							BufferedReader csvReader = new BufferedReader(new FileReader(convertor[49]));
							String row;
							String csv[] = new String[10];
							int f=0;
							while ((row = csvReader.readLine()) != null) {
									f++;
							    String[] data = row.split("|");
							    csv[f] = Arrays.toString(data).replace(", ", "").replace("|", ", ").replace("[", "").replace("]", "");
							    //String cs2 = csc.replace("[", "").replace("]", "").replace("][", ", ").replace("|", ", ");
							    System.out.println(csv[f]+"----------++----------");
							    
							}
							csvReader.close();
							String param = csv[1];
							String check = "null";
							String[] paramlst =param.split(",");
							int paramlent = paramlst.length;
							System.out.println("Param Lenght is: "+paramlent);
							try {
							System.out.println("Parameters are: "+param);
							for(int z=2; z<=f; z++) {
								String valuess = csv[z];
								String[] valuelist = valuess.split(",");
								String tras = transdate.substring(0, 13);
								if(valuess.contains(tras) && valuess.contains(val[2])) {
								check="pass";
								System.out.println("values Lenght is: "+valuelist.length);
								try {
								for(int w=1; w<=given; w++) {
									String compara = para[w];
									String comval = val[w];
									for(int i=0; i<=17; i++) {										
										try {
										if(paramlst[i].trim().contains(compara.trim())) {
											if(valuelist[i].trim().contains(comval.trim())) {
												Sta = "Pass";
												info("The parameter "+paramlst[i]+" in DB is validated");
												System.out.println("Expected Value of "+paramlst[i]+" is "+valuelist[i]);
												Reason1[w+z] = "<li>The provided pramater <b>"+compara+"</b> value is <b>"+comval+"</b> and the actual value <b>"+valuelist[i]+"</b> is equal and validated</b> </li>";
											}
											else {
												Statu = "fail";
												System.out.println("XXXXXXXXXXXXXXXXX Failed Value of "+paramlst[i]+" is "+valuelist[i]+" XXXXXXXXXXXXXX");
												Reason[w+z] = "<li>Problem with the provided pramater <b>"+compara+"</b> value <b>"+comval+"</b> , where the actual value is <b>"+valuelist[i]+"</b> </li>";
											}
											break;
										}
										
									}
									catch (Exception e){
										//e.printStackTrace();
										info("Problem with the iteration count");
									}
									}
								}
								break;
								}
								catch (Exception e){
									//e.printStackTrace();
									info("Problem with the iteration count");
								}
							}	
								else {
									Statu = "Fail";
									Reason[49] = "<li>Transaction_time itself not matching</li>";
								}
								
							}
							if(check.equalsIgnoreCase("fail")) {
								Statu = "Fail";
								Reason[49] = "<li>No Data Found for the provided detail: <b>"+val[1]+"</b> and <b>"+val[2]+"</b></li>";
								}
							
							}
							catch (Exception e){
								e.printStackTrace();
								info("Problem with the iteration count");
							}
							
							String Reasons = Arrays.toString(Reason).replace(", null", "").replace("null", "").replace("null,", "").replace("[", "").replace("]", "").replace(",", "");
							String Reasons1 = Arrays.toString(Reason1).replace(", null", "").replace("null", "").replace("null,", "").replace("[", "").replace("]", "").replace(",", "");

							test.pass("<b>CIS EDR: </b><br>" + convertor[3] 
								+ "<br><b>XML Link---></b><a style = 'color:hotpink' target = '_blank' href = '" + curtcid+ "/"+convertor[5]+"/"+convertor[7] + "/Output1.csv'>Click to View the EDR</a>"+"</table><br>");
							if(Sta.equals("Pass")) {
								test.pass("<b><u>Validation passed items:</u></b><ol>"+Reasons1);
							}
							if(Statu.equalsIgnoreCase("Fail")) {
								test.fail("<b><u>Failed due to following mismatch items:</u></b><ol>"+Reasons);
							}
							extent.flush();
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
						if(Test_Scenario.equals("LIVE USAGE DATA")) {
							curtcid = inputs.getField("Test_Case_ID")+"--"+Test_Case+"--"+inputs.getField("Test_Scenario")+"_"+inputs.getField("Test_Case");
							startTestCase(curtcid);
							ExtentTest test = extent.createTest(inputs.getField("Test_Case_ID")+": <br>"+MSISDN+" : "+Test_Case+" : "+inputs.getField("Test_Scenario"));
							String package_Data = ReadMobileproperties(inputs.getField("Table"), "apppackage");
							String activity_Data = ReadMobileproperties(inputs.getField("Table"), "appactivity");
							String package_Data1 = ReadMobileproperties(inputs.getField("Table"), "apppackage1");
							String activity_Data1 = ReadMobileproperties(inputs.getField("Table"), "appactivity1");
							
																			
					//-------------- YouTube activity -------------------//
							
							DesiredCapabilities capabilities = new DesiredCapabilities();
							capabilities.setCapability("deviceName", device);
							capabilities.setCapability("platformVersion", version);
							capabilities.setCapability("platformName", "ANDROID");
							capabilities.setCapability("bootstrapPort", bsport);
							capabilities.setCapability("appPackage", package_Data);
							capabilities.setCapability("appActivity", activity_Data);
							
							DesiredCapabilities capabilities1 = new DesiredCapabilities();
							capabilities1.setCapability("deviceName", device);
							capabilities1.setCapability("platformVersion", version);
							capabilities1.setCapability("platformName", "ANDROID");
							capabilities1.setCapability("bootstrapPort", bsport);
							capabilities1.setCapability("appPackage", package_Data1);
							capabilities1.setCapability("appActivity", activity_Data1);
							
							curtcid = inputs.getField("Test_Case_ID") + "--" + inputs.getField("Test_Scenario") + "_"
									+ inputs.getField("Test_Case");
							startTestCase(curtcid);
							if (table_type.contains("DATA_REGULAR")) {
								
								//-------------	Data Turn ON --------------------------//
							try {
								dr.set(new AndroidDriver(new URL(
										"http://127.0.0.1:" + ReadMobileproperties(device, "appiumport") + "/wd/hub"),
										capabilities1));
								Thread.sleep(2000);
								dr.get().findElement(By.xpath("//android.widget.TextView[@text='Connections']")).click();
								Thread.sleep(2000);
								dr.get().findElement(By.xpath("//android.widget.TextView[@text='Data usage']")).click();
								Thread.sleep(2000);
								dr.get().findElement(By.id("android:id/switch_widget")).click();
								Thread.sleep(2000);
								takeScreenShot("Data Turned On: " + timefold);
								
//								Runtime run = Runtime.getRuntime();
//								run.exec("adb shell svc data enable");
//								Thread.sleep(2000);
								dr.set(new AndroidDriver(new URL(
										"http://127.0.0.1:" + ReadMobileproperties(device, "appiumport") + "/wd/hub"),
										capabilities));
								try {
								Thread.sleep(3000);
								dr.get().findElement(By.xpath("//android.widget.TextView[@text='Trending']")).click();
								Thread.sleep(2000);
								dr.get().findElement(By.xpath("//android.view.ViewGroup[@index='1']")).click();
								Thread.sleep(15000);
								takeScreenShot("Regular Network -- you tube");
								}
								catch (Exception e) {
									//e.printStackTrace();
									System.out.println("--------++++++---------");
								 }
							}
							catch (Exception e) {
								e.printStackTrace();
							 }						
						//-------------	Data Turn OFF --------------------------//
																					
								dr.set(new AndroidDriver(new URL(
										"http://127.0.0.1:" + ReadMobileproperties(device, "appiumport") + "/wd/hub"),
										capabilities1));
								Thread.sleep(2000);
								dr.get().findElement(By.xpath("//android.widget.TextView[@text='Connections']")).click();
								Thread.sleep(2000);
								dr.get().findElement(By.xpath("//android.widget.TextView[@text='Data usage']")).click();
								Thread.sleep(2000);
								dr.get().findElement(By.id("android:id/switch_widget")).click();
								takeScreenShot("Data Truned OFF: " + timefold);
																
//								run.exec("adb shell svc data disable");
//								Thread.sleep(2000);
//								takeScreenShot("Data Turned off: " + timefold);
								
								
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
							
							test.pass("</table><table><tr><th style= 'min-width: 168px'><b>Test Scenario </b></th>"
									+ "<th style= 'min-width: 168px'><b>Test Case </b></th>"
									+"<th style= 'min-width: 168px'><b> ScreenShot</b></th></tr>" + 
									
									//Device Result
									"<tr><td style= 'min-width: 168px'>"+Test_Scenario+"</td><td style= 'min-width: 168px'>"+Test_Case+"</td><td style= 'min-width: 168px'><a href='"+curtcid+"/ScreenShots.html' target='_blank'>Deviced_Execution_ScreenShots</a></td></tr></table><br>");
							
							//CIS API
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
							extent.flush();
							endTestCase(curtcid);
//						}
						}
							
			}}
		}
	
		catch (Exception e) {
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