package com.zenyte.game.content.grandexchange;

import java.time.Instant;

 public class JSONGEItemDefinitions {

     //All of id, name and price are used!
     private int id;
     private String name;
     private int price;

     public JSONGEItemDefinitions(int id, String name, int price) {
         this.id = id;
         this.name = name;
         this.price = price;
     }

     public JSONGEItemDefinitions() {
     }

     public int getId() {
         return id;
     }

     public void setId(int id) {
         this.id = id;
     }

     public String getName() {
         return name;
     }

     public void setName(String name) {
         this.name = name;
     }

     public int getPrice() {
         return price;
     }

     public void setPrice(int price) {
         this.price = price;
     }

 }
