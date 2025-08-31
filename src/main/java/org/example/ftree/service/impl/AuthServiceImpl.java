package org.example.ftree.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.pig4cloud.captcha.ArithmeticCaptcha;
import com.pig4cloud.captcha.SpecCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.ftree.entity.User;
import org.example.ftree.mapper.UserMapper;
import org.example.ftree.model.dto.auth.LoginDto;
import org.example.ftree.model.enums.UserStatus;
import org.example.ftree.model.enums.UserType;
import org.example.ftree.model.exception.BusinessException;
import org.example.ftree.model.vo.auth.CaptchaVo;
import org.example.ftree.model.vo.auth.LoginVo;
import org.example.ftree.service.AuthService;
import org.example.ftree.service.EncValueService;
import org.example.ftree.utils.AESUtil;
import org.example.ftree.utils.wrapper.struct.QueryWrapper;
import org.example.ftree.utils.wrapper.struct.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EncValueService encValueService;

    @Value("${project.login.img-salt}")
    private String loginImgSalt;

    @Value("${project.login.token-salt}")
    private String loginTokenSalt;

    @Override
    public LoginVo userLogin(LoginDto dto) {
        //校验验证码
        String imgCode = dto.getImgCode();
        String verCode = encValueService.decrypt(imgCode, loginImgSalt);
        if (!dto.getCode().equals(verCode)) {
            throw new BusinessException("验证码错误，请重试");
        }
        //验证用户密码
        QueryWrapper<User> queryWrapper = Wrappers.lambdaQuery(User.class)
                .select(s -> s.column(User::getUserId))
                .where(w -> w.createCriteria()
                        .andEqualTo(User::getUserName, StringUtils.trim(dto.getUsername()))
                        .andEqualTo(User::getPassword, StringUtils.trim(dto.getPassword()))
                        .andEqualTo(User::getStatus, UserStatus.ENABLE.getVal())
                        .andEqualTo(User::getUserType, UserType.PC.getVal())
                );
        User user = userMapper.selectOne(queryWrapper);
        if (Objects.isNull(user) || Objects.isNull(user.getUserId())) {
            throw new BusinessException("用户不存在或密码错误");
        }
        LoginVo loginVo = new LoginVo();
        //Token 24小时过期
        Integer tokenExpireSecond = 24 * 60 * 60;
        loginVo.setToken(encValueService.encrypt(user.getUserId(), tokenExpireSecond, loginTokenSalt));
        return loginVo;
    }

    @Override
    public CaptchaVo captcha() {
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48, 2);
        String verCode = captcha.text().toLowerCase();
        //过期时间是三分钟
        String imgCode = encValueService.encrypt(verCode, 180, loginImgSalt);
        String imgSource = captcha.toBase64();

        CaptchaVo captchaVo = new CaptchaVo();
        captchaVo.setImgCode(imgCode);
        captchaVo.setImgSource(imgSource);

        return captchaVo;
    }

}
