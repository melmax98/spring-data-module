package org.example.storage.dao;

import org.example.model.Event;
import org.example.model.Storable;
import org.example.model.Ticket;
import org.example.model.TicketCategory;
import org.example.model.User;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketDaoTest {
    @Mock
    private NoData noData;

    @InjectMocks
    private TicketDao ticketDao;

    @Test
    void bookTicket() {
        when(noData.getStorage()).thenReturn(new HashMap<>());

        User user = new User(1L, "name", "email");
        Event event = new Event(1L, "title", new Date(), 500.0);

        Ticket ticket = ticketDao.bookTicket(user, event, 1, TicketCategory.PREMIUM);

        assertEquals(user, ticket.getUser());
        assertEquals(event, ticket.getEvent());
        assertEquals(1, ticket.getPlaceNumber());
        assertEquals(TicketCategory.PREMIUM, ticket.getCategory());
        assertEquals(1, ticket.getTicketId());
    }

    @Test
    void update() {
        Ticket ticket = new Ticket();
        assertThrows(UnsupportedOperationException.class, () -> ticketDao.update(ticket));
    }

    @Test
    void delete_ticketExists() {
        HashMap<String, Storable> storage = new HashMap<>();
        Ticket ticket = new Ticket(1L, new Event(), new User(), TicketCategory.PREMIUM, 1);
        storage.put("ticket:1", ticket);

        when(noData.getStorage()).thenReturn(storage);

        boolean ticketDeleted = ticketDao.delete(1);

        assertTrue(ticketDeleted);
    }

    @Test
    void delete_ticketDoesNotExist() {
        HashMap<String, Storable> storage = new HashMap<>();
        when(noData.getStorage()).thenReturn(storage);

        boolean ticketDeleted = ticketDao.delete(1);

        assertFalse(ticketDeleted);
    }

    @Test
    void getBookedTickets_byUser() {
        HashMap<String, Storable> storage = new HashMap<>();
        User user = new User();
        for (int i = 1; i < 10; i++) {
            Ticket ticket = new Ticket(1L, new Event(), user, TicketCategory.PREMIUM, 1);
            storage.put("ticket:" + i, ticket);
        }

        when(noData.getStorage()).thenReturn(storage);

        List<Ticket> ticketsFound = ticketDao.getBookedTickets(user, 5, 2);

        assertEquals(4, ticketsFound.size());
    }

    @Test
    void getBookedTickets_byEvent() {
        HashMap<String, Storable> storage = new HashMap<>();
        Event event = new Event();
        for (int i = 1; i < 10; i++) {

            Ticket ticket = new Ticket(1L, event, new User(), TicketCategory.PREMIUM, 1);
            storage.put("ticket:" + i, ticket);
        }

        when(noData.getStorage()).thenReturn(storage);

        List<Ticket> ticketsFound = ticketDao.getBookedTickets(event, 5, 2);

        assertEquals(4, ticketsFound.size());
    }
}