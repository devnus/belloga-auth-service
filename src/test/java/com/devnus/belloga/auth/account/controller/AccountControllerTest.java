package com.devnus.belloga.auth.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@EmbeddedKafka(
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:9092"
        },
        ports = { 9092 })
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerCustomAccountEnterpriseTest() throws Exception {
        //give
        Map<String, String> input = new HashMap<>();
        input.put("email", "test@test.com");
        input.put("name", "test_name");
        input.put("password", "test_password");
        input.put("organization", "test_organization");
        input.put("phoneNumber","01000000000");

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/account/v1/auth/signup/custom/account/enterprise")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input))
        )
                //then
                .andExpect(status().isOk())
                .andDo(print())

                //docs
                .andDo(document("register-custom-account-enterprise",
                        requestFields(
                                fieldWithPath("email").description("회원가입 하고자 하는 email"),
                                fieldWithPath("name").description("회원가입 하고자 하는 name"),
                                fieldWithPath("password").description("회원가입 하고자 하는 password"),
                                fieldWithPath("organization").description("회원가입 하고자 하는 organization"),
                                fieldWithPath("phoneNumber").description("회원가입 하고자 하는 phoneNumber")
                        ),
                        responseFields(
                                fieldWithPath("id").description("logging을 위한 api response 고유 ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("정상 응답 여부"),
                                fieldWithPath("response.userRole").description("회원가입 성공한 사용자의 역할"),
                                fieldWithPath("error").description("error 발생 시 에러 정보")
                        )
                ))
                .andExpect(jsonPath("$.response.userRole", is(notNullValue())));
    }

    @Test
    void signInCustomAccountTest() throws Exception {

        //give
        Map<String, String> input = new HashMap<>();
        input.put("email", "devnus@devnus.com");
        input.put("password", "devnus_password");

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/account/v1/auth/signin/custom/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                )
                //then
                .andExpect(status().isOk())
                .andDo(print())

                //docs
                .andDo(document("signin-custom-account",
                        requestFields(
                                fieldWithPath("email").description("로그인 하고자 하는 email"),
                                fieldWithPath("password").description("로그인 하고자 하는 password")
                        ),
                        responseFields(
                                fieldWithPath("id").description("logging을 위한 api response 고유 ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("정상 응답 여부"),
                                fieldWithPath("response.accessToken").description("로그인 성공 후 발급 받는 accessToken"),
                                fieldWithPath("response.refreshToken").description("로그인 성공 후 발급 받는 refreshToken"),
                                fieldWithPath("error").description("error 발생 시 에러 정보")
                        )
                ))
                .andExpect(jsonPath("$.response.accessToken", is(notNullValue())))
                .andExpect(jsonPath("$.response.refreshToken", is(notNullValue())));

    }

    @Test
    void signInNaverAccountTest() throws Exception {

        //give
        Map<String, String> input = new HashMap<>();
        input.put("token", "naver-oauth-access-token");

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/account/v1/auth/signin/naver/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                )
                //then
                .andExpect(status().isUnauthorized())
                .andDo(print())

                //docs
                .andDo(document("signin-naver-account",
                        requestFields(
                                fieldWithPath("token").description("네이버 인증 후 네이버로 부터 받은 access_token")
                        ),
                        responseFields(
                                fieldWithPath("id").description("logging을 위한 api response 고유 ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("정상 응답 여부"),
                                fieldWithPath("response").description("로그인 성공 후 발급 받는 accessToken 와 refreshToken"),
                                fieldWithPath("error.code").description("error 발생 시 에러 code"),
                                fieldWithPath("error.message").description("error 발생 시 에러 message"),
                                fieldWithPath("error.status").description("error 발생 시 에러 status")
                        )
                ));
    }
}