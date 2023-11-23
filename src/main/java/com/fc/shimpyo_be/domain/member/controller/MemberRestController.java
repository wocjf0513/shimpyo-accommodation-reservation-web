package com.fc.shimpyo_be.domain.member.controller;

import com.fc.shimpyo_be.domain.member.dto.request.CheckPasswordRequestDto;
import com.fc.shimpyo_be.domain.member.dto.request.UpdateMemberRequestDto;
import com.fc.shimpyo_be.domain.member.dto.response.CheckPasswordResponseDto;
import com.fc.shimpyo_be.domain.member.dto.response.MemberResponseDto;
import com.fc.shimpyo_be.domain.member.service.MemberService;
import com.fc.shimpyo_be.global.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberRestController {

    private final MemberService memberService;

    @PatchMapping
    public ResponseEntity<ResponseDto<MemberResponseDto>> updateMember(@RequestBody
    UpdateMemberRequestDto updateMemberRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDto.res(HttpStatus.OK, memberService.updateMember(updateMemberRequestDto),
                "성공적으로 회원 정보를 수정했습니다."));
    }

    @PostMapping
    public ResponseEntity<ResponseDto<CheckPasswordResponseDto>> checkPassword(
        @RequestBody CheckPasswordRequestDto checkPasswordRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(
            ResponseDto.res(HttpStatus.OK, memberService.checkPassword(checkPasswordRequestDto),
                "비밀번호가 일치합니다."));
    }
}
