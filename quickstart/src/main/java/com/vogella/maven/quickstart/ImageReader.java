package com.vogella.maven.quickstart;

import java.io.FileOutputStream;
import javax.imageio.ImageIO;

import java.awt.Graphics;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Created by Tze on 8/12/2017.
 */

public class ImageReader {
	private final String imageUrl = "http://10.12.121.62/html/cam_pic.php";
	private String destinationFile;
	private String destinationCroppedFile;
	
//	public static void main(String[] args) {
//		String imgDir = "C:\\Users\\Tze\\Documents\\GitHub\\TourismApp\\TourismAppTwo\\TensorFlow\\canteenImageDir\\";
//        String uncroppedFile = imgDir+"uncropped.jpg";
//        String croppedFile = imgDir+"cropped.jpg";
//        
//        ImageReader imageDownloader = new ImageReader(uncroppedFile,croppedFile);
//        imageDownloader.saveCroppedImage();
//	}
	public ImageReader(String destinationFile,String destinationCroppedFile) {
		this.destinationFile = destinationFile;
		this.destinationCroppedFile = destinationCroppedFile;
	}
	
	public void saveCroppedImage() {
		try {
			saveImage(this.imageUrl,destinationFile);
			File file = new File(destinationFile);
			BufferedImage img = ImageIO.read(file);
			File outputfile = new File(this.destinationCroppedFile);
//			ImageIO.write(img, "jpg", outputfile);
			BufferedImage croppedImage = cropImage(img,170,90,230,100);
//			BufferedImage croppedImage = cropImage(img,0,0,230,100);
			ImageIO.write(croppedImage, "jpg", outputfile);
//			System.out.println("Saving cropped image as "+outputfile);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	    
    public static void saveImage(String imageUrl, String destinationFile) throws IOException{
        URL url = new URL(imageUrl);
        InputStream inputStream = url.openStream();
        OutputStream outputStream = new FileOutputStream(destinationFile);
        System.out.println("Saving uncropped image as "+destinationFile);
        byte[] b = new byte[2048];
        int length;
        while((length=inputStream.read(b))!=-1){
            outputStream.write(b,0,length);
        }
        inputStream.close();
        outputStream.close();
    }
    
    public BufferedImage cropImage(BufferedImage img, int startX, int startY, int width, int height) {
    	BufferedImage tempCopy = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
    	Graphics g = tempCopy.createGraphics();
    	g.drawImage(img, 0, 0, null);
    	tempCopy = tempCopy.getSubimage(startX, startY, width, height);
    	return tempCopy;
    }
}