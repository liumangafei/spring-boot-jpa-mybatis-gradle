package com.mm.dev.service.impl;

import com.mm.dev.dao.jpa.UserDao;
import com.mm.dev.dao.mapper.UserMapper;
import com.mm.dev.entity.User;
import com.mm.dev.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Lipengfei on 2015/6/26.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUser(Long id) {
        return userDao.getOne(id);
    }

    @Override
    public Page<User> getAll(Pageable pageable){
        return userDao.findAll(pageable);
    }

    @Override
    public List<User> getUserList() {
        return userMapper.findAll();
    }

    @Override
    public Page<User> getUserAll(Pageable pageable) {
        return userMapper.getUserAll(pageable);
    }

    @Transactional
    @Override
    public void save() {

        User user = new User();
        user.setUsername("7");
        user.setPassword("7");

        userDao.save(user);

//        throw new RuntimeException("保存异常！");
    }

    @Transactional
    @Override
    public void saveUser() {

        User user = new User();
        user.setUsername("8");
        user.setPassword("8");

        userMapper.save(user);

        throw new RuntimeException("保存异常！");
    }

}
