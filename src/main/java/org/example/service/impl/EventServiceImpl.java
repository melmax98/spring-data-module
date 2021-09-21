package org.example.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.model.Event;
import org.example.service.EventService;
import org.example.storage.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Date;
import java.util.List;

@Slf4j
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public Event getEventById(long eventId) {
        return eventRepository.findById(eventId).orElse(null);
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        Page<Event> eventsPage = eventRepository.findByTitle(title, PageRequest.of(pageNum - 1, pageSize));
        return eventsPage.getContent();
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        Page<Event> eventsPage = eventRepository.findByDate(day, PageRequest.of(pageNum - 1, pageSize));
        return eventsPage.getContent();
    }

    @Override
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(Event event) {
        Event foundEvent = eventRepository.findById(event.getEventId()).orElseThrow(NullPointerException::new);
        foundEvent.setTitle(event.getTitle());
        foundEvent.setDate(event.getDate());
        foundEvent.setTicketPrice(event.getTicketPrice());
        return eventRepository.save(foundEvent);
    }

    @Override
    public boolean deleteEvent(long eventId) {
        try {
            eventRepository.deleteById(eventId);
            return true;
        } catch (Exception e) {
            log.error("Was not able to delete event with id {}", eventId, e);
            return false;
        }
    }

    @Override
    public List<Event> getAllEvents() {
        return (List<Event>) eventRepository.findAll();
    }
}
