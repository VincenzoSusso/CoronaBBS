package adventure.games.prisonbreak.movement;

import adventure.exceptions.inventoryException.InventoryEmptyException;
import adventure.exceptions.inventoryException.InventoryFullException;
import adventure.exceptions.inventoryException.ObjectNotFoundInInventoryException;
import adventure.exceptions.objectsException.ObjectNotFoundInRoomException;
import adventure.games.prisonbreak.PrisonBreakGame;
import adventure.games.prisonbreak.TokenPerson;
import adventure.type.TokenObject;
import adventure.type.TokenObjectContainer;

import static adventure.games.prisonbreak.ObjectType.*;
import static adventure.games.prisonbreak.RoomType.*;

class BasicVerbs {

    private final PrisonBreakGame game = ControllerMove.getInstance().getGame();
    private final TokenObject object = ControllerMove.getInstance().getObject();
    private final StringBuilder response = ControllerMove.getInstance().getResponse();
    private final short counterFaceUp = ControllerMove.getInstance().getCounterFaceUp();

    void inventory() throws InventoryEmptyException {
        if (!game.getInventory().isEmpty()) {
            response.append("Nel tuo inventario ci sono:\n");
            for (TokenObject o : game.getInventory().getObjects()) {
                response
                        .append(o.getName())
                        .append(": ")
                        .append(o.getDescription())
                        .append("\n");
            }
        }
    }

    void lookAt() {
        if (object != null && (game.getInventory().contains(object)
                || game.getCurrentRoom().getObjects().contains(object)
                || game.getCurrentRoom().getObjects().stream().anyMatch(obj -> obj instanceof TokenPerson
                && ((TokenPerson) obj).getInventory().contains(object)))) {
            response.append(object.getDescription()).append("\n");

        } else if (game.getCurrentRoom().getLook() != null && (object == null || object.getId() == ROOM_OBJ)) {
            response.append(game.getCurrentRoom().getLook()).append("\n");

        } else if (object != null && object.getId() == SCORE_OBJ) {
            //TODO cambiare frase in base allo score
            response.append("Non male, attualmente il tuo punteggio è ").append(game.getScore()).append("\n");
        } else {
            response.append("Non c'è niente di interessante qui.\n");
        }
    }

    void pickUp() throws InventoryFullException, ObjectNotFoundInRoomException {
        if (object != null && object.getId() == HACKSAW && object.isAccept()
                && ((TokenPerson) game.getObject(GENNY_BELLO)).getInventory().contains(object)) {

            //There is the need of the remove's operation from the room of the TokenPerson's objects
            game.getCurrentRoom().removeObject(game.getObject(HACKSAW));
            game.getInventory().add(object);
            response.append("Hai preso ").append(object.getName()).append("!\n");

        } else if (object != null && object.getId() == SCALPEL && game.getCurrentRoom().getId() == INFIRMARY
                && !object.isTaken()) {

            game.getCurrentRoom().removeObject(object);
            game.getInventory().add(object);
            object.setTaken(true);
            game.increaseScore();

            response.append("Hai preso ").append(object.getName()).append("!\n");
            response.append("Fai in fretta perché improvvisamente senti i passi dell’infermiera avvicinandosi " +
                    "alla porta, riesci a prendere il bisturi con te e l’infermiera ti dice che sei guarito" +
                    " e puoi ritornare nella cella visto che l’ora d’aria è finita.\n\n");
            game.setCurrentRoom(game.getRoom(MAIN_CELL));
            game.getInventory().add(game.getObject(MEDICINE));
            response.append("Zzzzzz.....\n\n");
            response.append("Caspita gli antidolorifici ti hanno fatto dormire molto e ti risvegli nella tua " +
                    "cella privo di qualsiasi dolore! Prima di andare via l’infermiera ti ha dato qualche " +
                    "medicinale tra cui un medicinale all’ortica. Guarda nel tuo inventario!\n\n");
            ControllerMove.getInstance().setMove(true);

        } else if (object != null && object.isPickupable() && game.getCurrentRoom().containsObject(object)) {

            if ((object.getId() == SCOTCH || object.getId() == SCREW) && !object.isTaken()) {
                game.increaseScore();
                object.setTaken(true);
            }
            game.getCurrentRoom().removeObject(object);
            game.getInventory().add(object);

            response.append("Hai preso ").append(object.getName()).append("!\n");

        } else if (object == null) {
            response.append("Cosa vorresti prendere di preciso?\n");

        } else if (object.getId() == SCREW && !object.isPickupable()) {
            response.append("Non puoi prendere quella vite se prima non affronti il gruppetto dei detenuti!\n");

        } else if (!game.getCurrentRoom().containsObject(object)) {
            throw new ObjectNotFoundInRoomException();

        } else if (!object.isPickupable()) {
            response.append("Non e' certo un oggetto che si può prendere imbecille!\n");

        } else if (game.getInventory().contains(object)) {
            response.append("Guarda bene nella tua borsa, cretino!\n");
        }
    }

