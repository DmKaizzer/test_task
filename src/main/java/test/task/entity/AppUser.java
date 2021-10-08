package test.task.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
@Entity
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "users_table")
public class AppUser {
    
    @Id
    @Column(name = "login")
    private String login;

    @Column(name = "name")
    private String name;
    
    @Column(name = "password")
    private String password;

    @ManyToMany(cascade = {CascadeType.REFRESH,CascadeType.PERSIST,CascadeType.DETACH,CascadeType.MERGE})
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonIgnore
    private List<Role> roles;
    
    public void addRoleToUser(Role role) {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        roles.add(role);
    }
    
    public AppUser(String login, String name, String password) {
        this.login = login;
        this.name = name;
        this.password = password;
    }
}
