package app.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class ParameterToolBar extends ToolBar{
	
	private TextField coefNum;
	private TextField windowWidth;
	private ComboBox<String> winFun;
	private Label recordingLabel;
	private ProgressBar pb;
	
	public ParameterToolBar() {
		
		Label l1 = new Label("MFCC parameters");
		l1.setStyle("-fx-text-fill: #ff8323");
		
		Label l2 = new Label("Coefs per window: ");
		coefNum = new TextField();
		coefNum.setText("12");
		coefNum.setPrefWidth(60);
		
		Label l3 = new Label("Window width: ");
		windowWidth = new TextField();
		windowWidth.setText("660");
		windowWidth.setPrefWidth(80);
		
		Label l4 = new Label("Window function: ");
		
		ObservableList<String> olista = FXCollections.observableArrayList();
		olista.add("None");
		olista.add("Hanning");
		olista.add("Hamming");
		
		winFun = new ComboBox<>(olista);
		winFun.getSelectionModel().select(2);
		winFun.setPrefWidth(120);
		
		recordingLabel = new Label("Recording...");
		recordingLabel.setStyle("-fx-text-fill: #ff0000");
		recordingLabel.setVisible(false);
		
		pb = new ProgressBar();
		pb.setMaxSize(150, 25);
		pb.setMinSize(150, 25);
		pb.setPrefSize(150, 25);
		
		pb.setVisible(false);
		
		HBox spacing = new HBox();
		spacing.setMinWidth(20);
		
		this.getItems().addAll(l1, new Separator(), l2, coefNum, l3, windowWidth, l4, winFun, recordingLabel, spacing, pb);
		this.setStyle("-fx-background-color: rgba(29,29,29,1);");
	}

	public TextField getCoefNum() {
		return coefNum;
	}

	public TextField getWindowWidth() {
		return windowWidth;
	}

	public ComboBox<String> getWinFun() {
		return winFun;
	}
	
	public Label getRecordingLabel() {
		return recordingLabel;
	}
	
	public ProgressBar getPb() {
		return pb;
	}

}
