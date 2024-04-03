package com.example.studyprojectbacked.service.impl;

import com.example.studyprojectbacked.entity.dto.Account;
import com.example.studyprojectbacked.entity.vo.request.EmailRegisterVO;
import com.example.studyprojectbacked.entity.vo.request.EmailResetVO;
import com.example.studyprojectbacked.entity.vo.request.ResetConfirmVO;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    @Resource
    PasswordEncoder passwordEncoder;
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
    @Override
    public String registerEmailAccount(EmailRegisterVO vo) {
        String email = vo.getEmail();
        String username = vo.getUsername();
        String code = stringRedisTemplate.opsForValue().get(this.getRedisCode(email));
        if (code == null) return "请先获取验证码";
        if (!code.equals(vo.getCode())) return "验证码输入错误，请重新输入";
        if (existAccountByEmail(email)) return "此电子邮件已被其他用户注册";
        if (existAccountByUsername(username)) return "此用户名已被其他人注册，请更新一个新的用户名";
        String password = passwordEncoder.encode(vo.getPassword());
        Account account = new Account(null,email,username,password,"USER",new Date());
        if (userMapper.saveAccount(account)) {
            stringRedisTemplate.delete(this.getRedisCode(email));
            return null;
        } else {
            return "内部错误，请联系管理员";
        }
    }

    @Override
    public String resetConfirm(ResetConfirmVO vo) {
        String email = vo.getEmail();
        String code = stringRedisTemplate.opsForValue().get(this.getRedisCode(email));
        if (code == null) return "请先获取验证码";
        if (!code.equals(vo.getCode())) return "验证码输入错误，请重新输入";
        return null;
    }

    @Override
    public String resetEmailAccountPassword(EmailResetVO vo) {
        String email = vo.getEmail();
        String verify = this.resetConfirm(new ResetConfirmVO(vo.getEmail(),vo.getCode()));
        if (verify != null) return verify;
        String password = passwordEncoder.encode(vo.getPassword());
        boolean update =  userMapper.updateAccountByEmail(password,email);
        if (update){
            stringRedisTemplate.delete(this.getRedisCode(email));
        }
        return null;
    }

    private String getRedisCode(String email){
        return Const.VERIFY_EMAIL_DATA + email;
    }

    private boolean verifyLimit(String ip){
        String key = Const.VERIFY_EMAIL_LIMIT + ip;
        return flowUtil.limitOnceCheck(key,Const.BLOCK_TIME);
    }

    private boolean existAccountByEmail(String email){
        return userMapper.getAccountByUsernameOrEmail(email) != null;
    }

    private boolean existAccountByUsername(String username){
        return userMapper.getAccountByUsernameOrEmail(username) != null;
    }
}
