package app.view.spectrogram;

import java.util.Arrays;

import app.processing.Complex;
import app.processing.FFT;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SpectrumVisualizer extends Pane{
	
	public static final int BAR_NUM = 64;
	public static final int width = 256, height = 80;
	
	public static double frequencyBins[];
	public static double[] buffer = new double[BAR_NUM];
	public static double[] bufferDecrease = new double[BAR_NUM];
	public static double[] magnitudes = new double[BAR_NUM];
	
	public static Rectangle[] rec, peaks;
	static boolean test = false;
	
	public SpectrumVisualizer() {
		initView();
		calculateBins();
	}
	
	public void initView() {
		this.setMinSize(width, height);
		this.setMaxSize(width, height);
		
		int c1[] = {26, 115, 216};
		int c2[] = {167, 0, 244};
		
		int c3[] = {0, 250, 255};
		int c4[] = {252, 132, 255};
		
		rec = new Rectangle[64];
		peaks = new Rectangle[64];
		
		for (int i = 0; i < 64; i++){
			int pos = i * 4 + 2;
			Rectangle r = new Rectangle(pos, height-1, 2, 1);
			

			DropShadow borderGlow = new DropShadow();
			borderGlow.setColor(Color.rgb(lerp(c3[0], c4[0], i/(float)rec.length),
					lerp(c3[1], c4[1], i/(float)rec.length),
					lerp(c3[2], c4[2], i/(float)rec.length)));
			borderGlow.setWidth(40);
			borderGlow.setHeight(40);
			
			r.setEffect(borderGlow);
			
			r.setFill(Color.rgb(lerp(c1[0], c2[0], i/(float)64),
					lerp(c1[1], c2[1], i/(float)64),
					lerp(c1[2], c2[2], i/(float)64)));
			rec[i] = r;
			this.getChildren().add(r);
			
			DropShadow peakGlow = new DropShadow();
			peakGlow.setColor(Color.rgb(lerp(c3[0], c4[0], i/(float)rec.length),
					lerp(c3[1], c4[1], i/(float)rec.length),
					lerp(c3[2], c4[2], i/(float)rec.length)));
			peakGlow.setWidth(3);
			peakGlow.setHeight(3);
			
			Rectangle peak = new Rectangle(pos, height-2, 2, 1);
			peak.setEffect(peakGlow);
			peak.setFill(Color.rgb(lerp(c1[0], c2[0], i/(float)rec.length),
					lerp(c1[1], c2[1], i/(float)rec.length),
					lerp(c1[2], c2[2], i/(float)rec.length)));
			
			peaks[i] = peak;
			
			this.getChildren().add(peak);
		}
	}
	
	private void calculateBins(){
		double maxFreq = 22050;
		double time = (Player.DEF_BUFFER_SAMPLE_SZ/2)/maxFreq;
		double minFreq = 1/time;
		
		frequencyBins = new double[BAR_NUM + 2];
		frequencyBins[0] = minFreq;
		frequencyBins[frequencyBins.length-1] = maxFreq;
		
		minFreq = melTransform(minFreq);
		maxFreq = melTransform(maxFreq);
		
		double amount = (maxFreq - minFreq)/(BAR_NUM + 1);
		
		/*
		Mel's scale is logarithmic so we can set the distances between
		frequencies to be linear, once we convert the values back 
		from Mel's scale we get logarithmic distances between frequencies 
		the distance increases as the frequencies increase
		which corresponds to how humans hear sound
		(we can detect fewer differences in higher frequencies) 
		*/
		
		for (int i = 1; i < frequencyBins.length-1; i++){
			frequencyBins[i] = iMelTransform(minFreq + i * amount);
		}
		
		//triangulation
		//frequencyBins[0] = 0;
		int index = 0;
		System.out.println(Arrays.toString(frequencyBins));
		for (int i = 1; i <= Player.DEF_BUFFER_SAMPLE_SZ/2; i++){
			double freq = i / time;
			if (freq >= frequencyBins[index]){
				frequencyBins[index++] = i-1;
			}
			if (index==(BAR_NUM+2)) break;
		}
		frequencyBins[frequencyBins.length-1] = Player.DEF_BUFFER_SAMPLE_SZ/2;
		System.out.println(Arrays.toString(frequencyBins));
	}
	
	public void drawWaveForm2(float samples[]) {
		Complex data[] = new Complex[samples.length];
		for (int i = 0; i < samples.length; i++){
			data[i] = new Complex(samples[i], 0);
		}
		Complex niz[] = FFT.fft(data);

		for (int i = 0; i < magnitudes.length; i++){
			int startIndex = (int)frequencyBins[i];
			int endIndex = (int)frequencyBins[i+2];
			
			int amount = (endIndex - startIndex) / 2;
			int amountFull = endIndex - startIndex;
			
			double maxFreq =  0;
			
			for (int j = startIndex; j < endIndex; j++){

				double freq = 0;
				if (j <= startIndex + amount) {
					freq = (((j - (startIndex - 1)) * 1.0) / (amount + 1)) * (niz[j].re() * niz[j].re() + niz[j].im() * niz[j].im());
				}
				else {
					freq = (((amountFull - (j - startIndex)) * 1.0) / amount) * (niz[j].re() * niz[j].re() + niz[j].im() * niz[j].im());
				}
		
				if (freq > maxFreq) maxFreq = freq;
			}
			
			magnitudes[i] = 20 * Math.log10(maxFreq);
			
			// default 0.5 & 1.8
			
			if (magnitudes[i] > buffer[i]){
				buffer[i] = magnitudes[i];
				bufferDecrease[i] = 0.9;
			}
			
			if (magnitudes[i] < buffer[i]){
				buffer[i] -= bufferDecrease[i];
				bufferDecrease[i] *= 1.3;
			}
			
		}
		
		double scale = height/110.0;
		for (int i = 0; i < magnitudes.length; i++){
			rec[i].setHeight((int)(buffer[i] * scale));
			rec[i].setY(height - 1 -rec[i].getHeight());
			if (peaks[i].getY()>rec[i].getY()-1){
				peaks[i].setY(rec[i].getY()-1);
			}
		}
	}
	
	public static void finished(){
		int animCounter = 0;
		int finishedCounter = 0;
		while (true){
			animCounter++;
			finishedCounter = 0;
			if (animCounter%1000000==0){
			animCounter = 0;
			for (int j = 0; j < rec.length; j++){
				if (rec[j].getHeight()<=height-2){
					finishedCounter++;
					continue;
				} else{
				rec[j].setHeight(rec[j].getHeight()-1);
				rec[j].setY(height - 1 -rec[j].getHeight());
				}
			}
			if (finishedCounter >= rec.length-1) break;
			}
		}
		for (int i = 0; i < rec.length; i++){
			rec[i].setHeight(1);
			rec[i].setY(height - 1);
		}
		animCounter = 0;
		finishedCounter = 0;
		while (true){
			animCounter++;
			finishedCounter = 0;
			if (animCounter%1000000==0){
			animCounter = 0;
			for (int j = 0; j < rec.length; j++){
				if (peaks[j].getY()>=height - 2){
					finishedCounter++;
					continue;
				} else{
					peaks[j].setY(peaks[j].getY()+1);
				}
			}
			if (finishedCounter >= peaks.length-1) break;
			}
		}
		for (int i = 0; i < peaks.length; i++)
			peaks[i].setY(height - 2);
	}
	
	private double melTransform(double freq){
		return 1125 * Math.log(1 + freq/(float)700);
	}
	
	private double iMelTransform(double freq){
		return 700 * (Math.pow(Math.E, freq/(float)1125) - 1);
	}
	
	public static int lerp(int a, int b, double x){
		return (int)(a + (b - a) * x);
	}

}
