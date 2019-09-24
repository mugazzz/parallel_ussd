package com.spectrum.appium.com.spectrum.appium;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;


public class Asnconvertor {
	public static String Result_FLD = System.getProperty("user.dir") + "\\reports";
	static File resfold = null;
	static String trfold = "";
	static String timefold = "";
	public static DateFormat For = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
	public static Calendar cal = Calendar.getInstance();
	public String ExecutionStarttime = "";
	public final static String Reference_Data = System.getProperty("user.dir") + "\\server\\Reference_Sheet.xlsx";
	///////////////////////////////////////////
	public static String Curr_user_directory_path;
	private static Session session;
	private static ChannelShell channel;
	private static ChannelSftp channel_sftp;
	private static String gzfile;
	private static String berfile;
	
	private static String SDP_Unix_username;
	private static String SDP_Unix_password;
	private static String SDP_unix_hostname;
	private static String CIS_Unix_username;
	private static String CIS_Unix_password;
	private static String CIS_unix_hostname;
	private static String OCC_Unix_username;
	private static String OCC_Unix_password;
	private static String OCC_unix_hostname;
	private static String OCC_Unix_username1;
	private static String OCC_Unix_password1;
	private static String OCC_unix_hostname1;
	private static String AIR_Unix_username;
	private static String AIR_Unix_password;
	private static String AIR_unix_hostname;
	private static String CCN_unix_hostname1;
	private static String CCN_Unix_username1;
	private static String CCN_Unix_password1;
	private static String CCN_unix_hostname;
	private static String CCN_Unix_username;
	private static String CCN_Unix_password;
	public static String global_Final_CDR_path;
	public static String Environment;
	public static String Root = System.getProperty("user.dir");
	public static String Data = Root + "\\Input_sheet_V2.xlsx";
	public static String curr_log_file_path = System.getProperty("user.dir") + "\\Report.txt";
	public String Input ;
	public String Test_Scenario;
	public static String cdrfiles = System.getProperty("user.dir") + "\\CDR";
	private static String gzfilepath = cdrfiles+"\\OCCzip\\";
	private static String finalpath = cdrfiles+"\\OCC\\";
	public static String date;
	public static String dateccn;
	public static String datecis;
	public static String now;
	public static String tbl;
	public static String cistbl;
	public static ExtentReports extent;
	public ExtentHtmlReporter htmlReporter;
	public ExtentTest test;
	
	public  static String Cis_Filepath =cdrfiles+"\\CIS\\meydvvmcis03_EDR_CISOnline1.csv";
	public static String Cis_viewpath;
	public static String nodetag;
	public static String idtag;
	public static String table_data;
	public static String table_data1;
	
