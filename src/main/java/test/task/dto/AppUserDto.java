package test.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import test.task.entity.Role;

import java.util.List;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@AllArgsConstructor
public class AppUserDto {

    private String login;

    private String name;

    private String password;
    
    private List<Role> roles;

}