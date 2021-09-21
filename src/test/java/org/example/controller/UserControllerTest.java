package org.example.controller;

import org.example.facade.BookingFacade;
import org.example.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @Autowired
    private BookingFacade bookingFacade;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void createUserViewTest() throws Exception {
        this.mockMvc.perform(get("/user/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("createUser"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    public void getUsersByNameTest() throws Exception {
        this.mockMvc.perform(get("/user/name/test"))
                .andExpect(status().isOk())
                .andExpect(view().name("userList"))
                .andReturn();
    }

    @Test
    public void getUsersByEmailTest() throws Exception {
        User user = bookingFacade.createUser(new User("test", "testmail"));
        this.mockMvc.perform(get("/user/email/testmail"))
                .andExpect(status().isOk())
                .andExpect(view().name("userList"))
                .andReturn();
        bookingFacade.deleteUser(user.getUserId());
    }

    @Test
    public void getUserEditForm() throws Exception {
        this.mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("userEdit"))
                .andReturn();
    }

    @Test
    public void createUser() throws Exception {
        this.mockMvc.perform(post("/user")
                        .param("name", "America")
                        .param("email", "america!@#$%%^"))
                .andExpect(status().isOk())
                .andExpect(view().name("userList"))
                .andReturn();

        User userByEmail = bookingFacade.getUserByEmail("america!@#$%%^");
        assertNotNull(userByEmail);
        assertTrue(bookingFacade.deleteUser(userByEmail.getUserId()));
    }

    @Test
    public void updateUser() throws Exception {
        User user = bookingFacade.createUser(new User("test", "testmail"));
        this.mockMvc.perform(post("/user/update/" + user.getUserId())
                        .param("name", "America")
                        .param("email", "america!@#$%%^"))
                .andExpect(status().isOk())
                .andExpect(view().name("userList"))
                .andReturn();

        User updatedUser = bookingFacade.getUserById(user.getUserId());
        assertEquals("America", updatedUser.getName());
        assertEquals("america!@#$%%^", updatedUser.getEmail());
        assertTrue(bookingFacade.deleteUser(updatedUser.getUserId()));
    }

    @Test
    public void deleteUser_userExists() throws Exception {
        User user = bookingFacade.createUser(new User("test", "testmail"));
        this.mockMvc.perform(post("/user/delete/" + user.getUserId()))
                .andExpect(status().isOk())
                .andExpect(content().string("User was successfully deleted"))
                .andReturn();

        assertNull(bookingFacade.getUserById(user.getUserId()));
    }

    @Test
    public void deleteUser_userDoesNotExist() throws Exception {
        this.mockMvc.perform(post("/user/delete/" + Integer.MAX_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string("User was not deleted"))
                .andReturn();

        assertNull(bookingFacade.getUserById(Integer.MAX_VALUE));
    }
}