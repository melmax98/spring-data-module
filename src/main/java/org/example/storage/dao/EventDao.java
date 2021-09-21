package org.example.storage.dao;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Event;
import org.example.model.Storable;
import org.example.storage.NoData;
import org.joda.time.DateTimeComparator;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class EventDao implements Dao {

    @Setter
    private NoData noData;

    private static final String EVENT_TITLE = "event:";

    @Override
    public Event save(Storable storable) {
        long lastEventId = getStorage().values()
                .stream()
                .filter(Event.class::isInstance)
                .map(Event.class::cast)
                .mapToLong(Event::getEventId)
                .max()
                .orElse(0L);

        Event event = (Event) storable;
        event.setEventId(lastEventId + 1);
        String entityKey = EVENT_TITLE + event.getEventId();

        getStorage().put(entityKey, event);
        return event;
    }

    @Override
    public Event update(Storable storable) {
        Event event = getEventById(((Event) storable).getEventId());

        if (event == null) {
            log.warn("Could not update the event with id {} because it does not exist", ((Event) storable).getEventId());
            return null;
        }

        event.setEventId(event.getEventId());
        event.setTitle(event.getTitle());
        event.setDate(event.getDate());

        String entityKey = EVENT_TITLE + event.getEventId();
        getStorage().put(entityKey, event);
        return event;
    }

    @Override
    public boolean delete(long eventId) {
        Event event = getEventById(eventId);

        if (event == null) {
            log.warn("Could not delete the event with id {} because it does not exist", eventId);
            return false;
        }

        getStorage().remove(EVENT_TITLE + eventId);
        return true;
    }

    public Event getEventById(long eventId) {
        String key = getStorage().keySet()
                .stream()
                .filter((EVENT_TITLE + eventId)::equals)
                .findFirst()
                .orElse(null);

        if (key == null) {
            log.warn("Event with id {} not found", eventId);
            return null;
        }
        return (Event) getStorage().get(key);
    }

    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        List<Storable> matchingEvents = getStorage().values()
                .stream()
                .filter(Event.class::isInstance)
                .map(Event.class::cast)
                .filter(event -> title.equals(event.getTitle()))
                .sorted(Comparator.comparing(Event::getEventId))
                .collect(Collectors.toList());

        if (matchingEvents.isEmpty()) {
            log.info("No events with title \"{}\" found", title);
            return Collections.emptyList();
        }

        return getEventsPage(pageSize, pageNum, matchingEvents);
    }

    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        DateTimeComparator dateTimeComparator = DateTimeComparator.getDateOnlyInstance();

        List<Storable> matchingEvents = getStorage().values()
                .stream()
                .filter(Event.class::isInstance)
                .map(Event.class::cast)
                .filter(event -> dateTimeComparator.compare(day, event.getDate()) == 0)
                .sorted(Comparator.comparing(Event::getEventId))
                .collect(Collectors.toList());

        if (matchingEvents.isEmpty()) {
            log.info("No events with specified date found");
            return Collections.emptyList();
        }

        return getEventsPage(pageSize, pageNum, matchingEvents);
    }

    private List<Event> getEventsPage(int pageSize, int pageNum, List<Storable> matchingEvents) {
        List<Storable> page = getPage(matchingEvents, pageNum, pageSize);

        return page
                .stream()
                .map(Event.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, Storable> getStorage() {
        return noData.getStorage();
    }

    public List<Event> getAllEvents() {
        return getStorage().values()
                .stream()
                .filter(Event.class::isInstance)
                .map(Event.class::cast)
                .sorted(Comparator.comparing(Event::getEventId))
                .collect(Collectors.toList());
    }
}
