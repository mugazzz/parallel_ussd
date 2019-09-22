package com.spectrum.appium.com.spectrum.appium;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class APIparam {
	public static final String Result_FLD = System.getProperty("user.dir") + "\\reports";
	public static final String Root = System.getProperty("user.dir");// .replace("\\", "/");
	public static DateFormat For = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
	public static Calendar cal = Calendar.getInstance();
	static File resfold = null;
	static String trfold = "";
	static String timefold = "";
	static String operation = "";
	public static ThreadLocal<Session> nsession = new ThreadLocal<Session>();
	public static ThreadLocal<Channel> channel = new ThreadLocal<Channel>();
	public static String ExecutionStarttime = For.format(cal.getTime()).toString();
	public static HashMap<String, String> tagmap = new HashMap<String, String>();
	public final static String Reference_Data = System.getProperty("user.dir") + "\\server\\Reference_Sheet.xlsx";
	public final static String Input_data = System.getProperty("user.dir") + "\\Input_sheet_V2.xlsx";
	private static BufferedReader br;
	public static String objectname = "";
	public static String suspendendpoint = "";
	public static String tc = "";
	public static String resfolder;

	@SuppressWarnings("unused")
	public static String[] APIcontrol(String Scenario, String ExecutionStarttime, String Test_case) {
		String[] Result = new String [50];
		try {
			createtimestampfold(ExecutionStarttime);
			System.setProperty("logfilename", trfold + "\\Logs");
			DOMConfigurator.configure("log4j.xml");
			//ExtentReports extent = new ExtentReports();
			//ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(trfold + "\\Master.html");
			//extent.attachReporter(htmlReporter);
			info("Starting execution at +:" + ExecutionStarttime);
			DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH:mm:ss");
			DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSS");
			LocalDateTime now = LocalDateTime.now();
			String originTimeStamp1 = dtf1.format(now).toString();
			String originTimeStamp = originTimeStamp1 + "+0000";
//			SimpleDateFormat formatter6=new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss+SSSS"); 
//			LocalDateTime originTimeStamp1 = formatter6.parse(originTimeStamp);
			String originTransactionID = dtf2.format(now).toString();
			System.out.println(originTimeStamp + " : " + originTransactionID);
			System.setProperty("logfilename", trfold + "\\Logs");
			DOMConfigurator.configure("log4j.xml");
			info("Starting execution at +:" + ExecutionStarttime);
			Fillo fillo = new Fillo();
			Connection conn = fillo.getConnection(Reference_Data);
			Connection conn1 = fillo.getConnection(Input_data);
			Recordset rs = null;
			String rs1 = "Select * from API_Data where TestCase_ID = '"+Test_case+"' and Request_Name = '"+Scenario+"'";
			System.out.println(rs1);
			Recordset rsi = conn1.executeQuery("Select * from API_Data where TestCase_ID = '"+Test_case+"' and Request_Name = '"+Scenario+"'");
			String cellval1 = "blank";
			String cellval2 = "blank";
			String cellval3 = "blank";
			String cellval4 = "blank";
			String cellval5 = "blank";
			String cellval6 = "blank";
			String curtcid = "";
			File Des = null;
			File Source = null;
			while (rsi.next()) {

				cellval1 = rsi.getField("TestCase_ID");
				curtcid = rsi.getField("Request_Name");
				tc = rsi.getField("TestCase_ID");
				cellval2 = "SOAP";
				cellval3 = rsi.getField("Request_Name");
				System.out.println(cellval3);
				// cellval4 = rs.getField("SoapAction");
				if(Scenario.contains("USSD_API")) {
					cellval5 = "http://10.95.215.6:8001/cisBusiness/ussd/httpService";
				}else {
				cellval5 = "http://10.95.214.166:10011/Air";
				}
				cellval6 = "Post";

				rs = conn.executeQuery("Select * from API_Param where Request_Name = '" + cellval3 + "'");
				while (rs.next()) {
					if(Scenario.contains("USSD_API")) {
					suspendendpoint = cellval5 = "http://10.95.215.6:8001/cisBusiness/ussd/httpService";
					}
					else {
						suspendendpoint = cellval5 = "http://10.95.214.166:10011/Air";
					}
					String requests = cellval3;
					startTestCase(cellval1);
//				for (int curtemplate = 0; curtemplate < 1 ; curtemplate++) {
//					operation = requests[curtemplate];
//					cellval3 = requests[curtemplate];
					info("Firing Request: " + cellval3);
					if (cellval2.equals("SOAP")) {
						Source = new File(Root + "\\API\\Request\\" + cellval3 + ".xml");
						Des = new File(Root + "\\API\\Request_Send\\" + cellval3 + ".xml");
						GenerateResponse(Source, Des);
						for (int Iterator = 1; Iterator <= 150; Iterator++) {
							if (rs.getField("Parameter" + Iterator).isEmpty() == false) {
								info(rs.getField("Parameter" + Iterator) + " : " + rs.getField("Value" + Iterator));
								String replaceval = rs.getField("Value" + Iterator);
								findandreplace(Des, "${" + rs.getField("Parameter" + Iterator) + "}$", replaceval);
								findandreplace(Des, "$current1$", originTransactionID);
								findandreplace(Des, "$current2$", originTimeStamp);
								// findandreplace(Des, "$subscriberNumber$", MSISDN);
								if (rs.getField("Parameter" + Iterator).equals("Data_Object_Name")) {
									objectname = replaceval;
								}
							} else
								break;
						}
						for (int Iterator = 1; Iterator <= 150; Iterator++) {
							if (rsi.getField("Parameter" + Iterator).isEmpty() == false) {
								info(rsi.getField("Parameter" + Iterator) + " : " + rsi.getField("Value" + Iterator));
								String replaceval = rsi.getField("Value" + Iterator);
								findandreplace(Des, "${" + rsi.getField("Parameter" + Iterator) + "}$", replaceval);
								findandreplace(Des, "$current1$", originTransactionID);
								findandreplace(Des, "$current2$", originTimeStamp);
								// findandreplace(Des, "$subscriberNumber$", MSISDN);
								if (rsi.getField("Parameter" + Iterator).equals("Data_Object_Name")) {
									objectname = replaceval;
								}
							} else
								break;
						}
					}
				}
				File file = Des;
				FileReader fr = new FileReader(file);
				br = new BufferedReader(fr);
				String line;
				String req = "";
				while ((line = br.readLine()) != null) {
					req = req + line;
				}
				if (cellval3.equals("")) {
					req = "";
				}
				System.out.println(req);
				info("request Fired:" + req);
				Response response = null;
				File respf = null;
				// String userPassword = "techmqatar:techmqatar";
				// System.out.println(userPassword);
				// String userPassword = "TechMahindra:TechMahindra";
				// String encoding = new
				// sun.misc.BASE64Encoder().encode(userPassword.getBytes());
				// String encoding = "dGVjaG1xYXRhcjp0ZWNobXFhdGFy";
				try {
					if(Scenario.contains("USSD_API")) {
						response = (Response) RestAssured.given().request().body(req)
								.headers("Content-Type", " text/xml")
								.when() // .contentType("text/xml; charset=utf-8")
								.post(cellval5).then().extract().response();
						respf = new File(Root + "\\API\\Response\\" + cellval1 + ".xml");
					}
					else {
					response = (Response) RestAssured.given().request().body(req)
							.headers("Content-Type", " text/xml", "Host ", "10.95.214.166:10011", "User-Agent",
									"UGw Server/5.0/1.0", "Authorization",
									"Basic " + "bWF2ZXJpYzpFcmljc3NvbnRlc3QxMjNA")
							.when() // .contentType("text/xml; charset=utf-8")
							.post(cellval5).then().extract().response();
					respf = new File(Root + "\\API\\Response\\" + cellval1 + ".xml");
					// respf = new File(Root + "\\API\\Response\\" + cellval1 + ".json");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (response != null) {
					info("Response Code  :" + response.getStatusCode());
					info("Response Header  :" + response.getHeaders().toString());
					info("Response   :" + response.asString());
					writeresponse(respf, response.asString());
					String path = fCreateReportFiles(Des, respf, tc, curtcid, trfold, cellval1);
					Result[0] = path;
					System.out.println(path);
					String outtable = WebService(path);
					System.out.println(outtable);
					// System.out.println(response.asString());
					String respheader = "";
					for (int i = 0; i < response.getHeaders().asList().size(); i++) {
						String Header = response.getHeaders().asList().get(i).toString();
						respheader = respheader + "<br>" + Header;
					}
					String res = "";
					String stat = "";
					String stattab = "";
					String fext = "";
					res = validateresp(respf, cellval1, response.statusCode());
					stat = res.split("##")[0];
					stattab = res.split("##")[1];
					fext = "xml";
					// for zain ksa
					String captureval = "";
					String linkv = "";
					// String ret = "";
					Result[1] = cellval3;
					Result[2] = outtable;
//					ExtentTest test = extent.createTest(cellval1 + "(" + cellval3 + ")");
//					test.pass("&nbsp<b><a style = 'color:hotpink' target = '_blank' href = '" + path
//							+ "'>Click to View the " + cellval3 + " Response file</a></b><br>" + outtable + "</table>");
//					extent.flush();
//					endTestCase(cellval1);
				}

			}

			rs.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			error(e.getMessage());
		}
		return Result;

	}
	
	
	
	public static String[] USSDcontrol(String Scenario, String ExecutionStarttime, String Test_case, String USSD, String MSISDN, String currshortcode, String env) {
		String[] Result = new String [50];
		String curtcid = Test_case+"_"+Scenario;
		try {
			createtimestampfold(ExecutionStarttime);
//			System.setProperty("logfilename", trfold+curtcid++ "\\Logs");
//			DOMConfigurator.configure("log4j.xml");
			//ExtentReports extent = new ExtentReports();
			//ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(trfold + "\\Master.html");
			//extent.attachReporter(htmlReporter);
			info("Starting execution at +:" + ExecutionStarttime);
			DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH:mm:ss");
			DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSS");
			LocalDateTime now = LocalDateTime.now();
			String originTimeStamp1 = dtf1.format(now).toString();
			String originTimeStamp = originTimeStamp1 + "+0000";
//			SimpleDateFormat formatter6=new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss+SSSS"); 
//			LocalDateTime originTimeStamp1 = formatter6.parse(originTimeStamp);
			String originTransactionID = dtf2.format(now).toString();
			System.out.println(originTimeStamp + " : " + originTransactionID);
			System.setProperty("logfilename", trfold + "\\Logs");
			DOMConfigurator.configure("log4j.xml");
			info("Starting execution at +:" + ExecutionStarttime);
			String cellval5="";
			String cellval6="";
			File Des = null;
			File Source = null;
					// cellval4 = rs.getField("SoapAction");
					cellval5 = "http://"+env+":8001/cisBusiness/ussd/httpService";
					suspendendpoint = cellval5 = "http://"+env+":8001/cisBusiness/ussd/httpService";
				cellval6 = "Post";
					
					String requests = Scenario;
					startTestCase(Test_case);
					info("Firing Request: " + requests);
						Source = new File(Root + "\\API\\Request\\" + requests + ".xml");
						Des = new File(Root + "\\API\\Request_Send\\" + requests + ".xml");
						GenerateResponse(Source, Des);
								findandreplace(Des, "$current1$", originTransactionID);
								findandreplace(Des, "$current2$", ExecutionStarttime);
								findandreplace(Des, "$USSD_CODE$", USSD);
								findandreplace(Des, "$subscriberNumber$", MSISDN);
				File file = Des;
				FileReader fr = new FileReader(file);
				br = new BufferedReader(fr);
				String line;
				String req = "";
				while ((line = br.readLine()) != null) {
					req = req + line;
				}
				if (requests.equals("")) {
					req = "";
				}
				System.out.println(req);
				info("request Fired:" + req);
				Response response = null;
				File respf = null;
				// String userPassword = "techmqatar:techmqatar";
				// System.out.println(userPassword);
				// String userPassword = "TechMahindra:TechMahindra";
				// String encoding = new
				// sun.misc.BASE64Encoder().encode(userPassword.getBytes());
				// String encoding = "dGVjaG1xYXRhcjp0ZWNobXFhdGFy";
				try {
						response = (Response) RestAssured.given().request().body(req)
								.headers("Content-Type", " text/xml")
								.when() // .contentType("text/xml; charset=utf-8")
								.post(cellval5).then().extract().response();
						respf = new File(Root + "\\API\\Response\\" + requests + ".xml");
						Result[1] = requests;
					}
				 catch (Exception e) {
					e.printStackTrace();
				}
				if (response != null) {
					info("Response Code  :" + response.getStatusCode());
					info("Response Header  :" + response.getHeaders().toString());
					info("Response   :" + response.asString());
					writeresponse(respf, response.asString());
					String path = fCreateReportFiles(Des, respf, tc, curtcid, trfold, currshortcode);
					Result[0] = path;
					System.out.println(path);
					String outtable = WebService(path);
					System.out.println(outtable);
					// System.out.println(response.asString());
					String respheader = "";
					for (int i = 0; i < response.getHeaders().asList().size(); i++) {
						String Header = response.getHeaders().asList().get(i).toString();
						respheader = respheader + "<br>" + Header;
					}
					String res = "";
					String stat = "";
					String stattab = "";
					String fext = "";
//					res = validateresp1(respf, Test_case, response.statusCode());
//					stat = res.split("##")[0];
//					stattab = res.split("##")[1];
//					fext = "xml";
					// for zain ksa
					String captureval = "";
					String linkv = "";
					// String ret = "";
					//Result[2] = outtable;
//					ExtentTest test = extent.createTest(cellval1 + "(" + cellval3 + ")");
//					test.pass("&nbsp<b><a style = 'color:hotpink' target = '_blank' href = '" + path
//							+ "'>Click to View the " + cellval3 + " Response file</a></b><br>" + outtable + "</table>");
//					extent.flush();
//					endTestCase(cellval1);
				}

		} catch (Exception e) {
			e.printStackTrace();
			error(e.getMessage());
		}
		return Result;

	}
	
	
	
	public static String[] CIS_API(String Scenario, String ExecutionStarttime, String Test_case) throws UnsupportedOperationException, IOException {
		String[] Result = new String [50];
		File respf = null;
		String curtcid = "";
		String url = "";
		try {
			createtimestampfold(ExecutionStarttime);
			System.setProperty("logfilename", trfold + "\\Logs");
			DOMConfigurator.configure("log4j.xml");
			//ExtentReports extent = new ExtentReports();
			//ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(trfold + "\\Master.html");
			//extent.attachReporter(htmlReporter);
			info("Starting execution at +:" + ExecutionStarttime);
			System.setProperty("logfilename", trfold + "\\Logs");
			DOMConfigurator.configure("log4j.xml");
			info("Starting execution at +:" + ExecutionStarttime);
			Fillo fillo = new Fillo();
			Connection conn = fillo.getConnection(Reference_Data);
			Connection conn1 = fillo.getConnection(Input_data);
			Recordset rs = null;
			Recordset rsi = conn1.executeQuery("Select * from API_Data where TestCase_ID = '"+Test_case+"' and Request_Name = '"+Scenario+"'");
			while (rsi.next()) {
			if(Scenario.equalsIgnoreCase("CIS_API_Credit_Amount"))
			{
				String MSISDN =rsi.getField("Value1");
				String INPUT = rsi.getField("Value2");
				String TO_CREDIT = rsi.getField("Value3");	
				url = "http://10.95.215.6:8001/cisBusiness/service/fulfillmentService?msisdn="+MSISDN+"&username=c39929de831bbe6b494e45dd5eb2926d&password=2cc935d0922c88fcbc5180b573040968&iname=TIBCO&input="+INPUT+"&clientTransactionId=4122867334537798&circleCode=UAE&opParam2=General%20Cash&adjustmentAction=1&amountToCredit="+TO_CREDIT+"&OpParam1=Complaint&OpParam4=30";
			}
			else if(Scenario.equalsIgnoreCase("CIS_API_Debit_Amount")){
				String MSISDN =rsi.getField("Value1");
				String INPUT = rsi.getField("Value2");
				String TO_CREDIT = rsi.getField("Value3");	
				url = "http://10.95.215.6:8001/cisBusiness/service/fulfillmentService?msisdn="+MSISDN+"&username=c39929de831bbe6b494e45dd5eb2926d&password=2cc935d0922c88fcbc5180b573040968&iname=TIBCO&input="+INPUT+"&clientTransactionId=4122867334537798&circleCode=UAE&opParam2=General%20Cash&adjustmentAction=1&amountToCredit="+"-"+TO_CREDIT+"&OpParam1=Complaint&OpParam4=30";
			}
			else if (Scenario.equalsIgnoreCase("CIS_API_Product_Subscription")) {
				String MSISDN =rsi.getField("Value1");
				String INPUT = rsi.getField("Value2");
				url = "http://10.95.215.6:8001/cisBusiness/service/fulfillmentService?msisdn="+MSISDN+"&username=c39929de831bbe6b494e45dd5eb2926d&password=2cc935d0922c88fcbc5180b573040968&iname=TIBCO&input="+INPUT+"&clientTransactionId=1000040996&circleCode=UAE&paySrc=&sendsms=&skipcharging=&productcost=";
			}
			else if (Scenario.equalsIgnoreCase("CIS_API_View_History")){
				String MSISDN =rsi.getField("Value1");
				url = "http://10.95.215.6:8001/cisBusiness/service/fulfillmentService?input=VIEW_SUBS_HISTORY&msisdn="+MSISDN+"&username=c39929de831bbe6b494e45dd5eb2926d&password=2cc935d0922c88fcbc5180b573040968&circlecode=UAE&iname=TIBCO&clientTransactionID=12345";
			}
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);

		// add header
		post.setHeader("User-Agent", "Mozilla/5.0");

//		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
//		urlParameters.add(new BasicNameValuePair("msisdn", "971520001714"));
//		urlParameters.add(new BasicNameValuePair("username", "c39929de831bbe6b494e45dd5eb2926d"));
//		urlParameters.add(new BasicNameValuePair("password", "2cc935d0922c88fcbc5180b573040968"));
//		urlParameters.add(new BasicNameValuePair("iname", "TIBCO"));
//		urlParameters.add(new BasicNameValuePair("input", "NACT_DebitCredit"));
//		urlParameters.add(new BasicNameValuePair("clientTransactionId", "4122867334537798"));
//		urlParameters.add(new BasicNameValuePair("circleCode", "UAE"));
//		urlParameters.add(new BasicNameValuePair("opParam2", "General%20Cash"));
//		urlParameters.add(new BasicNameValuePair("adjustmentAction", "1"));
//		urlParameters.add(new BasicNameValuePair("amountToCredit", "100"));
//		urlParameters.add(new BasicNameValuePair("OpParam1", "Complaint"));
//		urlParameters.add(new BasicNameValuePair("OpParam4", "30"));
//
//		post.setEntity(new UrlEncodedFormEntity(urlParameters, "UTF-8"));
		
		
		File Des = null;
		File Source = null;

		HttpResponse response = client.execute(post);
		System.out.println("Response Code : " 
	                + response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(
		        new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		String Res = result.toString();
		try
		{
			File tresfold = new File(trfold + "/CIS_API_Response/");
			if ((!tresfold.exists()))
				tresfold.mkdir();
		String thisFile = new String( trfold + "/CIS_API_Response/"+Scenario +".xml");
		Result[0] = trfold+"/CIS_API_Response/"+Scenario +".xml";
		Result[1] = Scenario;
		OutputStreamWriter oos = new OutputStreamWriter (new FileOutputStream(thisFile));
		oos.write (Res);
		oos.close();
		oos = null;
		thisFile=null;
		}
		catch (IOException ioe)
		{
		System.out.println("IO error: " + ioe);
		}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			error(e.getMessage());
		}
		return Result;
	}

	public static String gettagvalue(File resp, String tag, int index) {
		try {
			String retvall = "";
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setNamespaceAware(true);
			DocumentBuilder dBuilder;

			dBuilder = dbFactory.newDocumentBuilder();

			Document doc = dBuilder.parse(resp);
			doc.getDocumentElement().normalize();
			NodeList nl = doc.getDocumentElement().getElementsByTagName(tag);// customerOrderID
			if (nl.getLength() == 0) {
				nl = doc.getDocumentElement().getElementsByTagNameNS("*", tag);
			}
			if (nl.getLength() != 0) {
				retvall = nl.item(index).getTextContent();
			} else {
				retvall = "";
			}
			return retvall;
		} catch (Exception e) {

			e.printStackTrace();
			return "";
		}

	}
	
	public static void createtimestampfold(String ExecutionStarttime) {
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
			e.getMessage();
		}
	}

	public static String validaterespJSON(Response response, String TC, int statcode) {
		try {
			// File inputFile = resp;
			Fillo fillo = new Fillo();
			Connection conn = fillo.getConnection(Reference_Data);
			// String jsonSrouce =resp.toString();
			// JsonSlurper jsonParser = new JsonSlurper();

			Recordset rs = conn.executeQuery("Select * from API where TestCase_ID = '" + TC + "'");
			String status = "Pass";
			String retval = "<Table><tr><td>Parameter</td><td>Expected Value</td><td>Actual Value</td></tr>";
			while (rs.next()) {
				int expstatuscode = 200;
				retval = retval + "<tr><td>Status Code</td><td>" + expstatuscode + "</td><td>" + statcode
						+ "</td></tr>";
				if (expstatuscode != statcode) {
					status = "Fail";
				}
				for (int Iterator = 1; Iterator <= 40; Iterator++) {
					if (rs.getField("Parameter" + Iterator).isEmpty() == false) {
						String param = rs.getField("Parameter" + Iterator).toString();

						String expectedval = rs.getField("Value" + Iterator);
						String actval = response.jsonPath().get(param);
						retval = retval + "<tr><td>" + param + "</td><td>" + expectedval + "</td><td>" + actval
								+ "</td></tr>";
						if (status.equals("Pass")) {
							if (expectedval.equals(actval)) {
								status = "Pass";
							} else {
								status = "Fail";
							}

						}
					} else
						break;
				}
			}

			retval = retval + "</table>";
			String sendval = "";
			if (status.equals("Pass")) {
				sendval = "Pass##" + retval;
			} else {
				sendval = "Fail##" + retval;
			}
			return sendval;
		} catch (Exception e) {
			e.printStackTrace();
			error(e.getMessage());
			return "";
		}

	}
	
	public static String validateresp1(File resp, String TC, int statcode) {
		try {
			File inputFile = resp;
			Fillo fillo = new Fillo();
			Connection conn1 = fillo.getConnection(Input_data);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setNamespaceAware(true);
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			Recordset rsi = conn1.executeQuery("Select * from Execution_Sheet where TestCase_ID = '" + TC + "'");
			String status = "Pass";

			String retval = "<Table><tr><td>Parameter</td><td>Expected Value</td><td>Actual Value</td></tr>";
			while (rsi.next()) {
				int expstatuscode = 200;
				// if (rs.getField("Expected_Status_Code") == "") {
				// expstatuscode = 200;
				// }
				retval = retval + "<tr><td>Status Code</td><td>" + expstatuscode + "</td><td>" + statcode
						+ "</td></tr>";
				if (expstatuscode != statcode) {
					status = "Fail";
				}
				for (int Iterator = 1; Iterator <= 40; Iterator++) {
					if (rsi.getField("Parameter" + Iterator).isEmpty() == false) {
						String param = rsi.getField("Parameter" + Iterator).toString();
						String expectedval = rsi.getField("Value" + Iterator);
						// NodeList nlis = doc.getDocumentElement().getElementsByTagName(param);
						NodeList nlis = doc.getDocumentElement().getElementsByTagName(param);
						String actval = "";
						if (nlis.getLength() == 0) {
							nlis = doc.getDocumentElement().getElementsByTagNameNS("*", param);
						}
						if (nlis.getLength() != 0) {
							actval = nlis.item(0).getTextContent();
						} else {
							actval = "";
						}

						retval = retval + "<tr><td>" + param + "</td><td>" + expectedval + "</td><td>" + actval
								+ "</td></tr>";
						if (status.equals("Pass")) {
							if (expectedval.equals(actval)) {
								status = "Pass";
							} else {
								status = "Fail";
							}

						}
					} else
						break;
				}

			}
			retval = retval + "</table>";
			String sendval = "";
			if (status.equals("Pass")) {
				sendval = "Pass##" + retval;
			} else {
				sendval = "Fail##" + retval;
			}
			return sendval;
		} catch (Exception e) {
			e.printStackTrace();
			error(e.getMessage());
			return "";
		}
	}

	public static String validateresp(File resp, String TC, int statcode) {
		try {
			File inputFile = resp;
			Fillo fillo = new Fillo();
			Connection conn1 = fillo.getConnection(Input_data);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setNamespaceAware(true);
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			Recordset rsi = conn1.executeQuery("Select * from API_Data where TestCase_ID = '" + TC + "'");
			String status = "Pass";

			String retval = "<Table><tr><td>Parameter</td><td>Expected Value</td><td>Actual Value</td></tr>";
			while (rsi.next()) {
				int expstatuscode = 200;
				// if (rs.getField("Expected_Status_Code") == "") {
				// expstatuscode = 200;
				// }
				retval = retval + "<tr><td>Status Code</td><td>" + expstatuscode + "</td><td>" + statcode
						+ "</td></tr>";
				if (expstatuscode != statcode) {
					status = "Fail";
				}
				for (int Iterator = 1; Iterator <= 40; Iterator++) {
					if (rsi.getField("Parameter" + Iterator).isEmpty() == false) {
						String param = rsi.getField("Parameter" + Iterator).toString();
						String expectedval = rsi.getField("Value" + Iterator);
						// NodeList nlis = doc.getDocumentElement().getElementsByTagName(param);
						NodeList nlis = doc.getDocumentElement().getElementsByTagName(param);
						String actval = "";
						if (nlis.getLength() == 0) {
							nlis = doc.getDocumentElement().getElementsByTagNameNS("*", param);
						}
						if (nlis.getLength() != 0) {
							actval = nlis.item(0).getTextContent();
						} else {
							actval = "";
						}

						retval = retval + "<tr><td>" + param + "</td><td>" + expectedval + "</td><td>" + actval
								+ "</td></tr>";
						if (status.equals("Pass")) {
							if (expectedval.equals(actval)) {
								status = "Pass";
							} else {
								status = "Fail";
							}

						}
					} else
						break;
				}

			}
			retval = retval + "</table>";
			String sendval = "";
			if (status.equals("Pass")) {
				sendval = "Pass##" + retval;
			} else {
				sendval = "Fail##" + retval;
			}
			return sendval;
		} catch (Exception e) {
			e.printStackTrace();
			error(e.getMessage());
			return "";
		}
	}

	public static String capvals(File resp, String TC, String reqname, String tag) {
		String actval = "";
		try {
			File inputFile = resp;
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setNamespaceAware(true);
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			String param1 = tag;
			String[] parmsplt = param1.split("\\[");
			String param = parmsplt[0];
			int inde = 0;
			if (parmsplt.length == 1) {
				inde = 0;
			} else {
				String[] paramsplit2 = parmsplt[1].split("\\]");
				inde = Integer.parseInt(paramsplit2[0]);
			}
			// NodeList nlis = doc.getDocumentElement().getElementsByTagName(param);
			NodeList nlis = doc.getDocumentElement().getElementsByTagName(param);

			if (nlis.getLength() > inde) {
				nlis = doc.getDocumentElement().getElementsByTagNameNS("*", param);
			}
			if (nlis.getLength() > inde) {
				actval = nlis.item(inde).getTextContent();
			} else {
				actval = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return actval;
	}

	@SuppressWarnings("unused")
	public static String capturevalues(File resp, String TC, String reqname) {
		String sendval = "";
		try {
			File inputFile = resp;
			Fillo fillo = new Fillo();
			Connection conn1 = fillo.getConnection(Input_data);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setNamespaceAware(true);
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			Recordset rsi = conn1.executeQuery("Select * from API_Data where TestCase_ID = '" + TC + "'");
			String retval = "";
			String[] capval = { "suspendstate[0]", "suspendstate[1]", "suspendstate[2]" };
			int capvalcount = 1;
			while (rsi.next()) {

				for (int Iterator = 0; Iterator < capval.length; Iterator++) {

					// if (rs.getField("Capture_Value" + Iterator).)
					{
						// if (rs.getFieldNames().get(Iterator).toString().contains("Capture_Value"))
						{
							String param1 = capval[Iterator];
							capvalcount++;
							String[] parmsplt = param1.split("\\[");
							String param = parmsplt[0];
							int inde = 0;
							if (parmsplt.length == 1) {
								inde = 0;
							} else {
								String[] paramsplit2 = parmsplt[1].split("\\]");
								inde = Integer.parseInt(paramsplit2[0]);
							}
							// NodeList nlis = doc.getDocumentElement().getElementsByTagName(param);
							NodeList nlis = doc.getDocumentElement().getElementsByTagName(param);
							String actval = "";
							if (nlis.getLength() == 0) {
								nlis = doc.getDocumentElement().getElementsByTagNameNS("*", param);
							}
							if (nlis.getLength() != 0) {
								actval = nlis.item(inde).getTextContent();
							} else {
								actval = "";
							}
							if (param.equals("Id")) {
							}
							retval = retval + "," + actval;

						} // else
						{
							// break;
						}
					} // else
						// break;
				}

			}

			sendval = retval;
			return sendval;
		} catch (Exception e) {
			e.printStackTrace();
			// error(e.getMessage());
			return sendval;
		}
	}

	@SuppressWarnings("resource")
	public static void GenerateResponse(File sourceFile, File destFile) throws IOException {
		if (!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel origin = null;
		FileChannel destination = null;
		try {
			origin = new FileInputStream(sourceFile).getChannel();
			destination = new FileOutputStream(destFile).getChannel();

			long count = 0;
			long size = origin.size();
			while ((count += destination.transferFrom(origin, count, size - count)) < size)
				;
		} finally {
			if (origin != null) {
				origin.close();
			}
			if (destination != null) {
				destination.close();
			}
		}
	}

	public static void writeresponse(File destfile, String response) throws IOException {
		if (!destfile.exists()) {
			destfile.createNewFile();
		}
		FileWriter writer = new FileWriter(destfile, false);
		writer.write(response);

		writer.close();
	}

	public static void findandreplace(File file, String Find, String Replace) {
		try {
			// File file = new File("C://users//James//Desktop//newcommand.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "";
			while ((line = reader.readLine()) != null) {
				oldtext += line + "\r\n";
			}
			reader.close();
			if (!Replace.equals("")) {
				if (Replace.charAt(0) == '<') {
					String FindBuffer = Replace.replace("<", "");
					FindBuffer = FindBuffer.replace(">", "");
					String TCID = FindBuffer.split("=")[0];
					String Tag = FindBuffer.split("=")[1];
					String[] parmsplt = Tag.split("\\[");
					String param = parmsplt[0];
					int inde = 0;
					if (parmsplt.length == 1) {
						inde = 0;
					} else {
						String[] paramsplit2 = parmsplt[1].split("\\]");
						inde = Integer.parseInt(paramsplit2[0]);
					}
					File respf = new File(Root + "\\API\\Response\\" + TCID + ".xml");
					// DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					// DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					// dbFactory.setNamespaceAware(true);
					// Document doc = dBuilder.parse(respf);
					// doc.getDocumentElement().normalize();

					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					dbFactory.setNamespaceAware(true);
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(respf);
					doc.getDocumentElement().normalize();
					NodeList nlis = doc.getDocumentElement().getElementsByTagName(param);
					String actval = "";
					if (nlis.getLength() == 0) {
						nlis = doc.getDocumentElement().getElementsByTagNameNS("*", param);
					}
					actval = nlis.item(inde).getTextContent();
					Replace = actval;
				}
			}
			String replacedtext = oldtext.replace(Find, Replace);

			FileWriter writer = new FileWriter(file, false);
			writer.write(replacedtext);

			writer.close();

		} catch (Exception ioe) {
			ioe.printStackTrace();
			error(ioe.getMessage());
		}
	}

	public static void createtimestampfold() {
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

	public static String fCreateReportFiles(File request, File response, String tc, String curtcid, String trfold, String USSD) {
		// File ResultRequest = null, ResultResponse = null;
		try {
			String USSD_ = USSD.replace("*", "_");
			File ReqTypFold = new File(trfold + "/" + tc + "__" + curtcid);
			if ((!ReqTypFold.exists()))
				ReqTypFold.mkdir();
			System.out.println("Actual Path given: " + ReqTypFold);

			File TCReqFold = new File(ReqTypFold + "/Request");
			if ((!TCReqFold.exists()))
				TCReqFold.mkdir();
			System.out.println("Actual Path given: " + TCReqFold);
			File TCResFold = new File(ReqTypFold + "/Response");
			if ((!TCResFold.exists()))
				TCResFold.mkdir();
			File ResultRequest = new File(TCReqFold + "/" +USSD_+ "_request.xml");
			File ResultResponse = new File(TCResFold + "/"+USSD_ + "_response.xml");
			GenerateResponse(request, ResultRequest);
			GenerateResponse(response, ResultResponse);
			resfolder = ResultResponse.toString();

		} catch (Exception e) {
			e.printStackTrace();
			error(e.getMessage());
		}
		return resfolder;
	}

	private static Logger Log = Logger.getLogger(APIparam.class.getName());//

	// This is to print log for the beginning of the test case, as we usually run so
	// many test cases as a test suite

	public static void startTestCase(String sTestCaseName) {

		Log.info("****************************************************************************************");

		Log.info("****************************************************************************************");

		Log.info("$$$$$$$$$$$$$$$$$$$$$ " + sTestCaseName + " $$$$$$$$$$$$$$$$$$$$$$$$$");

		Log.info("****************************************************************************************");

		Log.info("****************************************************************************************");

	}

	// This is to print log for the ending of the test case

	public static void endTestCase(String sTestCaseName) {

		Log.info("XXXXXXXXXXXXXXXXXXXXXXX             " + "-E---N---D-" + "             XXXXXXXXXXXXXXXXXXXXXX");

	}

	// Need to create these methods, so that they can be called

	public static void info(String message) {

		Log.info(message);

	}

	public void warn(String message) {

		Log.warn(message);

	}

	public static void error(String message) {

		Log.error(message);

	}

	public void fatal(String message) {

		Log.fatal(message);

	}

	public void debug(String message) {

		Log.debug(message);

	}

	public static void LoginSSH(String host, String username, String password) {
		try {
			// Properties config = new Properties();
			// config.put("StrictHostKeyChecking", "no");
			JSch jsch = new JSch();
			nsession.set(jsch.getSession(username, host, 22));
			nsession.get().setPassword(password);
			nsession.get().setConfig("StrictHostKeyChecking", "no");
			nsession.get().setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
			nsession.get().connect();
			channel.set(nsession.get().openChannel("shell"));
			System.out.println("Connected to: " + host);
			channel.get().connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	public static String Executecmd(String command, String Exitval) {
		try {
			String str = "";
			String str1;
			OutputStream ops = channel.get().getOutputStream();
			PrintStream ps = new PrintStream(ops, true);
			ps.println(command);
			InputStream in = channel.get().getInputStream();
			byte[] bt = new byte[1024];
			while (true) {
				int i = in.read(bt, 0, 1024);
				if (i < 0)
					break;
				str1 = new String(bt, 0, i);
				if (str1 == null) {
					break;
				}
				str = str + str1;
				// System.out.println(str);
				// System.out.print(str);
				if (str.contains(Exitval)) {
					break;
				}
				if (channel.get().isClosed()) {
					System.out.println("exit-status: " + channel.get().getExitStatus());
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {
				}
			}
			// channel.disconnect();
			// System.out.println("DONE");
			return str;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

//	public static String readProperties(String propkey) {
//		Properties prop = new Properties();
//		String retval = "";
//		InputStream input = null;
//		try {
//			input = new FileInputStream("environment.properties");
//			prop.load(input);
//			retval = prop.getProperty(propkey);
//
//		} catch (Exception e) {
//			
//			e.printStackTrace();
//		}

//		return retval;
//	}

	public static String readnumbers(String propkey) {
		Properties prop = new Properties();
		String retval = "";
		InputStream input = null;
		try {
			input = new FileInputStream("MSISDN_Save.properties");
			prop.load(input);
			retval = prop.getProperty(propkey);

		} catch (Exception e) {

			e.printStackTrace();
		}

		return retval;
	}

	public static void StoreValue(String Name, String Value) {
		Properties prop = new Properties();

		File f = new File("MSISDN_Save.properties");
		try {

			InputStream in = new FileInputStream(f);
			prop.load(in);
			in.close();
			OutputStream output = new FileOutputStream(f);
			prop.setProperty(Name, Value);
			prop.store(output, "save");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String WebService(String XMLResponse_Path) throws Exception {

		String Nodetag = "member";
		String sub = null;
		String value = null;
		String nametag = "name";
		String valuetag = "value";
		String tbl = "<table><tr><th>Parameter</th><th>Value</th></tr>";
		String sot = null;
		String values;
		try {

			DocumentBuilderFactory dbFactory1 = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder1 = dbFactory1.newDocumentBuilder();
			Document doc1 = dBuilder1.parse(new File(XMLResponse_Path));
			doc1.getDocumentElement().normalize();

			NodeList data = doc1.getElementsByTagName(Nodetag);

			int totaldata = data.getLength();

			for (int temp = 0; temp < totaldata; temp++) {

				Node nNode = data.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;

					sub = eElement.getElementsByTagName(nametag).item(0).getTextContent();
					value = eElement.getElementsByTagName(valuetag).item(0).getTextContent();
					NodeList valuetags = eElement.getElementsByTagName(valuetag);

					int tagle = valuetags.getLength();

					if (sub.equalsIgnoreCase("accountFlagsAfter") || sub.equalsIgnoreCase("accountFlagsBefore")
							|| sub.equalsIgnoreCase("usageCounterUsageThresholdInformation")
							|| sub.equalsIgnoreCase("usageThresholdInformation")
							|| sub.equalsIgnoreCase("dedicatedAccountChangeInformation")
							|| sub.equalsIgnoreCase("accountFlags") || sub.equalsIgnoreCase("offerInformationList")
							|| sub.equalsIgnoreCase("dedicatedAccountInformation")
							|| sub.equalsIgnoreCase("serviceOfferings") || sub.equalsIgnoreCase("offerInformation")
							|| sub.equalsIgnoreCase("attributeInformationList")
							|| sub.equalsIgnoreCase("applicationResponse")
							|| sub.equalsIgnoreCase("freeflowState")) {
						sot = sub;
						value = "";
						tbl = tbl + "<tr><td>" + sub + "</td><td>" + value + "</td></tr>";

					} else if (tagle != 1) {
						for (int i = 1; i < tagle; i++) {
							Node vNode = valuetags.item(i);
							// System.out.println("row "+i);
							Element eElementval = (Element) vNode;

							if (sub.contains("DateTime")) {
								values = eElementval.getElementsByTagName("dateTime.iso8601").item(0).getTextContent();
							} else if (sub.contains("Flags")) {
								values = eElementval.getElementsByTagName("boolean").item(0).getTextContent();
							} else if (sub.contains("Value") || sub.contains("masterAccountNumber")
									|| sub.contains("originTransactionID") || sub.contains("currency")) {
								values = eElementval.getElementsByTagName("string").item(0).getTextContent();
							} else {
								values = eElementval.getElementsByTagName("i4").item(0).getTextContent();
							}
							sot = sub + "==" + values;
							// System.out.println("hi--i4 tag " + sot);
							tbl = tbl + "<tr><td>" + sub + "</td><td>" + values + "</td></tr>";

						}
					} else {
						sot = sub + " == " + value;
						tbl = tbl + "<tr><td>" + sub + "</td><td>" + value + "</td></tr>";
					}
				}
				// tbl = tbl + "<tr><td>" + sot + "</td></tr>";

			}
		} catch (Throwable e) {

			System.setProperty("Order_Status", "FAIL");
		}
		return tbl;
	}
}