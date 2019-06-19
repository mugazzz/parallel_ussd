package com.spectrum.appium.com.spectrum.appium;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class APIHandler {
	public static final String Result_FLD = System.getProperty("user.dir") + "\\Result";
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
	public static HashMap<String,String> tagmap = new HashMap<String,String>();
	public final static String Reference_Data = System.getProperty("user.dir") + "\\server\\Reference_Sheet.xlsx";
	private static BufferedReader br;
	public static String objectname = "";
	public static String suspendendpoint = "";
	public static String tc = "";
	
	public static void API(String curtcid, String trfold, String State) {
		
		try {
			//createtimestampfold();
			DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH:mm:ss");
			DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSS");
			LocalDateTime now = LocalDateTime.now();
			String originTimeStamp1 = dtf1.format(now).toString();
			String originTimeStamp = originTimeStamp1+"+0000";
//			SimpleDateFormat formatter6=new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss+SSSS"); 
//			LocalDateTime originTimeStamp1 = formatter6.parse(originTimeStamp);
			String originTransactionID = dtf2.format(now).toString();
			System.out.println(originTimeStamp+" : "+originTransactionID );
			String MSISDN = "971520001714";
			System.setProperty("logfilename", trfold + "\\Logs");
			DOMConfigurator.configure("log4j.xml");
			info("Starting execution at +:" + ExecutionStarttime);
			Fillo fillo = new Fillo();
			Connection conn = fillo.getConnection(Reference_Data);
			Recordset rs = conn.executeQuery("Select * from API where \"Execution Control\" = 'Yes'");
			String cellval1 = "blank";
			String cellval2 = "blank";
			String cellval3 = "blank";
			String cellval4 = "blank";
			String cellval5 = "blank";
			String cellval6 = "blank";
			File Des = null;
			File Source = null;
			while (rs.next()) {

				cellval1 = rs.getField("TestCase_ID");
				tc = rs.getField("TestCase_ID");
				cellval2 = rs.getField("Request_Type");
				cellval3 = rs.getField("Request_Name");
				cellval4 = rs.getField("SoapAction");
				cellval5 = rs.getField("NB_URI");
				cellval6 = rs.getField("Request_Method");
				suspendendpoint =cellval5 = rs.getField("NB_URI"); 
				String requests = "GetAccountDetails";
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
								findandreplace(Des, "$subscriberNumber$", MSISDN);
								if(rs.getField("Parameter" + Iterator).equals("Data_Object_Name"))
								{
									objectname = replaceval;
								}
							} else
								break;
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
					String userPassword ="techmqatar:techmqatar";
					System.out.println(userPassword);
					// String userPassword = "TechMahindra:TechMahindra";
					//String encoding = new sun.misc.BASE64Encoder().encode(userPassword.getBytes());
					// String encoding = "dGVjaG1xYXRhcjp0ZWNobXFhdGFy";
					try {
								response = (Response) RestAssured.given().request().body(req)
										.headers("Content-Type", " text/xml","Host ","10.95.214.166:10011", "User-Agent", "UGw Server/5.0/1.0",
												"Authorization", "Basic " + "bWF2ZXJpYzpFcmljc3NvbnRlc3QxMjNA")
										.when() // .contentType("text/xml; charset=utf-8")
										.post(cellval5).then().extract().response();
							respf = new File(Root + "\\API\\Response\\" + cellval1 + ".xml");
							//respf = new File(Root + "\\API\\Response\\" + cellval1 + ".json");
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (response != null) {
						info("Response Code  :" + response.getStatusCode());
						info("Response Header  :" + response.getHeaders().toString());
						info("Response   :" + response.asString());
						writeresponse(respf, response.asString());
						fCreateReportFiles(Des, respf, cellval2, curtcid, trfold, State);
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
						endTestCase(cellval1);

					}
				
			}
			rs.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			error(e.getMessage());
		}

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
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}

	}

	public static String validaterespJSON(Response response, String TC, int statcode) {
		try {
			// File inputFile = resp;
			Fillo fillo = new Fillo();
			Connection conn = fillo.getConnection(Reference_Data);
			// String jsonSrouce =resp.toString();
			// JsonSlurper jsonParser = new JsonSlurper();

			Recordset rs = conn.executeQuery("Select * from API2 where TestCase_ID = '" + TC + "'");
			String status = "Pass";
			String retval = "<Table><tr><td>Parameter</td><td>Expected Value</td><td>Actual Value</td></tr>";
			while (rs.next()) {
				int expstatuscode = Integer.parseInt(rs.getField("Expected_Status_Code"));
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

	public static String validateresp(File resp, String TC, int statcode) {
		try {
			File inputFile = resp;
			Fillo fillo = new Fillo();
			Connection conn = fillo.getConnection(Reference_Data);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setNamespaceAware(true);
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			Recordset rs = conn.executeQuery("Select * from API2 where TestCase_ID = '" + TC + "'");
			String status = "Pass";

			String retval = "<Table><tr><td>Parameter</td><td>Expected Value</td><td>Actual Value</td></tr>";
			while (rs.next()) {
				int expstatuscode = Integer.parseInt(rs.getField("Expected_Status_Code"));
				if (rs.getField("Expected_Status_Code") == "") {
					expstatuscode = 200;
				}
				retval = retval + "<tr><td>Status Code</td><td>" + expstatuscode + "</td><td>" + statcode
						+ "</td></tr>";
				if (expstatuscode != statcode) {
					status = "Fail";
				}
				for (int Iterator = 1; Iterator <= 40; Iterator++) {
					if (rs.getField("Parameter" + Iterator).isEmpty() == false) {
						String param = rs.getField("Parameter" + Iterator).toString();
						String expectedval = rs.getField("Value" + Iterator);
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

	public static String capturevalues(File resp, String TC, String reqname) {
		String sendval = "";
		try {
			File inputFile = resp;
			Fillo fillo = new Fillo();
			Connection conn = fillo.getConnection(Reference_Data);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setNamespaceAware(true);
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			Recordset rs = conn.executeQuery("Select * from API where TestCase_ID = '" + TC + "'");
			String retval = "";
			String[] capval = { "suspendstate[0]", "suspendstate[1]", "suspendstate[2]" };
			int capvalcount = 1;
			while (rs.next()) {

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

	public static void fCreateReportFiles(File request, File response, String RequestType, String curtcid, String trfold, String State) {
		//File ResultRequest = null, ResultResponse = null;
		try {
			File ReqTypFold = new File(trfold + "/" + curtcid); 
			if ((!ReqTypFold.exists()))
				ReqTypFold.mkdir();
			System.out.println("Actual Path given: "+ReqTypFold);
			File ReqTypFold1 = new File(ReqTypFold + "/"+ "CS_API_VALIDATION"); 
			if ((!ReqTypFold1.exists()))
				ReqTypFold1.mkdir();
			System.out.println("Actual Path given: "+ReqTypFold1 );
			File ReqTypFold2 = new File(ReqTypFold1 +"/"+State); 
			if ((!ReqTypFold2.exists()))
				ReqTypFold2.mkdir();
			System.out.println("Actual Path given: "+ReqTypFold2 );

			File TCReqFold = new File(ReqTypFold2+"/Request");
			if ((!TCReqFold.exists()))
				TCReqFold.mkdir();
			System.out.println("Actual Path given: "+TCReqFold );
			File TCResFold = new File(ReqTypFold2+"/Response");
			if ((!TCResFold.exists()))
				TCResFold.mkdir();
					File ResultRequest = new File(TCReqFold +"/"+"request.xml");
					File ResultResponse = new File(TCResFold +"/"+"response.xml");
			GenerateResponse(request, ResultRequest);
			GenerateResponse(response, ResultResponse);

		} catch (Exception e) {
			e.printStackTrace();
			error(e.getMessage());
		}
	}

	private static Logger Log = Logger.getLogger(APIHandler.class.getName());//

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
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
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
			// TODO Auto-generated catch block
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
}