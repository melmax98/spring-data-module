package org.example.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
@XmlType(propOrder = {"ticketId", "event", "user", "category", "placeNumber"})
@Entity
public class Ticket {

    public Ticket(Event event, User user, TicketCategory category, int placeNumber) {
        this.event = event;
        this.user = user;
        this.category = category;
        this.placeNumber = placeNumber;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    @OneToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private TicketCategory category;

    private int placeNumber;

    @XmlElement(name = "ticketId")
    public Long getTicketId() {
        return ticketId;
    }

    @XmlElement(name = "event")
    public Event getEvent() {
        return event;
    }

    @XmlElement(name = "user")
    public User getUser() {
        return user;
    }

    public TicketCategory getCategory() {
        return category;
    }

    public int getPlaceNumber() {
        return placeNumber;
    }

    @Override
    public String toString() {
        return "Ticket = " + ticketId +
                ", event = " + event +
                ", user = " + user +
                ", category = " + category +
                ", place = " + placeNumber;
    }
}
