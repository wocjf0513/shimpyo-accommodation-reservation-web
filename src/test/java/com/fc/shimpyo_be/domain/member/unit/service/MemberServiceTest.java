package com.fc.shimpyo_be.domain.member.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.fc.shimpyo_be.domain.member.dto.request.CheckPasswordRequestDto;
import com.fc.shimpyo_be.domain.member.dto.request.UpdateMemberRequestDto;
import com.fc.shimpyo_be.domain.member.dto.response.MemberResponseDto;
import com.fc.shimpyo_be.domain.member.entity.Authority;
import com.fc.shimpyo_be.domain.member.entity.Member;
import com.fc.shimpyo_be.domain.member.exception.InvalidPasswordException;
import com.fc.shimpyo_be.domain.member.exception.MemberNotFoundException;
import com.fc.shimpyo_be.domain.member.exception.UnmatchedPasswordException;
import com.fc.shimpyo_be.domain.member.exception.WrongPasswordException;
import com.fc.shimpyo_be.domain.member.repository.MemberRepository;
import com.fc.shimpyo_be.domain.member.service.MemberService;
import com.fc.shimpyo_be.global.util.SecurityUtil;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private SecurityUtil securityUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Nested
    @DisplayName("getMember()는")
    class Context_getMember {

        @Test
        @DisplayName("회원 정보를 조회할 수 있다.")
        void _willSuccess() {
            // given
            Member member = Member.builder()
                .id(1L)
                .email("test@mail.com")
                .name("test")
                .password("$10$ygrAExVYmFTkZn2d0.Pk3Ot5CNZwIBjZH5f.WW0AnUq4w4PtBi9Nm")
                .photoUrl("https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI")
                .authority(Authority.ROLE_USER)
                .build();

            given(memberRepository.findById(any(Long.TYPE))).willReturn(Optional.of(member));
            given(securityUtil.getCurrentMemberId()).willReturn(1L);

            // when
            MemberResponseDto result = memberService.getMember();

            assertNotNull(result);
            assertThat(result).extracting("memberId", "email", "name", "photoUrl")
                .containsExactly(1L, "test@mail.com", "test",
                    "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI");

            verify(memberRepository, times(1)).findById(any(Long.TYPE));
        }
    }

    @Nested
    @DisplayName("updateMember()는")
    class Context_updateMember {

        @Test
        @DisplayName("회원 프로필 사진을 수정할 수 있다.")
        void photoUrl_willSuccess() {
            // given
            UpdateMemberRequestDto updateMemberRequestDto = UpdateMemberRequestDto.builder()
                .photoUrl(
                    "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI")
                .build();
            Member member = Member.builder()
                .id(1L)
                .email("test@mail.com")
                .name("test")
                .password("$10$ygrAExVYmFTkZn2d0.Pk3Ot5CNZwIBjZH5f.WW0AnUq4w4PtBi9Nm")
                .photoUrl("")
                .authority(Authority.ROLE_USER)
                .build();

            given(memberRepository.findById(any(Long.TYPE))).willReturn(Optional.of(member));
            given(securityUtil.getCurrentMemberId()).willReturn(1L);

            // when
            MemberResponseDto result = memberService.updateMember(updateMemberRequestDto);

            assertNotNull(result);
            assertThat(result).extracting("memberId", "email", "name", "photoUrl")
                .containsExactly(1L, "test@mail.com", "test",
                    "https://fastly.picsum.photos/id/866/200/300.jpg?hmac=rcadCENKh4rD6MAp6V_ma-AyWv641M4iiOpe1RyFHeI");

            verify(memberRepository, times(1)).findById(any(Long.TYPE));
        }

        @Test
        @DisplayName("비밀번호를 수정할 수 있다.")
        void password_willSuccess() {
            // given
            UpdateMemberRequestDto updateMemberRequestDto = UpdateMemberRequestDto.builder()
                .passwordConfirm("qwer1234!!")
                .build();
            Member member = Member.builder()
                .id(1L)
                .email("test@mail.com")
                .name("test")
                .password("$10$ygrAExVYmFTkZn2d0.Pk3Ot5CNZwIBjZH5f.WW0AnUq4w4PtBi9Nm")
                .photoUrl("")
                .authority(Authority.ROLE_USER)
                .build();

            given(memberRepository.findById(any(Long.TYPE))).willReturn(Optional.of(member));
            given(securityUtil.getCurrentMemberId()).willReturn(1L);

            // when
            MemberResponseDto result = memberService.updateMember(updateMemberRequestDto);

            assertNotNull(result);
            assertThat(result).extracting("memberId", "email", "name", "photoUrl")
                .containsExactly(1L, "test@mail.com", "test", "");

            verify(memberRepository, times(1)).findById(any(Long.TYPE));
        }

        @Test
        @DisplayName("비밀번호가 양식에 맞지 않으면, 비밀번호를 수정할 수 없다.")
        void InvalidPassword_willFail() {
            // given
            UpdateMemberRequestDto updateMemberRequestDto = UpdateMemberRequestDto.builder()
                .password("qwer1234")
                .passwordConfirm("qwer1234")
                .build();
            Member member = Member.builder()
                .id(1L)
                .email("test@mail.com")
                .name("test")
                .password("$10$ygrAExVYmFTkZn2d0.Pk3Ot5CNZwIBjZH5f.WW0AnUq4w4PtBi9Nm")
                .photoUrl("")
                .authority(Authority.ROLE_USER)
                .build();

            given(memberRepository.findById(any(Long.TYPE))).willReturn(Optional.of(member));
            given(securityUtil.getCurrentMemberId()).willReturn(1L);

            // when
            Throwable exception = assertThrows(InvalidPasswordException.class, () -> {
                memberService.updateMember(updateMemberRequestDto);
            });

            // then
            assertEquals("비밀번호 양식에 맞지 않습니다.", exception.getMessage());

            verify(memberRepository, times(1)).findById(any(Long.TYPE));
        }

        @Test
        @DisplayName("비밀번호와 비밀번호 확인이 일치하지 않으면, 비밀번호를 수정할 수 없다.")
        void UnmatchedPassword_willFail() {
            // given
            UpdateMemberRequestDto updateMemberRequestDto = UpdateMemberRequestDto.builder()
                .password("qwer1234$$")
                .passwordConfirm("qwer1234##")
                .build();
            Member member = Member.builder()
                .id(1L)
                .email("test@mail.com")
                .name("test")
                .password("$10$ygrAExVYmFTkZn2d0.Pk3Ot5CNZwIBjZH5f.WW0AnUq4w4PtBi9Nm")
                .photoUrl("")
                .authority(Authority.ROLE_USER)
                .build();

            given(memberRepository.findById(any(Long.TYPE))).willReturn(Optional.of(member));
            given(securityUtil.getCurrentMemberId()).willReturn(1L);

            // when
            Throwable exception = assertThrows(UnmatchedPasswordException.class, () -> {
                memberService.updateMember(updateMemberRequestDto);
            });

            // then
            assertEquals("비밀번호와 비밀번호 확인이 일치하지 않습니다.", exception.getMessage());

            verify(memberRepository, times(1)).findById(any(Long.TYPE));
        }
    }

    @Nested
    @DisplayName("checkPassword()는")
    class Context_checkPassword {

        @Test
        @DisplayName("비밀번호가 일치하면 true를 반환한다.")
        void correct_willSuccess() {
            // given
            CheckPasswordRequestDto checkPasswordRequestDto = CheckPasswordRequestDto.builder()
                .password("qwer1234$$")
                .build();
            Member member = Member.builder()
                .id(1L)
                .email("test@mail.com")
                .name("test")
                .password("$10$ygrAExVYmFTkZn2d0.Pk3Ot5CNZwIBjZH5f.WW0AnUq4w4PtBi9Nm")
                .photoUrl("")
                .authority(Authority.ROLE_USER)
                .build();

            given(memberRepository.findById(any(Long.TYPE))).willReturn(Optional.of(member));
            given(passwordEncoder.matches(any(String.class), any(String.class))).willReturn(true);

            // when then
            memberService.checkPassword(checkPasswordRequestDto);

            verify(memberRepository, times(1)).findById(any(Long.TYPE));
            verify(passwordEncoder, times(1)).matches(any(String.class), any(String.class));
        }

        @Test
        @DisplayName("비밀번호가 일치하지 않으면 WrongPasswordException을 던진다.")
        void wrong_willSuccess() {
            // given
            CheckPasswordRequestDto checkPasswordRequestDto = CheckPasswordRequestDto.builder()
                .password("qwer5678$$")
                .build();
            Member member = Member.builder()
                .id(1L)
                .email("test@mail.com")
                .name("test")
                .password("$10$ygrAExVYmFTkZn2d0.Pk3Ot5CNZwIBjZH5f.WW0AnUq4w4PtBi9Nm")
                .photoUrl("")
                .authority(Authority.ROLE_USER)
                .build();

            given(memberRepository.findById(any(Long.TYPE))).willReturn(Optional.of(member));
            given(passwordEncoder.matches(any(String.class), any(String.class))).willReturn(false);

            // when
            Throwable exception = assertThrows(WrongPasswordException.class, () -> {
                memberService.checkPassword(checkPasswordRequestDto);
            });

            // then
            assertEquals("비밀번호가 틀렸습니다.", exception.getMessage());

            verify(memberRepository, times(1)).findById(any(Long.TYPE));
            verify(passwordEncoder, times(1)).matches(any(String.class), any(String.class));
        }
    }

    @Nested
    @DisplayName("getMemberById()는")
    class Context_getMemberById {

        @Test
        @DisplayName("회원 정보를 조회할 수 있다.")
        void _willSuccess() {
            // given
            Member member = Member.builder()
                .id(1L)
                .email("test@mail.com")
                .name("test")
                .password("$10$ygrAExVYmFTkZn2d0.Pk3Ot5CNZwIBjZH5f.WW0AnUq4w4PtBi9Nm")
                .photoUrl("")
                .authority(Authority.ROLE_USER)
                .build();

            given(memberRepository.findById(any(Long.TYPE))).willReturn(Optional.of(member));

            // when
            Member result = memberService.getMemberById(1L);

            assertNotNull(result);
            assertThat(result).isEqualTo(member);

            verify(memberRepository, times(1)).findById(any(Long.TYPE));
        }

        @Test
        @DisplayName("일치하는 회원이 없으면 정보를 조회할 수 없다.")
        void memberNotFound_willFail() {
            // given
            given(memberRepository.findById(any(Long.TYPE))).willReturn(Optional.empty());

            // when
            Throwable exception = assertThrows(MemberNotFoundException.class, () -> {
                memberService.getMemberById(1L);
            });

            // then
            assertEquals("회원 정보를 찾을 수 없습니다.", exception.getMessage());

            verify(memberRepository, times(1)).findById(any(Long.TYPE));
        }
    }
}
