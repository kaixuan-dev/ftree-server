package org.example.ftree.service.impl;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.ftree.model.dto.EncValueDto;
import org.example.ftree.model.exception.BusinessException;
import org.example.ftree.service.EncValueService;
import org.example.ftree.utils.AESUtil;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class EncValueServiceImpl implements EncValueService {


    @Override
    public String encrypt(String value, Integer expireSecond, String salt) {
        Long expireTime = System.currentTimeMillis() + expireSecond * 1000;
        EncValueDto encValueDto = new EncValueDto();
        encValueDto.setTs(expireTime);
        encValueDto.setVal(value);
        return AESUtil.encrypt(JSON.toJSONString(encValueDto), salt);
    }

    @Override
    public String decrypt(String value, String salt) {
        String decrypt = AESUtil.decrypt(value, salt);
        if (StringUtils.isBlank(decrypt)) {
            throw new BusinessException("解密失败");
        }
        EncValueDto encValueDto = JSON.parseObject(decrypt, EncValueDto.class);
        Long ts = encValueDto.getTs();
        if (Objects.isNull(ts) || ts < System.currentTimeMillis()) {
            throw new BusinessException("解密失败，已过期");
        }
        return encValueDto.getVal();
    }
}
