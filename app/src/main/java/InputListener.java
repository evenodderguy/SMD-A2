/*
changed by: Thur 14:15 workshop team 12
Members:
William Grimsey - 1270044
Even Guo - 1307884
Sophie Su - 1273360
 */

import ch.aplu.jgamegrid.GGKeyListener;

import java.awt.event.KeyEvent;

public class InputListener implements GGKeyListener {
    HumanStrategy humanStrategy;


    public InputListener(HumanStrategy hs) {
        this.humanStrategy = hs;
    }

    /**
     * notify human player to pass
     */
    private void notifyPass(){
        humanStrategy.listenKeyPress();
    }
    @Override
    public boolean keyPressed(KeyEvent keyEvent) {
        System.out.println("key pressed");
        if (keyEvent.getKeyChar() == '\n') {
            notifyPass();
        }
        return false;
    }

    @Override
    public boolean keyReleased(KeyEvent keyEvent) {
        return false;
    }
}