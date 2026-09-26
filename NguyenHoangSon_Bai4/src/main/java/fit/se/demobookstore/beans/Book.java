package fit.se.demobookstore.beans;

import java.io.Serializable;

public class Book implements Serializable {
    private int id;
    private String tittle;
    private String author;
    private String imgbook;
    private double price;
    private int quantity;

    public Book() {
    }

    public Book(int id, String tittle, String author, String imgbook, double price, int quantity) {
        this.id = id;
        this.tittle = tittle;
        this.author = author;
        this.imgbook = imgbook;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImgbook() {
        return imgbook;
    }

    public void setImgbook(String imgbook) {
        this.imgbook = imgbook;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
