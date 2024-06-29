package com.example.desktime.repository;


import com.example.desktime.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    User findByUsernameAndEmail(String username, String email);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM user u WHERE u.email = :userMail and u.isEnable=true" )
    User findUserByEmail(String userMail);





    boolean existsByEmailAndUsername(String email, String username);

    boolean existsByIdAndRolesRoleName(Long userId, String admin);

    @Query(value = "Select * from user where isEnable = : true",nativeQuery = true)
    List<User> findByIsEnableTrue();

//    @Query(value = "SELECT u FROM user u WHERE u.id = :userId AND u.is_enable = true", nativeQuery = true)
//    User findEnabledUserById(@Param("userId") Long userId);

    @Query(value = "SELECT * FROM user WHERE id = :userId AND is_enable = true", nativeQuery = true)
    User findEnabledUserById(@Param("userId") Long userId);

    @Query("SELECT u.id, u.username, u.email, u.createdAt, r.roleName FROM user u JOIN u.roles r WHERE u.isEnable = true")
    Page<Object[]> findEnabledUsersWithRoles(Pageable pageable);

}
