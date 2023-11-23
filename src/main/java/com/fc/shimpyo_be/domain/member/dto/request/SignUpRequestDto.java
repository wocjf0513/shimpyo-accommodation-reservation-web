package com.fc.shimpyo_be.domain.member.dto.request;

import com.fc.shimpyo_be.domain.member.entity.Authority;
import com.fc.shimpyo_be.domain.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor
public class SignUpRequestDto {

    @NotBlank(message = "이메일을 입력하세요.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "이메일 형식에 맞게 입력해주세요.")
    private String email;
    @NotBlank(message = "이름을 입력하세요.")
    @Size(min = 2, max = 30, message = "이름은 최소 2자 이상 최대 30자 이내로 입력하세요.")
    private String name;
    @NotBlank(message = "비밀번호를 입력하세요.")
    private String password;
    @NotBlank(message = "비밀번호 확인을 입력하세요.")
    private String passwordConfirm;

    @Builder
    public SignUpRequestDto(String email, String name, String password, String passwordConfirm) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }

    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
            .email(this.email)
            .name(this.name)
            .password(passwordEncoder.encode(this.password))
            .photoUrl("")
            .authority(Authority.ROLE_USER)
            .build();
    }
}
