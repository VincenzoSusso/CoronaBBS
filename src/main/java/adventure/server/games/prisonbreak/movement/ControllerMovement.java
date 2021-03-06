package adventure.server.games.prisonbreak.movement;

import adventure.server.games.prisonbreak.PrisonBreakGame;
import adventure.server.parser.ParserOutput;

import java.io.Serializable;

/**
 * @author Corona-Extra
 */
public class ControllerMovement implements Serializable {
    private final Move move;
    private final MovementVerbs movementVerbs;
    private final BasicVerbs basicVerbs;
    private final AdvancedVerbs advancedVerbs;

    public ControllerMovement(PrisonBreakGame game) {
        this.move = new Move(this, game);
        basicVerbs = new BasicVerbs(this);
        movementVerbs = new MovementVerbs(this);
        advancedVerbs = new AdvancedVerbs(this);
    }

    public Move getMove() {
        return move;
    }

    public MovementVerbs getMovementVerbs() {
        return movementVerbs;
    }

    public BasicVerbs getBasicVerbs() {
        return basicVerbs;
    }

    public AdvancedVerbs getAdvancedVerbs() {
        return advancedVerbs;
    }

    public String nextMove(ParserOutput p) {
        basicVerbs.resetResponse();
        advancedVerbs.resetResponse();
        return move.nextMove(p);
    }
}
