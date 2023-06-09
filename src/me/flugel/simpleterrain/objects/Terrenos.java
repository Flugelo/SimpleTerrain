package me.flugel.simpleterrain.objects;

import me.flugel.simpleterrain.Main;

import java.util.ArrayList;
import java.util.List;

public class Terrenos {
    private String owner;
    private List<String> friends;
    private ArrayList<String> chunksString;
    private int amount;

    public Terrenos(String owner, List<String> friends, ArrayList<String> chunksString) {
        this.owner = owner;
        this.friends = friends;
        this.chunksString = chunksString;
        Main.getInstance().getManager().getTerrenos().add(this);
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public ArrayList<String> getChunksString() {
        return chunksString;
    }

    public void setChunksString(ArrayList<String> chunksString) {
        this.chunksString = chunksString;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
