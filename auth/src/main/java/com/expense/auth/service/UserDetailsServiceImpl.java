package com.expense.auth.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.expense.auth.entities.UserInfo;
import com.expense.auth.eventProducer.UserInfoEvent;
import com.expense.auth.eventProducer.UserInfoProducer;
import com.expense.auth.model.UserInfoDto;
import com.expense.auth.repository.UserRepository;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Component
@AllArgsConstructor
@Data
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final UserInfoProducer userInfoProducer;

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.debug("Entering in loadUserByUsername Method...");
        UserInfo user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("Username not found: " + username);
            throw new UsernameNotFoundException("could not found user..!!");
        }
        log.info("User Authenticated Successfully..!!!");
        return new CustomUserDetails(user);
    }

    public UserInfo checkIfUserAlreadyExist(UserInfoDto userInfoDto) {
        return userRepository.findByUsername(userInfoDto.getUsername());
    }

    public String getUserByUsername(String userName) {
        return Optional.of(userRepository.findByUsername(userName)).map(UserInfo::getUserId).orElse(null);
    }

    public Boolean signupUser(UserInfoDto userInfoDto) throws InterruptedException, ExecutionException {
        // ValidationUtil.validateUserAttributes(userInfoDto);
        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
        if (Objects.nonNull(checkIfUserAlreadyExist(userInfoDto))) {
            return false;
        }
        String userId = UUID.randomUUID().toString();
        userRepository
                .save(new UserInfo(userId, userInfoDto.getUsername(), userInfoDto.getPassword(), new HashSet<>()));
        // pushEventToQueue

        userInfoProducer.sendEventToKafka(userInfoEventToPublish(userInfoDto, userId));

        return true;
    }

    private UserInfoEvent userInfoEventToPublish(UserInfoDto userInfoDto, String userId) {
        return UserInfoEvent.builder()
                .userId(userId)
                .firstName(userInfoDto.getUsername())
                .lastName(userInfoDto.getLastName())
                .email(userInfoDto.getEmail())
                .phoneNumber(userInfoDto.getPhoneNumber()).build();
    }
}