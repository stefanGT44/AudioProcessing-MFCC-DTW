package app.processing;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import app.view.View;


public class AudioFile implements Serializable{
	
	private String name;
	private ArrayList<ArrayList<Double>> vectors;
	
	public AudioFile(String name) {
		this.name = name;
	}
	
	public void analyze(){
		FileAnalyzer analyzer = new FileAnalyzer("AudioFiles/"+name+".wav");
		vectors = analyzer.calculateMFCC(Integer.parseInt(View.get().getParamTB().getWindowWidth().getText()), 
				Integer.parseInt(View.get().getParamTB().getCoefNum().getText()),
				(String)View.get().getParamTB().getWinFun().getSelectionModel().getSelectedItem());
	}
	
	public void saveData(){
		try {
			FileOutputStream fileOut = new FileOutputStream("AudioFileData/"+name+".ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this);
			out.close();
			fileOut.close();
			System.out.println("Successfully saved!");
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<ArrayList<Double>> getVectors() {
		return vectors;
	}
	
	public void readLastVectorForTesting(){
		ArrayList<Double> vec = vectors.get(vectors.size()-1);
		System.out.println("Last vector coefs: ");
		for (Double d:vec){
			System.out.println(d);
		}
	}
	
	@Override
	public String toString() {
		return this.name;
	}

}
