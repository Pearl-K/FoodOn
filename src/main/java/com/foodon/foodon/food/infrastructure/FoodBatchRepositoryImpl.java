package com.foodon.foodon.food.infrastructure;

import com.foodon.foodon.food.domain.FoodNutrientClaim;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FoodBatchRepositoryImpl implements FoodBatchRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public int[] saveAllFoodNutrientClaims(List<FoodNutrientClaim> foodNutrientClaims) {
        String bulkInsertSql = "INSERT INTO food_nutrient_claims (food_id, nutrient_claim_id) "
                + "VALUES "
                + "(?, ?)";

        log.trace("Bulk insert query size {} : {}", foodNutrientClaims.size(), bulkInsertSql);

        return jdbcTemplate.batchUpdate(bulkInsertSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                FoodNutrientClaim foodNutrientClaim = foodNutrientClaims.get(i);
                ps.setLong(1, foodNutrientClaim.getFoodId());
                ps.setLong(2, foodNutrientClaim.getNutrientClaimId());
            }

            @Override
            public int getBatchSize() {
                return foodNutrientClaims.size();
            }
        });
    }

}
