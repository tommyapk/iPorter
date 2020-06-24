package com.example.iporter2.model;

public class DbManager {

    public String username, nama_lengkap, email, pass;

    public DbManager(){

    }

    public DbManager(String username, String nama_lengkap, String email, String pass) {
        this.username = username;
        this.nama_lengkap = nama_lengkap;
        this.email = email;
        this.pass = pass;
    }
}

