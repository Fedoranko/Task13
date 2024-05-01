package ru.cyn.model;

public class JsonParse {
    public String squadName;
    public Boolean active;
    public Members [] members;

    public static  class Members {
        public String name;
        public int age;
    }
}
