package adventure.games.prisonbreak;

import adventure.exceptions.LockedRoomException;
import adventure.exceptions.NotAccessibleRoomException;
import adventure.exceptions.inventoryException.InventoryEmptyException;
import adventure.exceptions.inventoryException.InventoryFullException;
import adventure.exceptions.inventoryException.ObjectNotFoundInInventoryException;
import adventure.exceptions.objectsException.ObjectNotFoundInRoomException;
import adventure.exceptions.objectsException.ObjectsAmbiguityException;
import adventure.games.GameDescription;
import adventure.parser.ParserOutput;
import adventure.type.Inventory;
import adventure.type.TokenObject;
import adventure.type.TokenObjectContainer;
import adventure.type.VerbType;

import java.io.PrintStream;

import static adventure.games.prisonbreak.ObjectType.*;
import static adventure.games.prisonbreak.RoomType.*;


/**
 * @author Corona-Extra
 */
public class PrisonBreakGame extends GameDescription {

    private short counterFaceUp = 0;

    public PrisonBreakGame() {
        super(new PrisonBreakObjects(), new PrisonBreakRooms(), new PrisonBreakVerbs());



        //Set starting room
        setCurrentRoom(getRoom(MAIN_CELL));

        //Set Inventory
        setInventory(new Inventory(5));
    }

