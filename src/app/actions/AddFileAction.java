package app.actions;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import app.processing.AudioFile;
import app.view.MyAlert;
import app.view.View;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class AddFileAction implements EventHandler<ActionEvent>{

	@Override
	public void handle(ActionEvent event) {
		String name = View.get().getAddTB().getWordName().getText();
		if (name == null || name.equals("")){
			new MyAlert("Invalid name", "Please enter a name for the sound file.");
			return;
		}
		for (AudioFile file:View.get().getAudioFiles()){
			if (file.getName().equals(name)){
				new MyAlert("Invalid name", "The name you have entered is already being used."
						+ "Please choose another one.");
				return;
			}
		}
		
		try {
			int test = Integer.parseInt(View.get().getParamTB().getWindowWidth().getText());
			test = Integer.parseInt(View.get().getParamTB().getCoefNum().getText());
		} catch (Exception e){
			new MyAlert("Invalid parameters", "Parameters must be integers.");
			return;
		}
		
		String path = View.get().getAddTB().getPath().getText();
		if (path == null || path.equals("")){
			new MyAlert("Invalid path", "Please select a valid file.");
			return;
		}
		
		Path src = Paths.get(path);
		Path dest = Paths.get("AudioFiles/"+name+".wav");
		
		try {
		Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e){
			new MyAlert("Fatal Error", "Error copying files!");
			return;
		}
		
		View.get().getParamTB().getPb().setVisible(true);
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				AudioFile audioFile = new AudioFile(name);
				View.get().getAudioFiles().add(audioFile);
				audioFile.analyze();
				audioFile.saveData();
				Platform.runLater(new Runnable(){
					
					public void run(){
						View.get().getAddTB().getPath().setText("");
						View.get().getAddTB().getWordName().setText("");
						View.get().getLeftView().getListView().getItems().add(audioFile);
						View.get().getParamTB().getPb().setVisible(false);
					}
					
				});
			}
			
		});
		
		thread.start();

	}

}
