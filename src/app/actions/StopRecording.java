package app.actions;

import java.io.File;

import app.view.View;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class StopRecording implements EventHandler<ActionEvent>{
	
	private String source;
	
	public StopRecording(String source) {
		this.source = source;
	}
	
	@Override
	public void handle(ActionEvent arg0) {
		try {
		View.get().getRecorder().stop();
		int counter = 0;
		if (source.equals("add")) counter = 1;
		else counter = 2;
		String name = "ReC_"+counter+"_.wav";
		File wavFile = new File("AudioFiles/"+name);
		View.get().getRecorder().save(wavFile);
		
		View.get().getParamTB().getRecordingLabel().setVisible(false);
		if (source.equals("add")){
			View.get().getAddTB().getPath().setText("AudioFiles/"+name);
			View.get().getAddTB().getRecImage().setImage(View.get().getAddTB().getRec());
			View.get().getAddTB().getRecord().setOnAction(new RecordAction(source));
		} else {
			View.get().getCenterView().getPath().setText("AudioFiles/"+name);
			View.get().getCenterView().getRecImage().setImage(View.get().getAddTB().getRec());
			View.get().getCenterView().getRecord().setOnAction(new RecordAction(source));
		}
		View.get().setRecorder(null);
		} catch (Exception e){
			e.printStackTrace();
		}
	}

}