    @Override
    public String nextMove(ParserOutput p) {
        StringBuilder response = new StringBuilder();
        TokenObject object;
        boolean move = false;
        boolean mixed = false;

        try {
            object = getCorrectObject(p.getObject());
            if (p.getVerb().getVerbType().equals(VerbType.NORD)) {
                if (getCurrentRoom().getNorth() != null && !getCurrentRoom().getNorth().isLocked()) {
                    setCurrentRoom(getCurrentRoom().getNorth());
                    move = true;
                } else if (getCurrentRoom().getNorth() == null) {
                    throw new NotAccessibleRoomException();
                } else if (getCurrentRoom().getNorth().isLocked()) {
                    throw new LockedRoomException();
                }

            } else if (p.getVerb().getVerbType().equals(VerbType.SOUTH)) {
                if (getCurrentRoom().getSouth() != null && !getCurrentRoom().getSouth().isLocked()) {
                    setCurrentRoom(getCurrentRoom().getSouth());
                    move = true;
                } else if (getCurrentRoom().getSouth() == null) {
                    throw new NotAccessibleRoomException();
                } else if (getCurrentRoom().getSouth().isLocked()) {
                    throw new LockedRoomException();
                }

            } else if (p.getVerb().getVerbType().equals(VerbType.EAST)) {
                if (getCurrentRoom().getEast() != null && !getCurrentRoom().getEast().isLocked()) {
                    setCurrentRoom(getCurrentRoom().getEast());
                    move = true;
                } else if (getCurrentRoom().getEast() == null) {
                    throw new NotAccessibleRoomException();
                } else if (getCurrentRoom().getEast().isLocked()) {
                    throw new LockedRoomException();
                }

            } else if (p.getVerb().getVerbType().equals(VerbType.WEST)) {
                if (getCurrentRoom().getWest() != null && !getCurrentRoom().getWest().isLocked()) {
                    setCurrentRoom(getCurrentRoom().getWest());
                    move = true;
                } else if (getCurrentRoom().getWest() == null) {
                    throw new NotAccessibleRoomException();
                } else if (getCurrentRoom().getWest().isLocked()) {
                    throw new LockedRoomException();
                }

            } else if (p.getVerb().getVerbType().equals(VerbType.INVENTORY)) {
                if (!getInventory().isEmpty()) {
                    response.append("Nel tuo inventario ci sono:");
                    for (TokenObject o : getInventory().getObjects()) {
                        response.append(o.getName()).append(": ").append(o.getDescription());
                    }
                }

            } else if (p.getVerb().getVerbType().equals(VerbType.LOOK_AT)) {
                if (object != null
                        && (getInventory().contains(object) || getCurrentRoom().getObjects().contains(object)
                        || getCurrentRoom().getObjects().stream()
                        .anyMatch(obj -> obj instanceof TokenPerson
                                && ((TokenPerson) obj).getInventory().contains(object)))) {
                    response.append(object.getDescription());
                } else if (getCurrentRoom().getLook() != null && (object == null || object.getId() == ROOM_OBJ)) {
                    response.append(getCurrentRoom().getLook());
                } else if (object != null && object.getId() == SCORE_OBJ) {
                    //TODO cambiare frase in base allo score
                    response.append("Non male, attualmente il tuo punteggio è " + getScore());
                } else {
                    response.append("Non c'è niente di interessante qui.");
                }

            } else if (p.getVerb().getVerbType().equals(VerbType.PICK_UP)) {
                if (object != null && object.getId() == HACKSAW && object.isAccept()
                        && ((TokenPerson) getObject(GENNY_BELLO)).getInventory().contains(object)) {

                    //There is the need of the remove's operation from the room of the TokenPerson's objects
                    getCurrentRoom().removeObject(getObject(HACKSAW));
                    getInventory().add(object);
                    response.append("Hai preso " + object.getName() + "!");
                } else if (object != null
                        && object.getId() == SCALPEL
                        && getCurrentRoom().getId() == INFIRMARY
                        && !object.isTaken()) {

                    getCurrentRoom().removeObject(object);
                    getInventory().add(object);
                    object.setTaken(true);
                    increaseScore();
                    response.append("Hai preso " + object.getName() + "!");
                    response.append("Fai in fretta perché improvvisamente senti i passi dell’infermiera avvicinandosi " +
                            "alla porta, riesci a prendere il bisturi con te e l’infermiera ti dice che sei guarito" +
                            " e puoi ritornare nella cella visto che l’ora d’aria è finita.\n");
                    setCurrentRoom(getRoom(MAIN_CELL));
                    getInventory().add(getObject(MEDICINE));
                    response.append("Zzzzzz.....\n");
                    response.append("Caspita gli antidolorifici ti hanno fatto dormire molto e ti risvegli nella tua " +
                            "cella privo di qualsiasi dolore! Prima di andare via l’infermiera ti ha dato qualche " +
                            "medicinale tra cui un medicinale all’ortica. Guarda nel tuo inventario!\n");
                    move = true;

                } else if (object != null && object.isPickupable()
                        && getCurrentRoom().containsObject(object)) {

                    if ((object.getId() == SCOTCH || object.getId() == SCREW) && !object.isTaken()) {
                        increaseScore();
                        object.setTaken(true);
                    }
                    getCurrentRoom().removeObject(object);
                    getInventory().add(object);
                    response.append("Hai preso " + object.getName() + "!");

                } else if (object == null) {
                    response.append("Cosa vorresti prendere di preciso?");
                } else if (object.getId() == SCREW && !object.isPickupable()) {
                    response.append("Non puoi prendere quella vite se prima non affronti il gruppetto dei detenuti!");
                } else if (!getCurrentRoom().containsObject(object)) {
                    throw new ObjectNotFoundInRoomException();
                } else if (!object.isPickupable()) {
                    response.append("Non e' certo un oggetto che si può prendere imbecille!");
                } else if (getInventory().contains(object)) {
                    response.append("Guarda bene nella tua borsa, cretino!");
                }

            } else if (p.getVerb().getVerbType().equals(VerbType.REMOVE)) {
                if (object != null && getInventory().contains(object)) {
                    getCurrentRoom().setObject(object);
                    getInventory().remove(object);
                    response.append("Hai lasciato a terra ").append(object.getName()).append("!");

                } else if (object == null) {
                    response.append("Cosa vorresti rimuovere dall'inventario?");
                } else {
                    response.append("L'inventario non ha questo oggetto!");
                    response.append("L'avrai sicuramente scordato da qualche parte!");
                    response.append("Che pazienzaaa!!");
                }

            } else if (p.getVerb().getVerbType().equals(VerbType.USE)) {
                if (object != null && object.isUsable() && getCurrentRoom().isObjectUsableHere(object)
                        && (getInventory().contains(object) || getCurrentRoom().containsObject(object))) {

                    if (object.getId() == SCREW) {
                        getObject(SINK).setPushable(true);
                        setObjectNotAssignedRoom(object);
                        getInventory().remove(object);
                        response.append("Decidi di usare il cacciavite, chiunque abbia fissato quel lavandino non aveva una " +
                                "grande forza visto che le viti si svitano facilmente. Adesso che hai rimosso tuttte le " +
                                "viti, noti che il lavandino non è ben fissato");
                        increaseScore();
                        object.setUsed(true);

                    } else if (object.getId() == SCOTCH) {
                        setObjectNotAssignedRoom(object);
                        getInventory().remove(object);
                        getInventory().add(getObject(COMBINATION));
                        response.append("Metti lo scotch sui numeri della porta, dallo scotch noti le impronte dei ultimi " +
                                "tasti schiacciati, ora indovinare il pin segreto sembra molto più semplice!");
                        increaseScore();
                        object.setUsed(true);

                    } else if (object.getId() == TOOLS) {
                        response.append("Decidi di allenarti per un bel po’ di tempo… alla fine dell’allenamento " +
                                "ti senti già più forte!");

                        if (!object.isUsed()) {
                            increaseScore();
                            object.setUsed(true);
                        }


                    } else if (object.getId() == BALL) {
                        response.append("Il tempo è denaro, non penso sia il momento adatto per mettersi a giocare.");
                        object.setUsed(true);

                    } else if (object.getId() == SCALPEL) {
                        setObjectNotAssignedRoom(object);
                        getInventory().remove(object);
                        response.append("Riesci subito a tirare fuori il bisturi dalla tasca, il gruppetto lo vede e capito " +
                                "il pericolo decide di lasciare stare (Mettere a rischio la vita per una panchina " +
                                "sarebbe veramente stupido) e vanno via con un'aria di vendetta. Ora sei solo vicino " +
                                "alla panchina.");
                        getRoom(FRONTBENCH).setDescription("Sei solo vicino alla panchina!");
                        getRoom(FRONTBENCH).setLook("E' una grossa panchina in legno un po' malandata, " +
                                "ci sei solo tu nelle vicinanze.");
                        getRoom(BENCH).setDescription("Dopo aver usato il bisturi, il giardino si è svuotato, ci sei" +
                                "solo tu qui.");
                        getRoom(BENCH).setLook("In lontananza vedi delle panchine tutte vuote!");

                        increaseScore();
                        object.setUsed(true);
                        response.append("Riesci subito a tirare fuori il bisturi dalla tasca, il gruppetto lo vede e " +
                                "capito il pericolo decide di lasciare stare (Mettere a rischio la vita per una panchina " +
                                "sarebbe veramente stupido) e vanno via con un'aria di vendetta.\nOra sei solo vicino" +
                                " alla panchina.");
                        getCurrentRoom().setDescription("Sei solo vicino alla panchina!");
                        getCurrentRoom().setLook("E' una grossa panchina in legno un po' malandata, " +
                                "ci sei solo tu nelle vicinanze.");
                        getObject(SCREW).setPickupable(true);
                        counterFaceUp++;
                    } else if (object.getId() == HACKSAW && getObject(TOOLS).isUsed()) {
                        getRoom(AIR_DUCT_INFIRMARY).setLocked(false);
                        setObjectNotAssignedRoom(object);
                        getInventory().remove(object);
                        getRoom(AIR_DUCT_NORTH).removeObject(getObject(DESTROYABLE_GRATE));
                        response.append("Oh no! Il seghetto si è rotto e adesso ci sono pezzi di sega dappertutto, per " +
                                "fortuna sei riuscito a rompere la grata");
                        response.append("Dopo esserti allenato duramente riesci a tagliare le sbarre con il seghetto, " +
                                "puoi proseguire nel condotto e capisci che quel condotto porta fino all’infermeria.");
                        increaseScore();
                        increaseScore();
                        object.setUsed(true);

                    } else if (object.getId() == SINK) {
                        response.append("Decidi di lavarti le mani e il viso, l’igiene prima di tutto!");
                        object.setUsed(true);
                    } else if (object.getId() == GENERATOR_OBJ) {
                        if (object.isUsed() || getObject(BUTTON_GENERATOR).isPush()) {
                            response.append("Il generatore è gia stato usato, fai in fretta!!");
                        } else {
                            getObject(BUTTON_GENERATOR).setPush(true);
                            getRoom(DOOR_ISOLATION).setLocked(false);
                            getObject(LIGHTS).setOn(false);
                            response.append("Sembra che tutto il carcere sia nell’oscurità! È stata una bella mossa" +
                                    " la tua, peccato che i poliziotti prevedono queste bravate e hanno un generatore" +
                                    " di corrente ausiliario che si attiverà dopo un minuto dal blackout!");
                            increaseScore();
                            object.setUsed(true);
                        }

                    } else if (object.getId() == ACID) {
                        getRoom(ENDGAME).setLocked(false);
                        setObjectNotAssignedRoom(object);
                        getInventory().remove(object);
                        response.append("Adesso la finestra presenta un buco, sarebbe meglio infilarsi dentro!");
                        increaseScore();
                        increaseScore();
                        increaseScore();
                        increaseScore();
                        object.setUsed(true);

                    } else if (object.getId() == COMBINATION && !object.isUsed()) {
                        setObjectNotAssignedRoom(object);
                        getInventory().remove(object);
                        getRoom(ISOLATION).setLocked(false);
                        response.append("La porta si apre! Puoi andare a est per entrare dentro l'isolamento oppure" +
                                " tornare indietro anche se hai poco tempo a disposizione!");
                        increaseScore();
                        object.setUsed(true);
                    } else if (object.getId() == BED) {
                        response.append("Buona notte fiorellino!");
                        object.setUsed(true);
                    } else if (object.getId() == POSTER) {

                        object.setUsable(true);

                        // DON'T CHANGE THE ORDER
                        if (counterFaceUp == 0) {
                            response.append("Sei appena arrivato in carcere, perché non ti fai conoscere prendendo parte ad " +
                                    "una rissa in giardino?");
                        } else if (!getObject(SCALPEL).isUsed() && counterFaceUp == 1) {
                            response.append("Eccoti un consiglio: Dovresti andare nel giardino e utilizzare per bene quel " +
                                    "bisturi, coraggio prendi la tua vendetta!");
                        } else if (!getInventory().contains(getObject(SCREW)) && !getObject(SCREW).isUsed()) {
                            response.append("Adesso dovresti prendere la vite, ti servirà molto per portare al termine" +
                                    "la tua missione!");
                        } else if (getInventory().contains(getObject(SCREW))) {
                            response.append("Dovresti provare ad utilizzare la vite che hai in questa stanza, chissà cosa " +
                                    "potrà capitare...");
                        } else if (getObject(SCREW).isUsed() && !getObject(SINK).isPush()) {
                            response.append("I tuoi genitori hanno anche figli normali? Come fai a non comprendere che è " +
                                    "necessario spostare il lavandino!!");
                        } else if (((TokenPerson) getObject(GENNY_BELLO)).getInventory().contains(getObject(HACKSAW)) &&
                                !getInventory().contains(getObject(HACKSAW))) {
                            response.append("Dovresti cercare un utensile per rompere quelle grate che ti impediscono il " +
                                    "passaggio!");
                        } else if (!getObject(HACKSAW).isUsed() && !getObject(TOOLS).isUsed() &&
                                getInventory().contains(getObject(HACKSAW))) {
                            response.append("Adesso che hai il seghetto dovresti aumentare un pò la tua massa muscolare");
                        } else if (!getObject(HACKSAW).isUsed() && getObject(TOOLS).isUsed()) {
                            response.append("Ti vedo in forma adesso, ora sarai sicuramente in grado di distruggere " +
                                    "quella grata che è presente nel condotto d'aria");
                        } else if (getObject(HACKSAW).isUsed() && !getObject(SCOTCH).isUsed()
                                && !getInventory().contains(getObject(SCOTCH))) {
                            response.append("Nel condotto d'aria c'è qualcosa che ti tornerà utile più tardi!");
                        } else if (!getObject(GENERATOR_OBJ).isUsed() && !getObject(MEDICINE).isGiven()) {
                            response.append("Ti consiglio di cercare un pò nel condotto d'aria e spegnere il generatore" +
                                    " ci vorrà un pò di buio per salvare tuo fratello");
                        } else if (getObject(GENERATOR_OBJ).isUsed() && !getObject(MEDICINE).isGiven()) {
                            response.append("Adesso che la prigione è buia potrai andare vicino alla porta d'isolamento e " +
                                    "usare quello scotch che hai preso precedentemente");
                        } else if (getObject(SCOTCH).isUsed() && !getObject(MEDICINE).isGiven()) {
                            response.append("Dovresti usare quella combinazione che hai ottenuto utilizzando lo scotch" +
                                    "nella stanza che precede l'isolamento, fai in fretta il tempo a tua disposizione" +
                                    "sta per scadere!!!!");
                        } else if (getObject(COMBINATION).isUsed() && !getObject(MEDICINE).isGiven()) {
                            response.append("Cosa ci fai qui?? Dovresti dare la medicina a tuo fratello!!!!");
                        } else if (getObject(MEDICINE).isGiven()) {
                            response.append("Il tuo piano è quasi terminato, vai con Genny Bello in infermeria passando dal" +
                                    "passaggio segreto! Buona fortuna, te ne servirà molta!!!!!!!!!");
                        } else {
                            response.append("Mi dispiace ma non ho suggerimenti da darti attualmente!!");
                        }
                    }

                } else {
                    if (object == null) {
                        response.append("Sei sicuro di non voler usare niente?");
                    } else if (!object.isUsable()) {
                        response.append("Mi dispiace ma questo oggetto non si può utilizzare");
                    } else if (!getInventory().contains(object) && !getCurrentRoom().containsObject(object)) {
                        response.append("Io non vedo nessun oggetto di questo tipo qui!");
                    } else if (!getCurrentRoom().isObjectUsableHere(object)) {
                        response.append("C’è tempo e luogo per ogni cosa, ma non ora.");
                    }
                }

                if (object != null && object.getId() == HACKSAW
                        && !getObject(TOOLS).isUsed() && getCurrentRoom().isObjectUsableHere(getObject(HACKSAW))) {
                    response.append("Il seghetto sembra molto arrugginito e non riesci a tagliare le sbarre " +
                            "della grata! In realtà la colpa non è totalmente del seghetto ma anche la tua " +
                            "poiché sei molto stanco e hai poca forza nelle braccia!");
                }

            } else if (p.getVerb().getVerbType().equals(VerbType.OPEN)) {
                if (object != null
                        && object.isOpenable()
                        && !object.isOpen()
                        && (getCurrentRoom().containsObject(object))) {
                    if (!(object instanceof TokenObjectContainer)) {
                        response.append("Hai aperto " + object.getName() + "!");
                    } else if (!object.isOpen()) {
                        response.append("Hai aperto " + object.getName() + "!");
                        response.append("Contiene: ");
                        for (TokenObject obj : ((TokenObjectContainer) object).getObjects()) {
                            response.append(obj.getName() + ": " + obj.getDescription());
                        }
                    }
                    object.setOpen(true);
                } else if (object == null) {
                    response.append("Cosa vorresti aprire di preciso?");
                } else if (!getCurrentRoom().containsObject(object)) {
                    throw new ObjectNotFoundInRoomException();
                } else if (!object.isOpenable()) {
                    response.append("Sei serio? Vorresti veramente aprirlo?!");
                    response.append("Sei fuori di testa!");
                } else if (object.isOpen()) {
                    response.append("E' gia' aperto testa di merda!");
                }

            } else if (p.getVerb().getVerbType().equals(VerbType.CLOSE)) {
                if (object != null
                        && object.isOpenable()
                        && object.isOpen()
                        && (getCurrentRoom().containsObject(object))) {

                    response.append("Hai chiuso " + object.getName() + "!");
                    object.setOpen(false);

                } else if (object == null) {
                    response.append("Cosa vorresti chiudere di preciso?");
                } else if (!getCurrentRoom().containsObject(object)) {
                    throw new ObjectNotFoundInRoomException();
                } else if (!object.isOpenable()) {
                    response.append("Sei serio? Vorresti veramente chiuderlo?!");
                    response.append("Sei fuori di testa!");
                } else if (!object.isOpen()) {
                    response.append("E' gia' chiuso testa di merda!");
                }

            } else if (p.getVerb().getVerbType().equals(VerbType.PUSH)
                    || p.getVerb().getVerbType().equals(VerbType.PULL)) {
                if (object != null && object.isPushable() && getCurrentRoom().containsObject(object)) {
                    if (object.getId() == LADDER) {
                        // ladder case
                        if (getCurrentRoom().getId() == PASSAGE_SOUTH) {
                            getRoom(SECRET_PASSAGE).setObject(object);
                            getRoom(PASSAGE_SOUTH).removeObject(object);
                            response.append("La scala è stata spinta fino alla stanza a nord!");
                            increaseScore();
                        } else if (getCurrentRoom().getId() == SECRET_PASSAGE) {
                            getRoom(PASSAGE_NORTH).setObject(object);
                            getRoom(SECRET_PASSAGE).removeObject(object);
                            response.append("La scala è stata spinta fino alla stanza a nord e si è bloccata lì!");
                            increaseScore();
                        } else {
                            response.append("La scala è bloccata! Non esiste alcun modo per spostarla!");
                        }

                    } else if (object.getId() == SINK) {
                        if (getCurrentRoom().getId() == MAIN_CELL) {
                            if (object.isPush()) {
                                response.append("Il Lavandino è già stato spostato!");
                            } else {
                                object.setPush(true);
                                getRoom(SECRET_PASSAGE).setLocked(false);
                                response.append("Oissà!");
                                increaseScore();
                                increaseScore();
                            }
                        }

                    } else if (object.getId() == PICTURE) {
                        if (getCurrentRoom().getId() == INFIRMARY) {
                            // picture pushed
                            if (object.isPush()) {
                                response.append("Il quadro è già stato spostato!");
                            } else {
                                object.setPush(true);
                                getCurrentRoom().setObject(getObject(OLD_AIR_DUCT));
                                getObjectNotAssignedRoom().remove(getObject(OLD_AIR_DUCT));
                                response.append(getObject(OLD_AIR_DUCT).getDescription());
                            }
                        }
                    } else if (object.getId() == BUTTON_GENERATOR) {
                        if (getCurrentRoom().getId() == GENERATOR) {
                            // botton pushed
                            if (object.isPush()) {
                                response.append("Il pulsante è già stato premuto! Fai in fretta!!!");
                            } else {
                                object.setPush(true);
                                getObject(LIGHTS).setOn(false);
                                getObject(GENERATOR_OBJ).setUsed(true);
                                getRoom(DOOR_ISOLATION).setLocked(false);
                                response.append("Sembra che tutto il carcere sia nell’oscurità! È stata una bella mossa" +
                                        " la tua, peccato che i poliziotti prevedono queste bravate e hanno un " +
                                        "generatore di corrente ausiliario che si attiverà dopo un minuto dal blackout!");
                                increaseScore();
                                increaseScore();
                            }
                        }
                    }
                } else if (object == null) {
                    response.append("Cosa vuoi spostare? L'aria?!?");
                } else if (!getCurrentRoom().containsObject(object)) {
                    throw new ObjectNotFoundInRoomException();
                } else if (!object.isPushable()) {
                    response.append("Puoi essere anche Hulk ma quell'oggetto non si può spostare!!!");
                }

            } else if (p.getVerb().getVerbType().equals(VerbType.EAT)) {
                if (object != null && object.isEatable() && (getInventory().contains(object)
                        || getCurrentRoom().containsObject(object))) {
                    // food case
                    if (getCurrentRoom().getObjects().contains(object)) {
                        getCurrentRoom().removeObject(object);
                        response.append("Gnam Gnam...");
                    } else if (getInventory().getObjects().contains(object)) {
                        getInventory().remove(object);
                        response.append("Gnam Gnam...");
                    }
                } else if (object == null) {
                    response.append("Cosa vuoi mangiare??? Sembra non ci sia nulla di commestibile");
                } else if (!(getInventory().contains(object)
                        || getCurrentRoom().containsObject(object))) {
                    response.append("Non penso si trovi qui questo oggetto!!! Compriamo un paio di occhiali?");
                } else if (!object.isEatable()) {
                    response.append("Sei veramente sicuro??? Non mi sembra una buona idea!");
                }

            } else if (p.getVerb().getVerbType().equals(VerbType.STAND_UP)) {
                if (object == null) {
                    //On foot
                    if (getCurrentRoom().getObjects().stream()
                            .anyMatch(obj -> obj.isSitable() && obj.isSit())) {
                        getCurrentRoom().getObjects().stream()
                                .filter(obj -> obj.isSitable() && obj.isSit())
                                .findFirst()
                                .ifPresent(obj -> obj.setSit(false));
                        response.append("Oplà! Ti sei alzato!");
                    } else {
                        response.append("Sei così basso che non ti accorgi di stare già in piedi???");
                    }
                } else {
                    response.append("Non penso che questa cosa si possa fare ?!?");
                }

            } else if (p.getVerb().getVerbType().equals(VerbType.SIT_DOWN)) {
                if (object != null && object.isSitable() && getCurrentRoom().containsObject(object)) {

                    //Bed case
                    if (object.getId() == BED) {
                        if (getCurrentRoom().getObjects().stream()
                                .anyMatch(obj -> obj.isSitable() && !obj.isSit() && obj.getId() != WATER)) {
                            response.append("Buonanotte fiorellino!");
                        } else {
                            response.append("Sei talmente stanco che nemmeno ti accorgi che sei già seduto???");
                        }

                        //Water case
                    } else if (object.getId() == WATER) {
                        if (getCurrentRoom().getObjects().stream()
                                .anyMatch(obj -> obj.isSitable() && !obj.isSit() && obj.getId() == WATER)) {
                            response.append("Proprio ora devi farlo?");
                        } else {
                            response.append("Sei già seduto! Ricordati di tirare lo scarico!");
                        }
                    } else if (object.getId() == COT) {
                        if (getCurrentRoom().getObjects().stream()
                                .anyMatch(obj -> obj.isSitable() && !obj.isSit() && obj.getId() == COT)) {
                            response.append("Non sembra il momento di riposarti!");
                        } else {
                            response.append("Sei già sdraiato sul lettino!");
                        }
                    }
                    object.setSit(true);
                    if (getCurrentRoom().getObjects().stream()
                            .filter(obj -> obj.isSitable() && obj.isSit()).count() > 1) {
                        getCurrentRoom().getObjects().stream()
                                .filter(obj -> obj.isSitable() && obj.isSit() && obj.getId() != object.getId())
                                .findFirst()
                                .ifPresent(obj -> obj.setSit(false));
                    }
                } else if (object == null) {
                    response.append("Sedersi sul pavimento non mi sembra una buona idea!");
                } else if (!getCurrentRoom().containsObject(object)) {
                    response.append("Non penso si trovi qui questo oggetto!!! Guarda meglio!");
                } else if (!object.isSitable()) {
                    response.append("Con quell'oggetto puoi fare altro ma di certo non sederti!");
                }

            } else if (p.getVerb().getVerbType().equals(VerbType.CLIMB)) {

                if (getCurrentRoom().getId() == LOBBY) {
                    setCurrentRoom(getCurrentRoom().getWest());
                    move = true;
                } else if (object != null && getCurrentRoom().containsObject(object)) {
                    if (object.getId() == LADDER && getCurrentRoom().getId() == PASSAGE_NORTH) {
                        getRoom(ON_LADDER).setLocked(false);
                        setCurrentRoom(getCurrentRoom().getNorth());
                        move = true;
                    } else if (object.getId() != LADDER) {
                        response.append("Non puoi salire su quell'oggetto");
                    } else {
                        response.append("Usa quell'oggetto altrove, qui acchiappi solo le mosche!");
                    }
                } else if (object == null) {
                    response.append("Sei Spiderman? Non puoi arrampicarti sui muri!");
                } else if (!getCurrentRoom().containsObject(object)) {
                    response.append("Non penso si trovi qui questo oggetto!!! Guarda meglio!");
                }

            } else if (p.getVerb().getVerbType().equals(VerbType.GET_OFF)) {

                if (getCurrentRoom().getId() == LADDERS) {
                    setCurrentRoom(getCurrentRoom().getEast());
                    move = true;
                } else if (getCurrentRoom().getId() == AIR_DUCT) {
                    setCurrentRoom(getCurrentRoom().getSouth());
                    move = true;
                    response.append("Usi la scala per scendere!");
                } else {
                    response.append("Non puoi bucare il pavimento!");
                }

            } else if (p.getVerb().getVerbType().equals(VerbType.ENTER)) {

                if (getCurrentRoom().getId() == MAIN_CELL && getObject(SINK).isPush()) {
                    setCurrentRoom(getCurrentRoom().getWest());
                    move = true;
                } else if (getCurrentRoom().getId() == ON_LADDER) {
                    setCurrentRoom(getCurrentRoom().getNorth());
                    move = true;
                } else if (getCurrentRoom().getId() == DOOR_ISOLATION) {
                    setCurrentRoom(getCurrentRoom().getEast());
                    move = true;
                } else {
                    response.append("Non puoi entrare lì!");
                }

            } else if (p.getVerb().getVerbType().equals(VerbType.EXIT)) {
                if (getCurrentRoom().getId() == FRONTBENCH && !getInventory().contains(getObject(SCALPEL))) {
                    setCurrentRoom(getCurrentRoom().getNorth());
                    move = true;
                    response.append("Decidi di fuggire, ma prima o poi il pericolo dovrai affrontarlo!\n");
                } else {
                    response.append("Perchè scappare?? Ma soprattutto da cosa??? Fifone!");
                }

                //TODO NON FAR CREARE L'ACIDO LA PRIMA VOLTA IN INFERMERIA
            } else if (p.getVerb().getVerbType().equals(VerbType.MAKE)) {
                TokenObject substances = getObject(SUBSTANCES);
                if ((object != null
                        && object.isMixable()
                        && (getInventory().contains(object)
                        || getCurrentRoom().containsObject(object)))
                        || ((object != null && object.getId() == ACID))
                        && (getInventory().contains(substances)
                        || getCurrentRoom().containsObject(substances))) {

                    if (getCurrentRoom().getObjects().contains(object) && !(object.getId() == ACID)) {
                        getCurrentRoom().removeObject(object);
                        getInventory().add(getObject(ACID));
                        getObjectNotAssignedRoom().remove(getObject(ACID));
                        mixed = true;
                        increaseScore();
                    } else if (object.getId() != ACID && getInventory().getObjects().contains(object)) {
                        getInventory().remove(object);
                        getInventory().add(getObject(ACID));
                        getObjectNotAssignedRoom().remove(getObject(ACID));
                        mixed = true;
                        increaseScore();
                    } else if (getCurrentRoom().getObjects().contains(substances)
                            && object.getId() == ACID) {
                        getCurrentRoom().removeObject(substances);
                        getInventory().add(getObject(ACID));
                        getObjectNotAssignedRoom().remove(getObject(ACID));
                        mixed = true;
                        increaseScore();
                    } else if (getInventory().getObjects().contains(substances)
                            && object.getId() == ACID) {
                        getInventory().remove(substances);
                        getInventory().add(getObject(ACID));
                        getObjectNotAssignedRoom().remove(getObject(ACID));
                        mixed = true;
                        increaseScore();
                    }
                    if (mixed || !getInventory().getObjects().contains(getObject(ACID))) {
                        response.append("Hai creato un acido corrosivo, attento alle mani!");
                        response.append("L'acido è stato inserito nel tuo inventario!");
                    } else {
                        response.append("Hai già creato l'acido!!! Guarda bene nel tuo inventario!");
                    }

                } else if (object == null) {
                    response.append("Cosa vuoi creare esattamente?");
                } else if (!getCurrentRoom().containsObject(object)) {
                    response.append("Non penso si trovi qui questo oggetto!!! Guarda meglio!");
                } else if (!object.isMixable()) {
                    response.append("Non è una cosa che si può fare");
                }

            } else if (p.getVerb().getVerbType().equals(VerbType.TURN_OFF)) {
                if (object != null && object.isTurnOnAble()) {
                    // lights case
                    if (getCurrentRoom().getId() == GENERATOR) {
                        // lights turnOFF
                        if (!object.isOn()) {
                            response.append("Il pulsante è già stato premuto! Fai in fretta!!!");
                        } else {
                            getObject(BUTTON_GENERATOR).setPush(true);
                            getObject(GENERATOR_OBJ).setUsable(true);
                            object.setOn(false);
                            getRoom(DOOR_ISOLATION).setLocked(false);
                            response.append("Sembra che tutto il carcere sia nell’oscurità! È stata una bella mossa" +
                                    " la tua, peccato che i poliziotti prevedono queste bravate e hanno un generatore" +
                                    " di corrente ausiliario che si attiverà dopo un minuto dal blackout!");
                            increaseScore();
                            increaseScore();
                        }
                    } else {
                        response.append("Non puoi spegnere nulla qui!");
                    }
                } else if (object == null) {
                    response.append("Cosa vuoi spegnere esattamente???");
                } else if (!object.isTurnOnAble()) {
                    response.append("Come puoi spegnere questo oggetto???");
                }

            } else if (p.getVerb().getVerbType().equals(VerbType.TURN_ON)) {
                if (object != null && object.isTurnOnAble()) {
                    // lights case
                    if (getCurrentRoom().getId() == GENERATOR) {
                        // lights turnOFF
                        if (object.isOn()) {
                            response.append("Le luci sono già accese!");
                        } else {
                            response.append("Le luci si accenderanno da sole tra qualche minuto, non avere paura!");
                        }
                    } else {
                        response.append("Non puoi accendere nulla qui!");
                    }
                } else if (object == null) {
                    response.append("Cosa vuoi accendere esattamente???");
                } else if (!object.isTurnOnAble()) {
                    response.append("Come puoi accendere questo oggetto???");
                }

            } else if (p.getVerb().getVerbType().equals(VerbType.PLAY)) {
                if ((object != null && object.isPlayable())
                        || (object == null && getCurrentRoom().getId() == BASKET_CAMP)) {
                    //Ball case
                    if (getCurrentRoom().getId() == BASKET_CAMP) {
                        //Ball play
                        response.append("Il tempo è denaro, non penso sia il momento adatto per mettersi a giocare.");
                    } else {
                        response.append("Vuoi giocare con il tuo amico immaginario??");
                    }
                } else if (object == null) {
                    response.append("Con cosa vuoi giocare esattamente???");
                } else if (!object.isTurnOnAble()) {
                    response.append("Non puoi giocare con questo oggetto!!!");
                }

            } else if (p.getVerb().getVerbType().equals(VerbType.WORK_OUT)) {
                if (getCurrentRoom().getId() == GYM
                        || (getCurrentRoom().getId() == GYM && object != null && object.getId() == TOOLS)) {
                    response.append("Decidi di allenarti per un bel po’ di tempo… alla fine dell’allenamento " +
                            "ti senti già più forte!");

                    if (!getObject(TOOLS).isUsed()) {
                        increaseScore();
                        getObject(TOOLS).setUsed(true);
                    }

                } else if (getCurrentRoom().getId() != GYM
                        || (getCurrentRoom().getId() != GYM && object != null && object.getId() == TOOLS)) {
                    response.append("Ti sembra un posto dove potersi allenare?!!");
                } else if (!getCurrentRoom().containsObject(object)) {
                    throw new ObjectNotFoundInRoomException();
                } else {
                    response.append("Non ci si può allenare con quest'oggetto!");
                }

            } else if (p.getVerb().getVerbType().equals(VerbType.PUT_IN)) {
                if (object != null && object.isInsertable()) {
                    if (getCurrentRoom().getId() == DOOR_ISOLATION) {
                        getInventory().remove(object);
                        getRoom(ISOLATION).setLocked(false);
                        object.setUsed(true);
                        response.append("La porta si apre! Puoi andare a est per entrare dentro l'isolamento oppure" +
                                " tornare indietro anche se hai poco tempo a disposizione!");
                        increaseScore();
                    } else {
                        response.append("Non puoi inserire nulla qui!");
                    }
                } else if (object == null) {
                    response.append("Con cosa vuoi inserire??");
                } else if (!object.isInsertable()) {
                    response.append("Ho paura di quello che vuoi fare!!!");
                }

            } else if (p.getVerb().getVerbType().equals(VerbType.TALK_TO)) {
                if ((object instanceof TokenPerson && ((TokenPerson) object).isSpeakable())) {
                    if (getCurrentRoom().getId() == CANTEEN) {
                        response.append("Si avvicina a te e sussurrando ti chiede se sei interessato a qualche oggetto che " +
                                "lui possiede. Ovviamente ogni oggetto ha un costo ma tu non possiedi alcun soldo, " +
                                "per averne uno quindi sarai costretto a trattare.");
                    } else if (getCurrentRoom().getId() == BROTHER_CELL) {
                        response.append("Tuo fratello ti chiede il motivo della tua presenza nel carcere e tu gli " +
                                "racconti tutto il piano segreto per la fuga cosicché tuo fratello non venga " +
                                "giustiziato ingiustamente.\n" +
                                "Tuo fratello sembra al quanto felice e ti ringrazia enormemente di aver creato tutto" +
                                " questo piano per salvarlo! \n" +
                                "Il piano consiste nel far andare tuo fratello in qualche modo in infermeria!");
                        getObject(MEDICINE).setGiveable(true);
                    }
                } else if (object == null) {
                    response.append("Vuoi parlare da solo???");
                } else {
                    response.append("Parlare con quell'oggetto non sembra essere la soluzione migliore!");
                }

            } else if (p.getVerb().getVerbType().equals(VerbType.ASK)) {
                if ((object != null && object.isAskable())) {
                    if (getCurrentRoom().getId() == CANTEEN) {
                        if (object.getId() == DRUG) {
                            response.append("Meglio continuare il piano di fuga da lucidi e fortunatamente non hai soldi " +
                                    "con te per acquistarla! Ti ricordo che il tuo piano è fuggire di prigione e non " +
                                    "rimanerci qualche anno di più!");
                        } else if (object.getId() == VIDEOGAME) {
                            response.append("Sarebbe molto bello se solo avessi 8 anni! Quando uscirai di prigione" +
                                    " avrai molto tempo per giocare anche a videogiochi migliori!");
                        } else if (object.getId() == HACKSAW && !object.isAsked()) {
                            response.append("Il detenuto ti dice che a tutti quelli che ha venduto un seghetto avevano " +
                                    "sempre un piano di fuga per evadere di prigione che però non sono mai andati a " +
                                    "buon fine essendo un carcere di massima sicurezza ( in effetti con un seghetto " +
                                    "in prigione non hai tante alternative). In cambio gli dovrai confessare tutto " +
                                    "il tuo piano di fuga e farlo fuggire insieme a te, cosa scegli???");
                            object.setAsked(true);
                        } else if (object.isAsked()) {
                            response.append("Hai già chiesto per quell'oggetto!!!");
                        }
                    }
                } else if (object == null) {
                    response.append("Cosa devi chiedere?");
                } else if (!object.isAskable()) {
                    response.append("Non sembra la soluzione giusta!");
                }

            } else if (p.getVerb().getVerbType().equals(VerbType.ACCEPT)) {
                if (getObject(HACKSAW).isAsked()
                        && getCurrentRoom().getId() == CANTEEN
                        && !getObject(HACKSAW).isAccept()) {
                    response.append("Gli racconti tutto il piano segreto di fuga, il detenuto capisce che questo è " +
                            "il miglior piano che abbia sentito fin ora e accetta subito dandoti il seghetto e " +
                            "si unisce a te. Gli dici di incontrarsi stanotte alle 21:00 di fronte alla tua cella!");
                    response.append("Ora puoi prendere il seghetto da Genny!");
                    getObject(HACKSAW).setPickupable(true);
                    getObject(HACKSAW).setAccept(true);
                } else if (!getObject(HACKSAW).isAsked()) {
                    response.append("Non puoi accettare una cosa che non hai chiesto!!!");
                } else if (getObject(HACKSAW).isAccept()) {
                    response.append("Ormai hai già accettato! Ci avresti potuto pensare prima!");
                } else if (getCurrentRoom().getId() != CANTEEN) {
                    response.append("Cosa vuoi accettare? Nulla???");
                }

            } else if (p.getVerb().getVerbType().equals(VerbType.DECLINE)) {
                if (getObject(HACKSAW).isAsked() && getCurrentRoom().getId() == CANTEEN
                        && !getObject(HACKSAW).isAccept() && !getInventory().contains(getObject(HACKSAW))) {
                    response.append("Decidi di rifiutare l’accordo, quando vuoi il detenuto sarà sempre pronto " +
                            "a ricontrattare!");
                    getObject(HACKSAW).setPickupable(false);
                    getObject(HACKSAW).setAsked(false);
                    getObject(HACKSAW).setAccept(false);
                } else if (!getObject(HACKSAW).isAsked()) {
                    response.append("Non puoi rifiutare una cosa che non hai chiesto!!!");
                } else if (getObject(HACKSAW).isAccept() || getInventory().contains(getObject(HACKSAW))) {
                    response.append("Ormai hai già accettato! Ci avresti potuto pensare prima!");
                } else if (getCurrentRoom().getId() != CANTEEN) {
                    response.append("Cosa vuoi rifiutare? Nulla???");
                }

            } else if (p.getVerb().getVerbType().equals(VerbType.FACE_UP)) {
                if (getCurrentRoom().getId() == FRONTBENCH && counterFaceUp == 0) {
                    response.append("Sai benissimo che in un carcere non si possono comprare panchine e ti avvicini " +
                            "nuovamente con l’intento di prendere l’oggetto. Il gruppetto ti blocca e il piu' grosso " +
                            "di loro ti tira un pugno contro il viso... Perdendo i sensi non ti ricordi piu' nulla e" +
                            " ti svegli in infermeria.");
                    setCurrentRoom(getRoom(INFIRMARY));
                    increaseScore();
                    move = true;
                    counterFaceUp++;
                } else if (getCurrentRoom().getId() == FRONTBENCH && !getObject(SCALPEL).isUsed()
                        && getInventory().contains(getObject(SCALPEL)) && counterFaceUp == 1) {
                    increaseScore();
                    setObjectNotAssignedRoom(getObject(SCALPEL));
                    getInventory().remove(getObject(SCALPEL));
                    getObject(SCALPEL).setUsed(true);
                    getObject(SCREW).setPickupable(true);
                    response.append("Riesci subito a tirare fuori il bisturi dalla tasca, il gruppetto lo vede e " +
                            "capito il pericolo decide di lasciare stare (Mettere a rischio la vita per una panchina " +
                            "sarebbe veramente stupido) e vanno via con un'aria di vendetta.\nOra sei solo vicino" +
                            " alla panchina.");
                    getCurrentRoom().setDescription("Sei solo vicino alla panchina!");
                    getCurrentRoom().setLook("E' una grossa panchina in legno un po' malandata, " +
                            "ci sei solo tu nelle vicinanze.");
                    getRoom(BENCH).setDescription("Dopo aver usato il bisturi, il giardino si è svuotato, ci sei" +
                            "solo tu qui.");
                    getRoom(BENCH).setLook("In lontananza vedi delle panchine tutte vuote!");
                    counterFaceUp++;
                } else if (getCurrentRoom().getId() != FRONTBENCH || getObject(SCALPEL).isUsed() || counterFaceUp >= 2) {
                    response.append("Ehi John Cena, non puoi affrontare nessuno qui!!!");
                }

            } else if (p.getVerb().getVerbType().equals(VerbType.END)) {
                response.append("Non puoi usare quell'oggetto per uscire!");

            } else if (p.getVerb().getVerbType().equals(VerbType.DESTROY)) {
                if (object != null
                        && object.getId() == DESTROYABLE_GRATE
                        && getCurrentRoom().getId() == AIR_DUCT_NORTH
                        && getInventory().contains(getObject(HACKSAW))
                        && getObject(TOOLS).isUsed()
                        && !getObject(HACKSAW).isUsed()) {
                    getRoom(AIR_DUCT_INFIRMARY).setLocked(false);
                    getInventory().remove(getObject(HACKSAW));
                    getRoom(AIR_DUCT_NORTH).removeObject(object);
                    response.append("Oh no! Il seghetto si è rotto e adesso ci sono pezzi di sega dappertutto, per " +
                            "fortuna sei riuscito a rompere la grata");
                    response.append("Dopo esserti allenato duramente riesci a tagliare le sbarre con il seghetto, " +
                            "puoi proseguire nel condotto e capisci che quel condotto porta fino all’infermeria.");
                    increaseScore();
                    increaseScore();
                } else if (object == null) {
                    response.append("Cosa vuoi rompere???");
                } else if (!getCurrentRoom().containsObject(object)) {
                    throw new ObjectNotFoundInRoomException();
                } else if (object.getId() != DESTROYABLE_GRATE) {
                    response.append("Non puoi distruggere questo oggetto!");
                } else if (getCurrentRoom().getId() != AIR_DUCT_NORTH) {
                    response.append("Non puoi distruggere niente qui!");
                } else if (!getInventory().contains(getObject(HACKSAW))) {
                    response.append("Come puoi rompere la grata? Non hai nessun oggetto utile!");
                } else if (!getObject(TOOLS).isUsed()) {
                    response.append("Il seghetto sembra molto arrugginito e non riesci a tagliare le sbarre della grata! " +
                            "In realtà la colpa non è totalmente del seghetto ma anche la tua poichè sei molto stanco " +
                            "e hai poca forza nelle braccia!");
                } else if (!getObject(MEDICINE).isGiven()) {
                    response.append("Dopo esserti allenato duramente riesci a tagliare le sbarre con il seghetto, " +
                            "puoi proseguire nel condotto e capisci che quel condotto porta fino all’infermeria. " +
                            "Avrebbe più senso proseguire solo se la tua squadra è al completo… non ti sembri manchi " +
                            "la persona più importante???");
                } else if (!getObject(HACKSAW).isUsed()) {
                    response.append("Hai già usato quell'oggetto! Non puoi più rompere nulla!");
                }

            } else if (p.getVerb().getVerbType().equals(VerbType.GIVE)) {
                if (object != null && object.isGiveable() && getCurrentRoom().getId() == BROTHER_CELL
                        && object.getId() == MEDICINE) {
                    getInventory().remove(object);
                    response.append("Sai benissimo che tuo fratello ha una forte allergia alle ortiche" +
                            " e che non potrebbe prendere quel farmaco. Tu decidi di darlo ugualmente " +
                            "in modo che il tuo piano venga attuato!");
                    object.setGiven(true);
                    response.append("Appena dato il farmaco decidi di fuggire fuori dalla cella isolamento prima" +
                            " che le luci si accendano e le guardie ti scoprano!!!");
                    setCurrentRoom(getRoom(OUT_ISOLATION));
                    move = true;
                    response.append("Sono le 20:55 hai esattamente 5 minuti per tornare alla tua cella" +
                            " e completare il tuo piano! Speriamo che abbiano portato tuo fratello in infermeria!\n");
                    getCurrentRoom().setLook("Sono le 20:55 hai esattamente 5 minuti per tornare alla tua cella" +
                            "e completare il tuo piano! Speriamo che abbiano portato tuo fratello in infermeria!");

                    getRoom(INFIRMARY).setLocked(false);

                    //Lock the other rooms to lead the user to the end of the game
                    getRoom(DOOR_ISOLATION).setLocked(true);
                    getRoom(CANTEEN).setLocked(true);
                    getRoom(GYM).setLocked(true);
                    getRoom(GARDEN).setLocked(true);

                    //Set the description of the principal cell
                    getRoom(MAIN_CELL).setDescription("Sei arrivato alla tua cella , ad aspettarti puntuale " +
                            "c’è il tuo amichetto Genny. È ora di attuare il piano!");
                    getRoom(MAIN_CELL).setLook("Non perdere ulteriore tempo, bisogna attuare il piano e scappare via da qui!");
                    getRoom(AIR_DUCT_INFIRMARY).setLook("Dal condotto d'aria riesci a vedere tuo fratello " +
                            "nell'infermeria che ti aspetta!");

                    increaseScore();
                    increaseScore();
                    increaseScore();
                } else if (object == null) {
                    response.append("Cosa vuoi dare di preciso?");
                } else if (object instanceof TokenPerson) {
                    response.append("Una persona non è un oggetto che si può regalare!!!");
                } else {
                    response.append("Non puoi dare quest'oggetto a nessuno imbecille!");
                }
            }

            if (move) {
                response.append(getCurrentRoom().getName());
                response.append("================================================");
                response.append(getCurrentRoom().getDescription());
            }

        } catch (NotAccessibleRoomException e) {
            if (getCurrentRoom().getId() == BROTHER_CELL && p.getVerb().getVerbType().equals(VerbType.EAST)
                    || getCurrentRoom().getId() == OTHER_CELL && p.getVerb().getVerbType().equals(VerbType.WEST)) {
                response.append("Non hai ancora il potere di allargare le sbarre o oltrepassarle!!");
            } else {
                response.append("Da quella parte non si può andare c'è un muro! Non hai ancora acquisito i poteri" +
                        " per oltrepassare i muri...");
            }
        } catch (LockedRoomException e) {
            if (getObject(MEDICINE).isGiven()) {
                response.append("Non perdere ulteriore tempo, bisogna completare il piano!");
            } else if (getCurrentRoom().getEast() != null && getCurrentRoom().getId() == AIR_DUCT_INFIRMARY
                    && getCurrentRoom().getEast().getId() == INFIRMARY) {
                response.append("Avrebbe più senso proseguire solo se la tua squadra è al completo… " +
                        "non ti sembri manchi la persona più importante???");
            } else {
                response.append("Questa stanza è bloccata, dovrai fare qualcosa per sbloccarla!!");
            }
        } catch (InventoryEmptyException e) {
            response.append("L'inventario è vuoto!");
        } catch (InventoryFullException e) {
            response.append("Non puoi mettere più elementi nel tuo inventario!");
            response.append("!!!!Non hai mica la borsa di Mary Poppins!!!!!");
        } catch (ObjectNotFoundInInventoryException e) {
            response.append("Non hai questo oggetto nell'inventario, energumeno");
        } catch (ObjectsAmbiguityException e) {
            response.append("Ci sono più oggetti di questo tipo in questa stanza e non capisco a quale ti riferisci!");
        } catch (ObjectNotFoundInRoomException e) {
            if (p.getVerb().getVerbType() == VerbType.PICK_UP
                    || p.getVerb().getVerbType() == VerbType.USE
                    || p.getVerb().getVerbType() == VerbType.REMOVE
                    || p.getVerb().getVerbType() == VerbType.OPEN
                    || p.getVerb().getVerbType() == VerbType.CLOSE
                    || p.getVerb().getVerbType() == VerbType.DESTROY
                    || p.getVerb().getVerbType() == VerbType.GIVE
                    || p.getVerb().getVerbType() == VerbType.EAT
                    || p.getVerb().getVerbType() == VerbType.PULL
                    || p.getVerb().getVerbType() == VerbType.PUSH
                    || p.getVerb().getVerbType() == VerbType.MAKE
                    || p.getVerb().getVerbType() == VerbType.PLAY
                    || p.getVerb().getVerbType() == VerbType.WORK_OUT) {
                response.append("Lo vedi solo nei tuoi sogni!");
            } else {
                response.append("Hai fumato qualcosa per caso?!");
            }
        } catch (Exception e) {
            response.append("Qualcosa è andato storto....");
        }
        return response.toString();
    }
}
