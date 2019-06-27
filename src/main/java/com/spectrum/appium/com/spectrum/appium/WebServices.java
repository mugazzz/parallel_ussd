package com.spectrum.appium.com.spectrum.appium;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class WebServices {
	static String trfold = "";
	public static final String Result_FLD = System.getProperty("user.dir") + "\\Result";
	static File resfold = null;
	static String timefold = "";
	public static DateFormat For = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
	public static Calendar cal = Calendar.getInstance();
	public static String ExecutionStarttime = For.format(cal.getTime()).toString();
	private static String values;

	public static void main(String args[]) throws Exception {
		createtimestampfold();
		ExtentReports extent = new ExtentReports();
		ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(trfold + "\\Master.html");
		extent.attachReporter(htmlReporter);

		String data = WebService();
		// System.out.println(data);
		ExtentTest test = extent.createTest("Output");
		test.pass("&nbsp<br>" + data + "</table>");
		extent.flush();
	}

	public static String WebService() throws Exception {

		String XMLResponse_Path = "C:\\Users\\venureddyg\\git\\parallel_ussd\\Result\\27-Jun-2019\\27-Jun-2019_10-43-58\\GetAccountDetails\\Response\\response.xml";
		String Nodetag = "member";
		String sub;
		String value = null;
		String nametag = "name";
		String valuetag = "value";
		String tbl = "<table><tr><th>Parameter</th></tr>";
		String sot = null;
		try {

			DocumentBuilderFactory dbFactory1 = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder1 = dbFactory1.newDocumentBuilder();
			Document doc1 = dBuilder1.parse(new File(XMLResponse_Path));
			doc1.getDocumentElement().normalize();

			NodeList data = doc1.getElementsByTagName(Nodetag);

			int totaldata = data.getLength();
			//System.out.println(totaldata);

			for (int temp = 0; temp < totaldata; temp++) {
				//System.out.println("temp " + temp);
				Node nNode = data.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;

					sub = eElement.getElementsByTagName(nametag).item(0).getTextContent();
					value = eElement.getElementsByTagName(valuetag).item(0).getTextContent();
					NodeList valuetags = eElement.getElementsByTagName(valuetag);

					int tagle = valuetags.getLength();
					// System.out.println("tag length "+tagle);

					if (sub.equalsIgnoreCase("accountFlagsAfter") || sub.equalsIgnoreCase("accountFlagsBefore")
							|| sub.equalsIgnoreCase("accountFlagsAfter")
							|| sub.equalsIgnoreCase("dedicatedAccountChangeInformation")
							|| sub.equalsIgnoreCase("accountFlags") || sub.equalsIgnoreCase("accountFlagsBefore")
							|| sub.equalsIgnoreCase("offerInformationList")
							|| sub.equalsIgnoreCase("dedicatedAccountInformation")
							|| sub.equalsIgnoreCase("serviceOfferings") || sub.equalsIgnoreCase("offerInformation")
							|| sub.equalsIgnoreCase("attributeInformationList")) {
						sot = sub;
						System.out.println("Header----"+sot);
					} else if (tagle != 1) {
						for (int i = 1; i < tagle; i++) {
							Node vNode = valuetags.item(i);
							// System.out.println("row "+i);
							Element eElementval = (Element) vNode;

							values = eElementval.getElementsByTagName("i4").item(0).getTextContent();
							sot = sub + "==" + values;
							System.out.println("hi--i4 tag " + sot);

						}
					} else {
						sot = sub + "==" + value;
						System.out.println(sot);
						// System.out.println(val);

					}
				}
				tbl = tbl + "<tr><td>" + sot + "</td>></tr>";

			}
		} catch (Throwable e) {

			System.setProperty("Order_Status", "FAIL");
		}
		return tbl;
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
			e.getMessage();
		}
	}

}