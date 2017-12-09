package com.vogella.maven.quickstart;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.ArrayList;

import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.TensorFlow;

public class TensorFlowModel {
	
	private ArrayList<Float> history = new ArrayList<Float>();
	private String imgDir;
	private String modelDir;
	private File folder;
	private SavedModelBundle smb;
	private Session s;
	
	public TensorFlowModel(String imgDir, String modelDir) {
		this.imgDir = imgDir;
		this.modelDir = modelDir;
		this.folder = new File(imgDir);
		this.smb = SavedModelBundle.load(this.modelDir,"serve");
		this.s = smb.session();
	}
	
	public float evaluate(String fileName) {
		System.out.println("TensorFlowExample using TensorFlow version: " +  TensorFlow.version());
		float answer = 0;
		String imageFile = "";	
		FloatBuffer fb = FloatBuffer.allocate(23000);
		File file = new File(imgDir+fileName);
		if(file.isFile()){
            imageFile=imgDir+file.getName();
            System.out.println("Running TensorFlow model on "+imageFile);
        }
		else return 0;
        byte[] imageBytes = readAllBytesOrExit(Paths.get(imageFile));
        for(byte b:imageBytes){
            fb.put((float)(b&0XFF)/255.0f);
        }
        fb.rewind();

        float[] keep_prob_arr = new float[1024];
        Arrays.fill(keep_prob_arr,1f);

        Tensor inputTensor = Tensor.create(new long[] {23000},fb);
        Tensor keep_prob = Tensor.create(new long[]{1,1024},FloatBuffer.wrap(keep_prob_arr));
        Tensor result = s.runner()
                .feed("input_tensor",inputTensor)
                .feed("keep_prob",keep_prob)
                .fetch("output_tensor")
                .run().get(0);

        float[][] m = new float[1][1];
        float latestValue=0;
        float[][] matrix = (float[][])result.copyTo(m);
        for(float val:matrix[0]){
          latestValue=val;
        }
        if(history.size()>=4) {
        	history.remove(0);
        	history.add(latestValue);
        }
        else {
        	history.add(latestValue);
        }	
        answer = sumArrayList(history)/history.size();
		return answer;
	}
	
	private float sumArrayList(ArrayList<Float> l) {
		float sum = 0;
		for(Float f:l) {
			sum+=f;
		}
		return sum;
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