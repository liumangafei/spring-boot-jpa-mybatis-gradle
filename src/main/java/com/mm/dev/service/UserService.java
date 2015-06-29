package com.mm.dev.service;

import com.mm.dev.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Lipengfei on 2015/6/26.
 */
public interface UserService {

    User getUser(Long id);

    Page<User> getAll(Pageable pageable);

    List<User> getUserList();

    Page<User> getUserAll(Pageable pageable);

    void save();

    void saveUser();

}
