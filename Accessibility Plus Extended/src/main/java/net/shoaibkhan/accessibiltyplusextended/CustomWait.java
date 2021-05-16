package net.shoaibkhan.accessibiltyplusextended;

import java.util.Map;

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
            while(HudRenderCallBackClass.fallDetectorFlag > 0 && running){
                try {
                	if(client.world==null) {
                		HudRenderCallBackClass.fallDetectorFlag = 0;
                		return;
                	}
                	if(client.isPaused()) continue;
                    HudRenderCallBackClass.fallDetectorFlag--;
                    Thread.sleep(1);
                } catch (Exception e) {
                }
            }
        } else if(val==2) {
        	HudRenderCallBackClass.entityNarratorFlag = timeOut;
            while(HudRenderCallBackClass.entityNarratorFlag > 0 && running){
                try {
                	if(client.world==null) {
                		HudRenderCallBackClass.entityNarratorFlag = 0;
                		return;
                	}
                    Thread.sleep(1);
                } catch (Exception e) {
                    
                }
                HudRenderCallBackClass.entityNarratorFlag--;
            }
        } else if(val==3) {
        	while(running) {
        		try {
					if(!modInit.ores.isEmpty()) {
						for (Map.Entry<String, Integer> entry : modInit.ores.entrySet()) {
							entry.setValue(entry.getValue()-1);
							if(entry.getValue()<=10) {
								System.out.println("removed "+entry.getKey());
								modInit.ores.remove(entry.getKey());
							}
						}
					}
					Thread.sleep(1);
				} catch (Exception e) {
				}
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
