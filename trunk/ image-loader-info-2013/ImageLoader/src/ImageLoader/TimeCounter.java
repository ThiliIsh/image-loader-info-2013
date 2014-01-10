package ImageLoader;

public class TimeCounter {
	private long startTime_ms;
	private long startTime_ns;
	private long stopTime_ms;
	private long stopTime_ns;
	private String description;
	
	public TimeCounter(){
		
	}

	public TimeCounter(String name){
		this();
		description = name;
	}


	public void start(){
		startTime_ms = System.currentTimeMillis();
		startTime_ns = System.nanoTime();
	}
	public void stop(){
		stopTime_ms = System.currentTimeMillis();
		stopTime_ns = System.nanoTime();
	}
	public void printMS(){
		System.out.println("Runtime in ms: " + (stopTime_ms-startTime_ms));
	}
	public void printNS(){
		System.out.println("Runtime in ns: " + (stopTime_ns-startTime_ns));
	}
	public void stopPrMS(){
		stop();
		printMS();
	}
	public void stopPrNS(){
		stop();
		printNS();
	}
	public long getMS(){
		return (stopTime_ms-startTime_ms);
	}
	public long getNS(){
		return (stopTime_ns-startTime_ns);
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
