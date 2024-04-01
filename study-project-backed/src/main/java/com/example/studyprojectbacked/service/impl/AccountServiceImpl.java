package com.example.studyprojectbacked.service.impl;

import com.example.studyprojectbacked.entity.dto.Account;
import com.example.studyprojectbacked.mapper.UserMapper;
import com.example.studyprojectbacked.service.AccountService;
import com.example.studyprojectbacked.util.Const;
import com.example.studyprojectbacked.util.FlowUtil;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    UserMapper userMapper;
    @Resource
    RabbitTemplate rabbitTemplate;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    FlowUtil flowUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username == null) throw new UsernameNotFoundException("用户名不能为空");
        Account account = userMapper.getAccountByUsernameOrEmail(username);
        if(account == null) throw new UsernameNotFoundException("用户名或密码错误");
        return User
                .withUsername(username)
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }
    @Override
    public String registerEmailVerifyCode(String type, String email, String ip) {
        synchronized (ip.intern()){
            if (!this.verifyLimit(ip))
                return "请求频繁，请稍后再试";
            Random random = new Random();
            int code = random.nextInt(899999) + 100000;
            Map<String, Object> data = Map.of("type", type, "email", email, "code",code);
            rabbitTemplate.convertAndSend("mail",data);
            stringRedisTemplate.opsForValue()
                    .set(Const.VERIFY_EMAIL_DATA + email, String.valueOf(code), 3, TimeUnit.MINUTES);
            return null;
        }

    }
    private boolean verifyLimit(String ip){
        String key = Const.VERIFY_EMAIL_LIMIT + ip;
        return flowUtil.limitOnceCheck(key,Const.BLOCK_TIME);
    }
}
