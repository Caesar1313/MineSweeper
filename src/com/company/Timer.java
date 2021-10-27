package com.company;

public class Timer implements Runnable{
    public static int time;
    private static Timer t = null;
    public static boolean firstRun = true;

    public static Timer getTimer(){
        if(t == null)
            t = new Timer();
        return t;
    }

    @Override
    public void run() {
        if (firstRun)
            for (int i = 0; i <= 1000000; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.err.println("Interrupted!");
                }
                time++;
            }
    }
}
