package es.ucm.fdi.tp.view.gui;

public class ThreadsConfig {
	
	private int numThreads;
	private int timeOut;
	
	public ThreadsConfig() {
		this(1, 500);
	}
	
	public ThreadsConfig(int numThreads, int timeOut) {
		this.numThreads = numThreads;
		this.timeOut = timeOut;
	}
	
	public void setNumThreads(int numThreads) {
		this.numThreads = numThreads;
	}
	
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}
	
	public int getNumThreads() {
		return this.numThreads;
	}
	
	public int getTimeOut() {
		return this.timeOut;
	}

}
