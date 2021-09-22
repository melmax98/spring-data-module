package org.example.controller;

import org.example.facade.BookingFacade;
import org.example.model.Event;
import org.example.model.Ticket;
import org.example.model.User;
import org.example.model.UserAccount;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.InputStream;
import java.util.Date;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class TicketControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private TicketController ticketController;

    @Autowired
    private BookingFacade bookingFacade;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(ticketController).build();
    }

    @Test
    public void getBookedTicketsByUser() throws Exception {
        this.mockMvc.perform(get("/ticket/user/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("ticketList"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void getBookedTicketsByEvent() throws Exception {
        this.mockMvc.perform(get("/ticket/event/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("ticketList"))
                .andReturn();
    }

    @Test
    public void getBookedTicketsById() throws Exception {
        this.mockMvc.perform(get("/ticket/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("ticketCancel"))
                .andReturn();
    }

    @Test
    public void bookTicket() throws Exception {
        User user = bookingFacade.createUser(new User("test", "testmail"));
        Event event = bookingFacade.createEvent(new Event("test", new Date(1), 1000));
        UserAccount userAccount = bookingFacade.refillAccount(user, 1001.0);

        this.mockMvc.perform(post("/ticket")
                        .param("userId", String.valueOf(user.getUserId()))
                        .param("eventId", String.valueOf(event.getEventId()))
                        .param("categoryName", "BAR")
                        .param("place", "3"))
                .andExpect(status().isOk())
                .andExpect(view().name("ticketList"))
                .andReturn();

        Ticket ticket = bookingFacade.getBookedTickets(user, 1, 1).iterator().next();

        assertEquals(1, bookingFacade.getBalanceByUser(user));
        assertTrue(bookingFacade.deleteUserAccount(userAccount.getUserAccountId()));
        assertNotNull(ticket);
        assertEquals(ticket.getUser(), user);
        assertTrue(bookingFacade.cancelTicket(ticket.getTicketId()));
        assertTrue(bookingFacade.deleteUser(user.getUserId()));
        assertTrue(bookingFacade.deleteEvent(event.getEventId()));
        assertNull(bookingFacade.getTicketById(ticket.getTicketId()));
    }

    @Test
    public void cancelTicket_ticketExists() throws Exception {
        Ticket ticket = bookingFacade.createTicket(new Ticket());
        this.mockMvc.perform(post("/ticket/cancel/" + ticket.getTicketId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Ticket was successfully canceled"))
                .andReturn();

        assertNull(bookingFacade.getTicketById(ticket.getTicketId()));
    }

    @Test
    public void cancelTicket_doesNotExist() throws Exception {
        this.mockMvc.perform(post("/ticket/cancel/" + Integer.MAX_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string("Ticket was not canceled"))
                .andReturn();

        assertNull(bookingFacade.getTicketById(Integer.MAX_VALUE));
    }

    @Test
    public void bookTicketForm() throws Exception {
        this.mockMvc.perform(get("/ticket/new"))
                .andExpect(view().name("bookTicket"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void preloadTickets() throws Exception {
        BookingFacade spy = spy(BookingFacade.class);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new TicketController(spy)).build();
        doNothing().when(spy).preloadTickets(any(InputStream.class));

        mockMvc.perform(multipart("/ticket/preload").file(new MockMultipartFile("file", new byte[]{})))
                .andExpect(status().isOk())
                .andExpect(content().string("Success"))
                .andReturn();
    }
}