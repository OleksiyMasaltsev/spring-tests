package mate.academy.security;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.Role;
import mate.academy.model.User;
import mate.academy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

class CustomUserDetailsServiceTest {
    private CustomUserDetailsService userDetailsService;
    private UserService userService;
    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userDetailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername_ok() {
        String email = "bob@i.ua";
        String password = "1234";
        Role role = new Role(Role.RoleName.USER);
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(Set.of(role));

        Mockito.when(userService.findByEmail(ArgumentMatchers.any()))
                .thenReturn(Optional.of(user));

        UserDetails actual = userDetailsService.loadUserByUsername(email);
        assertNotNull(actual);
        assertEquals(email, actual.getUsername());
        assertEquals("1234", actual.getPassword());
    }

    @Test
    void loadUserByUsername_notOk() {
        String email = "bob@i.ua";
        String password = "1234";
        Role role = new Role(Role.RoleName.USER);
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoles(Set.of(role));

        Mockito.when(userService.findByEmail(email))
                .thenReturn(Optional.of(user));

        try {
            userDetailsService.loadUserByUsername("alice@i.ua");
        } catch (UsernameNotFoundException e) {
            assertEquals("User not found.", e.getMessage());
            return;
        }
        fail("UsernameNotFoundException is expected");
    }
}
