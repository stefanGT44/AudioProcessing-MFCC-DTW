package app.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import app.processing.AudioFile;
import app.processing.Recorder;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class View extends Stage{
	
	public static volatile boolean running;
	private static View instance;
	
	private ArrayList<AudioFile> audioFiles;
	
	//Main Views
	private ParameterToolBar parameterToolBar;
	private AddSoundToolBar addSoundToolBar;
	private DatabaseView databaseView;
	private AnalyzerView analyzerView;
	
	private Recorder recorder;
	
	public static View get(){
		if (instance == null)
			new View();
		return instance;
	}
	
	private View(){
		instance = this;
		running = true;
		audioFiles = new ArrayList<>();
		loadFiles();
		initWindow();
	}
	
	private void initWindow(){
		BorderPane bp = new BorderPane();
		VBox top = new VBox();
		
		parameterToolBar = new ParameterToolBar();
		
		addSoundToolBar = new AddSoundToolBar();
		
		top.getChildren().addAll(new WindowBar(), parameterToolBar, addSoundToolBar);
		
		bp.setTop(top);
		
		databaseView = new DatabaseView();
		bp.setLeft(databaseView);
		bp.setAlignment(databaseView, Pos.CENTER_LEFT);
		bp.setMargin(databaseView, new Insets(10, 5, 10, 10));
		
		analyzerView = new AnalyzerView();
		bp.setCenter(analyzerView);
		bp.setAlignment(analyzerView, Pos.CENTER);
		bp.setMargin(analyzerView, new Insets(10, 10, 10, 5));
		
		Scene scene = new Scene(bp);
		scene.getStylesheets().add("file:assets/styleSheet.css");
		this.initStyle(StageStyle.UNDECORATED);
		this.setScene(scene);
		this.setTitle("MFCC_DTW");
		this.getIcons().add(new Image("file:assets/icon.png"));
		this.setWidth(1070);
		this.setHeight(700);
		this.show();
	}
	
	private void loadFiles(){
		try {
		File audioFiles = new File("AudioFileData/");
		File[] list = audioFiles.listFiles();
		for (int i = 0; i < list.length; i++){
			String name = list[i].getName();
			if (name.endsWith(".ser")){
				AudioFile a = null;
				FileInputStream fileIn = new FileInputStream(list[i].getPath());
				ObjectInputStream in = new ObjectInputStream(fileIn);
				a = (AudioFile)in.readObject();
				in.close();
				fileIn.close();
				this.audioFiles.add(a);
			}
		}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public ParameterToolBar getParamTB() {
		return parameterToolBar;
	}
	
	public AddSoundToolBar getAddTB() {
		return addSoundToolBar;
	}
	
	public ArrayList<AudioFile> getAudioFiles() {
		return audioFiles;
	}
	
	public AnalyzerView getCenterView() {
		return analyzerView;
	}
	
	public DatabaseView getLeftView() {
		return databaseView;
	}
	
	public Recorder getRecorder() {
		return recorder;
	}
	
	public void setRecorder(Recorder recorder) {
		this.recorder = recorder;
	}
	
}
