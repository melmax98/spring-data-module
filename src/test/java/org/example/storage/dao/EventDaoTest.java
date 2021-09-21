package org.example.storage.dao;

import org.example.model.Event;
import org.example.model.Storable;
import org.example.storage.NoData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventDaoTest {

    @Mock
    private NoData noData;

    @InjectMocks
    private EventDao eventDao;

    @Test
    void save() {
        when(noData.getStorage()).thenReturn(new HashMap<>());

        Event event = new Event();
        event.setDate(new Date());
        event.setTitle("New");

        Event savedEvent = eventDao.save(event);

        assertEquals(1, savedEvent.getEventId());
        assertEquals(event.getDate(), savedEvent.getDate());
        assertEquals(event.getTitle(), savedEvent.getTitle());
    }

    @Test
    void update_eventExists() {
        HashMap<String, Storable> storage = new HashMap<>();
        Event event = new Event();
        event.setEventId(1L);
        event.setDate(new Date());
        event.setTitle("New");
        storage.put("event:1", event);

        when(noData.getStorage()).thenReturn(storage);

        event.setTitle("title");
        Event updatedEvent = eventDao.update(event);

        assertEquals(1, updatedEvent.getEventId());
        assertEquals("title", event.getTitle());
    }

    @Test
    void update_eventDoesNotExist() {
        HashMap<String, Storable> storage = new HashMap<>();
        Event event = new Event();
        event.setEventId(1L);
        event.setDate(new Date());
        event.setTitle("New");

        when(noData.getStorage()).thenReturn(storage);

        event.setTitle("title");
        Event updatedEvent = eventDao.update(event);

        assertNull(updatedEvent);
    }

    @Test
    void delete_eventExists() {
        HashMap<String, Storable> storage = new HashMap<>();
        Event event = new Event();
        event.setEventId(1L);
        event.setDate(new Date());
        event.setTitle("New");
        storage.put("event:1", event);

        when(noData.getStorage()).thenReturn(storage);

        boolean eventDeleted = eventDao.delete(1);

        assertTrue(eventDeleted);
    }

    @Test
    void delete_eventDoesNotExist() {
        when(noData.getStorage()).thenReturn(new HashMap<>());

        boolean eventDeleted = eventDao.delete(1);

        assertFalse(eventDeleted);
    }

    @Test
    void getEventsByTitle() {
        HashMap<String, Storable> storage = new HashMap<>();
        for (int i = 1; i < 10; i++) {
            Event event = new Event();
            event.setEventId(1L);
            event.setDate(new Date());
            event.setTitle("New");
            storage.put("event:" + i, event);
        }

        when(noData.getStorage()).thenReturn(storage);

        List<Event> eventsList = eventDao.getEventsByTitle("New", 5, 2);

        assertEquals(4, eventsList.size());
    }

    @Test
    void getEventsByTitle_noEventsWithSuchTitle() {
        when(noData.getStorage()).thenReturn(new HashMap<>());

        List<Event> eventsList = eventDao.getEventsByTitle("New", 5, 1);

        assertTrue(eventsList.isEmpty());
    }

    @Test
    void getEventsByDate() {
        HashMap<String, Storable> storage = new HashMap<>();
        for (int i = 1; i < 10; i++) {
            Event event = new Event();
            event.setEventId(1L);
            event.setDate(new Date());
            event.setTitle("New");
            storage.put("event:" + i, event);
        }

        when(noData.getStorage()).thenReturn(storage);

        List<Event> eventsList = eventDao.getEventsForDay(new Date(), 5, 2);

        assertEquals(4, eventsList.size());
    }

    @Test
    void getEventsByDate_noEventsWithSuchDate() {
        when(noData.getStorage()).thenReturn(new HashMap<>());

        List<Event> eventsList = eventDao.getEventsForDay(new Date(), 5, 1);

        assertTrue(eventsList.isEmpty());
    }
}