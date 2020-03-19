package app.view;

import app.actions.MouseEntered;
import app.actions.MouseExited;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.WindowEvent;

public class WindowBar extends BorderPane{

	private double mouseX;
	private double mouseY;
	private MouseEntered entered;
	private MouseExited exited;
	
	public WindowBar() {
		this.setMinHeight(24);
		this.setMaxHeight(24);
		this.setStyle("-fx-background-color: rgba(29,29,29,0.5)");
		ImageView logo = new ImageView();
		logo.setPreserveRatio(true);
		logo.setFitHeight(22);
		logo.setFitWidth(22);
		logo.setImage(new Image("file:assets\\icon.png"));
		
		HBox title = new HBox();
		title.setSpacing(5);
		Label naslov = new Label("Sound Analyzer");
		naslov.setStyle(" -fx-font-size: 11pt; -fx-text-fill: #f48f42; -fx-opacity: 1;");
		title.getChildren().addAll(logo,naslov);
		
		HBox buttons = new HBox();
		
		Button close = new Button("",new ImageView(new Image("file:assets\\closew.png")));
		close.setPrefWidth(40);
		close.setMaxWidth(40);
		close.setMinWidth(40);
		close.setStyle("-fx-background-color: transparent");
		close.setFocusTraversable(false);
		entered = new MouseEntered();
		exited = new MouseExited();
		close.setOnMouseEntered(entered);
		close.setOnMouseExited(exited);
		close.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				View.get().running = false;
				close.setStyle("-fx-background-color: #87c7ff");
				fireEvent(new WindowEvent(View.get(), WindowEvent.WINDOW_CLOSE_REQUEST));
			}
		});
		
		Button max = new Button("", new ImageView(new Image("file:assets\\resizew.png")));
		max.setPrefWidth(30);
		max.setMaxWidth(30);
		max.setMinWidth(30);
		max.setStyle("-fx-background-color:transparent");
		max.setDisable(true);
		
		Button min = new Button("", new ImageView(new Image("file:assets\\minimizew.png")));
		min.setPrefWidth(30);
		min.setMaxWidth(30);
		min.setMinWidth(30);
		min.setStyle("-fx-background-color:transparent");
		min.setFocusTraversable(false);
		min.setOnMouseEntered(entered);
		min.setOnMouseExited(exited);
		
		min.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				min.setStyle("-fx-background-color: #87c7ff");
				View.get().setIconified(true);
			}
		});
		
		buttons.getChildren().addAll(min,max,close);
		this.setRight(buttons);
		
		//wbar.setLeft(logo);
		this.setCenter(title);
		
		this.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				mouseX = event.getX();
				mouseY = event.getY();
			}
		});
		
		this.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				View.get().setX(event.getScreenX() - mouseX);
				View.get().setY(event.getScreenY() - mouseY);
			}
		});
		
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() == 2){
					max.fire();
					max.setStyle("-fx-background-color: transparent");
				}
			}
		});
	}
	
}
