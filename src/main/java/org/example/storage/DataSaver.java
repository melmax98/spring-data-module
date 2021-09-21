package org.example.storage;

import lombok.Setter;
import org.example.model.Event;
import org.example.model.Ticket;
import org.example.model.User;
import org.example.storage.dao.TicketDao;
import org.example.storage.dao.UserDao;
import org.example.storage.repository.EventRepository;

@Setter
public class DataSaver {

    private EventRepository eventRepository;
    private UserDao userDao;
    private TicketDao ticketDao;

    public void createTicketSaveUserAndEvent(Ticket ticket) {
        userDao.save(ticket.getUser());
        eventRepository.save(ticket.getEvent());
        ticketDao.save(ticket);
    }

    public void createTicketIfUserAndEventExist(Ticket ticket) {
        ticketDao.save(ticket);
    }

    public void createUser(User user) {
        userDao.save(user);
    }

    public void createEvent(Event event) {
        eventRepository.save(event);
    }
}
