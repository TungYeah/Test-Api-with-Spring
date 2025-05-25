package vn.minhtung.decentralization.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import vn.minhtung.decentralization.IntegrationTest;
import vn.minhtung.decentralization.entity.User;
import vn.minhtung.decentralization.repository.UserRepository;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
@IntegrationTest
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @BeforeEach
    public  void init(){
        this.userRepository.deleteAll();
    }
    @Test
    public void createUser_shouldReturnCreated_whenValid() throws Exception {
        User inputUser = new User(null, "minhtung2108", "minhtung2108@gmail.com");
        String result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(inputUser))
                )
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        System.out.println("result :" + result);
        User outputUser = objectMapper.readValue(result, User.class);
        assertEquals(inputUser.getName(), outputUser.getName());
    }
    @Test
    public void getAllUser() throws Exception{
        User user1 = new User(null, "user1", "user1@gmail.com");
        User user2 = new User(null, "user2", "user2@gmail.com");
        List<User> data = List.of(user1, user2);
        this.userRepository.saveAll(data);
       String resultStr =  this.mockMvc.perform(get("/users")).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
       List<User> result = this.objectMapper.readValue(resultStr, new TypeReference<List<User>>() {});
       assertEquals(2, result.size());
       assertEquals("user1@gmail.com", result.get(0).getEmail());
    }
    @Test
    public void  getUserById() throws Exception{
        User user1 = new User(null, "user1", "user1@gmail.com");
        User inputUser = this.userRepository.saveAndFlush(user1);
        String result = this.mockMvc.perform(get("/users/{id}", inputUser.getId()))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        User outputUser = this.objectMapper.readValue(result, User.class);
        assertEquals("user1", outputUser.getName());
    }
    @Test
    public void updateById() throws Exception {
        User user1 = new User(null, "user1", "user1@gmail.com");
        User inputUser = this.userRepository.saveAndFlush(user1);
        User updateUser = new User(inputUser.getId(), "user2", "user2@gmail.com");
        String result = this.mockMvc.perform(
                        put("/users/{id}", inputUser.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(updateUser))
                )
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        User outputUser = this.objectMapper.readValue(result, User.class);
        assertEquals("user2", outputUser.getName());
    }
    @Test
    public void deleteUserById() throws Exception {
        User user = new User(null, "user", "user@gmail.com");
        User inputUser = this.userRepository.saveAndFlush(user);
        this.mockMvc.perform(
                        delete("/users/{id}", inputUser.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        long outputUser = this.userRepository.count();
        assertEquals(0, outputUser);
    }
}
