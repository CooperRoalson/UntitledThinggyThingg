package untitled_thinggy_thingg;


import untitled_thinggy_thingg.core.GameManager;

/**
 * The main class. It creates a thread using the GameManager and runs it.
 */
public class Main {
    public static void main(String[] args) {
        Thread thread = new Thread(GameManager.getInstance());
        thread.start();
    }    
}