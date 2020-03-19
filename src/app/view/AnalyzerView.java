package app.view;

import app.actions.AnalyzeAction;
import app.actions.BrowseAction;
import app.actions.RecordAction;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AnalyzerView extends VBox{
	
	private ToolBar tb;
	private TextField path;
	private Button browse;
	private Button record;
	private Button analyze;
	private ListView<String> foundWords;
	private ImageView recImage;
	
	public AnalyzerView() {
		this.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4);"+
				"-fx-base: rgba(0, 0, 0, 0.4);"+
				"-fx-control-inner-background: rgba(0, 0, 0, 0.4)");
		this.setSpacing(6);
		this.setPadding(new Insets(7));
		
		Label l1 = new Label("Analyzer");
		l1.setStyle("-fx-text-fill: #ff8323");
		
		tb = new ToolBar();
		
		Label l3 = new Label("File path: ");
		path = new TextField();
		path.setPromptText("enter a path...");
		path.setPrefWidth(300);
		
		browse = new Button("Browse");
		browse.setAccessibleHelp("center");
		browse.setOnAction(new BrowseAction());
		
		recImage = new ImageView();
		recImage.setPreserveRatio(true);
		recImage.setFitHeight(20);
		recImage.setFitWidth(20);
		recImage.setImage(View.get().getAddTB().getRec());
		record = new Button("", recImage);
		record.setOnAction(new RecordAction("analyze"));
		
		ImageView analyzeImage = new ImageView();
		analyzeImage.setPreserveRatio(true);
		analyzeImage.setFitHeight(18);
		analyzeImage.setFitWidth(18);
		analyzeImage.setImage(new Image("file:assets/analyzeIcon.png"));
		analyze = new Button("Analyze", analyzeImage);
		analyze.setOnAction(new AnalyzeAction());
		
		HBox space = new HBox();
		space.setPrefWidth(116);
		tb.getItems().addAll(l3, path, browse, record, space, analyze);
		
		Label l2 = new Label("Words found: ");
		l2.setStyle("-fx-text-fill: #ff8323");
		
		foundWords = new ListView();
		foundWords.setPrefHeight(522);
		foundWords.setStyle("	-fx-base: rgba(0,0,0,0.3);"+
	"-fx-control-inner-background: rgba(0,0,0,0.3);"+
    "-fx-background-color: rgba(255, 131, 35, 0.3);"+
    "-fx-padding: 1;");
		
		this.getChildren().addAll(l1, tb, l2, foundWords);
	}
	
	public ToolBar getTb() {
		return tb;
	}
	
	public TextField getPath() {
		return path;
	}
	
	public ListView<String> getFoundWords() {
		return foundWords;
	}
	
	public Button getRecord() {
		return record;
	}
	
	public ImageView getRecImage() {
		return recImage;
	}

}
