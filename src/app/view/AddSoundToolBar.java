package app.view;

import app.actions.AddFileAction;
import app.actions.BrowseAction;
import app.actions.RecordAction;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AddSoundToolBar extends ToolBar{
	
	private TextField path;
	private Button browse;
	private Button record;
	private Button addFile;
	private TextField wordName;
	private ImageView recImage;
	private Image rec, stop;
	
	public AddSoundToolBar() {
		
		Label l1 = new Label("Add file to database");
		l1.setStyle("-fx-text-fill: #ff8323");
		
		Label l2 = new Label("File path: ");
		path = new TextField();
		path.setPromptText("enter a path...");
		path.setPrefWidth(300);
		
		browse = new Button("Browse");
		browse.setAccessibleHelp("add");
		browse.setOnAction(new BrowseAction());
		
		recImage = new ImageView();
		recImage.setPreserveRatio(true);
		recImage.setFitHeight(18);
		recImage.setFitWidth(18);
		rec = new Image("file:assets/recording.png");
		stop = new Image("file:assets/stop.png");
		recImage.setImage(rec);
		record = new Button("", recImage);
		record.setOnAction(new RecordAction("add"));
		
		Label l3 = new Label("Sound name: ");
		wordName = new TextField();
		wordName.setPromptText("enter a name...");
		
		ImageView addImage = new ImageView();
		addImage.setPreserveRatio(true);
		addImage.setFitHeight(18);
		addImage.setFitWidth(18);
		addImage.setImage(new Image("file:assets/addIcon.png"));
		addFile = new Button("Add file", addImage);
		addFile.setOnAction(new AddFileAction());
		
		this.getItems().addAll(l1, new Separator(), l2, path, browse, record, l3, wordName, addFile);
		
	}
	
	public ImageView getRecImage() {
		return recImage;
	}

	public TextField getPath() {
		return path;
	}


	public Button getBrowse() {
		return browse;
	}


	public Button getRecord() {
		return record;
	}


	public Button getAddFile() {
		return addFile;
	}


	public TextField getWordName() {
		return wordName;
	}
	
	public Image getRec() {
		return rec;
	}
	
	public Image getStop() {
		return stop;
	}
	
}
