package com.mm.dev.dao.jpa;

import com.mm.dev.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Lipengfei on 2015/6/27.
 */
@Repository
public interface UserDao extends JpaRepository<User, Long> {

    Page<User> findAll(Pageable pageable);

}