    void remove() throws ObjectNotFoundInInventoryException {
        if (object != null && game.getInventory().contains(object)) {
            game.getCurrentRoom().setObject(object);
            game.getInventory().remove(object);
            response.append("Hai lasciato a terra ").append(object.getName()).append("!\n");

        } else if (object == null) {
            response.append("Cosa vorresti rimuovere dall'inventario?\n");

        } else {
            response.append("L'inventario non ha questo oggetto!\n");
            response.append("L'avrai sicuramente scordato da qualche parte!\n");
            response.append("Che pazienzaaa!!\n");
        }
    }

    void use() throws ObjectNotFoundInInventoryException, InventoryFullException {
        if (object != null && object.isUsable() && game.getCurrentRoom().isObjectUsableHere(object)
                && (game.getInventory().contains(object) || game.getCurrentRoom().containsObject(object))) {

            switch (object.getId()) {
                case SCREW:
                    game.getObject(SINK).setPushable(true);
                    game.setObjectNotAssignedRoom(object);
                    game.getInventory().remove(object);
                    response.append("Decidi di usare il cacciavite, chiunque abbia fissato quel lavandino " +
                            "non aveva una grande forza visto che le viti si svitano facilmente. Adesso che hai " +
                            "rimosso tuttte le viti, noti che il lavandino non è ben fissato\n");
                    game.increaseScore();
                    object.setUsed(true);
                    break;

                case SCOTCH:
                    game.setObjectNotAssignedRoom(object);
                    game.getInventory().remove(object);
                    game.getInventory().add(game.getObject(COMBINATION));
                    response.append("Metti lo scotch sui numeri della porta, dallo scotch noti le impronte dei ultimi " +
                            "tasti schiacciati, ora indovinare il pin segreto sembra molto più semplice!\n");
                    game.increaseScore();
                    object.setUsed(true);
                    break;

                case TOOLS:
                    response.append("Decidi di allenarti per un bel po’ di tempo… alla fine dell’allenamento " +
                            "ti senti già più forte!\n");

                    if (!object.isUsed()) {
                        game.increaseScore();
                        object.setUsed(true);
                    }
                    break;

                case BALL:
                    response.append("Il tempo è denaro, non penso sia il momento adatto per mettersi a giocare.\n");
                    object.setUsed(true);
                    break;

                case SCALPEL:
                    game.setObjectNotAssignedRoom(object);
                    game.getInventory().remove(object);
                    response.append("Riesci subito a tirare fuori il bisturi dalla tasca, il gruppetto lo " +
                            "vede e capito il pericolo decide di lasciare stare (Mettere a rischio la " +
                            "vita per una panchina sarebbe veramente stupido) e vanno via con " +
                            "un'aria di vendetta. Ora sei solo vicino alla panchina.\n");
                    game.getRoom(FRONTBENCH).setDescription("Sei solo vicino alla panchina!");
                    game.getRoom(FRONTBENCH).setLook("E' una grossa panchina in legno un po' malandata, " +
                            "ci sei solo tu nelle vicinanze.");
                    game.getRoom(BENCH).setDescription("Dopo aver usato il bisturi, il giardino si è svuotato, ci sei" +
                            "solo tu qui.");
                    game.getRoom(BENCH).setLook("In lontananza vedi delle panchine tutte vuote!");
                    game.increaseScore();
                    object.setUsed(true);
                    response.append("Riesci subito a tirare fuori il bisturi dalla tasca, il gruppetto lo vede e " +
                            "capito il pericolo decide di lasciare stare (Mettere a rischio la vita per una panchina " +
                            "sarebbe veramente stupido) e vanno via con un'aria di vendetta.\nOra sei solo vicino" +
                            " alla panchina.");
                    game.getCurrentRoom().setDescription("Sei solo vicino alla panchina!");
                    game.getCurrentRoom().setLook("E' una grossa panchina in legno un po' malandata, " +
                            "ci sei solo tu nelle vicinanze.");
                    game.getObject(SCREW).setPickupable(true);
                    ControllerMove.getInstance().increaseCounterFaceUp();
                    break;

                case HACKSAW:
                    if (game.getObject(TOOLS).isUsed()) {
                        game.getRoom(AIR_DUCT_INFIRMARY).setLocked(false);
                        game.setObjectNotAssignedRoom(object);
                        game.getInventory().remove(object);
                        game.getRoom(AIR_DUCT_NORTH).removeObject(game.getObject(DESTROYABLE_GRATE));
                        response.append("Oh no! Il seghetto si è rotto e adesso ci sono pezzi di sega dappertutto," +
                                " per fortuna sei riuscito a rompere la grata\n");
                        response.append("Dopo esserti allenato duramente riesci a tagliare le sbarre " +
                                "con il seghetto, puoi proseguire nel condotto e capisci che quel condotto" +
                                " porta fino all’infermeria.\n");
                        game.increaseScore();
                        game.increaseScore();
                        object.setUsed(true);
                    }
                    break;

                case SINK:
                    response.append("Decidi di lavarti le mani e il viso, l’igiene prima di tutto!\n");
                    object.setUsed(true);
                    break;

                case GENERATOR_OBJ:
                    if (object.isUsed() || game.getObject(BUTTON_GENERATOR).isPush()) {
                        response.append("Il generatore è gia stato usato, fai in fretta!!\n");

                    } else {
                        game.getObject(BUTTON_GENERATOR).setPush(true);
                        game.getRoom(DOOR_ISOLATION).setLocked(false);
                        game.getObject(LIGHTS).setOn(false);
                        response.append("Sembra che tutto il carcere sia nell’oscurità! È stata una bella mossa" +
                                " la tua, peccato che i poliziotti prevedono queste bravate e hanno un generatore" +
                                " di corrente ausiliario che si attiverà dopo un minuto dal blackout!\n");
                        game.increaseScore();
                        object.setUsed(true);
                    }
                    break;

                case ACID:
                    game.getRoom(ENDGAME).setLocked(false);
                    game.setObjectNotAssignedRoom(object);
                    game.getInventory().remove(object);
                    response.append("Adesso la finestra presenta un buco, sarebbe meglio infilarsi dentro!\n");
                    game.increaseScore();
                    game.increaseScore();
                    game.increaseScore();
                    game.increaseScore();
                    object.setUsed(true);
                    break;

                case COMBINATION:
                    if (!object.isUsed()) {
                        game.setObjectNotAssignedRoom(object);
                        game.getInventory().remove(object);
                        game.getRoom(ISOLATION).setLocked(false);
                        response.append("La porta si apre! Puoi andare a est per entrare dentro l'isolamento oppure" +
                                " tornare indietro anche se hai poco tempo a disposizione!\n");
                        game.increaseScore();
                        object.setUsed(true);
                    }
                    break;

                case BED:
                    response.append("Buona notte fiorellino!\n");
                    object.setUsed(true);
                    break;

                case POSTER:
                    object.setUsable(true);

                    // DON'T CHANGE THE ORDER
                    if (counterFaceUp == 0) {
                        response.append("Sei appena arrivato in carcere, perché non ti fai conoscere prendendo parte ad " +
                                "una rissa in giardino?\n");
                    } else if (!game.getObject(SCALPEL).isUsed()
                            && counterFaceUp == 1) {
                        response.append("Eccoti un consiglio: Dovresti andare nel giardino e utilizzare per bene quel " +
                                "bisturi, coraggio prendi la tua vendetta!\n");
                    } else if (!game.getInventory().contains(game.getObject(SCREW)) && !game.getObject(SCREW).isUsed()) {
                        response.append("Adesso dovresti prendere la vite, ti servirà molto per portare al termine" +
                                "la tua missione!\n");
                    } else if (game.getInventory().contains(game.getObject(SCREW))) {
                        response.append("Dovresti provare ad utilizzare la vite che hai in questa stanza, chissà cosa " +
                                "potrà capitare...\n");
                    } else if (game.getObject(SCREW).isUsed() && !game.getObject(SINK).isPush()) {
                        response.append("I tuoi genitori hanno anche figli normali? Come fai a non comprendere che è " +
                                "necessario spostare il lavandino!!\n");
                    } else if (((TokenPerson) game.getObject(GENNY_BELLO)).getInventory().contains(game.getObject(HACKSAW)) &&
                            !game.getInventory().contains(game.getObject(HACKSAW))) {
                        response.append("Dovresti cercare un utensile per rompere quelle grate che ti impediscono il " +
                                "passaggio!\n");
                    } else if (!game.getObject(HACKSAW).isUsed() && !game.getObject(TOOLS).isUsed() &&
                            game.getInventory().contains(game.getObject(HACKSAW))) {
                        response.append("Adesso che hai il seghetto dovresti aumentare un pò la tua massa muscolare\n");
                    } else if (!game.getObject(HACKSAW).isUsed() && game.getObject(TOOLS).isUsed()) {
                        response.append("Ti vedo in forma adesso, ora sarai sicuramente in grado di distruggere " +
                                "quella grata che è presente nel condotto d'aria\n");
                    } else if (game.getObject(HACKSAW).isUsed() && !game.getObject(SCOTCH).isUsed()
                            && !game.getInventory().contains(game.getObject(SCOTCH))) {
                        response.append("Nel condotto d'aria c'è qualcosa che ti tornerà utile più tardi!\n");
                    } else if (!game.getObject(GENERATOR_OBJ).isUsed() && !game.getObject(MEDICINE).isGiven()) {
                        response.append("Ti consiglio di cercare un pò nel condotto d'aria e spegnere il generatore" +
                                " ci vorrà un pò di buio per salvare tuo fratello\n");
                    } else if (game.getObject(GENERATOR_OBJ).isUsed() && !game.getObject(MEDICINE).isGiven()) {
                        response.append("Adesso che la prigione è buia potrai andare vicino alla porta d'isolamento e " +
                                "usare quello scotch che hai preso precedentemente\n");
                    } else if (game.getObject(SCOTCH).isUsed() && !game.getObject(MEDICINE).isGiven()) {
                        response.append("Dovresti usare quella combinazione che hai ottenuto utilizzando lo scotch" +
                                "nella stanza che precede l'isolamento, fai in fretta il tempo a tua disposizione" +
                                "sta per scadere!!!!\n");
                    } else if (game.getObject(COMBINATION).isUsed() && !game.getObject(MEDICINE).isGiven()) {
                        response.append("Cosa ci fai qui?? Dovresti dare la medicina a tuo fratello!!!!\n");
                    } else if (game.getObject(MEDICINE).isGiven()) {
                        response.append("Il tuo piano è quasi terminato, vai con Genny Bello in infermeria passando dal" +
                                "passaggio segreto! Buona fortuna, te ne servirà molta!!!!!!!!!\n");
                    } else {
                        response.append("Mi dispiace ma non ho suggerimenti da darti attualmente!!\n");
                    }
                    break;
            }

        } else if (object == null) {
            response.append("Sei sicuro di non voler usare niente?\n");
        } else if (!object.isUsable()) {
            response.append("Mi dispiace ma questo oggetto non si può utilizzare\n");
        } else if (!game.getInventory().contains(object) && !game.getCurrentRoom().containsObject(object)) {
            response.append("Io non vedo nessun oggetto di questo tipo qui!\n");
        } else if (!game.getCurrentRoom().isObjectUsableHere(object)) {
            response.append("C’è tempo e luogo per ogni cosa, ma non ora.\n");
        }

        if (object != null && object.getId() == HACKSAW
                && !game.getObject(TOOLS).isUsed() && game.getCurrentRoom().isObjectUsableHere(game.getObject(HACKSAW))) {
            response.append("Il seghetto sembra molto arrugginito e non riesci a tagliare le sbarre " +
                    "della grata! In realtà la colpa non è totalmente del seghetto ma anche la tua " +
                    "poiché sei molto stanco e hai poca forza nelle braccia!\n");
        }
    }

