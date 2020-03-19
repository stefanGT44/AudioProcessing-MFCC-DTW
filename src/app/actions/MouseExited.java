package app.actions;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class MouseExited implements EventHandler<MouseEvent>{

	@Override
	public void handle(MouseEvent event) {
		((Button)event.getSource()).setStyle("-fx-background-color: transparent");
	}

}