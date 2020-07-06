package adventure.games.prisonbreak.movement;

import adventure.exceptions.LockedRoomException;
import adventure.exceptions.NotAccessibleRoomException;
import adventure.games.prisonbreak.PrisonBreakGame;

class MovementVerbs {

    private final Move movement;
    private final PrisonBreakGame game;

    MovementVerbs(ControllerMovement controller) {
        movement = controller.getMove();
        game = controller.getMove().getGame();
    }

    void nord() throws NotAccessibleRoomException, LockedRoomException {
        if (game.getCurrentRoom().getNorth() != null && !game.getCurrentRoom().getNorth().isLocked()) {
            game.setCurrentRoom(game.getCurrentRoom().getNorth());
            movement.setMove(true);
        } else if (game.getCurrentRoom().getNorth() == null) {
            throw new NotAccessibleRoomException();
        } else if (game.getCurrentRoom().getNorth().isLocked()) {
            throw new LockedRoomException();
        }
    }

    void south() throws NotAccessibleRoomException, LockedRoomException {
        if (game.getCurrentRoom().getSouth() != null && !game.getCurrentRoom().getSouth().isLocked()) {
            game.setCurrentRoom(game.getCurrentRoom().getSouth());
            movement.setMove(true);
        } else if (game.getCurrentRoom().getSouth() == null) {
            throw new NotAccessibleRoomException();
        } else if (game.getCurrentRoom().getSouth().isLocked()) {
            throw new LockedRoomException();
        }
    }

    void east() throws NotAccessibleRoomException, LockedRoomException {
        if (game.getCurrentRoom().getEast() != null && !game.getCurrentRoom().getEast().isLocked()) {
            game.setCurrentRoom(game.getCurrentRoom().getEast());
            movement.setMove(true);
        } else if (game.getCurrentRoom().getEast() == null) {
            throw new NotAccessibleRoomException();
        } else if (game.getCurrentRoom().getEast().isLocked()) {
            throw new LockedRoomException();
        }
    }

    void west() throws NotAccessibleRoomException, LockedRoomException {
        if (game.getCurrentRoom().getWest() != null && !game.getCurrentRoom().getWest().isLocked()) {
            game.setCurrentRoom(game.getCurrentRoom().getWest());
            movement.setMove(true);
        } else if (game.getCurrentRoom().getWest() == null) {
            throw new NotAccessibleRoomException();
        } else if (game.getCurrentRoom().getWest().isLocked()) {
            throw new LockedRoomException();
        }
    }

}
