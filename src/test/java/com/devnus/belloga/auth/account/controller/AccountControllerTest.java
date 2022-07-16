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
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("local")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@EmbeddedKafka(partitions = 1,
        topics = "register-custom-account-enterprise",
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
        input.put("name", "devnus_name");
        input.put("password", "devnus_password");
        input.put("organization", "devnus_organization");
        input.put("phoneNumber","01000000000");

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/auth/signup/custom/account/enterprise")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input))
        )
                //then
                .andExpect(status().isOk())
                .andDo(print())

                //docs
                .andDo(document("custom-account-enterprise",
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
                                fieldWithPath("response.username").description("회원가입 성공한 username"),
                                fieldWithPath("response.accountRole").description("회원가입 성공한 accountRole"),
                                fieldWithPath("error").description("error 발생 시 에러 정보")
                        )
                ))
                .andExpect(jsonPath("$.response.username", is(notNullValue())))
                .andExpect(jsonPath("$.response.accountRole", is(notNullValue())));
    }
}