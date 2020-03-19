package app.processing;

public class Word {

	private int start, length;
	
	public Word(int start, int length) {
		this.start = start;
		this.length = length;
	}
	
	public int getLength() {
		return length;
	}
	
	public int getStart() {
		return start;
	}
	
}