    void open() throws ObjectNotFoundInRoomException {
        if (object != null
                && object.isOpenable()
                && !object.isOpen()
                && (game.getCurrentRoom().containsObject(object))) {
            if (!(object instanceof TokenObjectContainer)) {
                response.append("Hai aperto ").append(object.getName()).append("!\n");
            } else if (!object.isOpen()) {
                response.append("Hai aperto ").append(object.getName()).append("!\n");
                response.append("Contiene: \n");
                for (TokenObject obj : ((TokenObjectContainer) object).getObjects()) {
                    response.append(obj.getName()).append(": ").append(obj.getDescription()).append("\n");
                }
            }
            object.setOpen(true);
        } else if (object == null) {
            response.append("Cosa vorresti aprire di preciso?\n");
        } else if (!game.getCurrentRoom().containsObject(object)) {
            throw new ObjectNotFoundInRoomException();
        } else if (!object.isOpenable()) {
            response.append("Sei serio? Vorresti veramente aprirlo?!\n");
            response.append("Sei fuori di testa!\n");
        } else if (object.isOpen()) {
            response.append("E' gia' aperto testa di merda!\n");
        }
    }

    void close() throws ObjectNotFoundInRoomException {
        if (object != null
                && object.isOpenable()
                && object.isOpen()
                && (game.getCurrentRoom().containsObject(object))) {

            response.append("Hai chiuso ").append(object.getName()).append("!\n");
            object.setOpen(false);

        } else if (object == null) {
            response.append("Cosa vorresti chiudere di preciso?\n");
        } else if (!game.getCurrentRoom().containsObject(object)) {
            throw new ObjectNotFoundInRoomException();
        } else if (!object.isOpenable()) {
            response.append("Sei serio? Vorresti veramente chiuderlo?!\n");
            response.append("Sei fuori di testa!\n");
        } else if (!object.isOpen()) {
            response.append("E' gia' chiuso testa di merda!\n");
        }
    }

