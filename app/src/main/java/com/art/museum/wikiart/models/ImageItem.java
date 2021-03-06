package com.art.museum.wikiart.models;

/**
 * Created by arpita on 20/09/16.
 */
public class ImageItem {
    private String name;
    private String artist;
    private String link;
    private int idx;
    private int rank;
    private int position;
    private String color;
    private String date;
    private String dColor;
    private String style;
    private String sColor;
    private String genre;
    private String gColor;

    public ImageItem(){
        super();
        this.idx = 0;
        this.rank = 0;
        this.name = null;
        this.link = null;
        this.position = 0;
        this.artist = null;
    }

    public ImageItem(String name, String link, int idx) {
        super();
        this.name = name;
        this.link = link;
        this.idx = idx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getIndex() {
        return idx;
    }

    public void setIndex(int idx) {
        this.idx = idx;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getColor(){
        return color;
    }

    public void setColor(String color){ this.color = color; }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getdColor(){
        return dColor;
    }

    public void setdColor(String dColor){ this.dColor = dColor; }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getsColor(){
        return sColor;
    }

    public void setsColor(String sColor){ this.sColor = sColor; }

    public String getGenre() {
        return genre;
    }

    public void setGente(String genre) {
        this.genre = genre;
    }
    public String getgColor(){
        return gColor;
    }

    public void setgColor(String gColor){ this.gColor = gColor; }


    @Override
    public boolean equals(Object obj) {
        ImageItem image = (ImageItem) obj;
        if(this.idx != image.idx) return false;
        if(!this.name.equals(image.name)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 53 * hash + this.idx;
        return hash;
    }
}
