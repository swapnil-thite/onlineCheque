package com.rest.imageProcessing;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUpdates {
	// JDBC driver name and database URL 
	   static final String JDBC_DRIVER = "org.h2.Driver";   
	   static final String DB_URL = "jdbc:h2:~/test";  
	   //  Database credentials 
	   static final String USER = "piyoosh.arvind"; 
	   static final String PASS = "piyoosh.arvind"; 
	   
	   public boolean validateUser(String accountNumber){
		   Connection conn = null; 
		   Statement stmt = null;
		   boolean isValidAccNo = false;
		   try { 
		         Class.forName(JDBC_DRIVER); 
		         System.out.println("Connecting to database for validating user..."); 
		         conn = DriverManager.getConnection(DB_URL,USER,PASS);
		         stmt = conn.createStatement();
		         String fetchAccNo = "SELECT * FROM BankPrimary WHERE payerAccNo="+accountNumber;
		         ResultSet rs = stmt.executeQuery(fetchAccNo);
		         //STEP 5: Extract data from result set
		         while(rs.next()){
		        	if (rs.getString(1).equalsIgnoreCase(accountNumber)) {
		        		isValidAccNo = true;
		        		break;
					}
		        	else{
		        		System.out.println(rs.getString(1));
		        		System.out.println(accountNumber);
		        	}
		         }
		         rs.close();
		         stmt.close(); 
		         conn.close();
		      } catch(SQLException se) {
					System.out.println("Some SQL Exception occured. Message: " + se.getMessage());
		      } catch(Exception e) { 
					System.out.println("Some Exception occured. Message: " + e.getMessage());
		      } finally { 
		         try{ 
		            if(stmt!=null) stmt.close(); 
		         } catch(SQLException se2) { 
					System.out.println("Some Exception occured in finally block. Message: " + se2.getMessage());
		         } 
		         try { 
		            if(conn!=null) conn.close(); 
		         } catch(SQLException se){ 
						System.out.println("Some Exception occured in finally block. Message: " + se.getMessage());
		         }
		      }
		return isValidAccNo;
	   }
	   //call below method to insert a new record from JAVA to DB
	   public boolean insert(String accountNumber, String chequeNumber){
		   Connection conn = null; 
		   Statement stmt = null;
		   boolean isInserted = false;
		   try { 
		         Class.forName(JDBC_DRIVER); 
		         System.out.println("Connecting to database for Inserting a new record..."); 
		         conn = DriverManager.getConnection(DB_URL,USER,PASS);
		         String sqlInsert = "INSERT INTO BankPrimary "
							+ " VALUES(1,'Priyanka Patel','4143330566422122','Piyoosh Arvind','1222466503333414','12451456957815264258','120000','9priyu@gmail.com','7586958425','piy.piyoosh@gmail.com','9674859652','Approved',NULL,NULL,NULL,NULL,NULL,NULL,false)";
				stmt.executeUpdate(sqlInsert);
		        // stmt.setString (1, accountNumber);
		         int numOfRowModify = stmt.executeUpdate(sqlInsert);
		         if (numOfRowModify > 0) {
					isInserted = true;
				} 
		         stmt.close(); 
		         conn.close(); 
		      } catch(SQLException se) {
					System.out.println("Some SQL Exception occured. Message: " + se.getMessage());
		      } catch(Exception e) { 
					System.out.println("Some Exception occured. Message: " + e.getMessage());
		      } 
		   	finally { 
		         try{ 
		            if(stmt!=null) stmt.close(); 
		         } catch(SQLException se2) { 
					System.out.println("Some Exception occured in finally block. Message: " + se2.getMessage());
		         } 
		         try { 
		            if(conn!=null) conn.close(); 
		         } catch(SQLException se){ 
						System.out.println("Some Exception occured in finally block. Message: " + se.getMessage());
		         }
		      }
		return isInserted;
	   }
	   //Below method will add the cheque number in DB for the account number captured from image
	   public boolean updateChqNo(String chequeNumber, String accountNumber){
		   Connection conn = null; 
		   Statement stmt = null;
		   boolean isUpdated = false;
		   try { 
		         Class.forName(JDBC_DRIVER); 
		         System.out.println("Connecting to database for adding cheque number..."); 
		         conn = DriverManager.getConnection(DB_URL,USER,PASS);
		         String query = "update BankPrimary set chequeNumber = ? where payerAccNo = ?";
		         PreparedStatement preparedStmt = conn.prepareStatement(query);
		         preparedStmt.setString(1, chequeNumber);
		         preparedStmt.setString(2, accountNumber);
		         int numOfRowModify = preparedStmt.executeUpdate();
		         if (numOfRowModify > 0) {
					isUpdated = true;
					System.out.println("ChqueNumber updated successfully in database.");
				} 
		         else{
		        	 System.out.println("chequeNumber is not added/updated in database");
		         }
		         preparedStmt.close(); 
		         conn.close(); 
		      } catch(SQLException se) {
					System.out.println("Some SQL Exception occured. Message: " + se.getMessage());
		      } catch(Exception e) { 
					System.out.println("Some Exception occured. Message: " + e.getMessage());
		      } 
		   	finally { 
		         try{ 
		            if(stmt!=null) stmt.close(); 
		         } catch(SQLException se2) { 
					System.out.println("Some Exception occured in finally block. Message: " + se2.getMessage());
		         } 
		         try { 
		            if(conn!=null) conn.close(); 
		         } catch(SQLException se){ 
						System.out.println("Some Exception occured in finally block. Message: " + se.getMessage());
		         }
		      }
		return isUpdated;
	   }
	   //Below method will save the given image in DB.
	   public boolean saveImageInDB(String coloumnName, String accountNumber, String filePath){
		   boolean isImageAdded = false;
			try{
				Class.forName(JDBC_DRIVER);
				Connection con=DriverManager.getConnection(DB_URL,USER,PASS);
				System.out.println("Saving the image :" + coloumnName +"in DB.");
				File file=new File(filePath);
				FileInputStream fis=new FileInputStream(file);
				PreparedStatement ps=con.prepareStatement("update BankPrimary set " + coloumnName + " = ? where payerAccNo = ?"); 
	           // InputStream inputStream = new FileInputStream(new File(filePath));
	 
	           // ps.setBlob(5, inputStream);
				ps.setBinaryStream(1,(InputStream)fis,(int)file.length());
				ps.setString(2, accountNumber);
				ps.executeUpdate();
				int numOfRowModify = ps.executeUpdate();
		         if (numOfRowModify > 0) {
		        	 isImageAdded = true;
					System.out.println("Image" + coloumnName +" updated successfully in database.");
				} 
		         else{
		        	 System.out.println("Image" + coloumnName +" is not added/updated in database");
		         }
				System.out.println("Image saved in  DB");
				ps.close();
				fis.close();
				con.close();
			}catch(Exception e){
				e.printStackTrace();
			}
			return isImageAdded;
		}
}
