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
import org.example.storage.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

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
    private TicketRepository ticketRepository;

    @Override
    public Ticket createTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    @Transactional
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
        return createTicket(new Ticket(event, user, category, place));
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        Page<Ticket> userPage = ticketRepository.findByUser(user, PageRequest.of(pageNum - 1, pageSize));
        return userPage.getContent();
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        Page<Ticket> userPage = ticketRepository.findByEvent(event, PageRequest.of(pageNum - 1, pageSize));
        return userPage.getContent();
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        try {
            ticketRepository.deleteById(ticketId);
            return true;
        } catch (Exception e) {
            log.error("Was not able to delete ticket with id {}", ticketId, e);
            return false;
        }
    }

    @Override
    public Ticket getTicketById(long ticketId) {
        return ticketRepository.findById(ticketId).orElse(null);
    }
}
