package ru.cyn.model;

import com.google.gson.annotations.SerializedName;

public class ParseMyJson {
    public  String myName;
    public MyWork [] myWork;

    public static class  MyWork {
//        @SerializedName("address_")
        public Address address;

        public static class Address{
            public String first;
            public String second;
        }
    }
}
