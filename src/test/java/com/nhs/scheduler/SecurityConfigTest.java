package com.nhs.scheduler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.nhs.scheduler.model.ScheduleState;
import com.nhs.scheduler.service.ScheduleStateStore;

import static org.mockito.BDDMockito.given;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "scheduler.legacy-migration.enabled=false")
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleStateStore scheduleStateStore;

    @Test
    void protectedRoutesRequireAuthentication() throws Exception {
        given(scheduleStateStore.read()).willReturn(new ScheduleState());
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login.html*"));
    }

    @Test
    void loginSucceedsWithConfiguredCredentials() throws Exception {
        given(scheduleStateStore.read()).willReturn(new ScheduleState());
        mockMvc.perform(formLogin("/login")
                        .user("admin")
                        .password("change-me"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}
