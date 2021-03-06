package adventure.server.games;

import adventure.exceptions.inventoryException.InventoryEmptyException;
import adventure.exceptions.objectException.ObjectAmbiguityException;
import adventure.exceptions.objectException.ObjectException;
import adventure.exceptions.objectException.ObjectNotFoundInRoomException;
import adventure.server.parser.ParserOutput;
import adventure.server.type.*;

import java.io.*;
import java.sql.*;
import java.util.*;

import static adventure.utils.Utils.DATABASE_PATH;

/**
 * @author Corona-Extra
 */
public abstract class GameDescription implements Serializable {
    private static final int INCREASE_SCORE = 10;
    private static Connection conn;
    private final String title;
    private final ObjectsInterface gameObjects;
    private final RoomsInterface gameRooms;
    private final VerbsInterface gameVerbs;
    private final Set<Room> rooms = new HashSet<>();
    private final Set<TokenVerb> tokenVerbs = new HashSet<>();
    private final Set<TokenObject> objectNotAssignedRoom = new HashSet<>();
    private String introduction = null;
    private Inventory inventory;
    private Room currentRoom;
    private int score = 0;

    public GameDescription(ObjectsInterface gameObjects, RoomsInterface gameRooms, VerbsInterface gameVerbs, String title) throws SQLException {
        this.title = title;
        this.gameRooms = gameRooms;
        this.gameObjects = gameObjects;
        this.gameVerbs = gameVerbs;
        connect();
        init();
    }

    public static GameDescription loadGame(String username, int gameType) throws IOException, ClassNotFoundException, SQLException {
        conn = DriverManager.getConnection(DATABASE_PATH);
        GameDescription game = null;
        PreparedStatement preparedStatement;
        ResultSet result;
        String foundGame = "select * from game natural join user_game where username = ? and game_type = ?";

        preparedStatement = conn.prepareStatement(foundGame);
        preparedStatement.setString(1, username);
        preparedStatement.setInt(2, gameType);

        result = preparedStatement.executeQuery();

        if (result.next()) {
            ByteArrayInputStream bais;
            ObjectInputStream ins;

            bais = new ByteArrayInputStream(result.getBytes("game_saved"));
            ins = new ObjectInputStream(bais);
            game = (GameDescription) ins.readObject();
            ins.close();

        }

        return game;
    }

    public static boolean existingGame(String username, int gameType) throws SQLException {
        conn = DriverManager.getConnection(DATABASE_PATH);
        String findGame = "select * from game natural join user_game where username = ? and game_type = ?";
        PreparedStatement findExistingGame;
        ResultSet existingGame;

        findExistingGame = conn.prepareStatement(findGame);
        findExistingGame.setString(1, username);
        findExistingGame.setInt(2, gameType);
        existingGame = findExistingGame.executeQuery();

        return existingGame.next();
    }

    private void init() {
        getGameVerbs().initVerbs(this);
        getGameRooms().initRooms(this);
        getGameObjects().initObjects(this);
    }

