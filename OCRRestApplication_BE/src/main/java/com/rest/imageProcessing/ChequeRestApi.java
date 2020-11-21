package com.rest.imageProcessing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.core.header.FormDataContentDisposition;
//import com.sun.jersey.multipart.FormDataParam;


@Path("/")
public class ChequeRestApi {
	@POST
	@Path("/chequeService")
//	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response crunchifyREST(InputStream incomingData) {
		StringBuilder crunchifyBuilder = new StringBuilder();
		String imagePath = "C://Users//priyanka.j.patel//Pictures//Online Cheque Submission//cheque.jpg";
		String tesseractPath = "C://Users//priyanka.j.patel//Downloads//tessdata//tessdata";
		ImageProcessing imageProcessing = new ImageProcessing();
		System.out.println("Data Received: " + crunchifyBuilder.toString());
		return imageProcessing.getAnImage(tesseractPath, imagePath);
		/*try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				crunchifyBuilder.append(line);
			}
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
		}*/
		
 
		// return HTTP response 200 in case of success
		//return imageProcessing.getAnImage(tesseractPath,imagePath);
		//return Response.status(200).entity(crunchifyBuilder.toString()).build();

		//Response response = new Response();
		/*StringBuilder crunchifyBuilder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				crunchifyBuilder.append(line);
			}
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
		}
		System.out.println("Data Received: " + crunchifyBuilder.toString());
		return Response.status(200).entity(crunchifyBuilder.toString()).build();*/
		}
	@GET
	@Path("/verify")
	@Produces(MediaType.TEXT_PLAIN)
	public Response verifyRESTService(InputStream incomingData) {
		//Response response = new Response();
		String result = "OnlineChequeSubmissionProjectREST Successfully started..";
	/*	response.setMessage("Get works fine");
		response.setStatus(true);
		return response;*/
		// return HTTP response 200 in case of success
		return Response.status(200).entity(result).build();
	}
	/*@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadFile(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail) {
		String uploadedFileLocation = "C://Users//priyanka.j.patel//Pictures//Online Cheque Submission//uploaded/" + fileDetail.getFileName();

		// save it
		writeToFile(uploadedInputStream, uploadedFileLocation);

		String output = "File uploaded to : " + uploadedFileLocation;

		return Response.status(200).entity(output).build();

	}*/
	// save uploaded file to new location
		private void writeToFile(InputStream uploadedInputStream,
			String uploadedFileLocation) {

			try {
				OutputStream out = new FileOutputStream(new File(
						uploadedFileLocation));
				int read = 0;
				byte[] bytes = new byte[1024];

				out = new FileOutputStream(new File(uploadedFileLocation));
				while ((read = uploadedInputStream.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
				out.flush();
				out.close();
			} catch (IOException e) {

				e.printStackTrace();
			}

		}

}
