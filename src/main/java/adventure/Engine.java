package adventure;

import adventure.exceptions.inputException.InputErrorException;
import adventure.exceptions.inputException.LexicalErrorException;
import adventure.exceptions.inputException.SyntaxErrorException;
import adventure.games.GameDescription;
import adventure.parser.Parser;
import adventure.parser.ParserOutput;
import adventure.type.VerbType;

import java.util.List;

/**
 * @author Corona-Extra
 */
public class Engine {
    private final GameDescription game;
    private final View view;
    private final Parser parser;

    public Engine(GameDescription game, View view, Parser parser) {
        this.game = game;
        this.view = view;
        this.parser = parser;
        manageEvent();
    }

    public Parser getParser() {
        return parser;
    }

    private void input(String string) {
        try {
            List<ParserOutput> listParser = parser.parse(string);
            for (ParserOutput p : listParser) {
                if (p.getVerb() != null && p.getVerb().getVerbType().equals(VerbType.END)
                        && p.getObject().isEmpty()) {
                    view.getjTextArea1().append("Addio!");
                    view.getJframe().dispose();
                    break;
                } else {
                    view.getjTextArea2().setText(Integer.toString(game.getScore()));
                    view.getjTextArea1().append(game.nextMove(p) + "\n");
                    view.getjTextArea1().append("====================================================================" +
                            "=============\n");
                }
            }
        } catch (LexicalErrorException e) {
            view.getjTextArea1().append("Non ho capito!\n");
            view.getjTextArea1().append("C'e' qualche parola che non conosco.\n");
            view.getjTextArea1().append("=============================================================================" +
                    "====\n");
        } catch (SyntaxErrorException e) {
            view.getjTextArea1().append("Non ho capito!\n");
            view.getjTextArea1().append("Dovresti ripassare un po' la grammatica!\n");
            view.getjTextArea1().append("=============================================================================" +
                    "====\n");
        } catch (InputErrorException e) {
            view.getjTextArea1().append("Non ho capito!\n");
            view.getjTextArea1().append("=============================================================================" +
                    "====\n");
        } catch (Exception e) {
            view.getjTextArea1().append(e.toString() + "\n");
            e.getMessage();
            e.printStackTrace();
        }
    }

    private void manageEvent() {
        actionListenerButtonNorth();
        actionListenerButtonSouth();
        actionListenerButtonEast();
        actionListenerButtonWest();
        actionListenerButtonInventory();
        actionListenerButtonLook();
        actionListenerButtonEnter();
        actionListenerInputField();
    }

    private void actionListenerButtonNorth() {
        view.getButtonNorth().addActionListener(e -> input("Nord"));
    }

    private void actionListenerButtonSouth() {
        view.getButtonSouth().addActionListener(e -> input("Sud"));
    }

    private void actionListenerButtonEast() {
        view.getButtonEast().addActionListener(e -> input("Est"));
    }

    private void actionListenerButtonWest() {
        view.getButtonWest().addActionListener(e -> input("Ovest"));
    }

    private void actionListenerButtonInventory() {
        view.getButtonInventory().addActionListener(e -> input("Inventario"));
    }

    private void actionListenerButtonLook() {
        view.getButtonLook().addActionListener(e -> input("Guarda"));
    }

    private void actionListenerButtonEnter() {
        view.getButtonEnter().addActionListener(ev -> {
            input(view.getInputField().getText());
            view.getInputField().setText("");
        });
    }

    private void actionListenerInputField() {
        view.getInputField().addActionListener(ev -> {
            input(view.getInputField().getText());
            view.getInputField().setText("");
        });
    }

    public void run() {
        if (game.getIntroduction() != null) {
            view.getjTextArea1().append(game.getIntroduction());
        }

        view.getjTextArea1().append(game.getCurrentRoom().getName() +
                "\n" + "======================================" +
                "===========================================\n" +
                game.getCurrentRoom().getDescription() + "\n" +
                "=================================================================================\n");
    }

}
