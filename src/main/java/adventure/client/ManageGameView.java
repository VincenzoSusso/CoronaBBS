package adventure.client;

import adventure.utils.Utils;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import static adventure.utils.Utils.*;

/**
 * @author Corona-Extra
 */
public class ManageGameView {
    private final BufferedReader in;
    private final PrintWriter out;
    private final GameView view = new GameView();

    public ManageGameView(BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
        manageEvent();
        initView();
        setVisibleWindow(true);
    }

    public void disposeWindow() {
        view.getJFmainFrame().setVisible(false);
        view.getJFmainFrame().dispose();
    }

    public void setVisibleWindow(boolean value) {
        view.getJFmainFrame().setVisible(value);
    }

    public GameView getView() {
        return view;
    }

    public void run() throws IOException {
        while (this.getView().getJFmainFrame().isVisible()) {
            manageInput(in.readLine());
        }
    }

    private void manageInput(String string) {
        if (string != null && string.contains(Utils.SEPARATOR_CHARACTER_STRING)) {
            String score = string.substring(string.indexOf(Utils.SEPARATOR_CHARACTER_STRING) + 1);
            view.getJTextAreaScore().setText(score);

            String textToAppend = string.substring(0, string.indexOf(Utils.SEPARATOR_CHARACTER_STRING));
            if (!textToAppend.isEmpty()) {
                view.getJToutputArea().append(textToAppend + System.lineSeparator());
            }
        } else if (string != null && !string.isEmpty()) {
            view.getJToutputArea().append(string + System.lineSeparator());
        }
    }

    private void initView() {
        view.setJButtonLook("Guarda");
        view.getJButtonLook().setToolTipText("Clicca qui per guardare la stanza");
        view.setJButtonInventory("Inventario");
        view.getJButtonInventory().setToolTipText("Clicca qui per guardare dentro il tuo inventario");
        view.setJButtonEnter("Invio");
        view.getJButtonEnter().setToolTipText("Clicca qui per inviare il tuo comando");
        view.setJButtonNorth("Nord");
        view.getJButtonNorth().setToolTipText("Clicca qui per andare a Nord");
        view.setJButtonSouth("Sud");
        view.getJButtonSouth().setToolTipText("Clicca qui per andare a Sud");
        view.setJButtonEast("Est");
        view.getJButtonEast().setToolTipText("Clicca qui per andare ad Est");
        view.setJButtonWest("Ovest");
        view.getJButtonWest().setToolTipText("Clicca qui per andare ad Ovest");
        view.setJLabelScore("Score");
        view.getJTextAreaScore().setToolTipText("Qui c'è il tuo punteggio attuale");
    }

    private void manageEvent() {
        actionListenerWindow();
        actionListenerButtonNorth();
        actionListenerButtonSouth();
        actionListenerButtonEast();
        actionListenerButtonWest();
        actionListenerButtonInventory();
        actionListenerButtonLook();
        actionListenerButtonEnter();
        actionListenerInputField();
    }

    private void closeGame() {
        int input;
        int inputSave;

        out.println(EXIT);

        input = JOptionPane.showConfirmDialog(view.getJFmainFrame(), "Sei sicuro di voler uscire " +
                "dal gioco?", "Esci", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (input == JOptionPane.YES_OPTION) {
            out.println(OK_EXIT);
            inputSave = JOptionPane.showConfirmDialog(view.getJFmainFrame(), "Vuoi salvare la partita? " +
                            "Eventuali salvataggi precedenti saranno sovrascritti", "Esci",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (inputSave == JOptionPane.YES_OPTION) {
                out.println(SAVE_GAME);
            } else {
                out.println(NO_SAVE_GAME);
            }
            disposeWindow();
        } else {
            out.println(NO_EXIT);
        }
    }

    private void actionListenerWindow() {
        view.getJFmainFrame().addWindowListener(new WindowListener() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeGame();
            }

            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }

    private void actionListenerButtonNorth() {
        view.getJButtonNorth().addActionListener(e -> out.println("Nord"));
    }

    private void actionListenerButtonSouth() {
        view.getJButtonSouth().addActionListener(e -> out.println("Sud"));
    }

    private void actionListenerButtonEast() {
        view.getJButtonEast().addActionListener(e -> out.println("Est"));
    }

    private void actionListenerButtonWest() {
        view.getJButtonWest().addActionListener(e -> out.println("Ovest"));
    }

    private void actionListenerButtonInventory() {
        view.getJButtonInventory().addActionListener(e -> out.println("Inventario"));
    }

    private void actionListenerButtonLook() {
        view.getJButtonLook().addActionListener(e -> out.println("Guarda"));
    }

    private void actionListenerButtonEnter() {
        view.getJButtonEnter().addActionListener(ev -> {
            out.println(view.getJTinputField().getText());
            view.getJTinputField().setText("");
        });
    }

    private void actionListenerInputField() {
        view.getJTinputField().addActionListener(ev -> {
            out.println(view.getJTinputField().getText());
            view.getJTinputField().setText("");
        });
    }

}
