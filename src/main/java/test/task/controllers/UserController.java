package test.task.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import test.task.dto.AppUserDto;
import test.task.dto.CustomResponse;
import test.task.entity.AppUser;
import test.task.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user")
    public ResponseEntity<List<AppUser>> showUsersList(){
        List<AppUser> users = userService.findAll();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/user/{login}")
    public ResponseEntity<AppUserDto> getUser(@PathVariable String login) {
        AppUserDto user = userService.findByLogin(login);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("/user/{login}")
    public CustomResponse deleteUser(@PathVariable String login) {
        return userService.deleteByLogin(login);
    }

    @PostMapping("/user")
    public CustomResponse saveUsers(@RequestBody List<AppUserDto> users) {
        return userService.saveUsers(users);
    }

    @PostMapping("/user/{login}")
    public CustomResponse updateUser(@RequestBody AppUserDto user) {
        return userService.updateUser(user);
    }

}
