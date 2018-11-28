package uet.oop.bomberman.gui.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import uet.oop.bomberman.gui.Frame;
import uet.oop.bomberman.Game;
import com.carlosflorencio.bomberman.gui.InfoDialog;

public class Help extends JMenu {

    public Help(Frame frame) {
        super("Help");

        /*
		 * How to play
         */
        JMenuItem instructions = new JMenuItem("How to play");
        instructions.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
        instructions.addActionListener(new MenuActionListener(frame));
        add(instructions);

        /*
		 * Credits
         */
        JMenuItem about = new JMenuItem("About");
        about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
        about.addActionListener(new MenuActionListener(frame));
        add(about);

    }

    class MenuActionListener implements ActionListener {

        public Frame _frame;

        public MenuActionListener(Frame frame) {
            _frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getActionCommand().equals("How to play")) {
                new InfoDialog(_frame, "How to Play", "Movement: W,A,S,D or UP,DOWN, RIGHT, LEFT\nPut Bombs: SHIFT, X", JOptionPane.QUESTION_MESSAGE);
            }

            if (e.getActionCommand().equals("About")) {
                new InfoDialog(_frame, "About", "Version: " + Game.VERSION + "\nAuthor: Nguyễn Giang\nWebsite: ", JOptionPane.INFORMATION_MESSAGE);
            }

        }
    }
}
