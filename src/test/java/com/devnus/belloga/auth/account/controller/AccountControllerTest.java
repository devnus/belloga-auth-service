package com.devnus.belloga.auth.account.controller;

import com.devnus.belloga.auth.account.dto.ResponseAuth;
import com.devnus.belloga.auth.account.service.AuthServiceImpl;
import com.devnus.belloga.auth.common.aop.annotation.UserRole;
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

    @Autowired
    private AuthServiceImpl authService;

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
                                fieldWithPath("email").description("???????????? ????????? ?????? email"),
                                fieldWithPath("name").description("???????????? ????????? ?????? name"),
                                fieldWithPath("password").description("???????????? ????????? ?????? password"),
                                fieldWithPath("organization").description("???????????? ????????? ?????? organization"),
                                fieldWithPath("phoneNumber").description("???????????? ????????? ?????? phoneNumber")
                        ),
                        responseFields(
                                fieldWithPath("id").description("logging??? ?????? api response ?????? ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("?????? ?????? ??????"),
                                fieldWithPath("response.userRole").description("???????????? ????????? ???????????? ??????"),
                                fieldWithPath("error").description("error ?????? ??? ?????? ??????")
                        )
                ))
                .andExpect(jsonPath("$.response.userRole", is(notNullValue())));
    }

    @Test
    void registerCustomAccountAdminTest() throws Exception {
        //give
        Map<String, String> input = new HashMap<>();
        input.put("email", "admin@admin.com");
        input.put("name", "test_admin_name");
        input.put("password", "test_admin_password");
        input.put("phoneNumber","01000001111");

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/account/v1/auth/signup/custom/account/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                )
                //then
                .andExpect(status().isOk())
                .andDo(print())

                //docs
                .andDo(document("register-custom-account-admin",
                        requestFields(
                                fieldWithPath("email").description("???????????? ????????? ?????? email"),
                                fieldWithPath("name").description("???????????? ????????? ?????? name"),
                                fieldWithPath("password").description("???????????? ????????? ?????? password"),
                                fieldWithPath("phoneNumber").description("???????????? ????????? ?????? phoneNumber")
                        ),
                        responseFields(
                                fieldWithPath("id").description("logging??? ?????? api response ?????? ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("?????? ?????? ??????"),
                                fieldWithPath("response.userRole").description("???????????? ????????? ???????????? ??????"),
                                fieldWithPath("error").description("error ?????? ??? ?????? ??????")
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
                                fieldWithPath("email").description("????????? ????????? ?????? email"),
                                fieldWithPath("password").description("????????? ????????? ?????? password")
                        ),
                        responseFields(
                                fieldWithPath("id").description("logging??? ?????? api response ?????? ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("?????? ?????? ??????"),
                                fieldWithPath("response.accessToken").description("????????? ?????? ??? ?????? ?????? accessToken"),
                                fieldWithPath("response.refreshToken").description("????????? ?????? ??? ?????? ?????? refreshToken"),
                                fieldWithPath("error").description("error ?????? ??? ?????? ??????")
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
                                fieldWithPath("token").description("????????? ?????? ??? ???????????? ?????? ?????? access_token")
                        ),
                        responseFields(
                                fieldWithPath("id").description("logging??? ?????? api response ?????? ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("?????? ?????? ??????"),
                                fieldWithPath("response").description("????????? ?????? ??? ?????? ?????? accessToken ??? refreshToken"),
                                fieldWithPath("error.code").description("error ?????? ??? ?????? code"),
                                fieldWithPath("error.message").description("error ?????? ??? ?????? message"),
                                fieldWithPath("error.status").description("error ?????? ??? ?????? status")
                        )
                ));
    }

    @Test
    void reissueTokenTest() throws Exception {

        //give

        //?????? ??????
        ResponseAuth.Token generateToken = authService.generateToken("reissue_account_id", UserRole.LABELER);

        //???????????? ???????????? ???????????? ?????? ?????????
        Map<String, String> input = new HashMap<>();
        input.put("refreshToken", generateToken.getRefreshToken());

        //when
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/account/v1/auth/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input))
                )
                //then
                .andExpect(status().isOk())
                .andDo(print())

                //docs
                .andDo(document("reissue-token",
                        requestFields(
                                fieldWithPath("refreshToken").description("????????? ????????? ???????????? refreshToken")
                        ),
                        responseFields(
                                fieldWithPath("id").description("logging??? ?????? api response ?????? ID"),
                                fieldWithPath("dateTime").description("response time"),
                                fieldWithPath("success").description("?????? ?????? ??????"),
                                fieldWithPath("response.accessToken").description("????????? ?????? accessToken"),
                                fieldWithPath("response.refreshToken").description("????????? ?????? refreshToken"),
                                fieldWithPath("error").description("error ?????? ??? ?????? ??????")
                        )
                ))
                .andExpect(jsonPath("$.response.accessToken", is(notNullValue())))
                .andExpect(jsonPath("$.response.refreshToken", is(notNullValue())));

    }
}