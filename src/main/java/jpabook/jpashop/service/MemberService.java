package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)    // 지금 조회용 함수가 많다보니 기본적으로 readOnly를 true로 한다.
@RequiredArgsConstructor  // final이 있는 클래스 변수만 생성자 생성 후 injection 수행
public class MemberService {

    private final MemberRepository memberRepository;   // 생성자 injection을 하게 되면 변경할 일이 없기 때문에 final을 권장함

//    @Autowired
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    /**
     *회원가입
     */
    @Transactional   // default가 false기 때문에 이렇게 주면 변경 가능
    public Long join(Member member) {
        validateDuplicateMember(member);   // 중복회원 로직
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // Exception
        List<Member> findMembers = memberRepository.findByName(member.getName());
        // memberRepository에서 member 객체와 같은 이름을 찾는다.
        if(!findMembers.isEmpty()) { // 만약 findMembers가 비어있지 않다면
            throw new IllegalStateException("이미 존재하는 회원입니다."); // 예외 발생
        }
    }
    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findALL();
    }

    private Member findMember(Long id) {
        return memberRepository.findOne(id);
    }
}
