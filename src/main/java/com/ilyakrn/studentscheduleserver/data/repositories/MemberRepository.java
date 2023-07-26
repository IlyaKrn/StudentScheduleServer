package com.ilyakrn.studentscheduleserver.data.repositories;


import com.ilyakrn.studentscheduleserver.data.tablemodels.Group;
import com.ilyakrn.studentscheduleserver.data.tablemodels.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
