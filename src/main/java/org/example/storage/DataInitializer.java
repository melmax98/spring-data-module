package org.example.storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Event;
import org.example.model.Ticket;
import org.example.model.User;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Slf4j
public class DataInitializer {

    @Value("${data.filepath}")
    private String dataFilePath;

    @Setter
    private DataSaver dataSaver;

    @PostConstruct
    private void init() {
        Gson gson = new GsonBuilder().create();

        try (FileReader reader = new FileReader(dataFilePath); BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("event") && line.contains("user")) {
                    Ticket ticket = gson.fromJson(line, Ticket.class);
                    dataSaver.createTicketIfUserAndEventExist(ticket);
                    log.info("Ticket from file was created.");
                } else if (line.contains("name")) {
                    User user = gson.fromJson(line, User.class);
                    dataSaver.createUser(user);
                    log.info("User from file was created.");
                } else if (line.contains("title")) {
                    Event event = gson.fromJson(line, Event.class);
                    dataSaver.createEvent(event);
                    log.info("Event from file was created.");
                } else {
                    log.warn("Wrong format of data");
                }
            }
        } catch (IOException e) {
            log.error("Was not able to find data file in path: {}", dataFilePath);
        }
    }
}