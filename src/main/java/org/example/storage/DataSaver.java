package org.example.storage;

import lombok.Setter;
import org.example.model.Event;
import org.example.model.Ticket;
import org.example.model.User;
import org.example.storage.dao.TicketDao;
import org.example.storage.repository.EventRepository;
import org.example.storage.repository.UserRepository;

@Setter
public class DataSaver {

    private EventRepository eventRepository;
    private UserRepository userRepository;
    private TicketDao ticketDao;

    public void createTicketSaveUserAndEvent(Ticket ticket) {
        userRepository.save(ticket.getUser());
        eventRepository.save(ticket.getEvent());
        ticketDao.save(ticket);
    }

    public void createTicketIfUserAndEventExist(Ticket ticket) {
        ticketDao.save(ticket);
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public void createEvent(Event event) {
        eventRepository.save(event);
    }
}
