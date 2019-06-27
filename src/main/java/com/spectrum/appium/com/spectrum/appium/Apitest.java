package com.spectrum.appium.com.spectrum.appium;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Apitest {
	
	public static String Root = System.getProperty("user.dir");
	public static String key1 = "";
	public static String value1 = "";
	public static String Value = "";
	public static String I4v = "";
	public static String sTringv = "";
	public static String DateTimev = "";
	
	 public static void main(String args[]) throws Throwable {
		 WebService();
	  }
	public static void WebService() throws Throwable 
	{
		String Test_StepName = "XML Request & Response"; 

		String XMLResponse_Path = "C:\\Users\\venureddyg\\git\\parallel_ussd\\Result\\24-Jun-2019\\24-Jun-2019_16-30-49\\AddDifferentDA\\Response\\response.xml";
		
		System.out.println(XMLResponse_Path);

		key1="name";

		value1="value";	
		String i4 = "i4";
		String  dateTime = "dateTime.iso8601";
		String string3 = "string";
			@SuppressWarnings("rawtypes")
			
		    //Fetch Data from Soap Response
		    DocumentBuilderFactory dbFactory1 = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder1 = dbFactory1.newDocumentBuilder();
			Document doc1 = dBuilder1.parse(new File(XMLResponse_Path));
			doc1.getDocumentElement().normalize();
			
 		    		 //String Output = getvalue(key1,value1,XMLResponse_Path);
 		    		 //String Output = getvalue(doc1, key1,value1);
			NodeList nList = doc1.getElementsByTagName(value1);
			NodeList nList1 = doc1.getElementsByTagName(key1);
			NodeList I4 = doc1.getElementsByTagName(i4);
			NodeList sTring = doc1.getElementsByTagName(string3);
			NodeList DateTime = doc1.getElementsByTagName(dateTime);

	           //  System.out.println("----------------------------");
	            	for(int k=0; k<=nList1.getLength();k++) {
	            		String Name = nList1.item(k).getTextContent();
	            		Value = nList.item(k).getTextContent();
//	            		if(nList.getLength() !=0) {
//	            			Value = nList.item(k).getTextContent();
//	            		}
//	            		if(I4.getLength() !=0){
//	            			I4v = I4.item(k).getTextContent();
//	            		}
////	            		if(sTring.getLength() !=0){
////	            			sTringv = sTring.item(k).getTextContent();
////	            		}
//	            		if(DateTime.getLength() !=0){
//	            			DateTimev = DateTime.item(k).getTextContent();
//	            		}
	            	//if (Name.equals("dedicatedAccountActiveValue1")) {
            	System.out.println("Output Value = "+":Name ="+Name+" :Value ="+Value);
	         //   }
	            	}
		}
	    	
}