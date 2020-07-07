package adventure.games.prisonbreak;

import adventure.games.GameDescription;
import adventure.games.prisonbreak.movement.ControllerMovement;
import adventure.parser.ParserOutput;
import adventure.type.Inventory;

import static adventure.games.prisonbreak.RoomType.MAIN_CELL;


/**
 * @author Corona-Extra
 */
public class Game extends GameDescription {

    private final ControllerMovement movement;
    private boolean firstTime = false;

    public Game() {
        super(new Objects(), new Rooms(), new Verbs());
        this.movement = new ControllerMovement(this);

        String introduction = "===========================================================================" +
                "======\n" + "\t\t BENVENUTO IN PRISON BREAK!!!\n" + "=====================================" +
                "============================================\n" + "Sei stato arrestato per aver commesso una" +
                " rapina nella banca centrale di New York! La corte giudiziaria ti ha dato 3 anni di carcere e " +
                "sotto tua richiesta sei stato collocato nel carcere di maggiore sicurezza di New York. Tutto" +
                " fa parte di un tuo malefico piano: salvare tuo fratello imprigionato all'interno dello stesso " +
                "carcere, accusato ingiustamente di aver commesso un omicidio. Il tempo non è dalla tua parte, " +
                "domani tuo fratello sarà giustiziato, riuscirai a evadere insieme a lui dal carcere senza farti " +
                "scoprire o uccidere???\n" + "====================================================================" +
                "=============\n";
        setIntroduction(introduction);

        //Set starting room
        setCurrentRoom(getRoom(MAIN_CELL));

        //Set Inventory
        setInventory(new Inventory(5));
    }

    @Override
    public String nextMove(ParserOutput p) {
        return movement.nextMove(p);
    }
}