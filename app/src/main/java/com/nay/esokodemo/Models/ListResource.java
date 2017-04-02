package com.nay.esokodemo.Models;

import java.util.ArrayList;

/**
 * Created by nayram on 3/29/17.
 */

public class ListResource {
    public String page;
    public int per_page,total,total_pages;
    public ArrayList<ResData> data;

    public class ResData{
        int id,year;
        String name,pantone_value;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPantone_value() {
            return pantone_value;
        }

        public void setPantone_value(String pantone_value) {
            this.pantone_value = pantone_value;
        }
    }


}
