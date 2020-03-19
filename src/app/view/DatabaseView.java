package app.view;

import app.actions.DeleteAction;
import app.actions.PlayAction;
import app.processing.AudioFile;
import app.view.spectrogram.SpectrumVisualizer;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DatabaseView extends VBox{
	
	private ListView<AudioFile> listView;
	private Button delete, play, rename;
	private Image play1, play2;
	private SpectrumVisualizer spectrumVisualizer;
	
	public DatabaseView() {
		this.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4);"+
				"-fx-base: rgba(0, 0, 0, 0.4);"+
				"-fx-control-inner-background: rgba(0, 0, 0, 0.4)");
		
		this.setSpacing(6);
		this.setPadding(new Insets(0,0,0,7));
		
		HBox hbox = new HBox();
		hbox.setSpacing(5);
		
		ImageView pl = new ImageView();
		pl.setPreserveRatio(true);
		pl.setFitHeight(20);
		pl.setFitWidth(20);
		play1 = new Image("file:assets\\play.png");
		play2 = new Image("file:assets\\play2.png");
		pl.setImage(play1);
		play = new Button("play", pl);
		play.setOnMouseEntered(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				pl.setImage(play2);
			}


		});
		play.setOnMouseExited(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				pl.setImage(play1);
			}
		});
		
		play.setOnAction(new PlayAction());
		
		ImageView del = new ImageView();
		del.setPreserveRatio(true);
		del.setFitHeight(18);
		del.setFitWidth(18);
		del.setImage(new Image("file:assets\\delete.png"));
		delete = new Button("delete", del);
		delete.setOnAction(new DeleteAction());
		
		ImageView renameImage = new ImageView();
		renameImage.setPreserveRatio(true);
		renameImage.setFitHeight(18);
		renameImage.setFitWidth(16);
		renameImage.setImage(new Image("file:assets/rename.png"));
		rename = new Button("rename", renameImage);
		
		hbox.getChildren().addAll(play, rename, delete);
		
		
		Label l1 = new Label("Words in database: ");
		l1.setStyle("-fx-text-fill: #ff8323");
		
		listView = new ListView(FXCollections.observableArrayList(View.get().getAudioFiles()));
		listView.setPrefHeight(600);
		listView.setMinWidth(278);
		listView.setMaxWidth(278);
		listView.setMaxHeight(366);
		listView.setMinHeight(366);
		listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		listView.setStyle("	-fx-base: rgba(0,0,0,0.3);"+
	"-fx-control-inner-background: rgba(0,0,0,0.3);"+
    "-fx-background-color: rgba(255, 131, 35, 0.3);"+
    "-fx-padding: 1;");
		
		spectrumVisualizer = new SpectrumVisualizer();
		
		Label l2 = new Label("Spectrum visualizer");
		l2.setStyle("-fx-text-fill: "
				+ "linear-gradient(to right, #00d8ff, #f430ff);");
		
		this.getChildren().addAll(l1, hbox, listView, spectrumVisualizer, l2);
		
		this.setMinWidth(290);
		this.setMaxWidth(290);
		this.setMaxHeight(554);
		this.setMinHeight(554);
	}
	
	public ListView<AudioFile> getListView() {
		return listView;
	}
	
	public SpectrumVisualizer getSpectrumVisualizer() {
		return spectrumVisualizer;
	}

}
