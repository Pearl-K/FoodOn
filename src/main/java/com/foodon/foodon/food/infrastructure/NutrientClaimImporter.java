package com.foodon.foodon.food.infrastructure;

import com.foodon.foodon.common.util.NutrientCalculator;
import com.foodon.foodon.food.domain.FoodNutrientClaim;
import com.foodon.foodon.food.domain.NutrientClaim;
import com.foodon.foodon.food.domain.NutrientClaimType;
import com.foodon.foodon.food.dto.FoodWithNutrientInfo;
import com.foodon.foodon.food.dto.NutrientServingInfo;
import com.foodon.foodon.food.repository.FoodRepository;
import com.foodon.foodon.food.repository.NutrientClaimRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class NutrientClaimImporter {

    private final FoodRepository foodRepository;
    private final FoodBatchRepository foodBatchRepository;
    private final NutrientClaimRepository nutrientClaimRepository;


    @Transactional
    public void saveNutrientClaim(){
        Map<NutrientClaimType, Long> nutrientClaimTypeIdMap = getNutrientClaimIdMap();
        List<FoodWithNutrientInfo> foods = foodRepository.findAllFoodInfo();
        List<FoodNutrientClaim> foodNutrientClaims = createNutrientClaims(foods, nutrientClaimTypeIdMap);

        int[] result = foodBatchRepository.saveAllFoodNutrientClaims(foodNutrientClaims);
        log.info("[NutrientClaimImporter] 총 {} 건의 FoodNutrientClaim insert 성공", Arrays.stream(result).sum());
    }

    private List<FoodNutrientClaim> createNutrientClaims(
            List<FoodWithNutrientInfo> foods,
            Map<NutrientClaimType, Long> nutrientClaimTypeIdMap
    ) {
        return foods.stream()
                .flatMap(food -> evaluateNutrientClaims(food).stream()
                        .map(type -> FoodNutrientClaim.createNutrientClaimOfFood(
                                food.foodId(),
                                nutrientClaimTypeIdMap.get(type))
                        ))
                .toList();
    }

    private Map<NutrientClaimType, Long> getNutrientClaimIdMap() {
        return nutrientClaimRepository.findAll().stream()
                .collect(Collectors.toMap(NutrientClaim::getType, NutrientClaim::getId));
    }

    private List<NutrientClaimType> evaluateNutrientClaims(FoodWithNutrientInfo foodWithNutrientInfo) {
        List<NutrientServingInfo> nutrientServingInfos = convertToNutrientServingInfos(foodWithNutrientInfo);
        return NutrientClaimEvaluator.evaluate(nutrientServingInfos);
    }

    private List<NutrientServingInfo> convertToNutrientServingInfos(
            FoodWithNutrientInfo food
    ) {
        return food.nutrients().stream()
                .map(nutrient -> new NutrientServingInfo(
                        nutrient.code(),
                        nutrient.value(),
                        NutrientCalculator.calculateNutrientPerServing(food.servingSize(), nutrient.value())
                ))
                .toList();
    }

}