	///////////////////////////////////////////////

//	@SuppressWarnings("deprecation")
//	public static void main(String args[]) {
	
	
		public static void nodeValidation(String Input, String MSISDN) {
		try {
			Calendar cal1 = Calendar.getInstance();
			now= timeoffour();
			datecis=Present_datecis();
			dateccn=Present_dateccn();
			
				Fillo fillo = new Fillo();
				Connection cons = fillo.getConnection(Data);
				Connection conn = fillo.getConnection(Reference_Data);
				String strQuery = "Select * from node_xml_conversion "
						+ "where Test_Scenario= '"+Input+"' and Execution ='Yes'";
				Recordset rs = conn.executeQuery(strQuery);
				
				file_deletion(cdrfiles);
				Curr_user_directory_path = System.getProperty("user.dir");
				File localFile = new File(Curr_user_directory_path + "\\" + "CDR");
				File localFileb = new File(Curr_user_directory_path + "\\" + "BackupCDR");
				date = Present_date();
				System.out.println("Connecting to server.....");
				Thread.sleep(3000);
				while (rs.next()) {
					String Node_Type = rs.getField("Node_To_Validate");
				// ---------------------------------------------------------------------------------------
				// ************** CIS Unix Interactions
					if (Node_Type.contains("CISOLD") || Input.contains("ALL")) {
					System.out.println("Waiting for CIS System to Connect");
					String querycis ="Select * from Credentials where Unix_System = 'CIS' ";
					Recordset input = cons.executeQuery(querycis);
					String path = null;
					while (input.next()) {
					String stime=input.getField("Wait_Time");
					int t=Integer.parseInt(stime)*1000;
					Thread.sleep(t);
					path= input.getField("Path");

					CIS_unix_hostname = input.getField("IP_HostName");
					CIS_Unix_username = input.getField("User_Name");
					CIS_Unix_password = input.getField("Password");
					// String date= Present_date();
					}
					// String date= Present_date();
					List<String> CIS_commands = new ArrayList<String>();
					CIS_commands.add("cd "+path);
					CIS_commands.add("sudo more meydvvmcis03_EDR_CISOnline1.csv > /home/"+CIS_Unix_username+"/meydvvmcis03_EDR_CISOnline1.csv");
					executeCommands(CIS_commands, CIS_unix_hostname, CIS_Unix_username, CIS_Unix_password);
					close();

					try {

						JSch jsch = new JSch();
						Session session = jsch.getSession(CIS_Unix_username, CIS_unix_hostname, 22);
						session.setPassword(CIS_Unix_password);
						session.setConfig("StrictHostKeyChecking", "no");
						session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
						session.connect();
						channel_sftp = null;
						channel_sftp = (ChannelSftp) session.openChannel("sftp");
						channel_sftp.connect();
						channel_sftp.cd("/home/"+CIS_Unix_username+"/");
						System.out.println(channel_sftp.pwd());

					//	File localFile = new File(Curr_user_directory_path + "\\" + "CDR");
						@SuppressWarnings("unchecked")
						Vector<ChannelSftp.LsEntry> list = channel_sftp.ls("*.csv");
						for (ChannelSftp.LsEntry entry : list) {
							if (entry.getFilename().contains("meydvvmcis03_EDR_CISOnline1.csv")) {
								System.out.println(entry.getFilename());
								channel_sftp.get("meydvvmcis03_EDR_CISOnline1.csv", localFile + "\\" + "CIS" + "\\" + entry.getFilename());
								Thread.sleep(5000);
								channel_sftp.get("meydvvmcis03_EDR_CISOnline1.csv", localFileb + "\\" + "CIS" + "\\" + MSISDN+date+now+entry.getFilename());
								//System.out.println(localFile + "\\" + "CISraw" + "\\" + entry.getFilename());
								System.out.println("EDR file transfered to "+ localFile + "\\" + "CIS" + "\\" );
							}
						}

						channel_sftp.disconnect();
						session.disconnect();

					} catch (Exception e) {
						e.printStackTrace();

					}
				}
				//---------------------------------CIS NEW ----------------------------------
					if (Node_Type.equalsIgnoreCase("CIS")) {
						System.out.println("Waiting for CIS System to Connect");
						String querycis ="Select * from Credentials where Unix_System = 'CIS' ";
						Recordset input = cons.executeQuery(querycis);
						String path = null;
						while (input.next()) {
						String stime=input.getField("Wait_Time");
						int t=Integer.parseInt(stime)*1000;
						Thread.sleep(t);
						path= input.getField("Path");

						CIS_unix_hostname = input.getField("IP_HostName");
						CIS_Unix_username = input.getField("User_Name");
						CIS_Unix_password = input.getField("Password");
						// String date= Present_date();
						}
						List<String> CIS_commands = new ArrayList<String>();
						CIS_commands.add("cd "+path);
						CIS_commands.add("grep -l "+MSISDN+" *"+datecis+"*|tail -1 > /home/"+CIS_Unix_username+"/EDRfile.txt");
						executeCommands(CIS_commands, CIS_unix_hostname, CIS_Unix_username, CIS_Unix_password);
						close();

						try {

							JSch jsch = new JSch();
							Session session = jsch.getSession(CIS_Unix_username, CIS_unix_hostname, 22);
							session.setPassword(CIS_Unix_password);
							session.setConfig("StrictHostKeyChecking", "no");
							session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
							session.connect();
							channel_sftp = null;
							channel_sftp = (ChannelSftp) session.openChannel("sftp");
							channel_sftp.connect();
							channel_sftp.cd("/home/"+CIS_Unix_username);

						//	File localFile = new File(Curr_user_directory_path + "\\" + "CDR");
							@SuppressWarnings("unchecked")
							Vector<ChannelSftp.LsEntry> list = channel_sftp.ls("*.txt");
							for (ChannelSftp.LsEntry entry : list) {
								if (entry.getFilename().contains("EDRfile.txt")) {
									channel_sftp.get(entry.getFilename(), localFile + "\\" + "EDRfile.txt");
									Thread.sleep(5000);
								}
							}
							// Code to get EDR file latest name
							String edrfile = filename(localFile + "\\" + "EDRfile.txt");
							System.out.println(edrfile);

							// Code to get file to local system
							channel_sftp.cd(path);

							@SuppressWarnings("unchecked")
							Vector<ChannelSftp.LsEntry> list1 = channel_sftp.ls("*.csv");
							Thread.sleep(5000);

							for (ChannelSftp.LsEntry entry1 : list1) {
								if (entry1.getFilename().contains(edrfile)) {
									channel_sftp.get(edrfile, localFile + "\\" + "CIS" + "\\"+"meydvvmcis03_EDR_CISOnline1.csv");
									Thread.sleep(10000);
									channel_sftp.get(edrfile, localFileb + "\\" + "CIS" + "\\");
									System.out.println("SDP file transfered to " + localFile + "\\" + "CIS" + "\\");
								} else {
								}
							}

							channel_sftp.disconnect();
							session.disconnect();

						} catch (Exception e) {
							e.printStackTrace();

						}

					}
				// ---------------------------------------------------------------------------------------
				// ************** SDP Unix Interactions
				if (Node_Type.contains("SDP") || Input.contains("ALL")) {
					System.out.println("waiting for SDP connectivty ");
					String query ="Select * from Credentials where Unix_System = 'SDP' ";
					Recordset input= cons.executeQuery(query);
					String path = null;
					while (input.next()) {
					String stime=input.getField("Wait_Time");
					int t=Integer.parseInt(stime)*1000;
					path= input.getField("Path");

					SDP_unix_hostname = input.getField("IP_HostName");
					SDP_Unix_username = input.getField("User_Name");
					SDP_Unix_password = input.getField("Password");
					Thread.sleep(t);
				
					//String date = Present_date();
					}
					//String date = Present_date();
					List<String> SDP_commands = new ArrayList<String>();
					SDP_commands.add("cd "+path);
					SDP_commands.add("grep -l "+MSISDN+" *20"+date+"* |tac |head -1 > /home/"+SDP_Unix_username+"/sdpfile.txt");
					executeCommands(SDP_commands, SDP_unix_hostname, SDP_Unix_username, SDP_Unix_password);
					close();

					try {
						JSch jsch = new JSch();
						Session session = jsch.getSession(SDP_Unix_username, SDP_unix_hostname, 22);
						session.setPassword(SDP_Unix_password);
						session.setConfig("StrictHostKeyChecking", "no");
						session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
						session.connect();
						channel_sftp = null;
						channel_sftp = (ChannelSftp) session.openChannel("sftp");
						channel_sftp.connect();
						channel_sftp.cd("/home/"+SDP_Unix_username);

					//	File localFile = new File(Curr_user_directory_path + "\\" + "CDR");
						@SuppressWarnings("unchecked")
						Vector<ChannelSftp.LsEntry> list = channel_sftp.ls("*.txt");
						for (ChannelSftp.LsEntry entry : list) {
							if (entry.getFilename().contains("sdpfile.txt")) {
								channel_sftp.get(entry.getFilename(), localFile + "\\" + "sdpfile.txt");
								Thread.sleep(5000);
							}
						}
						// Code to get CDR file latest name
						String cdrfile = filename(localFile + "\\" + "sdpfile.txt");
						System.out.println(cdrfile);

						// Code to get file to local system
						channel_sftp.cd(path);
						
						@SuppressWarnings("unchecked")
						Vector<ChannelSftp.LsEntry> list1 = channel_sftp.ls("*.ASN");
						Thread.sleep(5000);

						for (ChannelSftp.LsEntry entry1 : list1) {
							if (entry1.getFilename().contains(cdrfile)) {
								channel_sftp.get(cdrfile, localFile + "\\" + "SDP" + "\\");
								Thread.sleep(10000);
								channel_sftp.get(cdrfile, localFileb + "\\" + "SDP" + "\\");
								System.out.println("SDP file transfered to " + localFile + "\\" + "SDP" + "\\");
							} else {
							}
						}

						channel_sftp.disconnect();
						session.disconnect();

					} catch (Exception e) {
						e.printStackTrace();

					}


				}
				
				// ---------------------------------------------------------------------------------------
				// ************** OCC Unix Interactions port 22
				if (Node_Type.contains("OCC")){
					System.out.println("Waiting for OCC system to connect");
					String query ="Select * from Credentials where Unix_System = 'OCC2' ";
					Recordset input= cons.executeQuery(query);
					String path = null;
					while (input.next()) {
					String stime=input.getField("Wait_Time");
					int t=Integer.parseInt(stime)*1000;
					path= input.getField("Path");

					OCC_unix_hostname = input.getField("IP_HostName");
					OCC_Unix_username = input.getField("User_Name");
					OCC_Unix_password = input.getField("Password");
					Thread.sleep(t);
									
					//Curr_user_directory_path = System.getProperty("user.dir");
					
					//String date = Present_date();
					}
					//String date = Present_date();
					List<String> OCC_commands = new ArrayList<String>();
					OCC_commands.add("cd "+path);
					OCC_commands.add(
							"zgrep -l "+MSISDN+" *"+date+"* |tac|head -1 > /home/tasuser/Auto/occfile_22.txt");
					executeCommands(OCC_commands, OCC_unix_hostname, OCC_Unix_username, OCC_Unix_password);
					close();

					try {

						JSch jsch = new JSch();
						Session session = jsch.getSession(OCC_Unix_username, OCC_unix_hostname, 22);
						session.setPassword(OCC_Unix_password);
						session.setConfig("StrictHostKeyChecking", "no");
						session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
						session.connect();
						channel_sftp = null;
						channel_sftp = (ChannelSftp) session.openChannel("sftp");
						channel_sftp.connect();
						channel_sftp.cd("/home/tasuser/Auto");

					//	File localFile = new File(Curr_user_directory_path + "\\");
						@SuppressWarnings("unchecked")
						Vector<ChannelSftp.LsEntry> list = channel_sftp.ls("*.txt");
						for (ChannelSftp.LsEntry entry : list) {
							if (entry.getFilename().contentEquals("occfile_22.txt")) {
								channel_sftp.get(entry.getFilename(), localFile + "\\" + "occfile_22.txt");
								Thread.sleep(5000);
							}
						}
						// Code to get OCC file latest name
						String occfile = filename(localFile + "\\" + "occfile_22.txt");
						System.out.println(occfile);

						// Code to get file to local system
						channel_sftp.cd(path);
						// channel_sftp.pwd();
						//File localFile1 = new File(Curr_user_directory_path + "\\" + "CDR");
						@SuppressWarnings("unchecked")
						Vector<ChannelSftp.LsEntry> list1 = channel_sftp.ls("*.gz");
						Thread.sleep(5000);

						for (ChannelSftp.LsEntry entry2 : list1) {
							if (entry2.getFilename().contains(occfile)) {
								channel_sftp.get(occfile, localFile + "\\" + "OCCzip" + "\\");
								Thread.sleep(10000);
								channel_sftp.get(occfile, localFileb + "\\" + "OCCzip" + "\\");
								System.out.println("SDP file transfered to " + localFile + "\\" + "OCCzip" + "\\");
							} else {
							}
						}

						channel_sftp.disconnect();
						session.disconnect();

					} catch (Exception e) {
						e.printStackTrace();

					}

					// ---------------------------------------------------------------------------------------
					// ************** OCC Unix Interactions port 21
					//Curr_user_directory_path = System.getProperty("user.dir");
					String query1 ="Select * from Credentials where Unix_System = 'OCC1' ";
					Recordset input1= cons.executeQuery(query1);
					String path1 = null;
					while (input1.next()) {
					String stime1=input1.getField("Wait_Time");
					int t1=Integer.parseInt(stime1)*1000;
					path1= input1.getField("Path");

					OCC_unix_hostname1 = input1.getField("IP_HostName");
					OCC_Unix_username1= input1.getField("User_Name");
					OCC_Unix_password1 = input1.getField("Password");
					Thread.sleep(t1);
					
					// String date= Present_date();
					}

					List<String> OCC1_commands = new ArrayList<String>();
					OCC1_commands.add("cd "+path1);
					OCC1_commands.add(
							"zgrep -l "+MSISDN+" *"+date+"* |tac|head -1 > /home/tasuser/Auto/occfile_21.txt");
					executeCommands(OCC1_commands, OCC_unix_hostname1, OCC_Unix_username1, OCC_Unix_password1);
					close();

					try {

						JSch jsch = new JSch();
						Session session = jsch.getSession(OCC_Unix_username1, OCC_unix_hostname1, 22);
						session.setPassword(OCC_Unix_password1);
						session.setConfig("StrictHostKeyChecking", "no");
						session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
						session.connect();
						channel_sftp = null;
						channel_sftp = (ChannelSftp) session.openChannel("sftp");
						channel_sftp.connect();
						channel_sftp.cd("/home/tasuser/Auto");

					//	File localFile = new File(Curr_user_directory_path + "\\");
						@SuppressWarnings("unchecked")
						Vector<ChannelSftp.LsEntry> list = channel_sftp.ls("*.txt");
						for (ChannelSftp.LsEntry entry : list) {
							if (entry.getFilename().contains("occfile_21.txt")) {
								channel_sftp.get(entry.getFilename(), localFile + "\\" + "occfile_21.txt");
								Thread.sleep(5000);
							}
						}
						// Code to get OCC file latest name
						String occfile1 = filename(localFile + "\\" + "occfile_21.txt");
						System.out.println(occfile1);

						// Code to get file to local system
						channel_sftp.cd(path1);
						// channel_sftp.pwd();
						//File localFile1 = new File(Curr_user_directory_path + "\\" + "CDR");
						@SuppressWarnings("unchecked")
						Vector<ChannelSftp.LsEntry> list1 = channel_sftp.ls("*.gz");
						Thread.sleep(5000);

						for (ChannelSftp.LsEntry entry2 : list1) {
							if (entry2.getFilename().contentEquals(occfile1)) {
								channel_sftp.get(occfile1, localFile + "\\" + "OCCzip" + "\\");
								Thread.sleep(5000);
								channel_sftp.get(occfile1, localFileb + "\\" + "OCCzip" + "\\");
								System.out.println("SDP file transfered to " + localFile + "\\" + "OCCzip" + "\\");
							} else {
							}
						}

						channel_sftp.disconnect();
						session.disconnect();

					} catch (Exception e) {
						e.printStackTrace();

					}
					// Extract OCC.gz file to parsing ber file
					file_extraction(gzfilepath,finalpath);
					
					
				}
				// Connecting to CCN to get CDR file
				if (Node_Type.contains("CCN") || Input.contains("ALL")) {
					System.out.println("Waiting for CCN system to connect");
					String query ="Select * from Credentials where Unix_System = 'CCN0' ";
					Recordset input= cons.executeQuery(query);
					String path = null;
					while (input.next()) {
					String stime=input.getField("Wait_Time");
					int t=Integer.parseInt(stime)*1000;
					path= input.getField("Path");

					 CCN_unix_hostname = input.getField("IP_HostName");
					 CCN_Unix_username = input.getField("User_Name");
					 CCN_Unix_password = input.getField("Password");
					Thread.sleep(t);
	
					}
					List<String> CCN_commands = new ArrayList<String>();
					CCN_commands.add("cd "+path);
					CCN_commands.add("grep -l "+MSISDN+" *"+dateccn+now+"* |tac|head -1 > /cluster/home/system-oam/tasuser/Auto/CCN_0file.txt");
					executeCommands(CCN_commands, CCN_unix_hostname, CCN_Unix_username, CCN_Unix_password);
					close();
					//grep -l 971520001714 *20190529* |xargs ls -l|grep 13:0[0-9]|tac|head -1 > /cluster/home/system-oam/tasuser/Auto/CCNfile.txt
					//grep -l 971520001714 *20190530063*|tac|head -1

					try {

						JSch jsch = new JSch();
						Session session = jsch.getSession(CCN_Unix_username, CCN_unix_hostname, 22);
						session.setPassword(CCN_Unix_password);
						session.setConfig("StrictHostKeyChecking", "no");
						session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
						session.connect();
						channel_sftp = null;
						channel_sftp = (ChannelSftp) session.openChannel("sftp");
						channel_sftp.connect();
						channel_sftp.cd("/cluster/home/system-oam/tasuser/Auto");

					//	File localFile = new File(Curr_user_directory_path + "\\");
						@SuppressWarnings("unchecked")
						Vector<ChannelSftp.LsEntry> list = channel_sftp.ls("*.txt");
						System.out.println(list);
						for (ChannelSftp.LsEntry entry : list) {
							if (entry.getFilename().contains("CCN_0file.txt")) {
								channel_sftp.get(entry.getFilename(), localFile + "\\" + "CCN_0file.txt");
								Thread.sleep(5000);
							}
						}
						// Code to get CCN file latest name
						String CCN0file = filename(localFile + "\\" + "CCN_0file.txt");
						System.out.println(CCN0file);

						// Code to get file to local system
						channel_sftp.cd(path);
						// channel_sftp.pwd();
						//File localFile1 = new File(Curr_user_directory_path + "\\" + "InputCdrFile");
						@SuppressWarnings("unchecked")
						Vector<ChannelSftp.LsEntry> list1 = channel_sftp.ls(".");
						Thread.sleep(5000);

						for (ChannelSftp.LsEntry entry1 : list1) {
							
							if (entry1.getFilename().contains(CCN0file)) {
								channel_sftp.get(CCN0file, localFile + "\\" + "CCN" + "\\");
								
								Thread.sleep(10000);
								channel_sftp.get(CCN0file, localFileb + "\\" + "CCN" + "\\");
								System.out.println("SDP file transfered to " + localFile + "\\" + "CCN" + "\\");
							} 
							
						}

						channel_sftp.disconnect();
						session.disconnect();

					} catch (Exception e) {
						e.printStackTrace();

					}
					//
					
					String query1 ="Select * from Credentials where Unix_System = 'CCN1' ";
					Recordset input1= cons.executeQuery(query1);
					String path1 = null;
					while (input1.next()) {
					String stime1=input1.getField("Wait_Time");
					int t1=Integer.parseInt(stime1)*1000;
					path1= input1.getField("Path");

					CCN_unix_hostname1 = input1.getField("IP_HostName");
					CCN_Unix_username1 = input1.getField("User_Name");
					CCN_Unix_password1 = input1.getField("Password");
					Thread.sleep(t1);  
					
					}
						
						List<String> CCN1_commands = new ArrayList<String>();
						CCN1_commands.add("cd "+path1);
						CCN1_commands.add("grep -l "+MSISDN+" *"+dateccn+now+"* |tac|head -1 > /cluster/home/system-oam/tasuser/Auto/CCN_1file.txt");
						executeCommands(CCN1_commands, CCN_unix_hostname1, CCN_Unix_username1, CCN_Unix_password1);
						close();
						//grep -l 971520001714 *20190529* |xargs ls -l|grep 13:0[0-9]|tac|head -1 > /cluster/home/system-oam/tasuser/Auto/CCNfile.txt

						try {

							JSch jsch = new JSch();
							Session session = jsch.getSession(CCN_Unix_username, CCN_unix_hostname, 22);
							session.setPassword(CCN_Unix_password);
							session.setConfig("StrictHostKeyChecking", "no");
							session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
							session.connect();
							channel_sftp = null;
							channel_sftp = (ChannelSftp) session.openChannel("sftp");
							channel_sftp.connect();
							channel_sftp.cd("/cluster/home/system-oam/tasuser/Auto");

						//	File localFile = new File(Curr_user_directory_path + "\\");
							@SuppressWarnings("unchecked")
							Vector<ChannelSftp.LsEntry> list = channel_sftp.ls("*.txt");
							System.out.println(list);
							for (ChannelSftp.LsEntry entry : list) {
								if (entry.getFilename().contains("CCN_1file.txt")) {
									channel_sftp.get(entry.getFilename(), localFile + "\\" + "CCN_1file.txt");
									Thread.sleep(5000);
								}
							}
							// Code to get CCN file latest name
							String CCN1file = filename(localFile + "\\" + "CCN_1file.txt");
							System.out.println(CCN1file);

							// Code to get file to local system
							channel_sftp.cd(path1);
							// channel_sftp.pwd();
							//File localFile1 = new File(Curr_user_directory_path + "\\" + "InputCdrFile");
							@SuppressWarnings("unchecked")
							Vector<ChannelSftp.LsEntry> list1 = channel_sftp.ls(".");
							Thread.sleep(5000);

							for (ChannelSftp.LsEntry entry1 : list1) {
								
								if (entry1.getFilename().contains(CCN1file)) {
									channel_sftp.get(CCN1file, localFile + "\\" + "CCN" + "\\");
									
									Thread.sleep(10000);
									channel_sftp.get(CCN1file, localFileb + "\\" + "CCN" + "\\");
									System.out.println("SDP file transfered to " + localFile + "\\" + "CCN" + "\\");
								} 
								
							}

							channel_sftp.disconnect();
							session.disconnect();

						} catch (Exception e) {
							e.printStackTrace();

						}
				}
				// ************** AIR Unix Interactions
				//Curr_user_directory_path = System.getProperty("user.dir");
				if (Node_Type.contains("AIR")) {
					System.out.println("Waiting for Air system to connect");
					String query ="Select * from Credentials where Unix_System = 'AIR' ";
					Recordset input= cons.executeQuery(query);
					String path = null;
					while (input.next()) {
					String stime=input.getField("Wait_Time");
					int t=Integer.parseInt(stime)*1000;
					path= input.getField("Path");

					AIR_unix_hostname = input.getField("IP_HostName");
					AIR_Unix_username = input.getField("User_Name");
					AIR_Unix_password = input.getField("Password");
					Thread.sleep(t);
									
					//String date = Present_date();
					}

					List<String> AIR_commands = new ArrayList<String>();
					AIR_commands.add("cd "+path);
					AIR_commands.add("grep -l " + MSISDN + " *" + date + "* |tac |head -1 > /home/tasuser/Auto/Airfile.txt");
					executeCommands(AIR_commands, AIR_unix_hostname, AIR_Unix_username, AIR_Unix_password);
					close();

					try {

						JSch jsch = new JSch();
						Session session = jsch.getSession(AIR_Unix_username, AIR_unix_hostname, 22);
						session.setPassword(AIR_Unix_password);
						session.setConfig("StrictHostKeyChecking", "no");
						session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
						session.connect();
						channel_sftp = null;
						channel_sftp = (ChannelSftp) session.openChannel("sftp");
						channel_sftp.connect();
						channel_sftp.cd("/home/tasuser/Auto");

					//	File localFile = new File(Curr_user_directory_path + "\\");
						@SuppressWarnings("unchecked")
						Vector<ChannelSftp.LsEntry> list = channel_sftp.ls("*.txt");
						
						for (ChannelSftp.LsEntry entry : list) {
							if (entry.getFilename().contains("Airfile.txt")) {
								channel_sftp.get(entry.getFilename(), localFile + "\\" + "Airfile.txt");
								Thread.sleep(5000);
							}
						}
						// Code to get AIR file latest name
						String Airfile = filename(localFile + "\\" + "Airfile.txt");
						System.out.println(Airfile);

						// Code to get file to local system
						channel_sftp.cd(path);
						// channel_sftp.pwd();
						//File localFile1 = new File(Curr_user_directory_path + "\\" + "CDR");
						@SuppressWarnings("unchecked")
						Vector<ChannelSftp.LsEntry> list1 = channel_sftp.ls("*.AIR");
						Thread.sleep(5000);

						for (ChannelSftp.LsEntry entry1 : list1) {
							
							if (entry1.getFilename().contains(Airfile)) {
								channel_sftp.get(Airfile, localFile + "\\" + "AIR" + "\\");
								
								Thread.sleep(10000);
								channel_sftp.get(Airfile, localFileb + "\\" + "AIR" + "\\");
								System.out.println("AIR file transfered to " + localFile + "\\" + "AIR" + "\\");
							} 
							
						}

						channel_sftp.disconnect();
						session.disconnect();

					} catch (Exception e) {
						e.printStackTrace();

					}
				}
				
				if (Node_Type.contains("CIS-DB")) {
					System.out.println("------------ CIS _DB ------------");
					cis_db("adhoc", "971520001714");
				}
				
			}
				

		} catch (Exception e) {
			e.printStackTrace();
		}
		}
		
