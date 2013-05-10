package org.rom.myfreetv.view;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.WindowAdapter;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.rom.myfreetv.config.Config;

public class PlayerWaiter extends JDialog {

    private static int pas = 200;
    private JProgressBar progress;
    private int ms;
    private WaiterThread runner;

    class WaiterThread extends Thread {

        private boolean cancelled;

        public void run() {
            int i = 0;
            while(!cancelled && i < ms) {
                int percent = (int) ((i * 100.0) / ms);
                if(percent < 0)
                    percent = 0;
                else if(percent > 100)
                    percent = 100;
                progress.setValue(percent);
                try {
                    Thread.sleep(pas);
                } catch(InterruptedException e) {}
                i += pas;
            }
            dispose();
        }

        public void kill() {
            cancelled = true;
        }
    }

    public PlayerWaiter(final int ms) {
        super(MyFreeTV.getInstance(), "TimeShift", true);

//        SkinManager.decore(this,Config.getInstance().getDecoration());

        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowClosing() {
                if(runner != null)
                    runner.kill();
            }
        });

        this.ms = ms;
        setResizable(false);

        JPanel pan = new JPanel();
        pan.setLayout(new BoxLayout(pan, BoxLayout.Y_AXIS));

        Point p = MyFreeTV.getInstance().getLocation();
        Dimension d = MyFreeTV.getInstance().getSize();
        int x1 = (int) p.getX();
        int y1 = (int) p.getY();
        int x2 = x1 + (int) d.getWidth();
        int y2 = y1 + (int) d.getHeight();

        progress = new JProgressBar();

        JPanel top = new JPanel();
        top.add(new JLabel("Mise en mémoire tampon (TimeShift)..."));
        JPanel bottom = new JPanel();
        bottom.add(progress);

        pan.add(top);
        pan.add(bottom);

        setContentPane(pan);

        pack();

        int posX = (x1 + x2 - (int) getSize().getWidth()) / 2;
        int posY = (y1 + y2 - (int) getSize().getHeight()) / 2;

        setLocation(posX, posY);

        runner = new WaiterThread();
        runner.start();

        setVisible(true);
    }

}