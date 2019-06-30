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

public class WebServices {
	static String trfold = "";
	public static final String Result_FLD = System.getProperty("user.dir") + "\\Result";
	static File resfold = null;
	static String timefold = "";
	public static DateFormat For = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
	public static Calendar cal = Calendar.getInstance();
	public static String ExecutionStarttime = For.format(cal.getTime()).toString();
	

	public static void main(String args[]) throws Exception {
		
		String XMLResponsePath="C:\\Users\\venureddyg\\git\\parallel_ussd\\Result\\30-Jun-2019\\30-Jun-2019_15-37-21\\TC_002__GetOffers\\Response\\response.xml";
		String data = WebService(XMLResponsePath);
		 System.out.println(data);
		
	}

	@SuppressWarnings("unused")
	public static String WebService(String XMLResponse_Path) throws Exception {

		 
		String Nodetag = "member";
		String sub;
		String value = null;
		String nametag = "name";
		String valuetag = "value";
		String tbl = "<table><tr><th>Parameter</th></tr>";
		String sot = null;
		String values = null;
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
							|| sub.equalsIgnoreCase("accountFlags") || sub.equalsIgnoreCase("accountFlagsBefore")
							|| sub.equalsIgnoreCase("offerInformationList")
							|| sub.equalsIgnoreCase("dedicatedAccountInformation")
							|| sub.equalsIgnoreCase("serviceOfferings") || sub.equalsIgnoreCase("offerInformation")
							|| sub.equalsIgnoreCase("attributeInformationList")) {
						sot = sub;
						System.out.println("Header----"+sot);
						tbl = tbl + "<tr><td>" + sot + "</td></tr>";
					} else if (tagle != 1) {
						for (int i = 1; i < tagle; i++) {
							Node vNode = valuetags.item(i);
							// System.out.println("row "+i);
							Element eElementval = (Element) vNode;
 							
							if(sub.contains("DateTime")) {
								values = eElementval.getElementsByTagName("dateTime.iso8601").item(0).getTextContent();
							}else if(sub.contains("Flags")) {
								values = eElementval.getElementsByTagName("boolean").item(0).getTextContent();
							}
							else {
								values = eElementval.getElementsByTagName("i4").item(0).getTextContent();
							}
							sot = sub + "==" + values;
							System.out.println("hi--i4 tag " + sot);
							tbl = tbl + "<tr><td>" + sot + "</td></tr>";

						}
					} else {
						sot = sub + "==" + value;
						System.out.println(sot);
						tbl = tbl + "<tr><td>" + sot + "</td></tr>";
						// System.out.println(val);

					}
				}
				

			}	
		} catch (Throwable e) {

			System.setProperty("Order_Status", "FAIL");
		}
		return tbl;
	}

}