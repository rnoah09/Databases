package com.mistershorr.databases;

import android.os.Parcel;
import android.os.Parcelable;

public class Game implements Parcelable, Comparable<Game> {

    private String name;
    private String objective;
    private String rule;
    private int maxPlayer;
    private int minPlayer;
    private double rating;
    private boolean usesCards;
    private boolean usesCoins;
    private boolean usesDice;
    private boolean usesGameMaster;
    private boolean usesPaper;
    // need objectId and ownerId
    private String objectId;
    private String ownerId;

    public Game(){

    }
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public void setMaxPlayer(int maxPlayer) {
        this.maxPlayer = maxPlayer;
    }

    public int getMinPlayer() {
        return minPlayer;
    }

    public void setMinPlayer(int minPlayer) {
        this.minPlayer = minPlayer;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public boolean isUsesCards() {
        return usesCards;
    }

    public void setUsesCards(boolean usesCards) {
        this.usesCards = usesCards;
    }

    public boolean isUsesCoins() {
        return usesCoins;
    }

    public void setUsesCoins(boolean usesCoins) {
        this.usesCoins = usesCoins;
    }

    public boolean isUsesDice() {
        return usesDice;
    }

    public void setUsesDice(boolean usesDice) {
        this.usesDice = usesDice;
    }

    public boolean isUsesGameMaster() {
        return usesGameMaster;
    }

    public void setUsesGameMaster(boolean usesGameMaster) {
        this.usesGameMaster = usesGameMaster;
    }

    public boolean isUsesPaper() {
        return usesPaper;
    }

    public void setUsesPaper(boolean usesPaper) {
        this.usesPaper = usesPaper;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.objective);
        dest.writeString(this.rule);
        dest.writeInt(this.maxPlayer);
        dest.writeInt(this.minPlayer);
        dest.writeDouble(this.rating);
        dest.writeByte(this.usesCards ? (byte) 1 : (byte) 0);
        dest.writeByte(this.usesCoins ? (byte) 1 : (byte) 0);
        dest.writeByte(this.usesDice ? (byte) 1 : (byte) 0);
        dest.writeByte(this.usesGameMaster ? (byte) 1 : (byte) 0);
        dest.writeByte(this.usesPaper ? (byte) 1 : (byte) 0);
        dest.writeString(this.objectId);
        dest.writeString(this.ownerId);
    }

    protected Game(Parcel in) {
        this.name = in.readString();
        this.objective = in.readString();
        this.rule = in.readString();
        this.maxPlayer = in.readInt();
        this.minPlayer = in.readInt();
        this.rating = in.readDouble();
        this.usesCards = in.readByte() != 0;
        this.usesCoins = in.readByte() != 0;
        this.usesDice = in.readByte() != 0;
        this.usesGameMaster = in.readByte() != 0;
        this.usesPaper = in.readByte() != 0;
        this.objectId = in.readString();
        this.ownerId = in.readString();
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel source) {
            return new Game(source);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    //TODO: SORTING
    //TODO: SORTING
    //TODO: SORTING
    //TODO: SORTING
    //TODO: SORTING

    @Override
    public int compareTo(Game game) {
        String game1 = game.getName().toUpperCase();

        if(name.compareTo(game1) > 0){
            return 1;
        }
        else if (name.compareTo(game1) < 0){
            return -1;
        }
        return 0;
    }
}
