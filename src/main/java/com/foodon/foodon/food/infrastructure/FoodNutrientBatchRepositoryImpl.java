package com.foodon.foodon.food.infrastructure;

import com.foodon.foodon.food.domain.FoodNutrient;
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
public class FoodNutrientBatchRepositoryImpl implements FoodNutrientBatchRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public int[] saveAllFoodNutrients(List<FoodNutrient> foodNutrients) {
        String bulkInsertSql = "INSERT INTO food_nutrients (food_id, nutrient_id, value) "
                + "VALUES "
                + "(?, ?, ?)";

        log.trace("Bulk insert query size {} : {}", foodNutrients.size(), bulkInsertSql);

        return jdbcTemplate.batchUpdate(bulkInsertSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                FoodNutrient foodNutrient = foodNutrients.get(i);
                ps.setLong(1, foodNutrient.getFoodId());
                ps.setLong(2, foodNutrient.getNutrientId());
                ps.setBigDecimal(3, foodNutrient.getValue());
            }

            @Override
            public int getBatchSize() {
                return foodNutrients.size();
            }
        });
    }
}
