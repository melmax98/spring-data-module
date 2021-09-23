package org.example.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Event;
import org.example.model.Ticket;
import org.example.model.TicketCategory;
import org.example.model.User;
import org.example.model.UserAccount;
import org.example.service.EventService;
import org.example.service.TicketService;
import org.example.service.UserAccountService;
import org.example.service.UserService;
import org.example.util.XMLConverter;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class BookingFacadeImpl implements BookingFacade {

    private final EventService eventService;
    private final UserService userService;
    private final TicketService ticketService;
    private final XMLConverter xmlConverter;
    private final UserAccountService userAccountService;

    @Override
    public Boolean withdrawMoneyFromAccount(User user, Double amount) {
        return userAccountService.withdrawMoneyFromAccount(user, amount);
    }

    @Override
    public Boolean deleteUserAccount(long userAccountId) {
        return userAccountService.deleteUserAccount(userAccountId);
    }

    @Override
    public Double getBalanceByUser(User user) {
        return userAccountService.getBalanceByUser(user);
    }

    @Override
    public UserAccount refillAccount(User user, Double amount) {
        return userAccountService.refillAccount(user, amount);
    }

    @Override
    public Event getEventById(long eventId) {
        return eventService.getEventById(eventId);
    }

    @Override
    public Ticket getTicketById(long ticketId) {
        return ticketService.getTicketById(ticketId);
    }

    @Override
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Override
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        return eventService.getEventsByTitle(title, pageSize, pageNum);
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        return eventService.getEventsForDay(day, pageSize, pageNum);
    }

    @Override
    public Event createEvent(Event event) {
        return eventService.createEvent(event);
    }

    @Override
    public Event updateEvent(Event event) {
        return eventService.updateEvent(event);
    }

    @Override
    public boolean deleteEvent(long eventId) {
        return eventService.deleteEvent(eventId);
    }

    @Override
    public User getUserById(long userId) {
        return userService.getUserById(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        return userService.getUserByEmail(email);
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        return userService.getUsersByName(name, pageSize, pageNum);
    }

    @Override
    public User createUser(User user) {
        return userService.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        return userService.updateUser(user);
    }

    @Override
    public boolean deleteUser(long userId) {
        return userService.deleteUser(userId);
    }

    @Override
    public Ticket createTicket(Ticket ticket) {
        return ticketService.createTicket(ticket);
    }

    @Override
    public Ticket bookTicket(long userId, long eventId, int place, TicketCategory category) {
        return ticketService.bookTicket(userId, eventId, place, category);
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        return ticketService.getBookedTickets(user, pageSize, pageNum);
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        return ticketService.getBookedTickets(event, pageSize, pageNum);
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        return ticketService.cancelTicket(ticketId);
    }

    @Override
    @Transactional
    public void preloadTickets(InputStream inputStream) {
        try {
            xmlConverter.xmlToObj(inputStream);
        } catch (Exception e) {
            log.error("Error while preloading tickets from xml file", e);
        }
    }
}
