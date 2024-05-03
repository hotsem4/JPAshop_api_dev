package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    @Rollback(false)
    @DisplayName("회원가입_테이스")
    public void 회원가입() throws Exception {
        // given
        Member member = new Member();
        member.setName("kim");
        // when
        Long join = memberService.join(member);  // id 반환
        // then
        assertThat(join).isEqualTo(member.getId());
        assertEquals(member, memberRepository.findOne(join));
    }
    // 왜 insert 문이 없이 select문이 실행되었을까?
    // 우리 sava 코드를 보면 em.persist(member)를 하고 있는데 여기서는 insert 쿼리가 날아가지 않고 commit한 시점에서 실제 Insert 쿼리가 날아가게 된다.
    @Test(expected = IllegalStateException.class)
    @DisplayName("중복_회원_예외")
    public void 중복_회원_예외() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");
        // when
        memberService.join(member1);
        memberService.join(member2);
        // then
        org.junit.jupiter.api.Assertions.fail("예외가 발생해야 한다.");


    }
}