		public static String[] Result(String trfold, String MSISDN, String Prod_ID, String input, String Test_Case_ID, String curtcid, String Product_Name, String Test_Scenario_I, String Test_Case, String Confirmation, String Message, String Recharge_Coupon, String Voice_Call_To, String Text_Message, String SMS_To_Receiver, String Balancemsg, String p2p_To_Number, String p2p_Amount, String ExecutionStarttime, String CALL_DURATION, String Count)
		{
			String[] convertor = new String [50];
			try {
				/////////////////////////////////////////////////////////////////////////////
				
//				createtimestampfold(ExecutionStarttime);
//				//System.setProperty("logfilename", trfold + "\\Logs");
//				DOMConfigurator.configure("log4j.xml");
//				ExtentReports extent = new ExtentReports();
//				ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(trfold + "\\Master.html");
//				extent.attachReporter(htmlReporter);
//				info("Starting execution at +:" + ExecutionStarttime);
//				String Data = "TestData.xlsx";
				Fillo fillo = new Fillo();
//				Connection conn = fillo.getConnection(Data);
//				Recordset rs = conn.executeQuery("Select * from TestData where \"Execution Control\" = 'Yes'");
				
			// DB Table connection
				Connection conn = fillo.getConnection(Reference_Data);
				String strQuery = "Select * from node_xml_conversion "
						+ "where Test_Scenario = '"+input+"' and Execution ='Yes'";
				System.out.println(strQuery);
				Recordset rs = conn.executeQuery(strQuery);
				
				while (rs.next()) {
					String type = rs.getField("Node_To_Validate");
					String strQuery2 = "Select * from cdr_parameters "
							+ "where Type= '"+type+"'";
					Recordset rs2 = conn.executeQuery(strQuery2);
				while (rs2.next()) {
					String filename = "";
					String filetype = rs2.getField("Type");
					String refid = rs2.getField("Refrence_ID");
					File dir = new File(System.getProperty("user.dir") + "\\CDR\\" + filetype);
					File[] directoryListing = dir.listFiles();
					if (directoryListing != null) {
						for (File child : directoryListing) {
							filename = child.getAbsoluteFile().getName();
							if(!filetype.equalsIgnoreCase("CIS")) {
														
							//startTestCase("Parsing File " + filename);
							String schemaname = "";
							File TCFold = new File(trfold + "/" + curtcid+"/"+filetype);
							if ((!TCFold.exists()))
								TCFold.mkdir();
							File TCFold1 = new File(trfold + "/" + curtcid+"/"+filetype + "/" + filename);
							if ((!TCFold1.exists()))
								TCFold1.mkdir();
							File file = new File(trfold + "/" + curtcid+"/"+filetype + "/" + filename + "/output.xml");
							System.out.println("SDP------>"+trfold+"/"+ curtcid + filetype+"/output.xml");
							if (file.exists()) {
								file.delete();
							}
							file.createNewFile();
							Runtime rt = Runtime.getRuntime();
							// System.out.println(System.getProperty("user.dir"));
							if (filetype.equalsIgnoreCase("AIR")) {
								schemaname = "AIROUTPUTCDR411A.asn1 -pdu DetailOutputRecord";
							} else if (filetype.equalsIgnoreCase("SDP")) {
								schemaname = "SDPOUTPUTCDRCS416A.asn1";

							} else if (filetype.equalsIgnoreCase("OCC")) {
								schemaname = "ccn55a_latest_1.asn1 -pdu DetailOutputRecord";
							} else if (filetype.equalsIgnoreCase("CCN")) {
								schemaname = "ccn55a_latest_1.asn1 -pdu DetailOutputRecord";
													
							}
							else {
								schemaname = "ccn55a.asn1 -pdu DetailOutputRecord";
							}
							String commands = "asn2xml CDR/" + filetype + "/" + filename + " -schema Schema/"
									+ schemaname;
							Process proc = rt.exec(commands);
							BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
							BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
							// read the output from the command
							// System.out.println("Here is the standard output of the command:\n");
							String s = null;
							// File file = new File("test.xml");
							String start = "no";
							String replaceq = "no";
							String replaceo = "no";
							while ((s = stdInput.readLine()) != null) {
								// System.out.println(s);
								if (s.equals("<?xml version=\"1.0\"?>")) {
									start = "yes";
								}
								if (start.equals("yes")) {
									if (!s.equals("Decoded Successfully")) {
										if (s.contains("<selectionTreeQualifiers>")) {
											replaceq = "yes";
										} else if (s.contains("</selectionTreeQualifiers>")) {
											replaceq = "no";
										}
										if (s.contains("<usedOffers>")) {
											replaceo = "yes";
										} else if (s.contains("</usedOffers>")) {
											replaceo = "no";
										}
										if (replaceq.equals("yes")) {
											s = s.replace("<>", "<Qualifier>");
											s = s.replace("</>", "</Qualifier>");
										}
										if (replaceo.equals("yes")) {
											s = s.replace("<>", "<OfferID>");
											s = s.replace("</>", "</OfferID>");
										}

										FileUtils.writeStringToFile(file, s + "\n", true);
									}
								}
							}

							// read any errors from the attempted command
							System.out.println("Here is the standard error of the command (if any):\n");
							while ((s = stdError.readLine()) != null) {
								System.out.println(s);
							}
							if(filetype.equals("OCC")) {
							String tbl = "<table><tr><th>Parameter</th><th>Value</th></tr>";
							//String filenameq = "";
							convertor[1] = filename;
							convertor[0] = filetype;
							for (int Iterator = 1; Iterator < rs2.getFieldNames().size(); Iterator++) {
								if (rs2.getFieldNames().get(Iterator).toString().contains("Parameter")) {
									if (rs2.getField(rs2.getFieldNames().get(Iterator)) != "") {
										//if (rs.getFieldNames().get(Iterator + 1).toString().contains("value")) {
											//if (rs.getField(rs.getFieldNames().get(Iterator + 1)).contains("YES")) {

												String param1 = rs2.getField(rs2.getFieldNames().get(Iterator).toString())
														.toString();
												//String retval = parsexml(param1, file);
												String filepath=file.toString();
												String retval = parsedata(filetype,filepath ,param1,MSISDN);												
												
												tbl = tbl + "<tr><td>" + param1 + "</td><td>" + retval + "</td></tr>";
												// conn.executeUpdate("Update TestData set Value"+findx+"='"+retval+"'
												// where Refrence_ID ='"+refid+"'");
											//}
										}

								}

							}
							convertor[2] = tbl;
							}
							
							else if(filetype.equals("SDP")) {
								convertor[4] = filetype;
								convertor[6] = filename;
								System.out.println("SDP Path: ----------%%%%%%%%%--> "+convertor[4]+convertor[6]);
								
							}
							
							else if(filetype.equals("AIR")){
								String tbl = "<table><tr><th>Parameter</th><th>Value</th></tr>";
								//String filenameq = "";
								convertor[9] = filename;
								for (int Iterator = 1; Iterator < rs2.getFieldNames().size(); Iterator++) {
									if (rs2.getFieldNames().get(Iterator).toString().contains("Parameter")) {
										if (rs2.getField(rs2.getFieldNames().get(Iterator)) != "") {
											//if (rs.getFieldNames().get(Iterator + 1).toString().contains("value")) {
												//if (rs.getField(rs.getFieldNames().get(Iterator + 1)).contains("YES")) {

													String param1 = rs2.getField(rs2.getFieldNames().get(Iterator).toString())
															.toString();
													//String retval = parsexml(param1, file);
													String filepath=file.toString();
													String retval = parsedata(filetype,filepath ,param1,MSISDN);												
													
													tbl = tbl + "<tr><td>" + param1 + "</td><td>" + retval + "</td></tr>";
													// conn.executeUpdate("Update TestData set Value"+findx+"='"+retval+"'
													// where Refrence_ID ='"+refid+"'");
												//}
											}

									}

								}
								convertor[10] = tbl;
								convertor[11] = filetype;
								
							}
							
							else if(filetype.equals("CCN")){
								String tbl = "<table><tr><th>Parameter</th><th>Value</th></tr>";
								//String filenameq = "";
								convertor[12] = filename;
								for (int Iterator = 1; Iterator < rs2.getFieldNames().size(); Iterator++) {
									if (rs2.getFieldNames().get(Iterator).toString().contains("Parameter")) {
										if (rs2.getField(rs2.getFieldNames().get(Iterator)) != "") {
											//if (rs.getFieldNames().get(Iterator + 1).toString().contains("value")) {
												//if (rs.getField(rs.getFieldNames().get(Iterator + 1)).contains("YES")) {

													String param1 = rs2.getField(rs2.getFieldNames().get(Iterator).toString())
															.toString();
													//String retval = parsexml(param1, file);
													String filepath=file.toString();
													String retval = parsedata(filetype,filepath ,param1,MSISDN);												
													
													tbl = tbl + "<tr><td>" + param1 + "</td><td>" + retval + "</td></tr>";
													// conn.executeUpdate("Update TestData set Value"+findx+"='"+retval+"'
													// where Refrence_ID ='"+refid+"'");
												//}
											}

									}

								}
								convertor[14] = tbl;
								convertor[13] = filetype;
								
							}
							}
							
							else if(filetype.equalsIgnoreCase("CIS-DB")){
								System.out.println("------ CIS DB Part ---------");
								convertor[20] = cis_db("adhoc", MSISDN);
							}
							else {
								startTestCase("Parsing File " + filename);
								//String schemaname = "";
								File TCFold = new File(trfold + "/" + curtcid+"/"+filetype);
								if ((!TCFold.exists()))
									TCFold.mkdir();
								File TCFold1 = new File(trfold + "/" + curtcid+"/"+filetype + "/" + filename);
								if ((!TCFold1.exists()))
									TCFold1.mkdir();
								File filecsv = new File(trfold + "/"  + curtcid+"/"+filetype + "/" + filename + "/Output1.csv");
								String FileCSV = "trfold + '/'  + curtcid+'/'+filetype + '/' + filename+'/'";
								Cis_viewpath=filecsv.toString();
								System.out.println(Cis_viewpath);
								if (filecsv.exists()) {
									filecsv.delete();
								}
								filecsv.createNewFile();
								cistbl=CSVparse(Cis_Filepath,Cis_viewpath,MSISDN);
								convertor[5] = filetype;
								convertor[7] = filename;
								convertor[3] = cistbl;
							}
						}
					}
				}

				}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("No CDR Found for the given MSISDN");
		}
			return convertor;
		}
		
