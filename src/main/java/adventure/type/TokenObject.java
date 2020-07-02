package adventure.type;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import adventure.parser.Token;
import adventure.parser.TokenType;

/**
 * @author Corona-Extra
 */
public class TokenObject extends Token {

    private final int id;
    private final Set<TokenAdjective> adjectives = new HashSet<>();
    private String name;
    private String description;
    private boolean openable = false;
    private boolean pickupable = false;
    private boolean give = false;
    private boolean mixable = false;
    private boolean usable = false;
    private boolean pushable = false;
    private boolean eatable = false;
    private boolean turnOnAble = false;
    private boolean sit = false; //indica se si può sedere su un oggetto
    private boolean given = false;
    private boolean eat = false;
    private boolean open = false;
    private boolean push = false;
    private boolean on = true;
    private boolean climb = false;
    private boolean used = false;

    public TokenObject(int id, String name, Set<String> alias) {
        super(TokenType.OBJECT);
        this.id = id;
        this.name = name;
        super.setAlias(alias);
    }

    public TokenObject(int id, String name, Set<String> alias, String description) {
        super(TokenType.OBJECT);
        this.id = id;
        this.name = name;
        super.setAlias(alias);
        this.description = description;
    }

    public TokenObject(int id, String name, Set<String> alias, String description, Set<TokenAdjective> adjectives) {
        super(TokenType.OBJECT);
        this.id = id;
        this.name = name;
        super.setAlias(alias);
        this.description = description;
        this.adjectives.addAll(adjectives);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<TokenAdjective> getAdjectives() {
        return adjectives;
    }

    public void setAdjectives(Set<String> adjectives) {
        adjectives.addAll(adjectives);
    }

    public void setAdjective(TokenAdjective adjective) {
        this.adjectives.add(adjective);
    }

    public boolean isOpenable() {
        return openable;
    }

    public void setOpenable(boolean openable) {
        this.openable = openable;
    }

    public boolean isTurnOnAble() {
        return turnOnAble;
    }

    public void setTurnOnAble(boolean turnOnAble) {
        this.turnOnAble = turnOnAble;
    }

    public boolean isClimb() {
        return climb;
    }

    public void setClimb(boolean climb) {
        this.climb = climb;
    }

    public boolean isPickupable() {
        return pickupable;
    }

    public void setPickupable(boolean pickupable) {
        this.pickupable = pickupable;
    }

    public boolean isPushable() {
        return pushable;
    }

    public void setPushable(boolean pushable) {
        this.pushable = pushable;
    }

    public boolean isSit() {
        return sit;
    }

    public void setSit(boolean sit) {
        this.sit = sit;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isMixable() {
        return mixable;
    }

    public void setMixable(boolean mixable) {
        this.mixable = mixable;
    }

    public boolean isEat() {
        return eat;
    }

    public void setEat(boolean eat) {
        this.eat = eat;
    }

    public boolean isEatable() {
        return eatable;
    }

    public void setEatable(boolean eatable) {
        this.eatable = eatable;
    }

    public boolean isUsable() {
        return usable;
    }

    public void setUsable(boolean usable) {
        this.usable = usable;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public boolean isGive() {
        return give;
    }

    public void setGive(boolean give) {
        this.give = give;
    }

    public boolean isGiven() {
        return given;
    }

    public void setGiven(boolean given) {
        this.given = given;
    }

    public boolean isPush() {
        return push;
    }

    public void setPush(boolean push) {
        this.push = push;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenObject that = (TokenObject) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
