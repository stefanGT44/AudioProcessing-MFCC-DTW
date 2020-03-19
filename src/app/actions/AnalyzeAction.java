package app.actions;

import java.util.ArrayList;

import app.processing.AudioFile;
import app.processing.AudioFileExtended;
import app.view.MyAlert;
import app.view.View;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class AnalyzeAction implements EventHandler<ActionEvent>{

	@Override
	public void handle(ActionEvent event) {
		try {
			int test = Integer.parseInt(View.get().getParamTB().getWindowWidth().getText());
			test = Integer.parseInt(View.get().getParamTB().getCoefNum().getText());
		} catch (Exception e){
			new MyAlert("Invalid parameters", "Parameters must be integers.");
			return;
		}
		
		String path = View.get().getCenterView().getPath().getText();
		if (path == null || path.equals("")){
			new MyAlert("Invalid path", "Please select a valid file.");
			return;
		}
		
		View.get().getParamTB().getPb().setVisible(true);
		Thread thread = new Thread(new Runnable(){
			
			public void run(){
				AudioFileExtended audioFile = new AudioFileExtended("ime");
				audioFile.analyzeTemp(path);
				ObservableList<String> olista = FXCollections.observableArrayList();
				for (int i = 0; i < audioFile.getWords().size(); i++){
					olista.add(DTW(audioFile.getWords().get(i)));
				}
				
				Platform.runLater(new Runnable(){
					
					public void run(){
						View.get().getCenterView().getFoundWords().getItems().clear();
						View.get().getCenterView().getFoundWords().setItems(olista);
						View.get().getParamTB().getPb().setVisible(false);
					}
					
				});
				
			}
			
		});
		
		thread.start();
		
	}
	
	public static String DTW(ArrayList<ArrayList<Double>> vectors){
		String tempWord = "";
		double tempMinValue = Double.MAX_VALUE;
		int wordSize = vectors.size();
		for (AudioFile file:View.get().getAudioFiles()){
			int tempWordSize = file.getVectors().size();
			double mat[][] = new double[tempWordSize + 1][wordSize + 1];
			
			for (int i = 0; i < tempWordSize + 1; i++){
				mat[i][0] = Double.MAX_VALUE;
			}
			
			for (int i = 0; i < wordSize + 1; i++){
				mat[tempWordSize][i] = Double.MAX_VALUE;
			}
			
			int k = 0;
			for (int i = tempWordSize - 1; i >= 0; i--){
				for (int j = 1; j < wordSize + 1; j++){
					
					if (i == tempWordSize - 1 && j == 1){
						mat[i][j] = euclideanDistance(file.getVectors().get(k), vectors.get(j-1));
					}
					else
						mat[i][j] = euclideanDistance(file.getVectors().get(k), vectors.get(j-1)) + 
							min(mat[i+1][j-1], mat[i+1][j], mat[i][j-1]);
					
				}
				k++;
			}
			
			double sum = mat[0][wordSize];
			int i = 0, j = wordSize;
			
			while (i < tempWordSize - 1 && j > 1){
				double a = mat[i+1][j-1];
				double b = mat[i+1][j];
				double c = mat[i][j-1];
				
				double min = min(a, b, c);
				sum += min;
				
				if (min == a){
					i++;
					j--;
				} else if (min == b){
					i++;
				} else{
					j--;
				}
			}
			
			System.out.println("Poredjenje sa "+ file.getName());
			System.out.println("Suma = "+sum);
			
			if (sum < tempMinValue){
				tempMinValue = sum;
				tempWord = file.getName();
			}
			
		}
		
		return tempWord;
	}
	
	public static double euclideanDistance(ArrayList<Double> vec1, ArrayList<Double> vec2){
		double suma = 0;
		for (int i = 0; i < vec1.size(); i++){
			suma += Math.pow(vec1.get(i) - vec2.get(i), 2);
		}
		suma = Math.sqrt(suma);
		return suma;
	}
	
	public static double min(double a, double b, double c){
		double x = Math.min(a, b);
		return Math.min(x, c);
	}

}