    public String getTitle() {
        return title;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public ObjectsInterface getGameObjects() {
        return gameObjects;
    }

    public RoomsInterface getGameRooms() {
        return gameRooms;
    }

    public VerbsInterface getGameVerbs() {
        return gameVerbs;
    }

    public Set<Room> getRooms() {
        return rooms;
    }

    public Room getRoom(int id) {
        return rooms.stream().filter(room -> room.getId() == id).findFirst().orElse(null);
    }

    public Set<TokenVerb> getTokenVerbs() {
        return tokenVerbs;
    }

    public Set<TokenObject> getObjectNotAssignedRoom() {
        return objectNotAssignedRoom;
    }

    public void setObjectNotAssignedRoom(Set<TokenObject> objectNotAssignedRoom) {
        this.objectNotAssignedRoom.addAll(objectNotAssignedRoom);
    }

    public void setObjectNotAssignedRoom(TokenObject objectNotAssignedRoom) {
        this.objectNotAssignedRoom.add(objectNotAssignedRoom);
    }

    public void removeObjectNotAssigned(TokenObject object) {
        objectNotAssignedRoom.remove(object);
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public abstract String nextMove(ParserOutput p);

    public Set<TokenObject> getObjects() {
        Set<TokenObject> objects = new HashSet<>(objectNotAssignedRoom);

        try {
            objects.addAll(getInventory().getObjects());
        } catch (InventoryEmptyException ignored) {

        }

        if (!rooms.isEmpty()) {
            for (Room i : rooms) {
                for (TokenObject o : i.getObjects()) {
                    objects.add(o);

                    if (o instanceof TokenObjectContainer) {
                        objects.addAll(((TokenObjectContainer) o).getObjects());
                    }
                }
            }
        }

        return objects;
    }

    public TokenObject getObject(int id) {
        return getObjects().stream().filter(tokenObject -> tokenObject.getId() == id).findFirst().orElse(null);
    }

    public Set<TokenAdjective> getAdjectives() {
        Set<TokenAdjective> adjectives = new HashSet<>();
        Set<TokenObject> objects = new HashSet<>(getObjects());

        if (!objects.isEmpty()) {
            for (TokenObject i : objects) {
                adjectives.addAll(i.getAdjectives());
            }
        }

        return adjectives;
    }

    public void increaseScore() {
        this.score += INCREASE_SCORE;
    }

    public String toStringScore() {
        return "\n\nIl tuo punteggio è aumentato!!!\nOra hai " + this.score + " punti!\n\n";
    }

    public int getScore() {
        return score;
    }

    public TokenObject getCorrectObject(Set<TokenObject> tokenObjects) throws ObjectException {
        long countObjectRoomAndInventory = tokenObjects.stream()
                .filter(o -> (getCurrentRoom().containsObject(o) || getInventory().contains(o))).count();
        TokenObject correctObject = null;

        if (countObjectRoomAndInventory > 1) {
            throw new ObjectAmbiguityException();
        } else if (countObjectRoomAndInventory == 1) {
            correctObject = tokenObjects.stream()
                    .filter(o -> (getCurrentRoom().containsObject(o) || getInventory().contains(o)))
                    .findFirst()
                    .orElse(null);
        } else if (tokenObjects.stream()
                .filter(o -> getObjectNotAssignedRoom().contains(o)).count() >= 1) {
            correctObject = tokenObjects.stream()
                    .filter(o -> getObjectNotAssignedRoom().contains(o))
                    .findFirst()
                    .orElse(null);
        } else if (tokenObjects.stream()
                .noneMatch(object -> getCurrentRoom().containsObject(object)
                        || getInventory().contains(object) || getObjectNotAssignedRoom().contains(object))
                && !tokenObjects.isEmpty()) {
            throw new ObjectNotFoundInRoomException();
        }

        return correctObject;
    }

    public void createRooms(Room root) {
        Set<Room> visited = new LinkedHashSet<>();
        Queue<Room> queue = new LinkedList<>();

        queue.add(root);
        visited.add(root);

        while (!queue.isEmpty()) {
            Room vertex = queue.poll();
            List<Room> link = new ArrayList<>();
            if (vertex.getNorth() != null) {
                link.add(vertex.getNorth());
            }
            if (vertex.getSouth() != null) {
                link.add(vertex.getSouth());
            }
            if (vertex.getEast() != null) {
                link.add(vertex.getEast());
            }
            if (vertex.getWest() != null) {
                link.add(vertex.getWest());
            }
            for (Room v : link) {
                if (!visited.contains(v)) {
                    visited.add(v);
                    queue.add(v);
                }
            }
        }

        rooms.addAll(visited);
    }

    private void connect() throws SQLException {
        conn = DriverManager.getConnection(DATABASE_PATH);
    }

    public void saveGame(String username, int gameType) throws SQLException, IOException {
        String newGame = "insert into game values(null, ?, ?)";
        String userGame = "insert into user_game values (? , null)";
        String findGame = "select * from game natural join user_game where username = ? and game_type = ?";
        String updateGame = "update game set game_saved = ? where id = ?";
        PreparedStatement newUserGame;
        PreparedStatement newGameStatement;
        PreparedStatement findExistingGame;
        PreparedStatement updateExistingGame;
        ResultSet existingGame;
        int idUser;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);

        oos.writeObject(this);
        oos.flush();
        oos.close();
        bos.close();
        byte[] game = bos.toByteArray();

        findExistingGame = conn.prepareStatement(findGame);
        findExistingGame.setString(1, username);
        findExistingGame.setInt(2, gameType);
        existingGame = findExistingGame.executeQuery();

        if (existingGame.next()) {
            idUser = existingGame.getInt("id");
            updateExistingGame = conn.prepareStatement(updateGame);
            updateExistingGame.setObject(1, game);
            updateExistingGame.setInt(2, idUser);
            updateExistingGame.executeUpdate();
            updateExistingGame.close();
        } else {
            newGameStatement = conn.prepareStatement(newGame);
            newGameStatement.setObject(1, game);
            newGameStatement.setObject(2, gameType);
            newGameStatement.executeUpdate();
            newGameStatement.close();
            newUserGame = conn.prepareStatement(userGame);
            newUserGame.setObject(1, username);
            newUserGame.executeUpdate();
            newUserGame.close();
        }
    }

}
