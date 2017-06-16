package com.db.hackaton.repository;

import com.db.hackaton.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the UserGroup entity.
 */
@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup,Long> {

    @Query("select user_group from UserGroup user_group where user_group.user.login = ?#{principal.username}")
    List<UserGroup> findByUserIsCurrentUser();

    @Query("select user_group from UserGroup user_group where user_group.user.id = :userId and user_group.group.id = :groupId")
    UserGroup findByUserIdAndGroupId(@Param("userId")Long userId, @Param("groupId")Long groupId);

}
