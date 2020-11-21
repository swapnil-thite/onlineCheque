package com.rest.imageProcessing;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

public class CreateTable {
	// JDBC driver name and database URL 
	   static final String JDBC_DRIVER = "org.h2.Driver";   
	   static final String DB_URL = "jdbc:h2:~/test";  
	   
	   //  Database credentials 
	   static final String USER = "piyoosh.arvind"; 
	   static final String PASS = "piyoosh.arvind"; 
	   
	   public void createTable()
	   {
		      Connection conn = null; 
		      Statement stmt = null; 
			try {
				// STEP 1: Register JDBC driver
				Class.forName(JDBC_DRIVER);
				// STEP 2: Open a connection
				System.out.println("Connecting to database...");
				conn = DriverManager.getConnection(DB_URL, USER, PASS);
				DatabaseMetaData metadata = conn.getMetaData();
				ResultSet tables = metadata.getTables(null, null, "BankPrimary", null);
				if (tables.next()) {
					System.out.println("Table is already there in Database");
					conn.close();
				} else {
					// Table does not exist
					System.out.println("Creating table in given database...");
					stmt = conn.createStatement();
					System.out.println("Created table in given database...");
					String sql = "CREATE TABLE BankPrimary " + "("
							+ "id INTEGER not NULL, "
							+ " payerName VARCHAR(255), "
							+ " payerAccNo VARCHAR(16), " 
							+ " payeeName VARCHAR(255), " 
							+ " payeeAccNo VARCHAR(16), "
							+ " chequeNumber VARCHAR(20), " 
							+ " amount VARCHAR(255), " 
							+ " payerEmail VARCHAR(255), "
							+ " payerMobile VARCHAR(10), " 
							+ " payeeEmail VARCHAR(255), " 
							+ " payeeMobile VARCHAR(10), "
							+ " status VARCHAR(255), " 
							+ " originalImage longblob, " 
							+ " bwFrontImage longblob, "
							+ " bwBackImage longblob, " 
							+ " grayImage longblob, " 
							+ " sigImage longblob, "
							+ " info VARCHAR(255), " 
							+ " infoFlag BOOLEAN, " 
							+ " PRIMARY KEY ( payerAccNo ))";
					stmt.executeUpdate(sql);
					System.out.println("Created table in given database...");
					System.out.println("Inserting a dummy row.");
					String sqlInsert = "INSERT INTO BankPrimary "
							+ " VALUES(1,'Priyanka Patel','4143330566422122','Piyoosh Arvind','1222466503333414','12451456957815264258','120000','9priyu@gmail.com','7586958425','piy.piyoosh@gmail.com','9674859652','Approved',NULL,NULL,NULL,NULL,NULL,NULL,false)";
					stmt.executeUpdate(sqlInsert);
					System.out.println("Inserted in DemoBank Table..");
					// STEP 4: Clean-up environment
					stmt.close();
					conn.close();
				}
			}
			catch(SQLException se) 
			{ 
				System.out.println("Some SQL Exception occured. Message: " + se.getMessage());
			}
			catch(Exception e) 
			{
				System.out.println("Some Exception occured while creating table. Message: " + e.getMessage());
				e.printStackTrace();
			} 
			finally
			{ 
		         //finally block used to close resources 
		         try{ 
		            if(stmt!=null) stmt.close(); 
		         } catch(SQLException se2) { 
		         } // nothing we can do 
		         try { 
		            if(conn!=null) conn.close(); 
		         } catch(SQLException se){ 
		            se.printStackTrace(); 
		         } 
		      } 
		      System.out.println("Goodbye!");
	   }

	public void verifyImageDataInDB(Map<String, String> imageData) 
	{
	}
	  
}
