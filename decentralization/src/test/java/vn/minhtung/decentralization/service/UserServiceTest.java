package vn.minhtung.decentralization.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.minhtung.decentralization.entity.User;
import vn.minhtung.decentralization.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    @Test
    public void createUser_shouldReturnUser_WhenEmailValid(){
        User inputUser  = new User(null, "minhtung", "minhtung2108@gmail.com");
        User oututUser  = new User(1L, "minhtung", "minhtung2108@gmail.com");
        when(this.userRepository.existsByEmail(inputUser.getEmail())).thenReturn(false);
        when(this.userRepository.save(any())).thenReturn(oututUser);
        User result = this.userService.createUser(inputUser);
        assertEquals(1L, result.getId());
    }
    @Test
    public void createUser_shouldThrowExeption_WhenEmailValid(){
        User inputUser  = new User(null, "minhtung", "minhtung2108@gmail.com");
        when(this.userRepository.existsByEmail(inputUser.getEmail())).thenReturn(true);
        Exception ex = assertThrows(IllegalArgumentException.class,() -> {
            this.userService.createUser(inputUser);
        });
        assertEquals("Email already exists", ex.getMessage());
    }
    @Test
    public void getAllUsers_shouldReturnAllUsers(){
        List<User> ouputUser = new ArrayList<>();
        ouputUser.add(new User(1L,"minhtung", "minhtung2108@gmail.com"));
        ouputUser.add(new User(1L,"minhtung218", "minhtung218@gmail.com"));
        when(this.userRepository.findAll()).thenReturn(ouputUser);
        List<User> result = this.userService.getAllUsers();
        assertEquals(2,result.size());
        assertEquals("minhtung2108@gmail.com",result.get(0).getEmail());
    }
    @Test
    public void getUserById_shouldReturnOptionalUSer(){
        Long inputId =1L;
        Optional<User> userOptional = Optional.of(new User(1L, "minhtung2108", "minhtung2108@gmail.com"));
        when(this.userRepository.findById(inputId)).thenReturn(userOptional);
        Optional<User> result = this.userService.getUserById(inputId);
        assertEquals(true, result.isPresent());
    }
    @Test
    public void deleteUser_shouldReturnExepion_WhenUserExists(){
    Long inputId = 1L;
    when(this.userRepository.existsById(inputId)).thenReturn(true);
    this.userService.deleteUser(inputId);
    verify(this.userRepository).deleteById(inputId);
    }
    @Test
    public void deleteUser_shouldReturnExepion_WhenUserNotExists(){
        Long inputId = 1L;
        when(this.userRepository.existsById(inputId)).thenReturn(false);
        Exception ex = assertThrows(NoSuchElementException.class, () ->{
            this.userService.deleteUser(inputId);
        });
        assertEquals("User not found", ex.getMessage());
    }
    @Test
    public void updateUser_shouldReturnUser_WhenUserValid(){
        Long inputId = 1L;
        User inputUser = new User(1L, "minhtung2108", "minhtung2108@gmail.com");
        User outputUser = new User(1L, "minhtung218", "minhtung218@gmail.com");
        when(this.userRepository.findById(inputId)).thenReturn(Optional.of(inputUser));
        when(this.userRepository.save(any())).thenReturn(outputUser);
        User result = this.userService.updateUser(inputId, inputUser);
        assertEquals("minhtung218", result.getName());
    }

}