    void pushAndPull() throws ObjectNotFoundInRoomException {
        if (object != null && object.isPushable() && game.getCurrentRoom().containsObject(object)) {

            switch (object.getId()) {
                case LADDER:
                    if (game.getCurrentRoom().getId() == PASSAGE_SOUTH) {
                        game.getRoom(SECRET_PASSAGE).setObject(object);
                        game.getRoom(PASSAGE_SOUTH).removeObject(object);
                        response.append("La scala è stata spinta fino alla stanza a nord!\n");
                        game.increaseScore();
                    } else if (game.getCurrentRoom().getId() == SECRET_PASSAGE) {
                        game.getRoom(PASSAGE_NORTH).setObject(object);
                        game.getRoom(SECRET_PASSAGE).removeObject(object);
                        response.append("La scala è stata spinta fino alla stanza a nord e si è bloccata lì!\n");
                        game.increaseScore();
                    } else {
                        response.append("La scala è bloccata! Non esiste alcun modo per spostarla!\n");
                    }
                    break;

                case SINK:
                    if (game.getCurrentRoom().getId() == MAIN_CELL) {
                        if (object.isPush()) {
                            response.append("Il Lavandino è già stato spostato!\n");
                        } else {
                            object.setPush(true);
                            game.getRoom(SECRET_PASSAGE).setLocked(false);
                            response.append("Oissà!\n");
                            game.increaseScore();
                            game.increaseScore();
                        }
                    }
                    break;

                case PICTURE:
                    if (game.getCurrentRoom().getId() == INFIRMARY) {
                        // picture pushed
                        if (object.isPush()) {
                            response.append("Il quadro è già stato spostato!\n");
                        } else {
                            object.setPush(true);
                            game.getCurrentRoom().setObject(game.getObject(OLD_AIR_DUCT));
                            game.getObjectNotAssignedRoom().remove(game.getObject(OLD_AIR_DUCT));
                            response.append(game.getObject(OLD_AIR_DUCT).getDescription()).append("\n");
                        }
                    }
                    break;

                case BUTTON_GENERATOR:
                    if (game.getCurrentRoom().getId() == GENERATOR) {
                        // botton pushed
                        if (object.isPush()) {
                            response.append("Il pulsante è già stato premuto! Fai in fretta!!!\n");
                        } else {
                            object.setPush(true);
                            game.getObject(LIGHTS).setOn(false);
                            game.getObject(GENERATOR_OBJ).setUsed(true);
                            game.getRoom(DOOR_ISOLATION).setLocked(false);
                            response.append("Sembra che tutto il carcere sia nell’oscurità! È stata una bella mossa" +
                                    " la tua, peccato che i poliziotti prevedono queste bravate e hanno un " +
                                    "generatore di corrente ausiliario che si attiverà dopo un minuto dal blackout!\n");
                            game.increaseScore();
                            game.increaseScore();
                        }
                    }
                    break;
            }

        } else if (object == null) {
            response.append("Cosa vuoi spostare? L'aria?!?\n");
        } else if (!game.getCurrentRoom().containsObject(object)) {
            throw new ObjectNotFoundInRoomException();
        } else if (!object.isPushable()) {
            response.append("Puoi essere anche Hulk ma quell'oggetto non si può spostare!!!\n");
        }
    }

