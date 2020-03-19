package app.processing;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class FileAnalyzer {
	
	//for reading
	private int size, freq, channelNum, bps, audioFormat, sampleNum;
	private double length;
	private double samples[];
	
	//for noise calculation
	private int noiseSampleNum;
	private double granicaGovora, ZCT;
	
	//for speech windows calculation
	private int wSampleNum;
	private int windows[];
	
	//pronalazenje reci
	private int startFrameIndex, wordLength;
	
	//za analizu fajla sa vise reci
	private ArrayList<Word> words;
	
	public FileAnalyzer(String path){
		readWavFile(path);
		
		System.out.println("Data size: "+size+" bytes");
		System.out.println("Frequency: "+freq+"Hz");
		System.out.println("Channel num: "+channelNum);
		System.out.println("Bits per sample: "+bps);
		System.out.println("Audio format code: "+audioFormat);
		System.out.println("Sample number: "+sampleNum);
		System.out.println("Length: "+length);
		
		calculateNoise();
		calculateSpeechWindows();
		
		ispis("Raw windows: ");
		
		// podizemo nule
		smoothen(10, 20, 0, 1);
		// spustamo jedinice
		smoothen(10, 20, 1, 0);
		ispis("Nakon smoothing");
		
		ZSmoothing(10);
		ispis("Nakon Z smoothing");
		
		//150ms pre i posle govora se analizira
		applyZCT(15);
		ispis("Nakon ZCT");
		
		
		findWord();
	}
	
	public FileAnalyzer(String path, boolean multiple){
		readWavFile(path);
		
		System.out.println("Data size: "+size+" bytes");
		System.out.println("Frequency: "+freq+"Hz");
		System.out.println("Channel num: "+channelNum);
		System.out.println("Bits per sample: "+bps);
		System.out.println("Audio format code: "+audioFormat);
		System.out.println("Sample number: "+sampleNum);
		System.out.println("Length: "+length);
		
		calculateNoise();
		calculateSpeechWindows();
		
		ispis("Raw windows: ");
		
		// podizemo nule
		smoothen(10, 20, 0, 1);
		// spustamo jedinice
		smoothen(10, 20, 1, 0);
		ispis("Nakon smoothing");
		
		ZSmoothing(10);
		ispis("Nakon Z smoothing");
		
		//150ms pre i posle govora se analizira
		applyZCT(15);
		ispis("Nakon ZCT");
		
		words = new ArrayList<>();
		findWords();
	}
	
	
	//VERZIJA KOJA KORISTI DCT
	public ArrayList<ArrayList<Double>> calculateMFCC(int mfccWidth, int coefNum, String winFun){
		ArrayList<ArrayList<Double>> vectors = new ArrayList<>();
		
		//uzima trecinu kao step size
		//int stepSize = mfccWidth - mfccWidth/3;
		
		int stepSize = 220;
		
		System.out.println("StepSize: "+stepSize);
		System.out.println("Overlap: "+(mfccWidth/3));
		//stepSize = frameSize - overlap
		
		
		//broj mfcc prozora u reci
		int frameNum = wordLength/stepSize - (mfccWidth/stepSize - 1);
		
		//vremensko trajanje jednog prozora
		//44100 : 1s = mfccWdith : x?
		double time = (mfccWidth /  (float)freq);
		
		int freqArrayLength = (mfccWidth - 1)/2;
		
		System.out.println("freqArrayLength: "+freqArrayLength);
		
		double minFreq = 1/(float)time;
		double maxFreq = freqArrayLength/(float)time;
		
		System.out.println("minFreq: "+minFreq);
		System.out.println("maxFreq: "+maxFreq);
		
		int X = coefNum;
		
		minFreq = melTransform(minFreq);
		maxFreq = melTransform(maxFreq);
		double amount = (maxFreq - minFreq) / (float) (X + 2);
		
		//+ 2 zbog min i max
		double frequencies[] = new double[X + 2];
		frequencies[0] = iMelTransform(minFreq);
		frequencies[frequencies.length-1] = iMelTransform(maxFreq);
		for (int i = 1; i < frequencies.length - 1; i++){
			frequencies[i] = iMelTransform(minFreq + amount * i);
		}
		
		System.out.println("Frekvencije pre setovanja");
		for (int i = 0; i < frequencies.length; i++){
			System.out.println(frequencies[i]);
		}
		
		int index = 1;
		frequencies[0] = 0;
		for (int i = 2; i < (mfccWidth-1)/2 + 1; i++){
			double freq = i/(float)time;
			System.out.println("freq: "+freq);
			System.out.println("frequencies[index]: "+frequencies[index]);
			if (freq>=frequencies[index]){
				frequencies[index++] = i-1;
			}
		}
		
		System.out.println("Frekvencije posle setovanja");
		for (int i = 0; i < frequencies.length; i++){
			System.out.println(frequencies[i]);
		}
		
		//frameNum + 1 zbog ostatka
		for (int i = 0; i < frameNum + 1; i++){
			
			double x1[] = new double[mfccWidth];
			double y1[] = new double[mfccWidth];
			
			int start = startFrameIndex + i * stepSize;
			int l = 0;
			
			double prevSample = samples[start];
			for (int j = start; j < start + mfccWidth; j++){
				int k = j;
				
				//padding with 0 if we get out of bound of word
				if (k >= startFrameIndex + wordLength || k >= sampleNum){
					x1[l] = 0;
					y1[l] = 0;
					l++;
					continue;
				}
				
				
				//filter (preemphasis)
				double currSample = samples[k];
				if (k > start){
					currSample = currSample - 0.95 * prevSample;
					prevSample = currSample;
				}
				
				if (winFun.equals("Hanning")){
					x1[l] = (double)(currSample * 0.5 * (1 - Math.cos(2.0*Math.PI * l / mfccWidth)));
					y1[l] = 0;
				} else if (winFun.equals("Hamming")){
					x1[l] = (double)(currSample * (0.54 - 0.46 * Math.cos(2.0*Math.PI * l / mfccWidth)));
					y1[l] = 0;
				} else {
					x1[l] = currSample;
					y1[l] = 0;
				}
				l++;
			}
			
			DFT.DFT(1, mfccWidth, x1, y1);
			
			double dftRes[] = new double[(mfccWidth-1)/2];
			for (int j = 1; j < (mfccWidth-1)/2 + 1; j++){
				dftRes[j-1] = (Math.sqrt(x1[j] * x1[j] + y1[j] * y1[j]));
			}
			
			double sumCoefs[] = new double[frequencies.length - 2];
			int k = 0;
			for (int j = 0; j < sumCoefs.length; j++){
				
				int startIndex = (int)frequencies[k];
				int middleIndex = (int)frequencies[k+1];
				int lastIndex = (int)frequencies[k+2];
				int length = middleIndex - startIndex;
				
				double sum = 0;
				
				for (int p = startIndex; p <= lastIndex; p++){
					double coef;
					if (p>middleIndex){
						length = lastIndex - middleIndex;
						coef = (lastIndex - p)/(float)length;
					}else 
						coef = (p - startIndex)/(float)length;
					sum += dftRes[p] * coef;
				}
				
				sumCoefs[j] = Math.log10(sum);
				k++;
				
			}
			
			double resultCoefs[] = DFT.DCTtransform(sumCoefs);
			ArrayList<Double> coefs = new ArrayList<>();
			for (int j = 0; j < resultCoefs.length; j++){
				coefs.add(new Double(resultCoefs[j]));
			}
			
			//delta
			int N = 2;
			double deltas[] = new double[resultCoefs.length];
			for (int j = 0; j < resultCoefs.length; j++){
				double suma = 0;
				int kol = 0;
				for (int n = 1; n <= N; n++){
					int next = j + n;
					int prev = j - 1;
					if (next > resultCoefs.length-1)
						next = resultCoefs.length-1;
					if (prev < 0)
						prev = 0;
					suma += n*(float)(resultCoefs[next] - resultCoefs[prev]);
					kol += n*n;
				}
				kol *= 2;
				
				deltas[j] = suma/(float)kol;
				coefs.add(new Double(deltas[j]));
			}
			
			//delta delta
			for (int j = 0; j < deltas.length; j++){
				double suma = 0;
				int kol = 0;
				for (int n = 1; n <= N; n++){
					int next = j + n;
					int prev = j - 1;
					if (next > deltas.length-1)
						next = deltas.length-1;
					if (prev < 0)
						prev = 0;
					suma += n*(float)(deltas[next] - deltas[prev]);
					kol += n*n;
				}
				kol *= 2;
				coefs.add(new Double(suma/(float)kol));
			}
			
			vectors.add(coefs);
			
		}
		return vectors;
	}
	
	private double melTransform(double freq){
		return 1125 * Math.log(1 + freq/(float)700);
	}
	
	private double iMelTransform(double freq){
		return 700 * (Math.pow(Math.E, freq/(float)1125) - 1);
	}
	
	private void findWords(){
		int i = 0;
		int start;
		while (i<windows.length){
			if (windows[i] == 0) {
				i++;
				continue;
			}
			start = i;
			while (i < windows.length && windows[i] == 1){
				i++;
			}
			int length = i - start;
			System.out.println("start: "+start+" length: "+length);
			start = noiseSampleNum + start * wSampleNum;
			length = length * wSampleNum;
			words.add(new Word(start, length));
		}
	}
	
	private void findWord(){
		int pos = 0;
		while (pos < windows.length && windows[pos] == 0)
			pos++;
		
		startFrameIndex = pos;
		
		while (pos <  windows.length && windows[pos] == 1)
			pos++;
		
		wordLength = pos - startFrameIndex;
		
		wordLength = wordLength * wSampleNum;
		startFrameIndex = startFrameIndex * wSampleNum + noiseSampleNum;
	}
	
	private void applyZCT(int frameNum){
		for (int i = 0; i < windows.length; i++){
			if (windows[i] == 1 && i!=0){
				int n = i - frameNum;
				if (n<0) n = 0;
				//pre govora
				for (int j = i-1; j>=n; j--){
					int crossCounter = 0;
					int p = noiseSampleNum + j * wSampleNum;
					for (int k = p; k < p + wSampleNum - 1; k++){
						if (Math.signum(samples[k])!= Math.signum(samples[k+1]))
							crossCounter++;
					}
					if (crossCounter>ZCT){
						windows[j] = 1;
					} else {
						break;
					}
				}
				
				while (windows[i] == 1){
					i++;
					if (i == windows.length) return;
				}
				
				//posle govora
				n = i + frameNum;
				if (n > windows.length)
					n = windows.length;
				
				for (int j = i; j<n; j++){
					int crossCounter = 0;
					int p = noiseSampleNum + j * wSampleNum;
					for (int k = p; k < p + wSampleNum - 1; k++){
						if (Math.signum(samples[k])!= Math.signum(samples[k+1]))
							crossCounter++;
					}
					if (crossCounter>ZCT){
						windows[j] = 1;
					} else {
						n = j;
						break;
					}
				}
				i = n-1;
			}
		}
	}
	
	private void ZSmoothing(int Z){
		int counter = 0;
		for (int i = 0; i < windows.length; i++){
			if (windows[i] == 1){
				counter++;
			} 
			if (windows[i] == 0 || (i == windows.length-1 && windows[i] == 1)) {
				if (counter!=0){
					if (counter < Z){
						int n = i;
						if (i == windows.length-1) n++;
						for (int j = i - counter; j < n; j++){
							windows[j] = 0;
						}
					}
				}
				counter = 0;
			}
		}
	}
	
	private void smoothen(int X, int Y, int arg1, int arg2){
		int counter = 0;
		for (int i = 0; i < windows.length; i++){
			if (windows[i] == arg1){
				counter++;
			} 
			if (windows[i] == arg2 || (i == windows.length - 1 && windows[i] == arg1)) {
				if (counter!=0){
					if (counter<X){
						int n = i;
						if (i == windows.length - 1) n++;
						for (int j = i - counter; j < n; j++){
							windows[j] =  arg2;
						}
						int k = i;
						int test = 0;
						while (k < windows.length && windows[k] == arg2){
							test++;
							k++;
							if (test >= Y)
								break;
						}
						k = i - 1;
						while (k>=0 && windows[k] == arg2){
							test++;
							k--;
							if (test >= Y)
								break;
						}
						if (test < Y){
							for (int j = i - counter; j < n; j++){
								windows[j] = arg1;
							}
						}
					}	
				}
				counter = 0;
			}
		}
	}
	
	private void calculateSpeechWindows(){
		wSampleNum = freq/100;
		int windowNum = sampleNum/wSampleNum;
		windows = new int[windowNum-10];
		int counter = 0;
		int winCounter = 0;
		for (int i = noiseSampleNum; i < sampleNum-wSampleNum; i+=wSampleNum){
			int suma = 0;
			winCounter = 0;
			for (int j = i; j<i+wSampleNum; j++){
				if (j >= sampleNum){
					break;
				}
				suma += Math.abs(samples[j]);
				winCounter++;
			}
			double u = suma/(float)winCounter;
			if (u > granicaGovora){
				windows[counter] = 1;
			} else{
				windows[counter] = 0;
			}
			counter++;
		}
	}
	
	private void calculateNoise(){
		//broj semplova za prvih 100ms
		noiseSampleNum = freq/10;
		double suma = 0;
		double crossCounter = 0;
		for (int i = 0; i < noiseSampleNum; i++){
			suma += Math.abs(samples[i]);
			if (i>0){
				if (Math.signum(samples[i-1])!=Math.signum(samples[i])) crossCounter++;
			}
		}
		double u = suma/(float)noiseSampleNum;
		
		double devijacija = 0;
		double suma2 = 0;
		for (int i = 0; i < noiseSampleNum; i++){
			suma2 += Math.pow(Math.abs(samples[i]) - u,2);
		}
		devijacija = Math.sqrt(suma2/(float)noiseSampleNum);
		//granicaGovora = u + 2*devijacija;
		granicaGovora = u + devijacija;
		
		//ZCT = (crossCounter/(float)noiseSampleNum + 2*devijacija)/10;
		ZCT = (crossCounter/(float)noiseSampleNum + 2*devijacija)/10;
		if (ZCT > 25) ZCT = 25;
	}
	
	private void readWavFile(String path) {
		try{
		File srcFile = new File(path);
		FileInputStream in  = new FileInputStream(srcFile);
		byte buf[] = new byte[44];
		
		in.read(buf);
		size = Byte.toUnsignedInt(buf[40]) + (Byte.toUnsignedInt(buf[41]) << 8)
				+ (Byte.toUnsignedInt(buf[42]) << 16) + (Byte.toUnsignedInt(buf[43]) << 24);
		
		freq = Byte.toUnsignedInt(buf[24]) + (Byte.toUnsignedInt(buf[25]) << 8)
				+ (Byte.toUnsignedInt(buf[26]) << 16) + (Byte.toUnsignedInt(buf[27]) << 24);
		
		channelNum = Byte.toUnsignedInt(buf[22]) + (Byte.toUnsignedInt(buf[23]) << 8);
		
		bps = Byte.toUnsignedInt(buf[34]) + (Byte.toUnsignedInt(buf[35]) << 8);
		
		audioFormat = Byte.toUnsignedInt(buf[20]) + (Byte.toUnsignedInt(buf[21]) << 8);
		
		byte data[] = new byte[size];
		in.read(data);
		in.close();
		
		sampleNum = (size / (channelNum * (bps / 8)));
		
		//duzina audio fajla u ms
		length = sampleNum / (freq*1.0);

		int p = 0;
		samples = new double[sampleNum];
		//if stereo and PCM code = 1
		//CONVERTING RAW DATA INTO SAMPLES
		if (channelNum == 2)
		for (int i = 0; i < size; i += 4){
			int left = data[i] + (data[i+1] << 8);
			int right = data[i+2] + (data[i+3] << 8);
			samples[p++] = (left + right)/2.0;
		} else if (channelNum == 1)
		for (int i = 0; i < size; i+=2){
			samples[p++] = data[i] + (data[i+1] << 8);	
		}
		
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void ispis(String string){
		System.out.println(string);
		for (int i = 0; i <windows.length; i++){
			System.out.print(windows[i]);
		}
		System.out.println();
	}
	
	int upper_power_of_two(int v){
	    v--;
	    v |= v >> 1;
	    v |= v >> 2;
	    v |= v >> 4;
	    v |= v >> 8;
	    v |= v >> 16;
	    v++;
	    return v;

	}
	
	public void setStartFrameIndex(int startFrameIndex) {
		this.startFrameIndex = startFrameIndex;
	}
	
	public void setWordLength(int wordLength) {
		this.wordLength = wordLength;
	}
	
	public ArrayList<Word> getWords() {
		return words;
	}

}
