package com.redditclone.backend.service;

import com.redditclone.backend.model.User;
import com.redditclone.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                                     .orElseThrow(()-> new UsernameNotFoundException("User with this email "+email));

        return UserPrincipal.createUser(user);
    }

    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id)
                                            .orElseThrow(()-> new RuntimeException("User avec l'id "+id+" n'est pas trouvé"));

        return UserPrincipal.createUser(user);
    }


}//
