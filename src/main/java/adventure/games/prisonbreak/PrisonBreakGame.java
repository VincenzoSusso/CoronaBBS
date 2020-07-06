package adventure.games.prisonbreak;

import adventure.games.GameDescription;
import adventure.games.prisonbreak.movement.ControllerMovement;
import adventure.parser.ParserOutput;
import adventure.type.Inventory;

import static adventure.games.prisonbreak.RoomType.MAIN_CELL;


/**
 * @author Corona-Extra
 */
public class PrisonBreakGame extends GameDescription {

    private ControllerMovement movement;
    private boolean firstTime = false;

    public PrisonBreakGame() {
        super(new PrisonBreakObjects(), new PrisonBreakRooms(), new PrisonBreakVerbs());
        String introduction = "===========================================================================" +
                "======\n" + "\t\t BENVENUTO IN PRISON BREAK!!!\n" + "=====================================" +
                "============================================\n" + "Sei stato arrestato per aver commesso una" +
                " rapina nella banca centrale di New York! La corte giudiziaria ti ha dato 3 anni di carcere e " +
                "sotto tua richiesta sei stato collocato nel carcere di maggiore sicurezza di New York. Tutto" +
                " fa parte di un tuo malefico piano: salvare tuo fratello imprigionato all'interno dello stesso " +
                "carcere, accusato ingiustamente di aver commesso un omicidio. Il tempo non è dalla tua parte, " +
                "domani tuo fratello sarà giustiziato, riuscirai a evadere insieme a lui dal carcere senza farti " +
                "scoprire o uccidere???\n" + "====================================================================" +
                "=============\n" + getCurrentRoom().getName() + "\n" + "======================================" +
                "===========================================\n" + getCurrentRoom().getDescription() + "\n" +
                "=============================================================================" +
                "====\n";
        init();
        setIntroduction(introduction);

        //Set starting room
        setCurrentRoom(getRoom(MAIN_CELL));

        //Set Inventory
        setInventory(new Inventory(5));
    }

    private void init() {
        getGameVerbs().initVerbs(this);
        getGameRooms().initRooms(this);
        getGameObjects().initObjects(this);
        this.movement = new ControllerMovement(this);
    }

    @Override
    public String nextMove(ParserOutput p) {
        return movement.nextMove(p);
    }
}
