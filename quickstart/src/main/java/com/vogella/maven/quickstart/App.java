package com.vogella.maven.quickstart;

/**
 * Created by Tze on 6/12/2017.
 */

/**
 * Created by Tze on 6/12/2017.
 */

/* Copyright 2016 The TensorFlow Authors. All Rights Reserved.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisStringCommands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Hello world!
 *
 */
public class App {
/** Sample use of the TensorFlow Java API to label images using a pre-trained model. */

    public static void main(String[] args) {
        String imgDir = "C:\\Users\\Tze\\Documents\\GitHub\\TourismApp\\TourismAppTwo\\TensorFlow\\canteenImageDir\\";
        String modelDir = "C:\\Users\\Tze\\Documents\\GitHub\\TourismApp\\TourismAppTwo\\TensorFlow\\model\\";
        String uncroppedFile = imgDir+"uncropped.jpg";
        String croppedFile = imgDir+"cropped.jpg";
        
        TensorFlowModel cnn = new TensorFlowModel(imgDir,modelDir);
        
        File folder = new File(imgDir);
        File[] listOfFiles = folder.listFiles();
        
        ImageReader imageDownloader = new ImageReader(uncroppedFile,croppedFile);

        //Initialize Redis client instruction uploading
        RedisClient client = RedisClient.create("redis://redistogo:eeb60232febb9e34c9d61b54948de9b1@grouper.redistogo.com:11914");
        StatefulRedisConnection<String, String> connection = client.connect();
        RedisStringCommands sync = connection.sync();
        
        boolean runLoop = true;
        while(runLoop) {
        	imageDownloader.saveCroppedImage();
	        for(File file:listOfFiles){
//	        	if(file.getName().equals("cropped.jpg")) {
	        		float canteenPopulation = cnn.evaluate(file.getName());
	        		int numofPeople = Math.round(canteenPopulation*20);
	                sync.set("stat:Canteen:latest",String.valueOf(numofPeople));
	                sync.set("stat:Canteen:"+String.valueOf(System.currentTimeMillis()),String.valueOf(numofPeople));
	                
	                //Debugging
	                System.out.println("Filename "+file.getName()+":Evaluated number of people is "+numofPeople);
	                System.out.println("Time since epoch is "+System.currentTimeMillis());
//	        	}
	        }
	        try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }

//        Use this if you want to publish notifications to all existing subscribers
//        StatefulRedisConnection<String,String> sender = client.connect();
//        sender.sync().publish("55:quiet","1");
    }

    private static byte[] readAllBytesOrExit(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            System.err.println("Failed to read [" + path + "]: " + e.getMessage());
            System.exit(1);
        }
        return null;
    }
}