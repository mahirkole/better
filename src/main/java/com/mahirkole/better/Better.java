package com.mahirkole.better;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Better {

    private final ApiBuilder apiBuilder;

    public Better(){
        this.apiBuilder = new ApiBuilder(new OkHttpClient());
    }

    public static void main(String[] args) throws IOException {
        new Better().printBulletin();
    }

    public void printBulletin() throws IOException {
        //System.out.println(apiBuilder.getEvents(BulletinType.FOOTBALL));
        Gson gson = new Gson();
        System.out.println(gson.toJson(apiBuilder.getEvents(BulletinType.FOOTBALL)));
    }

    public List<Event> getEvents(){
        return new ArrayList<Event>();
    }

    public Event getEvent(Short matchCode){
        return new Event(null, null);
    }

}
