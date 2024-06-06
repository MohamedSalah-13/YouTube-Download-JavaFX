package com.hamza.youtubedownload;

import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

@Log4j2
public class WriteReadFiles {

    public static final String ID = "id";
    public static final String TITLE = "title";

    @SuppressWarnings("unchecked")
    public void writeFile() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ID", "1");
        jsonObject.put("First_Name", "Mohamed");
        jsonObject.put("Last_Name", "Salah");
        try {
            FileWriter file = new FileWriter("E:/output.json");
            file.write(jsonObject.toJSONString());
            file.close();
        } catch (IOException e) {
            log.error(e.getClass().getCanonicalName(), e.getCause());
        }
        System.out.println("JSON file created: " + jsonObject);
    }

    public JSONObject getJsonObject() {
        JSONParser jsonParser = new JSONParser();
        Object obj = new JSONObject();
        try (FileReader reader = new FileReader("data/fileData.json")) {
            obj = jsonParser.parse(reader);
        } catch (ParseException | IOException e) {
            log.error(e.getClass().getCanonicalName(), e.getCause());
        }
        return (JSONObject) obj;
    }

    public HashMap<String, String> extracted() {
        HashMap<String, String> stringHashMap = new HashMap<>();
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("data/fileData.json")) {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            stringHashMap = parseEmployeeObject((JSONObject) obj);
//            JSONArray employeeList = (JSONArray) obj;
//            System.out.println(employeeList);

            //Iterate over employee array
//            employeeList.forEach( emp -> parseEmployeeObject( (JSONObject) emp ) );
        } catch (ParseException | IOException e) {
            log.error(e.getClass().getCanonicalName(), e.getCause());
        }

        return stringHashMap;
    }

    private HashMap<String, String> parseEmployeeObject(JSONObject employee) {
//        JSONObject employeeObject = (JSONObject) employee.get("employee");
        HashMap<String, String> stringHashMap = new HashMap<>();
        //Get employee first name
        String firstName = (String) employee.get(ID);
        String lastName = (String) employee.get(TITLE);

        stringHashMap.put(ID, firstName);
        stringHashMap.put(TITLE, lastName);
        return stringHashMap;
    }
}

