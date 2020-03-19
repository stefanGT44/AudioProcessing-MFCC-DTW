package app.view;

import javafx.scene.control.Alert;

public class MyAlert extends Alert{

	public MyAlert(String title, String text) {
		super(AlertType.ERROR);
		this.setTitle(title);
		this.setHeaderText(null);
		this.setContentText(text);
		this.show();
	}
	
}
