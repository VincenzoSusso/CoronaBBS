/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prisonbreak.type;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author pierpaolo
 */
public class AdvObject {

    private final int id;
    private String name;
    private String description;
    private Set<String> alias;
    private boolean openable = false;
    private boolean pickupable = true;
    private boolean give = false;
    private boolean mixable = false;
    private boolean usable = false;
    private boolean pushable = false;
    private boolean eatable = false;
    private boolean given = false;
    private boolean eat = false;
    private boolean open = false;
    private boolean push = false;
    private boolean on = false;
    private boolean used = false;


    public AdvObject(int id) {
        this.id = id;
    }

    public AdvObject(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public AdvObject(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public AdvObject(int id, String name, String description, Set<String> alias) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isOpenable() {
        return openable;
    }

    public void setOpenable(boolean openable) {
        this.openable = openable;
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

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public void setUsable(boolean usable) {
        this.usable = usable;
    }

    public void setEat(boolean eat) {
        this.eat = eat;
    }

    public void setEatable(boolean eatable) {
        this.eatable = eatable;
    }

    public void setGive(boolean give) {
        this.give = give;
    }

    public void setGiven(boolean given) {
        this.given = given;
    }

    public void setMixable(boolean mixable) {
        this.mixable = mixable;
    }

    public boolean isMixable() {
        return mixable;
    }

    public boolean isEat() {
        return eat;
    }

    public boolean isEatable() {
        return eatable;
    }

    public boolean isUsable() {
        return usable;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public boolean isUsed() {
        return used;
    }

    public boolean isOn() {
        return on;
    }

    public boolean isGive() {
        return give;
    }

    public boolean isGiven() {
        return given;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public boolean isPush() {
        return push;
    }

    public void setPush(boolean push) {
        this.push = push;
    }

    public Set<String> getAlias() {
        return alias;
    }

    public void setAlias(Set<String> alias) {
        this.alias = alias;
    }

    public void setAlias(String[] alias) {
        this.alias = new HashSet<>(Arrays.asList(alias));
    }

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AdvObject other = (AdvObject) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

}
