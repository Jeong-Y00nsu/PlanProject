package com.jRyun.demo.planProject.user.service;

import com.jRyun.demo.planProject.user.domain.User;
import com.jRyun.demo.planProject.user.mapper.UserMapper;
import com.jRyun.demo.planProject.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;

@Service
public class UserService {

    private UserMapper userMapper;

    static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    public Response signIn(User user){
        Response resp = new Response();
        try {
            //검증
            Response response = validationSignInParam(user);
            if (response.getResult() != ResultCode.OK) return response;
            User existUser = userMapper.selectUserById(user.getId());
            if (existUser == null) {
                return new Response(ResultCode.INCORRECT, "ID를 잘못 입력하셨습니다.");
            }
            if (user.getId().equals(existUser.getId()) && !user.getPw().equals(existUser.getPw())) {
                return new Response(ResultCode.ONLY_ID_CORRECT, "PW를 잘못 입력하셨습니다.");
            }
            resp =  new Response(ResultCode.OK, "OK");
            resp.setReturnValue(existUser);
            return resp;
        }catch (Exception e){
            logger.info("[UserService][signIn] fail signIn");
            return new Response(ResultCode.FAIL,"FAIL");
        }
    }

    private Response validationSignInParam(User user) throws Exception{
        String[] chars = {"/", "=", "!", "'"};
        try {
            //id
            if (user.getId() == null) {
                return new Response(ResultCode.INVALID_PARAM, "ID를 입력해주세요.");
            }
            if (user.getId().getBytes().length <= 0 || user.getId().getBytes().length > 100) {
                return new Response(ResultCode.INVALID_PARAM, "ID byte 길이는 0 초과, 100 이하여야 합니다.");
            }
            if (Validation.isContainChars(user.getId(), chars)) {
                return new Response(ResultCode.INVALID_PARAM, "ID에 특수 문자가 포함됩니다.");
            }
            //pw
            if (user.getPw() == null) {
                return new Response(ResultCode.INVALID_PARAM, "PW를 입력해주세요.");
            }
            user.setPw(encryptUserPw(user));
            if (user.getPw().getBytes().length <= 0 || user.getPw().getBytes().length > 100) {
                return new Response(ResultCode.INVALID_PARAM, "PW byte 길이는 0 초과, 100 이하여야 합니다.");
            }
            return new Response(ResultCode.OK, "OK");
        } catch (NoSuchAlgorithmException e){
            throw new Exception(e);
        }
    }
    public User getUserById(String id){
        return userMapper.selectUserById(id);
    }

    public Response signUp(User user){
        try {
            //salt 생성
            user.setSalt(MakeRandomStr.makeRandomPk(7));
            //검증
            Response response = validationSignUpParam(user);
            if (response.getResult() != ResultCode.OK) return response;
            //user 추가
            userMapper.insertUser(user);
            return new Response(ResultCode.OK, "OK");
        } catch (Exception e){
            logger.info("[UserService][regUser] fail insert");
            e.printStackTrace();
            return new Response(ResultCode.FAIL,"FAIL");
        }
    }

    private Response validationSignUpParam(User user) throws Exception{
        try {
            String[] chars = {"/", "=", "!", "'"};
            //id
            if (user.getId() == null) {
                return new Response(ResultCode.INVALID_PARAM, "ID를 입력해주세요.");
            }
            if (user.getId().getBytes().length <= 0 || user.getId().getBytes().length > 100) {
                return new Response(ResultCode.INVALID_PARAM, "ID byte길이는 0보다 크고 100 이하여야 합니다.");
            }
            if (Validation.isContainChars(user.getId(), chars)) {
                return new Response(ResultCode.INVALID_PARAM, "ID에 특수 문자가 포함됩니다.");
            }
            // ID 중복 체크
            if(userMapper.countDuplicateId(user.getId())>0){
                return new Response(ResultCode.DUPLICATE_ID, "이미 존재하는 ID입니다.");
            }

            //pw
            if (user.getPw() == null) {
                return new Response(ResultCode.INVALID_PARAM, "PW를 입력해주세요.");
            }
            user.setPw(encryptUserPw(user));
            if (user.getPw().getBytes().length <= 0 || user.getPw().getBytes().length > 100) {
                return new Response(ResultCode.INVALID_PARAM, "PW byte길이는 0보다 크고 100 이하여야 합니다.");
            }

            //name
            if (user.getName() == null) {
                return new Response(ResultCode.INVALID_PARAM, "이름을 입력해주세요.");
            }
            if (user.getName().getBytes().length <= 0 || user.getName().getBytes().length > 100) {
                return new Response(ResultCode.INVALID_PARAM, "이름은 0보다 크고, 100 이하여야 합니다.");
            }
            if (Validation.isContainChars(user.getName(), chars)) {
                return new Response(ResultCode.INVALID_PARAM, "이름에 특수 문자가 포함됩니다.");
            }
        } catch (NoSuchAlgorithmException e){
            throw new Exception(e);
        }

        return new Response(ResultCode.OK,"OK");
    }

    private String encryptUserPw(User user) throws NoSuchAlgorithmException {
        Cypher cypher = new Cypher();
        return cypher.encrypt(user.getPw() + user.getSalt());
    }

    public Response isDuplicatedId(String userId) {
        if(userId==null|| userId.isEmpty()) return new Response(ResultCode.FAIL, "ID값을 입력해주세요.");
        int cnt = userMapper.countDuplicateId(userId);
        if(cnt <= 0){
            return new Response(ResultCode.OK, "사용할 수 있는 ID입니다.");
        } else {
            return new Response(ResultCode.DUPLICATE_ID, "중복된 ID입니다. 다시 입력해주세요.");
        }
    }
}
