package net.shoaibkhan.accessibiltyplusextended.basemod;

import java.util.Map;

public class CustomWait extends Thread {
    private boolean running=false;

    public void run() {
    	while(running) {
    		try {
				if(!AccessibilityPlus.delayThreadMap.isEmpty()) {
					for (Map.Entry<String, Integer> entry : AccessibilityPlus.delayThreadMap.entrySet()) {
						entry.setValue(entry.getValue()-1);
						if(entry.getValue()<=10) {
							System.out.println("removed "+entry.getKey()+" from thread");
							AccessibilityPlus.delayThreadMap.remove(entry.getKey());
						}
					}
				}
				Thread.sleep(1);
			} catch (Exception e) {
			}
    	}
    }

    public void stopThread() {
        running = false;
        interrupt();
    }

    public void startThread(){
        running = true;
        this.start();
    }
}
