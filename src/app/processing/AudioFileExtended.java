package app.processing;

import java.util.ArrayList;

import app.view.View;

public class AudioFileExtended extends AudioFile{
	
	private ArrayList<ArrayList<ArrayList<Double>>> words;

	public AudioFileExtended(String name) {
		super(name);
		words = new ArrayList<>();
	}
	
	public void analyzeTemp(String path){
		FileAnalyzer analyzer = new FileAnalyzer(path, true);
		for (Word word:analyzer.getWords()){
			analyzer.setStartFrameIndex(word.getStart());
			analyzer.setWordLength(word.getLength());
			words.add(analyzer.calculateMFCC(Integer.parseInt(View.get().getParamTB().getWindowWidth().getText()), 
					Integer.parseInt(View.get().getParamTB().getCoefNum().getText()),
					(String)View.get().getParamTB().getWinFun().getSelectionModel().getSelectedItem()));
		}
	}
	
	public ArrayList<ArrayList<ArrayList<Double>>> getWords() {
		return words;
	}
	
}
