package com.mahirkole.better;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.*;

public class ApiBuilder {

    private static final String baseUrl = "https://bulletin.iddaa.com/data/bulletin-with-percentage";
    private final OkHttpClient client;

    ApiBuilder(OkHttpClient client) {
        this.client = client;
    }

    public List<Event> getEvents() throws IOException {
        JsonObject bulletin = getBulletinForBulletinType(BulletinType.ALL);
        List<Event> events = new LinkedList<Event>();

        for (Map.Entry<String, JsonElement> bulletinEntry : bulletin.entrySet()) {
            String bulletinTypeFromIddaa = bulletinEntry.getKey();
            BulletinType bulletinType = BulletinType.ALL;

            if (bulletinTypeFromIddaa.equals("football")) {
                bulletinType = BulletinType.FOOTBALL;
            } else if (bulletinTypeFromIddaa.equals("goldenFootball")) {
                bulletinType = BulletinType.GOLDEN_FOOTBALL;
            } else if (bulletinTypeFromIddaa.equals("basketball")) {
                bulletinType = BulletinType.BASKETBALL;
            } else if (bulletinTypeFromIddaa.equals("volleyball")) {
                bulletinType = BulletinType.VOLLEYBALL;
            } else if (bulletinTypeFromIddaa.equals("handball")) {
                bulletinType = BulletinType.HANDBALL;
            } else if (bulletinTypeFromIddaa.equals("tennis")) {
                bulletinType = BulletinType.TENNIS;
            } else if (bulletinTypeFromIddaa.equals("billards")) {
                bulletinType = BulletinType.BILLARDS;
            } else if (bulletinTypeFromIddaa.equals("motorsports")) {
                bulletinType = BulletinType.MOTORSPORTS;
            } else if (bulletinTypeFromIddaa.equals("other")) {
                bulletinType = BulletinType.OTHER;
            }

            events.addAll(getEventsForBulletinType(bulletin, bulletinType));
        }

        return events;
    }

    public List<Event> getEvents(BulletinType bulletinType) throws IOException {
        JsonObject bulletin = getBulletinForBulletinType(BulletinType.ALL);
        return getEventsForBulletinType(bulletin, bulletinType);
    }

    private List<Event> getEventsForBulletinType(JsonObject bulletin, BulletinType bulletinType) {
        //TODO: bulletinType ALL gelirse Exception fırlat

        List<Event> events = new LinkedList<Event>();
        String bulletinTypeForIddaa = "";

        switch (bulletinType) {
            case FOOTBALL:
                bulletinTypeForIddaa = "football";
                break;
            case GOLDEN_FOOTBALL:
                bulletinTypeForIddaa = "goldenFootball";
                break;
            case BASKETBALL:
                bulletinTypeForIddaa = "basketball";
                break;
            case VOLLEYBALL:
                bulletinTypeForIddaa = "volleyball";
                break;
            case HANDBALL:
                bulletinTypeForIddaa = "handball";
                break;
            case TENNIS:
                bulletinTypeForIddaa = "tennis";
                break;
            case BILLARDS:
                bulletinTypeForIddaa = "billard";
                break;
            case MOTORSPORTS:
                bulletinTypeForIddaa = "motorsports";
                break;
            case OTHER:
                bulletinTypeForIddaa = "other";
                break;
        }

        JsonArray eventListArray = bulletin.get(bulletinTypeForIddaa)
                .getAsJsonObject().get("eventList").getAsJsonArray();

        for (JsonElement eventElement : eventListArray) {
            JsonObject eventObject = eventElement.getAsJsonObject();
            JsonObject helperInfoObject = eventObject.get("helperInfo").getAsJsonObject();

            Long iddaaId = eventObject.get("id").getAsLong();
            Integer matchCode = eventObject.get("matchCode").getAsInt();
            Short mbs = eventObject.get("mbs").getAsShort();

            Long teamId = helperInfoObject.get("homeTeam").getAsJsonObject().get("id").getAsLong();
            String teamShortName = helperInfoObject.get("homeTeam").getAsJsonObject().get("shortName").getAsString();
            String teamName = helperInfoObject.get("homeTeam").getAsJsonObject().get("name").getAsString();

            Team home = new Team(teamId, teamShortName, teamName);

            teamId = helperInfoObject.get("awayTeam").getAsJsonObject().get("id").getAsLong();
            teamShortName = helperInfoObject.get("awayTeam").getAsJsonObject().get("shortName").getAsString();
            teamName = helperInfoObject.get("awayTeam").getAsJsonObject().get("name").getAsString();

            Team away = new Team(teamId, teamShortName, teamName);

            Map<OddType, Map<OddResult, Odd>> odds = getOddListForBulletinType(bulletinType, eventElement);

            //events.add(new Event(home, away));
            events.add(new Event(iddaaId, EventType.FOOTBALL, new Date(), matchCode, mbs, home, away, odds));
        }

        return events;
    }

    public JsonObject getBulletin() throws IOException {
        return getBulletinForBulletinType(BulletinType.ALL);
    }

    public JsonObject getBulletin(BulletinType bulletinType) throws IOException {
        return getBulletinForBulletinType(bulletinType);
    }

    private JsonObject getBulletinForBulletinType(BulletinType bulletinType) throws IOException {
        Request bulletinRequest = new Request.Builder().url(baseUrl).build();
        Response response = client.newCall(bulletinRequest).execute();

        Gson gson = new Gson();
        JsonObject bulletin = gson
                .fromJson(response.body().string(), JsonObject.class)
                .getAsJsonObject("data").getAsJsonObject("bulletin");

        switch (bulletinType) {
            case FOOTBALL:
                bulletin = bulletin.getAsJsonObject("football");
                break;
        }

        return bulletin;
    }

    private Map<OddType, Map<OddResult, Odd>> getOddListForBulletinType(BulletinType bulletinType, JsonElement eventElement) {
        switch (bulletinType) {
            case FOOTBALL:
                return getFootballOdds(eventElement);
        }
        return null;
    }

    private Map<OddType, Map<OddResult, Odd>> getFootballOdds(JsonElement eventElement) {
        Map<OddType, Map<OddResult, Odd>> odds = new HashMap<OddType, Map<OddResult, Odd>>();

        JsonArray oddList = eventElement.getAsJsonObject().get("oddList").getAsJsonArray();

        for (JsonElement oddElement : oddList) {
            OddType oddType;
            OddResult oddResult;
            String type = oddElement.getAsJsonObject().get("type").getAsString();
            Short mbs = oddElement.getAsJsonObject().get("mbs").getAsShort();
            Double rate = oddElement.getAsJsonObject().get("v").getAsDouble();

            if (type.equals("F.1")) {
                oddType = OddType.FINAL_RESULT;
                oddResult = OddResult.HOME;
            }else if(type.equals("F.X")){
                oddType = OddType.FINAL_RESULT;
                oddResult = OddResult.DRAW;
            }else if(type.equals("F.2")){
                oddType = OddType.FINAL_RESULT;
                oddResult = OddResult.AWAY;
            }else {
                oddType = OddType.FIRST_HALF_15_UNDER_OVER;
                oddResult = OddResult.UNDER;
            }

            Odd odd = new Odd(oddType, oddResult, mbs, rate);

            Map<OddResult, Odd> oddResultOddMap;

            if(odds.containsKey(oddType)){
                oddResultOddMap = odds.get(oddType);
            }else{
                oddResultOddMap = new HashMap<OddResult, Odd>();
            }

            oddResultOddMap.put(oddResult, odd);
            odds.put(oddType, oddResultOddMap);
        }

        return odds;
    }

}
