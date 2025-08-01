package com.foodon.foodon.food.infrastructure;

import com.foodon.foodon.food.domain.NutrientClaimType;
import com.foodon.foodon.food.domain.NutrientCode;
import com.foodon.foodon.food.domain.rule.*;
import com.foodon.foodon.food.dto.NutrientServingInfo;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class NutrientClaimEvaluator {

    private static final List<NutrientClaimRule> rules = List.of(
            new HighProteinRule(), // 고단백질
            new LowSugarRule(), // 저당
            new LowSodiumRule(), // 저나트륨
            new HighKcalRule(), // 고칼로리
            new LowKcalRule() // 저칼로리
    );

    public static List<NutrientClaimType> evaluate(List<NutrientServingInfo> nutrientServingInfos) {
        Map<NutrientCode, NutrientServingInfo> nutrientMap = convertToMap(nutrientServingInfos);
        return rules.stream()
                .filter(rule -> rule.matches(nutrientMap))
                .map(NutrientClaimRule::getNutrientClaimType)
                .toList();
    }

    private static Map<NutrientCode, NutrientServingInfo> convertToMap(
            List<NutrientServingInfo> nutrientServingInfos
    ) {
        return nutrientServingInfos.stream().collect(Collectors.toMap(
                NutrientServingInfo::nutrientCode,
                Function.identity(),
                (existing, replacement) -> existing
        ));
    }

}

