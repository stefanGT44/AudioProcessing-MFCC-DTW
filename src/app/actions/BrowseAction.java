package app.actions;

import java.io.File;

import app.view.View;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

public class BrowseAction implements EventHandler<ActionEvent>{

	@Override
	public void handle(ActionEvent event) {
		try {
			FileChooser chooser = new FileChooser();
			chooser.setTitle("Select a file");
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("wav files (*.wav)", "*.wav");
            chooser.getExtensionFilters().add(extFilter);
			
			File selectedFile = chooser.showOpenDialog(View.get());
			
			if (selectedFile == null)
				return;
			
			if (!selectedFile.getAbsolutePath().endsWith(".wav")){
				return;
			}
			
			String src = ((Button)(event.getSource())).getAccessibleHelp();
			
			if (src.equals("add"))
				View.get().getAddTB().getPath().setText(selectedFile.getAbsolutePath());
			else
				View.get().getCenterView().getPath().setText(selectedFile.getAbsolutePath());
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	

}
