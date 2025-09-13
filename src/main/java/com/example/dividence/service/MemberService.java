package com.example.dividence.service;

import com.example.dividence.model.Auth;
import com.example.dividence.model.MemberEntity;
import com.example.dividence.persist.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return this.memberRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("couldn't find user -> " + username));
  }

  public MemberEntity register(Auth.SignUp member){
    boolean exists = this.memberRepository.existsByUsername(member.getUsername());
    if(exists){
      throw new RuntimeException("이미 사용 중인 아이디입니다");
    }

    member.setPassword(this.passwordEncoder.encode(member.getPassword()));
    var result = this.memberRepository.save(member.toEntity());
    return result;
  }

  public MemberEntity authenticate(){
    return null;
  }
}
