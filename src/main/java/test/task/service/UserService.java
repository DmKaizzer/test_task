package test.task.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.task.dto.AppUserDto;
import test.task.dto.CustomResponse;
import test.task.entity.AppUser;
import test.task.repositories.RoleRepository;
import test.task.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AppUserDto findByLogin(String login) {
        AppUser user = userRepository.findByLogin(login);
        log.info("Found user: {}", user);
        return new AppUserDto()
                .setLogin(login)
                .setName(user.getName())
                .setPassword(user.getPassword())
                .setRoles(user.getRoles());
    }

    public List<AppUser> findAll() {
        List<AppUser> list = userRepository.findAll();
        log.info("Found {} users: {}", list.size(), list);
        return list;
    }

    public CustomResponse deleteByLogin(String login) {
        try {
            userRepository.delete(userRepository.findByLogin(login));
        } catch (Exception e) {
            log.error("deleteByLogin() with login = {} error: {}", login, e);
            return new CustomResponse(false);
        }
        return new CustomResponse(true);
    }

    public CustomResponse saveUsers(List<AppUserDto> users) {
        for (AppUserDto appUserDto :users) {
            CustomResponse response = saveUser(appUserDto);
            if (!response.getSuccess()) {
                return response;
            }
        }
        return new CustomResponse(true);
    }

    public CustomResponse saveUser(AppUserDto appUserDto) {
        log.info("Try to save user: {}",  appUserDto);
        List<String> errors = validateAppUserDto(appUserDto);

        AppUser userForCheck = userRepository.findByLogin(appUserDto.getLogin());
        if (userForCheck != null) {
            errors.add("User with login = " + appUserDto.getLogin() + " already exists");
        }

        if (errors.size() > 0) {
            log.error("Found {} errors: {}", errors.size(), errors);
            return new CustomResponse(false, errors);
        }

        AppUser appUser = new AppUser()
                .setLogin(appUserDto.getLogin())
                .setName(appUserDto.getName())
                .setPassword(appUserDto.getPassword())
                .setRoles(appUserDto.getRoles());

        return saveOrUpdateCheckedUser(appUser);
    }

    @Transactional
    public CustomResponse updateUser(AppUserDto appUserDto) {
        List<String> errors = validateAppUserDto(appUserDto);
        if (errors.size() > 0) {
            return new CustomResponse(false, errors);
        }

        AppUser appUser = userRepository.findByLogin(appUserDto.getLogin());
        if (appUser == null) {
            errors.add("User is not found");
            return new CustomResponse(false, errors);
        }

        roleRepository.deleteAll(appUser.getRoles());

        appUser.setLogin(appUserDto.getLogin())
                .setPassword(appUserDto.getPassword())
                .setName(appUserDto.getName())
                .setRoles(appUserDto.getRoles());
        return saveOrUpdateCheckedUser(appUser);
    }

    private CustomResponse saveOrUpdateCheckedUser(AppUser appUser) {
        try {
            userRepository.save(appUser);
        } catch (Exception e) {
            log.error("userRepository.save(AppUser()) error: ", e);
            List<String> list = new ArrayList<>();
            list.add(e.getMessage());
            return new CustomResponse(false, list);
        }
        log.info("Success save user: {}", appUser);
        return new CustomResponse(true);
    }

    private List<String> validateAppUserDto(AppUserDto appUserDto) {
        List<String> errors = new ArrayList<>();
        if (appUserDto.getLogin() == null || appUserDto.getLogin().equals("")) {
            errors.add("login cannot be empty");
        }
        if (appUserDto.getName() == null || appUserDto.getName().equals("")) {
            errors.add("Name cannot be empty");
        }
        Pattern p = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).*$");
        Matcher m = p.matcher(appUserDto.getPassword());
        if (!m.find() || appUserDto.getPassword() == null) {
            errors.add("Password must contain at least 1 number and 1 capital letter");
        }
        return errors;
    }

}
