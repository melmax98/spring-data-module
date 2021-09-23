package org.example.util;

import com.itextpdf.text.DocumentException;
import org.example.model.Event;
import org.example.model.Ticket;
import org.example.model.TicketCategory;
import org.example.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class PdfCreatorTest {

    @Autowired
    private PdfCreator pdfCreator;

    @Test
    public void createPdfTest() throws DocumentException, IOException {
        List<Ticket> tickets = Collections.singletonList(new Ticket(new Event("", new Date(), 0.0), new User(), TicketCategory.STANDARD, 1));
        byte[] pdfForTickets = pdfCreator.createPdfForTickets(tickets);
        assertTrue(pdfForTickets.length > 0);
    }

}