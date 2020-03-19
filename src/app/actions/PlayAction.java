package app.actions;

import app.processing.AudioFile;
import app.view.View;
import app.view.spectrogram.Player;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class PlayAction implements EventHandler<ActionEvent>{

	@Override
	public void handle(ActionEvent arg0) {
		if (View.get().getLeftView().getListView().getSelectionModel().getSelectedItem() == null) return;
		if (View.get().getLeftView().getListView().getSelectionModel().getSelectedItems().size()>1) return;
		Player player = new Player(View.get().getLeftView().getSpectrumVisualizer(),
				((AudioFile)(View.get().getLeftView().getListView().getSelectionModel().getSelectedItem())).getName());
		Thread thread = new Thread(new Runnable(){

			@Override
			public void run() {
				player.start();
			}
			
		});
		
		thread.start();
	}

}
