package org.example.controller;

import org.example.facade.BookingFacade;
import org.example.model.Event;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class EventControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private EventController eventController;

    @Autowired
    private BookingFacade bookingFacade;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyy");

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();
    }

    @Test
    public void createEventViewTest() throws Exception {
        this.mockMvc.perform(get("/event/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("createEvent"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void getEventsByTitleTest() throws Exception {
        this.mockMvc.perform(get("/event/title/test"))
                .andExpect(status().isOk())
                .andExpect(view().name("eventList"))
                .andReturn();
    }

    @Test
    public void getEventsByDateTest() throws Exception {
        this.mockMvc.perform(get("/event/date/2099-10-14"))
                .andExpect(status().isOk())
                .andExpect(view().name("eventList"))
                .andReturn();
    }

    @Test
    public void getEventEditForm() throws Exception {
        this.mockMvc.perform(get("/event/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("eventEdit"))
                .andReturn();
    }

    @Test
    public void createEvent() throws Exception {
        this.mockMvc.perform(post("/event")
                        .param("title", "America")
                        .param("date", "Sep 10, 2099"))
                .andExpect(status().isOk())
                .andExpect(view().name("eventList"))
                .andReturn();

        Event createdEvent = bookingFacade.getAllEvents().stream().max(Comparator.comparing(Event::getEventId)).orElseThrow(NullPointerException::new);
        assertEquals("America", createdEvent.getTitle());
        assertEquals("Sep 10, 2099", simpleDateFormat.format(createdEvent.getDate()));
        assertTrue(bookingFacade.deleteEvent(createdEvent.getEventId()));
    }

    @Test
    public void updateEvent() throws Exception {
        Event event = bookingFacade.createEvent(new Event("test", new Date(1), 0));
        this.mockMvc.perform(post("/event/update/" + event.getEventId())
                        .param("title", "America")
                        .param("date", "Sep 10, 2099"))
                .andExpect(status().isOk())
                .andExpect(view().name("eventList"))
                .andReturn();

        Event updatedEvent = bookingFacade.getEventById(event.getEventId());
        assertEquals("America", updatedEvent.getTitle());
        assertEquals("Sep 10, 2099", simpleDateFormat.format(updatedEvent.getDate()));
        assertTrue(bookingFacade.deleteEvent(updatedEvent.getEventId()));
    }

    @Test
    public void deleteEvent_eventExists() throws Exception {
        Event event = bookingFacade.createEvent(new Event("test", new Date(1), 0));
        this.mockMvc.perform(post("/event/delete/" + event.getEventId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Event was successfully deleted"))
                .andReturn();

        assertNull(bookingFacade.getEventById(event.getEventId()));
    }

    @Test
    public void deleteEvent_eventDoesNotExist() throws Exception {
        this.mockMvc.perform(post("/event/delete/" + Integer.MAX_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string("Event was not deleted"))
                .andReturn();

        assertNull(bookingFacade.getEventById(Integer.MAX_VALUE));
    }
}