package app.processing;

import java.text.DecimalFormat;
import java.util.Objects;

public class DFT {

	public static void DFT(int dir, int m, double x1[], double y1[]){
		DecimalFormat df = new DecimalFormat("#.000000");
		int i,k;
		double arg;
		double cosarg, sinarg;
		double x2[] = new double[m];
		double y2[] = new double[m];
		
		for (i = 0; i < m; i++){
			x2[i] = 0;
			y2[i] = 0;
			arg = - dir * 2.0 * Math.PI * (double)i / (double)m;
			for (k = 0; k < m; k ++){
				cosarg = Math.cos(k * arg);
				sinarg = Math.sin(k * arg);
				x2[i] += (x1[k] * cosarg - y1[k] * sinarg);
				y2[i] += (x1[k] * sinarg + y1[k] * cosarg);
			}
		}
		if (dir == 1){
			for (i = 0; i < m; i++){
				x1[i] = Double.parseDouble(df.format(x2[i] / (double)m));
				y1[i] = Double.parseDouble(df.format(y2[i] / (double)m));
			}
		} else {
			for (i = 0; i < m; i++){
				x1[i] = Double.parseDouble(df.format(x2[i]));
				y1[i] = Double.parseDouble(df.format(y2[i]));
			}
		}
		
	}
	
	public static double[] DCTtransform(double[] vector) {
		Objects.requireNonNull(vector);
		double[] result = new double[vector.length];
		double factor = Math.PI / vector.length;
		for (int i = 0; i < vector.length; i++) {
			double sum = 0;
			for (int j = 0; j < vector.length; j++)
				sum += vector[j] * Math.cos((j + 0.5) * i * factor);
			result[i] = sum;
		}
		return result;
	}
	
}
