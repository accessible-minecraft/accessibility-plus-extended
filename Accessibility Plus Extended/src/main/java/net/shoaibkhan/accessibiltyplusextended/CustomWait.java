package net.shoaibkhan.accessibiltyplusextended;

import net.minecraft.client.MinecraftClient;

public class CustomWait extends Thread {
    private int timeOut, val;
    private MinecraftClient client;
    private boolean running=false;

    public void setWait(int timeOut, int val, MinecraftClient client) {
        this.timeOut = timeOut;
        this.val = val;
        this.client = client;
    }

    public void run() {
		if (val==1) {
            HudRenderCallBackClass.fallDetectorFlag = timeOut;
            System.out.println(HudRenderCallBackClass.fallDetectorFlag);
            while(HudRenderCallBackClass.fallDetectorFlag > 0 && running){
                try {
                	if(client.world==null) {
                		HudRenderCallBackClass.fallDetectorFlag = 0;
                		return;
                	}
                	if(client.isPaused()) continue;
                    Thread.sleep(1);
                } catch (Exception e) {
                    
                }
                HudRenderCallBackClass.fallDetectorFlag--;
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
