package adventure;

//fixme import prisonbreak.games.FireHouseGame;

import adventure.exceptions.inputException.InputErrorException;
import adventure.exceptions.inputException.LexicalErrorException;
import adventure.exceptions.inputException.SyntaxErrorException;
import adventure.games.GameDescription;
import adventure.games.prisonbreak.PrisonBreakGame;
import adventure.parser.Parser;
import adventure.parser.ParserIta;
import adventure.parser.ParserOutput;
import adventure.type.VerbType;

import java.util.List;
import java.util.Scanner;

/**
 * ATTENZIONE: l'Engine è molto spartano, in realtà demanda la logica alla
 * classe che implementa GameDescription e si occupa di gestire I/O sul
 * terminale.
 *
 * @author Corona-Extra
 */
public class Engine {
    private final GameDescription game;
    private View view;

    public Engine(GameDescription game, View view) {
        this.game = game;
        this.view = view;
    }

    private void manageEvent() {

    }

    public void run() {
        boolean endGame = false;
        Parser parser = new ParserIta(game.getTokenVerbs(), game.getObjects(), game.getAdjectives());
        List<ParserOutput> listParser;
        System.out.println(game.getCurrentRoom().getName());
        System.out.println("================================================");
        System.out.println(game.getCurrentRoom().getDescription());
        Scanner scanner = new Scanner(System.in, "UTF-8");
        while (!endGame && scanner.hasNextLine()) {
            String input = scanner.nextLine();
            try {
                listParser = parser.parse(input);
                for (ParserOutput p : listParser) {
                    if (p.getVerb() != null && p.getVerb().getVerbType().equals(VerbType.END)
                            && p.getObject().isEmpty()) {
                        System.out.println("Addio!");
                        endGame = true;
                        break;
                    } else {
                        System.out.println(game.nextMove(p));
                        System.out.println("================================================");
                    }
                }
            } catch (LexicalErrorException le) {
                System.out.println("Non ho capito!");
                System.out.println("C'e' qualche parola che non conosco.");
            } catch (SyntaxErrorException se) {
                System.out.println("Non ho capito!");
                System.out.println("Dovresti ripassare un po' la grammatica!");
            } catch (InputErrorException ie) {
                System.out.println("Non ho capito!");
            } catch (Exception e) {
                System.out.println(e.toString());
                e.getMessage();
                e.printStackTrace();
            }
        }
    }

}
