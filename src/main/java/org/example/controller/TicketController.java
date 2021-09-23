package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.facade.BookingFacade;
import org.example.model.Event;
import org.example.model.Ticket;
import org.example.model.TicketCategory;
import org.example.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {
    private final BookingFacade bookingFacade;

    private static final String TICKETS_ATTRIBUTE = "tickets";
    private static final String TICKETS_VIEW = "ticketList";

    @GetMapping("/user/{id}")
    public String getBookedTicketsByUser(@PathVariable Integer id,
                                         @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                         @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                         Model model) {

        User user = bookingFacade.getUserById(id);
        List<Ticket> ticketsByUser = bookingFacade.getBookedTickets(user, pageSize, pageNum);
        model.addAttribute(TICKETS_ATTRIBUTE, ticketsByUser);

        return TICKETS_VIEW;
    }

    @GetMapping("/event/{id}")
    public String getBookedTicketsByEvent(@PathVariable Integer id,
                                          @RequestParam(name = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                          @RequestParam(name = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                          Model model) {

        Event event = bookingFacade.getEventById(id);
        List<Ticket> ticketsByEvent = bookingFacade.getBookedTickets(event, pageSize, pageNum);
        model.addAttribute(TICKETS_ATTRIBUTE, ticketsByEvent);

        return TICKETS_VIEW;
    }

    @GetMapping("{id}")
    public String getTicketById(@PathVariable Integer id, Model model) {
        Ticket ticket = bookingFacade.getTicketById(id);
        model.addAttribute("ticket", ticket);
        return "ticketCancel";
    }

    @ResponseBody
    @PostMapping("/cancel/{ticketId}")
    public String cancelTicket(@PathVariable Integer ticketId) {
        if (bookingFacade.cancelTicket(ticketId)) {
            return "Ticket was successfully canceled";
        }
        return "Ticket was not canceled";
    }

    @GetMapping("/new")
    public String bookTicketForm(Model model) {
        model.addAttribute("users", bookingFacade.getAllUsers());
        model.addAttribute("events", bookingFacade.getAllEvents());
        model.addAttribute("categories", Arrays.asList(TicketCategory.values()));
        return "bookTicket";
    }

    @PostMapping
    public String bookTicket(@RequestParam Integer userId,
                             @RequestParam Integer eventId,
                             @RequestParam Integer place,
                             @RequestParam String categoryName,
                             Model model) {

        Ticket ticket = bookingFacade.bookTicket(userId, eventId, place, TicketCategory.valueOf(categoryName));
        model.addAttribute(TICKETS_ATTRIBUTE, Collections.singletonList(ticket));
        return TICKETS_VIEW;
    }

    @ResponseBody
    @PostMapping("/preload")
    public String preloadTickets(@RequestParam("file") MultipartFile file) throws IOException {
        byte[] byteArr = file.getBytes();
        InputStream inputStream = new ByteArrayInputStream(byteArr);
        bookingFacade.preloadTickets(inputStream);
        return "Success";
    }

}
