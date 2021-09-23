package org.example.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.util.DateUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@XmlRootElement
@XmlType(propOrder = {"eventId", "title", "date", "ticketPrice"})
@Entity
public class Event {

    public Event(String title, Date date, double ticketPrice) {
        this.title = title;
        this.date = date;
        this.ticketPrice = ticketPrice;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;
    private String title;
    private Date date;
    private Double ticketPrice;

    @Override
    public String toString() {
        return eventId + " " + title + " " + DateUtils.getInstance().getSimpleDateFormat().format(date);
    }
}
