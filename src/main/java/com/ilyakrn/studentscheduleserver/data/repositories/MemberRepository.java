package com.ilyakrn.studentscheduleserver.data.repositories;


import com.ilyakrn.studentscheduleserver.data.tablemodels.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<List<Member>> findMemberByGroupId(long groupId);
    Optional<Member> findByGroupIdAndUserId(long groupId, long userId);

    Optional<List<Member>> findMemberByUserId(long id);
}
