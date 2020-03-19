package app.actions;

import java.io.File;
import java.util.Optional;

import app.processing.AudioFile;
import app.view.View;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class DeleteAction implements EventHandler<ActionEvent>{

	@Override
	public void handle(ActionEvent arg0) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		//alert.getDialogPane().getStylesheets().add("file:styleSheet.css");
		alert.setTitle("Confirmation");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you wan't to delete the selected item(s)?");
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() != ButtonType.OK){
			alert.close();
			return;
		}
		
		ObservableList<AudioFile> lista = View.get().getLeftView().getListView().getSelectionModel().getSelectedItems();
		for (AudioFile file:lista){
			File data = new File("AudioFileData/"+file.getName()+".ser");
			File soundFile = new File("AudioFiles/"+file.getName()+".wav");
			data.delete();
			soundFile.delete();
			View.get().getAudioFiles().remove(file);
		}
		
		View.get().getLeftView().getListView().getSelectionModel().clearSelection();
		View.get().getLeftView().getListView().setItems(FXCollections.observableArrayList(View.get().getAudioFiles()));
		
	}

}
