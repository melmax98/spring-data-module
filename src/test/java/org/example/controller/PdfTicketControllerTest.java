package org.example.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class PdfTicketControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private PdfTicketController pdfTicketController;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(pdfTicketController).build();
    }


    @Test
    public void getBookedTicketsByUser() throws Exception {
        this.mockMvc.perform(get("/ticket/user/1")
                        .header("accept", "application/pdf"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andReturn();
    }

    @Test
    public void getBookedTicketsByEvent() throws Exception {
        this.mockMvc.perform(get("/ticket/event/1")
                        .header("accept", "application/pdf"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andReturn();
    }
}