		public static String cis_db(String table, String MSISDN) {
			try {
				String validate_adhoc = "select msisdn, product_id, status,start_date, expiry_date,product_cost,srcchannel,network_status from rs_adhoc_products where msisdn="+MSISDN+" order by last_action_date desc limit 1";
				String validate_renewal = "select * from renewal where msisdn="+MSISDN+" order by last_action_date desc limit 1";

				//String validate_renewal = "select msisdn,last_renewal_date,renewal_date,status,activation_date,product_id,product_description,product_type,srcchannel,product_category,product_purchase_type,language_id,network_status from renewal where msisdn="+MSISDN+" order by last_action_date desc limit 1";
				if (table.equalsIgnoreCase("adhoc")) {
					table_data=ValidationQuery(validate_adhoc, table);
				} else if (table.equalsIgnoreCase("renewal")) {
					table_data=ValidationQuery(validate_renewal, table);

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Table available: "+table_data + table_data1);
//			if(table_data1.equalsIgnoreCase(null)) {
//				table_data1 = " ";
//			}
//			if(table_data.equalsIgnoreCase(null)){
//				table_data=" ";
//			}
			return table_data;
		}
		
		public static String ValidationQuery(String Validatin_Query, String Table) throws SQLException {
			String table_data = null;
			String dbURL = "jdbc:postgresql://10.95.214.136:5444/scs";
			Properties parameters = new Properties();
			parameters.put("user", "mugazmaveric1");
			parameters.put("password", "maverick");

			java.sql.Connection conn11 = DriverManager.getConnection(dbURL, parameters);
			System.out.println("Opened database successfully");
			Statement st = conn11.createStatement();
			ResultSet rs = st.executeQuery(Validatin_Query);

			System.out.println("Query Executed");
			// display actor information
			if (Table.equalsIgnoreCase("adhoc")) {

				table_data = displayActorAdhoc(rs);

			} else if (Table.equalsIgnoreCase("renewal")) {

				table_data = displayActorRenew(rs);

			} else {

			}

			st.close();
			conn11.close();
			return table_data;

		}

//	public static String parsexml(String param1, File file)
//			throws ParserConfigurationException, SAXException, IOException, IndexOutOfBoundsException {
//		String[] parmsplt = param1.split("\\[");
//		String param = parmsplt[0];
//		int inde = 0;
//		if (parmsplt.length == 1) {
//			inde = 0;
//		} else {
//			String[] paramsplit2 = parmsplt[1].split("\\]");
//			inde = Integer.parseInt(paramsplit2[0]);
//		}
//
//		File ipfile = file;
//		String fstr = "";
//		StringBuilder fileContents = new StringBuilder((int) ipfile.length());
//
//		try (Scanner scanner = new Scanner(ipfile)) {
//			while (scanner.hasNextLine()) {
//				fileContents.append(scanner.nextLine() + System.lineSeparator());
//			}
//			fstr = fileContents.toString();
//		}
//
//		Document doc = Jsoup.parse(fstr, "", Parser.xmlParser());
//		// System.out.println(param +" = "+ doc.select(param).get(0).text());
//		if (doc.select(param).size() != 0) {
//			info(param + " = " + doc.select(param).get(inde).text());
//			return doc.select(param).get(inde).text();
//		} else {
//			return "";
//		}
//
//	}

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

	private static Logger Log = Logger.getLogger(Asnconvertor.class.getName());//

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
	
	///////////////////////////////////////////

	private static Session connect(String hostname, String username, String password) {

		JSch jSch = new JSch();

		try {

			session = jSch.getSession(username, hostname, 22);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
			session.setPassword(password);

			System.out.println("Connecting SSH to " + hostname + " - Please wait for few seconds... ");
			write_in_exiting_file_without_loosing_old_data("Connecting SSH to " +
			hostname + " - Please wait for few seconds... ",curr_log_file_path);
			session.connect();
			System.out.println("Successfully Connected to the Host " + hostname + "  !!!");
			write_in_exiting_file_without_loosing_old_data("Successfully Connected to the Host " + hostname + " !!!",curr_log_file_path);
		} catch (Exception e) {
			System.out.println("An error occurred while connecting to " + hostname + ": " + e);
			write_in_exiting_file_without_loosing_old_data("An error occurred while connecting to "+hostname+": "+e,curr_log_file_path);
		}

		return session;

	}

	// *****Function to get session
	private static Session getSession(String hostname, String username, String password) {
		if (session == null || !session.isConnected()) {
			session = connect(hostname, username, password);
		}
		return session;
	}

	// **Function to get Channel
	private static Channel getChannel(String hostname, String username, String password, String Channel_type) {
		if (channel == null || !channel.isConnected()) {
			try {
				channel = (ChannelShell) getSession(hostname, username, password).openChannel(Channel_type);
				channel.connect();

			} catch (Exception e) {
				System.out.println("Error while opening channel: " + e);
				write_in_exiting_file_without_loosing_old_data("Error while opening channel:"+ e,curr_log_file_path);
			}
		}
		return channel;
	}

	// *****Function to Execute Commands in the Unix-box
	private static void executeCommands(List<String> commands, String hostname, String username, String password) {

		try {
			Channel channel = getChannel(hostname, username, password, "shell");
			System.out.println("Sending commands...");

			sendCommands(channel, commands);

			readChannelOutput(channel);
			System.out.println("Finished sending commands!");

		} catch (Exception e) {
			System.out.println("An error ocurred during executeCommands: " + e);

		}
	}

	// **** Capture the Send-commands and fire
	private static void sendCommands(Channel channel, List<String> commands) {

		try {
			PrintStream out = new PrintStream(channel.getOutputStream());

			out.println("#!/bin/bash");
			for (String command : commands) {
					
				out.println(command);
			}
			out.println("exit");

			out.flush();
		} catch (Exception e) {
			System.out.println("Error while sending commands: " + e);
			write_in_exiting_file_without_loosing_old_data("Error while sending commands: " + e, curr_log_file_path);
		}

	}

	// ***** Function to Read the Channel Out-put and capture the same
	private static void readChannelOutput(Channel channel) {

		byte[] buffer = new byte[1024];

		try {
			InputStream in = channel.getInputStream();
			String line = "";
			global_Final_CDR_path = "";
			while (true) {
				while (in.available() > 0) {
					int i = in.read(buffer, 0, 1024);
					if (i < 0) {
						break;
					}
					line = new String(buffer, 0, i);
					System.out.println(line);
					write_in_exiting_file_without_loosing_old_data(line, curr_log_file_path);
					
					global_Final_CDR_path = global_Final_CDR_path + line + "\n";
				}

				if (line.contains("logout")) {
					break;
				}

				if (channel.isClosed()) {
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {
				}
			}
		} catch (Exception e) {
			System.out.println("Error while reading channel output: " + e);
			write_in_exiting_file_without_loosing_old_data("Error while reading channel output: " + e,
					curr_log_file_path);
		}

	}

	// ***** Function to close the Channel and Session
	public static void close() throws InterruptedException {
		
		channel.disconnect();
		session.disconnect();
		Thread.sleep(2000);
		System.out.println("Disconnected channel and session");
		write_in_exiting_file_without_loosing_old_data("Disconnected channel and session", curr_log_file_path);
	}

	// Function to create a text file in the specified directory with the current
	// time-stamp as file name
	public static void create_new_txt_file_in_specified_dir(String curr_log_file_path) {

		try {
			File file = new File(curr_log_file_path);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Function to write in the existing text file with out loosing old data
	public static void write_in_exiting_file_without_loosing_old_data(String content_to_write,
			String curr_log_file_path) {

		try {

			File file = new File(curr_log_file_path);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content_to_write);
			bw.newLine();
			bw.close();

			// System.out.println("Done");
			// "/users/mkyong/filename.txt"

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Function to generate Random digits based on the current time stamp
	public static String Random_Number_With_Required_Digits(int digits_Req) {

		String Rndm_Number;
		String Master_14_Digit_Number;
		Master_14_Digit_Number = "";
		Rndm_Number = "";

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMdd-HH:mm");
		LocalDateTime now = LocalDateTime.now();
		Master_14_Digit_Number = (dtf.format(now).toString().replaceAll("/", "")).replaceAll(" ", "").replaceAll(":",
				"");
		System.out.println((Master_14_Digit_Number.substring(Master_14_Digit_Number.length() - digits_Req + 1,
				Master_14_Digit_Number.length())));
		Rndm_Number = (Master_14_Digit_Number.substring(Master_14_Digit_Number.length() - digits_Req,
				Master_14_Digit_Number.length()));
		System.out.println("Final Rndm_Number:-  " + Rndm_Number);
		return Rndm_Number;
	}
	
	//Function to read a file for cdr name
	public static String filename(String filepath) throws IOException {
		InputStream is = new FileInputStream(filepath);
		BufferedReader buf = new BufferedReader(new InputStreamReader(is));
		String line = buf.readLine();
		StringBuilder sb = new StringBuilder();
		while (line != null) {
			sb.append(line);
			line = buf.readLine();
		}
		String fileAsString = sb.toString().replace(" ", "");

		buf.close();
		return fileAsString;

	}
	//Function to create a date format in linux command
	public static String Present_date() {

		String datetoday;
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMdd-HH:");
		LocalDateTime now = LocalDateTime.now();
		datetoday = (dtf.format(now).toString().replaceAll("/", "")).replaceAll(" ", "").replaceAll(":", "");
		String finaldate=datetoday.substring(0, datetoday.length()-1);	
		return finaldate;
	}	
	public static String Present_dateccn() {

		String datetodayccn;
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDateTime now = LocalDateTime.now();
		datetodayccn = (dtf.format(now).toString().replaceAll("/", "")).replaceAll(" ", "").replaceAll(":", "");
		//String finaldateocc=datetoday.substring(0, datetoday.length()-1);	
	
		return datetodayccn;
	}
	public static String timeoffour() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, -4);
		java.util.Date date = calendar.getTime();
		//System.out.println(date);
		String date1=date.toString();
		//System.out.println(date1);
		String finaldate=date1.substring(11,date1.length()-13).replace(":", "");
		//System.out.println(finaldate);
		
		
		return finaldate;
		
	}
	
	//Function to extract files 
	public static void file_extraction(String gzfilepath, String finalpath) {
		File folder = new File(gzfilepath);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {

			if (listOfFiles[i].isFile()) {
				//System.out.println("File " + listOfFiles[i].getName());

				gzfile = gzfilepath + listOfFiles[i].getName();
				

				//System.out.println("Zip file of OCC is " + gzfile);

				String bername = listOfFiles[i].getName();
				String bername1 = (bername.substring(0, bername.length() - 3));
				berfile = finalpath + bername1;

				//System.out.println("Extracted ber file is " + berfile);

				gunzipIt(gzfile, berfile);
				
			} else if (listOfFiles[i].isDirectory()) {
				System.out.println("Directory " + listOfFiles[i].getName());

			}

		}

	}

	//Function to Unzip files
	public static void gunzipIt(String INPUT_GZIP_FILE1, String OUTPUT_FILE2) {

		byte[] buffer = new byte[1024];
		try {
			GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(INPUT_GZIP_FILE1));

			FileOutputStream out = new FileOutputStream(OUTPUT_FILE2);
			int len;
			while ((len = gzis.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			gzis.close();
			out.close();
			System.out.println("Extraction of OCC zip file completed");

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	//Function to delete files
	public static void file_deletion(String gzfilepath) {
		File folder = new File(gzfilepath);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {

			if (listOfFiles[i].isFile()) {
				//System.out.println("File " + listOfFiles[i].getName());
				

		 
			} else if (listOfFiles[i].isDirectory()) {
				//System.out.println("Directory " + listOfFiles[i].getName());
				String pathofDir= listOfFiles[i].getPath();
				//System.out.println(pathofDir);
				 File folderin =new File(pathofDir);
				 File[] list = folderin.listFiles();
				 for (int j = 0; j < list.length; j++) {

						if (list[j].isFile()) {
							//System.out.println("File " + list[j].getName());
							
						
								File currentFile = new File(list[j].getPath());
								currentFile.delete();
								//System.out.println("Existing file deleted");
							

					 
						} else if (listOfFiles[j].isDirectory()) {
							//System.out.println("Directory " + listOfFiles[j].getName());

			}else {
				//System.out.println("No Files or Folder's Found");
				
			}

				 }
			}
		}
	}
	public static String CSVparse(String Filepath, String viewpath, String MSISDN) {
		String csvtb = null;
		try {
			
			Random rand = new Random(); 
			//Filepath ="D:\\DU Automation\\ASNConverter\\CDR\\CIS\\EDRfile.csv";
			// MSISDN ="971520001714";
			String ClearData="Delete from public.EDR_CIS_DataSamp;";
			//tableCreation(sql);
			String loadCSV= "COPY public.EDR_CIS_DataSamp  (Transaction_Time  ,Client_Transaction_Id  ,Transaction_Id  ,IP_Address ," + 
					"Event_Type  ,A_Party_Msisdn  ,B_Party_Msisdn  ,input  ,Result_Code  ,Result_Description ," + 
					"Service_Class  ,Requested_Product_ID  ,Product_Name  ,Product_Type  ,Product_Cost  ,Applied_product_cost," + 
					"Product_Validity  ,Access_Channel  ,Access_Code  ,Charge_Indicator  ,Vat_Fee  ,Language_Id  ," + 
					"Iname  ,Circle_Code  ,Pay_Source  ,Send_sms  ,Skip_charging  ,Bill_Cycle_ID  ," + 
					"User_ID  ,Origin_Host  ,Faf_Indicator  ,Faf_MSISDN  ,Offer_ID  ,New_Imei  ,Old_Imei  ," + 
					"Dealer_ID  ,Transfer_Remark  ,DrCr  ,Subscription_Date  ,Expiry_Date  ,Last_Renewal_Date ," + 
					"Grace_Expiry_Date  ,Status  ,Subscription_Mode  ,Network_Status  ,Last_Status  ,Status_Change_time," + 
					"Command_Count  ,Charging_Session_Id  ,Notification_Message  ,Commission_Fee  ,Transfer_Fee ,GL_Code," + 
					"State  ,Subscriber_Type  ,OpParam1  ,OpParam2  ,OpParam3  ,OpParam4  ,OpParam5  ,OpParam6 ," + 
					"OpParam7  ,OpParam8  ,OpParam9  ,OpParam10  ,OpParam11  ,OpParam12  ,TDF_Event_Class  ," + 
					"TDF_Event_Name  ,TDF_Voucher_Type  ,TDF_Periodic_Charge  ,TDF_Usage  ,External_Data1  ,External_Data2  ," + 
					"External_Data3  ,External_Data4  ,Callback,ParentProductSPInfo) FROM '"+Filepath+"' DELIMITER '|' CSV HEADER";
			
			
			//FileOperation();
			ExecuteQuery(ClearData);
			ExecuteQuery(loadCSV);
			int num = rand.nextInt(10000000);
			String Validation_Query ="CREATE VIEW public.CIS_EDR_Validation_"+num+" AS Select Transaction_Time,Product_Name, Event_Type,Access_Channel,Result_Description,Result_Code,Offer_ID,Service_Class,input,Requested_Product_ID," + 
					"Expiry_Date,Subscription_Mode,A_Party_Msisdn, Product_Validity, Vat_Fee, Iname,Network_Status FROM public.edr_cis_datasamp where A_Party_Msisdn='" +MSISDN +"'";
			ExecuteQuery(Validation_Query);
			String getValidationData ="Select Transaction_Time,Product_Name, Event_Type,Access_Channel,Result_Description,Result_Code,Offer_ID,Service_Class,input,Requested_Product_ID," + 
					"Expiry_Date,Subscription_Mode,A_Party_Msisdn, Product_Validity, Vat_Fee, Iname,Network_Status FROM public.edr_cis_datasamp where A_Party_Msisdn='" +MSISDN +"'";
		 csvtb=ValidationQuery(getValidationData);
		 
		 // To export the required data to csv file
		 
		 String Export_Data ="COPY (select * from public.CIS_EDR_Validation_"+num+") TO '"+viewpath+"' DELIMITER '|' CSV HEADER" ;
		 ExecuteQuery(Export_Data);
		 return csvtb;
				 
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return csvtb;
	}

	public static String displayActorAdhoc(ResultSet rs) throws SQLException {
		String tbl ="<br><table>" + "<caption align='left'>Adhoc Table:</caption>" + "<tr>" + "<th>msisdn</th>" + "<th>product_id</th>" + "<th>status</th>"
				+ "<th>start_date</th>" + "<th>expiry_date</th>" + "<th>product_cost</th>" + "<th>srcchannel</th>"
				+ "<th>network_status</th>" + "</tr>";
		while (rs.next()) {
			tbl = tbl + "<tr><td style= 'min-width: 162px'>"
					+ (rs.getString("msisdn") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("product_id") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("status") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("start_date") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("expiry_date") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("product_cost") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("srcchannel") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("network_status"))
					+ "</td></tr style= 'min-width: 162px'>";

		}
		// System.out.println(tbl);
		return tbl;

	}
	
	public static String displayActorRenew(ResultSet rs) throws SQLException {
		String tbl = "<table>" +"<caption align='left'>Renewal Table:</caption>" +  "<tr>" + "<th>msisdn</th>" + "<th>last_renewal_date</th>" + "<th>renewal_date</th>"
				+ "<th>status</th>" + "<th>activation_date</th>" + "<th>product_id</th>"
				+ "<th>product_description</th>" + "<th>product_type</th>" + "<th>srcchannel</th>"
				+ "<th>product_category</th>" + "<th>product_purchase_type</th>" + "<th>language_id</th>"
				+ "<th>network_status</th>" + "</tr>";
		while (rs.next()) {
			tbl = tbl + "<tr><td style= 'min-width: 162px'>"
					+ (rs.getString("msisdn") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("last_renewal_date") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("renewal_date") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("status") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("activation_date") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("product_id") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("product_description") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("product_type") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("srcchannel") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("product_category") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("product_purchase_type") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("language_id") + "\t" + "</td>" + "<td style= 'min-width: 162px'>"
							+ rs.getString("network_status"))
					+ "</td></tr style= 'min-width: 162px'>";

		}
		// System.out.println(tbl);
		return tbl;

	}
	
	public static void ExecuteQuery(String sql) throws SQLException, ClassNotFoundException  {
		   
		  // Connection conn = null;
		   
	       Statement stmt = null;
		
		      String dbURL = "jdbc:postgresql://localhost:5432/DU";
		      Properties parameters = new Properties();
		      parameters.put("user", "postgres");
		      parameters.put("password", "maveric");
		      java.sql.Connection con = DriverManager.getConnection(dbURL, parameters);
		      
		     // conn.setAutoCommit(false);
		   //   System.out.println("Opened database successfully");
		      
		 System.out.println("Opened database successfully");

        stmt = con.createStatement();
        stmt.executeUpdate(sql);
		 stmt.close();
		 con.close();
	       System.out.println("Table created successfully");  
	}   
	
	public static String ValidationQuery(String Validatin_Query) throws SQLException {
		 String dbURL = "jdbc:postgresql://localhost:5432/DU";
	      Properties parameters = new Properties();
	      parameters.put("user", "postgres");
	      parameters.put("password", "maveric");
	     
	     
     java.sql.Connection con3 = DriverManager.getConnection(dbURL, parameters);
            Statement st = con3.createStatement();
    		ResultSet rs3 = st.executeQuery(Validatin_Query);
    	   		 
    		 System.out.println("Query Executed");
    		// display actor information
            String csvtbl= displayActor(rs3);

            st.close();
   		 con3.close();
   		 return csvtbl;
    
}
	public static String displayActor(ResultSet rs3) throws SQLException {
		 String tbl = "<table>"
		 		+ "<tr>"
		 		+ "<th style= 'min-width: 162px'>Transaction_Time</th>"
		 		+ "<th style= 'min-width: 162px'>A_Party_Msisdn</th>"
		 		+ "<th style= 'min-width: 162px'>Product_Name</th>"
		 		+ "<th style= 'min-width: 162px'>Event_Type</th>"
		 		+ "<th style= 'min-width: 162px'>Access_Channel</th>"
		 		+ "<th style= 'min-width: 162px'>Result_Description</th>"
		 		+ "<th style= 'min-width: 162px'>Result_Code</th>"
		 		+ "<th style= 'min-width: 162px'>Offer_ID</th>"
		 		+ "<th style= 'min-width: 162px'>Service_Class</th>"
		 		+ "<th style= 'min-width: 162px'>input</th>"
		 		+ "<th style= 'min-width: 162px'>Requested_Product_ID</th>"
		 		+ "<th style= 'min-width: 162px'>Expiry_Date</th>"
		 		+ "<th style= 'min-width: 162px'>Subscription_Mode</th>"
		 		+ "<th style= 'min-width: 162px'>Product_Validity</th>"
		 		+ "<th style= 'min-width: 162px'>Vat_Fee</th>"
		 		+ "<th style= 'min-width: 162px'>Iname</th>"
		 		+ "<th style= 'min-width: 162px'>Network_Status</th>"
		 				 		
		 		+ "</tr>";
        while (rs3.next()) {
        	tbl = tbl + "<tr><td style= 'min-width: 162px'>" +(rs3.getString("Transaction_Time") + "\t" +"</td>"+"<td style= 'min-width: 162px'>"
                    + rs3.getString("A_Party_Msisdn") + "\t" +"</td>"+"<td style= 'min-width: 162px'>"
                    + rs3.getString("Product_Name") + "\t" +"</td>"+"<td style= 'min-width: 162px'>"
                    + rs3.getString("Event_Type") + "\t" +"</td>"+"<td style= 'min-width: 162px'>"
                    + rs3.getString("Access_Channel") + "\t" +"</td>"+"<td style= 'min-width: 162px'>"
                    + rs3.getString("Result_Description") + "\t" +"</td>"+"<td style= 'min-width: 162px'>"
                    + rs3.getString("Result_Code") + "\t" +"</td>"+"<td style= 'min-width: 162px'>"
                    + rs3.getString("Offer_ID") + "\t" +"</td>"+"<td style= 'min-width: 162px'>"
                    + rs3.getString("Service_Class") + "\t" +"</td>"+"<td style= 'min-width: 162px'>"
                    + rs3.getString("input") + "\t" +"</td>"+"<td style= 'min-width: 162px'>"
                    + rs3.getString("Requested_Product_ID") + "\t" +"</td>"+"<td style= 'min-width: 162px'>"
                    + rs3.getString("Expiry_Date") + "\t" +"</td>"+"<td style= 'min-width: 162px'>"
                    + rs3.getString("Subscription_Mode") + "\t" +"</td>"+"<td style= 'min-width: 162px'>"
                    + rs3.getString("Product_Validity") + "\t" +"</td>"+"<td style= 'min-width: 162px'>"
                    + rs3.getString("Vat_Fee") + "\t" +"</td>"+"<td style= 'min-width: 162px'>"
                    + rs3.getString("Iname") + "\t" +"</td>"+"<td style= 'min-width: 162px'>"
                  	+ rs3.getString("Network_Status"))+ "</td></tr>"; 
        }
        return tbl;
    }
	
	public static String parsedata(String filetype,String filepath ,String param1,String MSISDN) {
		String value="" ;
	try {

		if(filetype.equalsIgnoreCase("OCC")||filetype.equalsIgnoreCase("CCN")) {
			nodetag="onlineCreditControlRecord";
			idtag="subscriptionIDValue";
		}else {
			nodetag="refillRecordV2";
			idtag="subscriberNumber";
		}

		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		org.w3c.dom.Document docc = docBuilder.parse(filepath);
		docc.getDocumentElement().normalize();

		NodeList data = docc.getElementsByTagName(nodetag);

		int totaldata = data.getLength();

		for (int temp = 0; temp < totaldata; temp++) {
            Node nNode = data.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                String[] parmsplt = param1.split("\\[");
        		String param = parmsplt[0];
        		int inde = 0;
        		if (parmsplt.length == 1) {
        			inde = 0;
        		} else {
        			String[] paramsplit2 = parmsplt[1].split("\\]");
        			inde = Integer.parseInt(paramsplit2[0]);
        		}


                String sub=eElement.getElementsByTagName(idtag).item(0).getTextContent();
                if (sub.contentEquals(MSISDN)){
                value=(eElement.getElementsByTagName(param).item(inde).getTextContent());
                info(param + " = " +value);
                }



            }

		}

	} catch (SAXParseException err) {
		System.out.println("** Parsing error" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
		System.out.println(" " + err.getMessage());

	} catch (SAXException e) {
		Exception x = e.getException();
		((x == null) ? e : x).printStackTrace();

	} catch (Throwable t) {
		t.printStackTrace();
	}

	return value;

}

	public static String Present_datecis() {

		String datetoday;

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_.");
		LocalDateTime now = LocalDateTime.now();
		datetoday = (dtf.format(now).toString().replaceAll("/", "")).replaceAll(" ", "").replaceAll(":", "");
		String finaldate=datetoday.substring(0, datetoday.length()-1);	

		return finaldate;
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
	// End


	////////////////////////////////////////////////////////

}
