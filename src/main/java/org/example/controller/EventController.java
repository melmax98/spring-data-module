package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.facade.BookingFacade;
import org.example.model.Event;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

    private final BookingFacade bookingFacade;

    private static final String EVENTS_ATTRIBUTE = "events";
    private static final String EVENTS_VIEW = "eventList";

    @GetMapping("/title/{title}")
    public String getEventByName(@PathVariable String title,
                                 @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                 @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                 Model model) {
        List<Event> eventsByTitle = bookingFacade.getEventsByTitle(title, pageSize, pageNum);
        model.addAttribute(EVENTS_ATTRIBUTE, eventsByTitle);

        return EVENTS_VIEW;
    }

    @GetMapping("/date/{date}")
    public String getEventsForDay(@PathVariable String date,
                                  @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                  @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                  Model model) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate = simpleDateFormat.parse(date, new ParsePosition(0));
        List<Event> eventsForDay = bookingFacade.getEventsForDay(parsedDate, pageSize, pageNum);
        model.addAttribute(EVENTS_ATTRIBUTE, eventsForDay);

        return EVENTS_VIEW;
    }

    @GetMapping("{id}")
    public String getEventById(@PathVariable Integer id, Model model) {
        Event event = bookingFacade.getEventById(id);
        model.addAttribute("event", event);
        return "eventEdit";
    }

    @GetMapping("/new")
    public String createEventForm() {
        return "createEvent";
    }

    @PostMapping
    public String createEvent(@RequestParam String title,
                              @RequestParam String date,
                              Model model) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyy");
        Date parsedDate = simpleDateFormat.parse(date, new ParsePosition(0));
        Event event = bookingFacade.createEvent(new Event(title, parsedDate, 0));
        model.addAttribute(EVENTS_ATTRIBUTE, Collections.singletonList(event));
        return EVENTS_VIEW;
    }

    @PostMapping("/update/{eventId}")
    public String updateEvent(@RequestParam String title,
                              @RequestParam String date,
                              @PathVariable Integer eventId,
                              Model model) {
        Event event = bookingFacade.getEventById(eventId);
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyy");
        if (event != null) {
            event.setTitle(title);
            Date parsedDate = simpleDateFormat.parse(date, new ParsePosition(0));
            event.setDate(parsedDate);
            Event updatedEvent = bookingFacade.updateEvent(event);
            List<Event> updatedEventForModel = Collections.singletonList(updatedEvent);
            model.addAttribute(EVENTS_ATTRIBUTE, updatedEventForModel);
        }
        return EVENTS_VIEW;
    }

    @ResponseBody
    @PostMapping("/delete/{eventId}")
    public String deleteEvent(@PathVariable Integer eventId) {
        if (bookingFacade.deleteEvent(eventId)) {
            return "Event was successfully deleted";
        }
        return "Event was not deleted";
    }
}
