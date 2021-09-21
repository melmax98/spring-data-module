package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.util.DateUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
@XmlType(propOrder = {"eventId", "title", "date", "ticketPrice"})
@Entity
public class Event implements Storable {

    public Event(String title, Date date) {
        this.title = title;
        this.date = date;
    }

    @Id
    @GeneratedValue
    private Long eventId;
    private String title;
    private Date date;
    private Double ticketPrice;

    @Override
    public String toString() {
        return eventId + " " + title + " " + DateUtils.simpleDateFormat.format(date);
    }
}
