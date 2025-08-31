package org.example.ftree.service;

import org.example.ftree.model.dto.EncValueDto;

public interface EncValueService {

    /**
     * 加密
     *
     * @param value
     * @param expireSecond
     * @param salt
     * @return
     */
    String encrypt(String value, Integer expireSecond, String salt);

    /**
     * 解密
     *
     * @param value
     * @param salt
     * @return
     */
    String decrypt(String value, String salt);
}
