package com.vanlang.webbanhang.model;

public class CartItem {
    private Product product;
    private int quantity;
    private String address;
    private String phoneNumber;
    private String email;
    private String note;
    private String payment;
    //constructor
    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;

    }


    public CartItem(Product product, int quantity, String address, String phoneNumber, String email, String note, String payment) {
        this.product = product;
        this.quantity = quantity;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.note = note;
        this.payment = payment;
    }


    //getter & setter

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
