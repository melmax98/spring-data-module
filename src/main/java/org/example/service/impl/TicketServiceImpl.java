package org.example.service.impl;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.model.Event;
import org.example.model.Ticket;
import org.example.model.TicketCategory;
import org.example.model.User;
import org.example.service.EventService;
import org.example.service.TicketService;
import org.example.service.UserAccountService;
import org.example.service.UserService;
import org.example.storage.dao.TicketDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class TicketServiceImpl implements TicketService {

    @Setter
    private UserService userService;
    @Setter
    private EventService eventService;
    @Setter
    private UserAccountService userAccountService;

    @Autowired
    private TicketDao ticketDao;

    @Override
    public Ticket createTicket(Ticket ticket) {
        return ticketDao.save(ticket);
    }

    @Override
    public Ticket bookTicket(long userId, long eventId, int place, TicketCategory category) {
        User user = userService.getUserById(userId);
        Event event = eventService.getEventById(eventId);

        if (user == null || event == null) {
            log.warn("Cannot book ticket because user or event does not exist");
            return null;
        }
        Double userBalance = userAccountService.getBalanceByUser(user);
        Double ticketPrice = event.getTicketPrice();
        if (userBalance < ticketPrice) {
            log.info("Not enough money. Money needed: " + ticketPrice + ", balance: " + userBalance);
            return null;
        }

        userAccountService.withdrawMoneyFromAccount(user, ticketPrice);
        return ticketDao.bookTicket(user, event, place, category);
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        return ticketDao.getBookedTickets(user, pageSize, pageNum);
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        return ticketDao.getBookedTickets(event, pageSize, pageNum);
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        return ticketDao.delete(ticketId);
    }

    @Override
    public Ticket getTicketById(long ticketId) {
        return ticketDao.getTicketById(ticketId);
    }
}
