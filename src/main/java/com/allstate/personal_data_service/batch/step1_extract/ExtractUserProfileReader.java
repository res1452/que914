package com.allstate.personal_data_service.batch.step1_extract;

import com.allstate.personal_data_service.model.UserProfile;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class ExtractUserProfileReader {

    @Bean
    public ItemReader<UserProfile> oracleUserProfileReader() {

        List<UserProfile> mockData = List.of(
                new UserProfile(
                        1L, "  John  ", "Doe", "JoHn@example.COM", "(123)456-7890",
                        null, null, null, null, null, // dateOfBirth → vehicle
                        "USER", false,                // role, cacheable
                        false,                        // wantsPromotions
                        "en-US"                       // Locale
                ),
                new UserProfile(
                        2L, "  Jane  ", "Smith", "jane@EXAMPLE.com", "123.456.7891",
                        null, null, null, null, null,
                        "USER", false,
                        false,
                        "en-US"
                )
        );
        return new ListItemReader<>(mockData);
    }
}