package com.nay.esokodemo.Models;

import java.util.ArrayList;

/**
 * Created by nayram on 3/29/17.
 */

public class ListUsers {

    public String page;
    public int per_page,total,total_pages;
    public ArrayList<Users> data;



    public class Users {
         int id;
         String first_name,last_name,avatar;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }
}
