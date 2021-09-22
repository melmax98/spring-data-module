package org.example.facade;

import org.example.model.Event;
import org.example.model.Ticket;
import org.example.model.TicketCategory;
import org.example.model.User;
import org.example.model.UserAccount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BookingFacadeImplTest {

    @Autowired
    private BookingFacade bookingFacade;

    @Test
    public void bookAndCancelTicket() {
        Event event = bookingFacade.createEvent(new Event("title", new Date(), 490.0));
        User user = bookingFacade.createUser(new User("josh", "josh_mail"));
        UserAccount userAccount = bookingFacade.refillAccount(user, 495.0);
        Ticket ticket = bookingFacade.bookTicket(user.getUserId(), event.getEventId(), 1, TicketCategory.PREMIUM);
        List<Ticket> bookedTicketsByUser = bookingFacade.getBookedTickets(user, 1, 1);
        List<Ticket> bookedTicketsByEvent = bookingFacade.getBookedTickets(event, 1, 1);

        assertNotNull(bookingFacade.getUserById(user.getUserId()));
        assertNotNull(bookingFacade.getEventById(event.getEventId()));
        assertEquals(1, bookedTicketsByUser.size());
        assertEquals(1, bookedTicketsByEvent.size());
        assertEquals(5, bookingFacade.getBalanceByUser(user));

        assertTrue(bookingFacade.cancelTicket(ticket.getTicketId()));
        assertTrue(bookingFacade.getBookedTickets(user, 1, 1).isEmpty());
        assertTrue(bookingFacade.getBookedTickets(event, 1, 1).isEmpty());

        assertTrue(bookingFacade.deleteEvent(event.getEventId()));
        assertTrue(bookingFacade.deleteUserAccount(userAccount.getUserAccountId()));
        assertTrue(bookingFacade.deleteUser(user.getUserId()));

        assertNull(bookingFacade.getUserById(user.getUserId()));
        assertNull(bookingFacade.getEventById(event.getEventId()));
    }

    @Test
    public void updateUserTest() {
        User user = bookingFacade.createUser(new User("john", "john_mail"));
        User foundUser = bookingFacade.getUserById(user.getUserId());
        assertEquals(user.getName(), foundUser.getName());
        assertEquals(user.getEmail(), foundUser.getEmail());

        user.setName("Drake");
        user.setEmail("drake_mail");

        bookingFacade.updateUser(user);
        User updatedUser = bookingFacade.getUserById(user.getUserId());
        assertEquals("Drake", updatedUser.getName());
        assertEquals("drake_mail", updatedUser.getEmail());

        assertTrue(bookingFacade.deleteUser(updatedUser.getUserId()));
        assertNull(bookingFacade.getUserById(updatedUser.getUserId()));
    }

    @Test
    public void updateEventTest() {
        Event event = bookingFacade.createEvent(new Event("New", new Date(), 0));
        Event foundEvent = bookingFacade.getEventById(event.getEventId());
        assertEquals(event.getTitle(), foundEvent.getTitle());
        assertEquals(event.getDate(), foundEvent.getDate());

        event.setTitle("event");
        event.setDate(new Date(1));

        bookingFacade.updateEvent(event);
        Event updatedEvent = bookingFacade.getEventById(event.getEventId());
        assertEquals("event", updatedEvent.getTitle());
        assertEquals(new Date(1), updatedEvent.getDate());

        assertTrue(bookingFacade.deleteEvent(updatedEvent.getEventId()));
        assertNull(bookingFacade.getEventById(updatedEvent.getEventId()));
    }

    @Test
    public void getUserByEmail() {
        User user = bookingFacade.createUser(new User("test", "test_mail"));
        User foundUser = bookingFacade.getUserByEmail(user.getEmail());

        assertEquals("test", foundUser.getName());
        assertEquals("test_mail", foundUser.getEmail());
        assertTrue(bookingFacade.deleteUser(user.getUserId()));
        assertNull(bookingFacade.getUserById(user.getUserId()));
    }

    @Test
    public void getUsersByName() {
        List<User> testUsers = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            testUsers.add(bookingFacade.createUser(new User("testName", "testName_mail")));
        }

        List<User> foundUsersFirstPage = bookingFacade.getUsersByName("testName", 4, 1);
        List<User> foundUsersSecondPage = bookingFacade.getUsersByName("testName", 4, 2);

        assertEquals(4, foundUsersFirstPage.size());
        assertEquals(1, foundUsersSecondPage.size());

        for (User user : testUsers) {
            assertTrue(bookingFacade.deleteUser(user.getUserId()));
            assertNull(bookingFacade.getUserById(user.getUserId()));
        }

        assertTrue(bookingFacade.getUsersByName("testName", 100, 1).isEmpty());
    }

    @Test
    public void getEventsByTitle() {
        List<Event> testEvents = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            testEvents.add(bookingFacade.createEvent(new Event("testTitle", new Date(1), 0)));
        }

        List<Event> foundEventsFirstPage = bookingFacade.getEventsByTitle("testTitle", 4, 1);
        List<Event> foundEventsSecondPage = bookingFacade.getEventsByTitle("testTitle", 4, 2);

        assertEquals(4, foundEventsFirstPage.size());
        assertEquals(1, foundEventsSecondPage.size());

        for (Event event : testEvents) {
            assertTrue(bookingFacade.deleteEvent(event.getEventId()));
            assertNull(bookingFacade.getEventById(event.getEventId()));
        }

        assertTrue(bookingFacade.getEventsByTitle("testTitle", 100, 1).isEmpty());
    }

    @Test
    public void getEventsForDay() {
        List<Event> testEvents = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            testEvents.add(bookingFacade.createEvent(new Event("testTitle", new Date(1), 0)));
        }

        List<Event> foundEventsFirstPage = bookingFacade.getEventsForDay(new Date(1), 4, 1);
        List<Event> foundEventsSecondPage = bookingFacade.getEventsForDay(new Date(1), 4, 2);

        assertEquals(4, foundEventsFirstPage.size());
        assertEquals(1, foundEventsSecondPage.size());

        for (Event event : testEvents) {
            assertTrue(bookingFacade.deleteEvent(event.getEventId()));
            assertNull(bookingFacade.getEventById(event.getEventId()));
        }

        assertTrue(bookingFacade.getEventsForDay(new Date(1), 100, 1).isEmpty());
    }
}