    void turnOn() {
        if (object != null && object.isTurnOnAble()) {
            // lights case
            if (game.getCurrentRoom().getId() == GENERATOR) {
                // lights turnOFF
                if (object.isOn()) {
                    response.append("Le luci sono già accese!\n");
                } else {
                    response.append("Le luci si accenderanno da sole tra qualche minuto, non avere paura!\n");
                }
            } else {
                response.append("Non puoi accendere nulla qui!\n");
            }
        } else if (object == null) {
            response.append("Cosa vuoi accendere esattamente???\n");
        } else if (!object.isTurnOnAble()) {
            response.append("Come puoi accendere questo oggetto???\n");
        }
    }

    void turnOff() {
        if (object != null && object.isTurnOnAble()) {
            // lights case
            if (game.getCurrentRoom().getId() == GENERATOR) {
                // lights turnOFF
                if (!object.isOn()) {
                    response.append("Il pulsante è già stato premuto! Fai in fretta!!!\n");
                } else {
                    game.getObject(BUTTON_GENERATOR).setPush(true);
                    game.getObject(GENERATOR_OBJ).setUsable(true);
                    object.setOn(false);
                    game.getRoom(DOOR_ISOLATION).setLocked(false);
                    response.append("Sembra che tutto il carcere sia nell’oscurità! È stata una bella mossa" +
                            " la tua, peccato che i poliziotti prevedono queste bravate e hanno un generatore" +
                            " di corrente ausiliario che si attiverà dopo un minuto dal blackout!\n");
                    game.increaseScore();
                    game.increaseScore();
                }
            } else {
                response.append("Non puoi spegnere nulla qui!\n");
            }
        } else if (object == null) {
            response.append("Cosa vuoi spegnere esattamente???\n");
        } else if (!object.isTurnOnAble()) {
            response.append("Come puoi spegnere questo oggetto???\n");
        }
    }

    void end() {
        response.append("Non puoi usare quell'oggetto per uscire!\n");
    }


}