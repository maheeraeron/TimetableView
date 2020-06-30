package com.github.tlaabs.timetableview;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SaveManager {
    public static String saveSticker(HashMap<Integer, Sticker> stickers) {
        JsonObject obj1 = new JsonObject();
        JsonArray arr1 = new JsonArray();
        int[] orders = getSortedKeySet(stickers);
        for (int i = 0; i < orders.length; i++) {
            JsonObject obj2 = new JsonObject();
            int idx = orders[i];
            obj2.addProperty("idx", orders[i]);
            JsonArray arr2 = new JsonArray();//5
            ArrayList<TimeTableCourse> timeTableCourses = stickers.get(idx).getTimeTableCourses();
            for (TimeTableCourse timeTableCourse : timeTableCourses) {
                JsonObject obj3 = new JsonObject();
                obj3.addProperty("subject", timeTableCourse.getSubject());
                obj3.addProperty("subjectCode", timeTableCourse.getSubjectCode());
                obj3.addProperty("crn", timeTableCourse.getCrn());
                obj3.addProperty("name", timeTableCourse.getName());
                obj3.addProperty("instructor", timeTableCourse.getInstructor());
                obj3.addProperty("lectureType", timeTableCourse.getLectureType());
                obj3.addProperty("day", timeTableCourse.getDay());
                obj3.addProperty("location", timeTableCourse.location);

                // startTime
                JsonObject obj4 = new JsonObject();
                obj4.addProperty("hour", timeTableCourse.getStartTime().getHour());
                obj4.addProperty("minute", timeTableCourse.getStartTime().getMinute());
                obj3.add("startTime", obj4);

                // endTime
                JsonObject obj5 = new JsonObject();
                obj5.addProperty("hour", timeTableCourse.getEndTime().getHour());
                obj5.addProperty("minute", timeTableCourse.getEndTime().getMinute());
                obj3.add("endTime", obj5);

                arr2.add(obj3);
            }
            obj2.add("schedule", arr2);
            arr1.add(obj2);
        }
        obj1.add("sticker", arr1);
        return obj1.toString();
    }

    public static HashMap<Integer, Sticker> loadSticker(String json) {
        HashMap<Integer, Sticker> stickers = new HashMap<Integer, Sticker>();
        JsonParser parser = new JsonParser();
        JsonObject obj1 = (JsonObject) parser.parse(json);
        JsonArray arr1 = obj1.getAsJsonArray("sticker");
        for (int i = 0; i < arr1.size(); i++) {
            Sticker sticker = new Sticker();
            JsonObject obj2 = (JsonObject) arr1.get(i);
            int idx = obj2.get("idx").getAsInt();
            JsonArray arr2 = (JsonArray) obj2.get("schedule");
            for (int k = 0; k < arr2.size(); k++) {
                TimeTableCourse timeTableCourse = new TimeTableCourse();
                JsonObject obj3 = (JsonObject) arr2.get(k);

                timeTableCourse.setSubject(obj3.get("subject").getAsString());
                timeTableCourse.setSubjectCode(obj3.get("subjectCode").getAsString());
                timeTableCourse.setCrn(obj3.get("crn").getAsInt());
                timeTableCourse.setName(obj3.get("name").getAsString());
                timeTableCourse.setInstructor(obj3.get("instructor").getAsString());
                timeTableCourse.setLectureType(obj3.get("lectureType").getAsString());
                timeTableCourse.setDay(obj3.get("day").getAsInt());
                timeTableCourse.setLocation(obj3.get("location").getAsString());

                Time startTime = new Time();
                JsonObject obj4 = (JsonObject) obj3.get("startTime");
                startTime.setHour(obj4.get("hour").getAsInt());
                startTime.setMinute(obj4.get("minute").getAsInt());
                timeTableCourse.setStartTime(startTime);

                Time endTime = new Time();
                JsonObject obj5 = (JsonObject) obj3.get("endTime");
                endTime.setHour(obj5.get("hour").getAsInt());
                endTime.setMinute(obj5.get("minute").getAsInt());
                timeTableCourse.setEndTime(endTime);

                sticker.addSchedule(timeTableCourse);
            }
            stickers.put(idx, sticker);
        }
        return stickers;
    }


    static private int[] getSortedKeySet(HashMap<Integer, Sticker> stickers) {
        int[] orders = new int[stickers.size()];
        int i = 0;
        for (int key : stickers.keySet()) {
            orders[i++] = key;
        }
        Arrays.sort(orders);
        return orders;
    }
}
