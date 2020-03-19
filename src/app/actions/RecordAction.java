package app.actions;

import app.processing.Recorder;
import app.view.View;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class RecordAction implements EventHandler<ActionEvent>{
	
	private String source;
	
	public RecordAction(String source) {
		this.source = source;
	}
	
	@Override
	public void handle(ActionEvent event) {		
		Recorder recorder = new Recorder();
		
		Thread recordThread = new Thread(new Runnable(){

			@Override
			public void run() {
				try {
					System.out.println("Start recording...");
					recorder.start();
				} catch (Exception e){
					e.printStackTrace();
					System.exit(-1);
				}
			}
			
		});
		View.get().setRecorder(recorder);
		recordThread.start();
		View.get().getParamTB().getRecordingLabel().setVisible(true);
		if (source.equals("add")){
			View.get().getAddTB().getRecImage().setImage(View.get().getAddTB().getStop());
			View.get().getAddTB().getRecord().setOnAction(new StopRecording(source));
		} else {
			View.get().getCenterView().getRecImage().setImage(View.get().getAddTB().getStop());
			View.get().getCenterView().getRecord().setOnAction(new StopRecording(source));
		}
	}

}
