package com.rest.imageProcessing;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.ws.rs.core.Response;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class ImageProcessing 
{
	public Response getAnImage(String tesseractPath, String imagePath)
	{
		ITesseract instance = new Tesseract();
		instance.setDatapath(tesseractPath);
		instance.setLanguage("eng");
		Map<String, String> imageData = getTextFromImage(instance, imagePath);
		String accFromImage = (String)imageData.get("accNo").trim();
		String chqNoFromImage = (String)imageData.get("chqNo").trim();
		String pathForGreyImage ="C://Users//priyanka.j.patel//Pictures//Online Cheque Submission//chequeInGrey.jpg";
		String pathForFrontCheck = "C://Users//priyanka.j.patel//Pictures//Online Cheque Submission//BWImageFrontCheck.jpg";
		String pathForBackCheck = "C://Users//priyanka.j.patel//Pictures//Online Cheque Submission//BWImageBackCheck.jpg";
		if (getSignature(imagePath) && getTextFromImage(instance, imagePath).size() > 0) 
		{
			boolean conertedToGreyScale = conertToGreyScale(imagePath);
			boolean convertedToBWImageFrontCheck = convertToBWImage(imagePath, pathForFrontCheck);
			boolean convertedToBWImageBackCheck = convertToBWImage(imagePath, pathForBackCheck);
			if (conertedToGreyScale && convertedToBWImageBackCheck && convertedToBWImageFrontCheck) {
				System.out.println("All the process completed successfully");
				System.out.println("Connecting to Database for further operations.");
				CreateTable createTable = new CreateTable();
				createTable.createTable();
				DBUpdates dbUpdates = new DBUpdates();
				if(dbUpdates.validateUser(accFromImage)){
					System.out.println("User is valid.");
					if (dbUpdates.updateChqNo(accFromImage, chqNoFromImage)) {
						System.out.println("Cheque Number is inserted in DB.");
					}
					else{
						System.out.println("Can not insert chequeNumber: " + chqNoFromImage +" For the accountNumner: " + accFromImage);
					}
					dbUpdates.saveImageInDB("bwFrontImage",accFromImage , pathForFrontCheck);
					dbUpdates.saveImageInDB("bwBackImage",accFromImage , pathForBackCheck);
					dbUpdates.saveImageInDB("grayImage",accFromImage , pathForGreyImage);
				}
				else{
					System.out.println("Invalid User. User Information is not available in DB for the accountNumner: " + accFromImage);
				}
				return Response.status(200).build();
			}
			else{
				System.out.println("Response not modified as something went wrong while conversion of image.");
				return Response.notModified().build();
			}
		}
		else{
			System.out.println("Response not modified as something went wrong while getting text or signature from image.");
			return Response.notModified().build();
		}
		//return Response.status(200).build();
	}

	private Map<String, String>  getTextFromImage(ITesseract instance, String imageLocation)
	{     
		System.out.println("Getting Text from Image");
		Map<String, String> dataMap = null;
		try{
		//To get all the text : imgText =  instance.doOCR(new File(imageLocation));
			String accNo, chqNo, signature ;
		/*accNo = instance.doOCR(new File(imageLocation));
        chqNo = instance.doOCR(new File(imageLocation));
        signature = instance.doOCR(new File(imageLocation));*/
        accNo =  instance.doOCR(new File(imageLocation), new java.awt.Rectangle(150, 180, 320, 180)).trim();
        chqNo =  instance.doOCR(new File(imageLocation), new java.awt.Rectangle(240, 400, 800, 60)).trim();
        chqNo = chqNo.replaceAll("\"", "");
        signature =  instance.doOCR(new File(imageLocation), new java.awt.Rectangle(620, 260, 300, 120));
        dataMap = new HashMap<>();
        System.out.println("Payer's Details: 1) Account Number : " + accNo + "\n" + "2) Cheque Number : " + chqNo + " \n" + "signature :" + signature  );
        dataMap.put("accNo", accNo);
        dataMap.put("chqNo", chqNo);
        dataMap.put("signature", signature);
        System.out.println("Payer's information is saved in Map");
		getSignature(imageLocation);
		}
		 catch (TesseractException e) 
	      {
			 System.out.println("Exception occoured while reading the text from image");
	         e.getMessage();
	      }
		return dataMap;
	}
	 //This method captures a part of image
		public boolean getSignature(String imageLocation) {
			BufferedImage image;
			String SigImage = "C:\\Users\\priyanka.j.patel\\Pictures\\Online Cheque Submission\\sign1.jpg";
			try {
				System.out.println("Capturing signature from Image");
				image = ImageIO.read(new File(imageLocation));
				// The above line throws an checked IOException which must be caught.
				System.out.println("Capturing signature from image");
				BufferedImage sign = image.getSubimage(680, 300, 280, 70);
				ImageIO.write(sign, "jpg", new File(SigImage));
				System.out.println("Signature captured and saved at:" + SigImage);
				return true;
			} catch (IOException e) {
				System.out.println("Exception occured while capturing signature from image. Error messgae is : " + e.getMessage());
				return false;
			}
		}
		//convert image to grey scale
		public boolean conertToGreyScale(String imageLocation)
		{
			BufferedImage image = null;
			File file = null;
			// read image
			try {
				System.out.println("Converting to Grey Scale Image");
				file = new File(imageLocation);
				image = ImageIO.read(file);
				// get image's width and height
				int width = image.getWidth();
				int height = image.getHeight();
				// convert to greyscale
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						// Here (x,y)denotes the coordinate of image
						// for modifying the pixel value.
						int p = image.getRGB(x, y);

						int a = (p >> 24) & 0xff;
						int r = (p >> 16) & 0xff;
						int g = (p >> 8) & 0xff;
						int b = p & 0xff;

						// calculate average
						int avg = (r + g + b) / 3;

						// replace RGB value with avg
						p = (a << 24) | (avg << 16) | (avg << 8) | avg;

						image.setRGB(x, y, p);
					}
				}
				// write image
				file = new File("C://Users//priyanka.j.patel//Pictures//Online Cheque Submission//chequeInGrey.jpg");
				System.out.println("Grey Scale image is stored as " + file.getName());
				ImageIO.write(image, "jpg", file);
				return true;
			} catch (IOException e) {
				System.out.println("Exception ocuured while conerting to grey scale image. Error message is : " +e.getMessage());
				return false;
			}
		}
		
	public boolean convertToBWImage(String imageLocation, String fileLocation)
	{
		try {
			System.out.println("Converting to black & white Image.");
			BufferedImage orginalImage = ImageIO.read(new File((imageLocation)));
			BufferedImage blackAndWhiteImg = new BufferedImage(orginalImage.getWidth(), orginalImage.getHeight(),
					BufferedImage.TYPE_BYTE_BINARY);
			Graphics2D graphics = blackAndWhiteImg.createGraphics();
			graphics.drawImage(orginalImage, 0, 0, null);
			ImageIO.write(blackAndWhiteImg, "png", new File(fileLocation));
			return true;
		}
		catch (IOException e) {
			System.out.println(
					"Exception ocuured while conerting to black & whilte image. Error message is : " + e.getMessage());
			return false;
		}
	}
}
