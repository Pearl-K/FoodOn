package com.foodon.foodon.food.application;

import com.foodon.foodon.common.util.NutrientCalculator;
import com.foodon.foodon.food.domain.*;
import com.foodon.foodon.food.dto.*;
import com.foodon.foodon.food.exception.FoodException;
import com.foodon.foodon.food.exception.FoodException.FoodBadRequestException;
import com.foodon.foodon.food.exception.FoodException.FoodConflictException;
import com.foodon.foodon.food.infrastructure.FoodNutrientBatchRepository;
import com.foodon.foodon.food.repository.FoodNutrientRepository;
import com.foodon.foodon.food.repository.FoodRepository;
import com.foodon.foodon.food.repository.NutrientRepository;
import com.foodon.foodon.meal.dto.NutrientProfile;
import com.foodon.foodon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.foodon.foodon.food.exception.FoodErrorCode.CONFLICT_CUSTOM_FOOD;
import static com.foodon.foodon.food.exception.FoodErrorCode.ILLEGAL_FOOD_NAME_BLANK;
import static org.eclipse.jdt.internal.compiler.parser.Parser.name;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final FoodNutrientRepository foodNutrientRepository;
    private final FoodNutrientBatchRepository foodNutrientBatchRepository;
    private final NutrientRepository nutrientRepository;

    @Transactional
    public CustomFoodCreateResponse saveCustomFood(
            CustomFoodCreateRequest request,
            Member member
    ) {
        checkDuplicateCustomFood(request.foodName(), member);
        List<Nutrient> nutrients = nutrientRepository.findAll();
        Food food = Food.createCustomFoodByMember(request, member);
        foodRepository.save(food);
        List<FoodNutrient> foodNutrients = registerFoodNutrients(request.nutrients(), nutrients, food);
        Map<NutrientCode, BigDecimal> nutrientMap = convertToPerServingMap(foodNutrients, nutrients, food.getServingSize());

        return CustomFoodCreateResponse.from(food, NutrientProfile.from(nutrientMap));
    }

    @Transactional
    public CustomFoodCreateResponse saveModifiedFood(
            CustomFoodCreateRequest request,
            Member member
    ){
        String registerName = getRegisterNameWithOrigName(request.foodName(), LocalDateTime.now());
        checkDuplicateCustomFood(registerName, member);
        List<Nutrient> nutrients = nutrientRepository.findAll();
        Food food = Food.createCustomFoodModifiedByMember(request, registerName, member);
        foodRepository.save(food);
        List<FoodNutrient> foodNutrients = registerFoodNutrients(request.nutrients(), nutrients, food);
        Map<NutrientCode, BigDecimal> nutrientMap = convertToPerServingMap(foodNutrients, nutrients, food.getServingSize());

        return CustomFoodCreateResponse.from(food, NutrientProfile.from(nutrientMap));
    }

    private String getRegisterNameWithOrigName(String foodName, LocalDateTime dateTime) {
        return foodName.trim() + "_" + dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    private void checkDuplicateCustomFood(
            String foodName,
            Member member
    ) {
        if(foodRepository.existsByMemberIdAndName(member.getId(), foodName.trim())) {
            throw new FoodConflictException(CONFLICT_CUSTOM_FOOD);
        }
    }

    private List<FoodNutrient> registerFoodNutrients(
            NutrientProfile nutrientInfo,
            List<Nutrient> nutrients,
            Food food
    ) {
        Map<NutrientCode, Long> nutrientCodeIdMap = convertToNutrientCodeMap(nutrients);
        List<FoodNutrient> foodNutrients = nutrientInfo.toMap().entrySet().stream()
                .filter(e -> e.getValue() != null && e.getValue().compareTo(BigDecimal.ZERO) > 0)
                .map(e -> FoodNutrient.createFoodNutrient(
                        food.getId(),
                        nutrientCodeIdMap.get(e.getKey()),
                        NutrientCalculator.calculateNutrientPer100g(food.getServingSize(), e.getValue())
                ))
                .collect(Collectors.toList());

        foodNutrientBatchRepository.saveAllFoodNutrients(foodNutrients);
        return foodNutrients;
    }

    private Map<NutrientCode, Long> convertToNutrientCodeMap(List<Nutrient> nutrients) {
        return nutrients.stream()
                .collect(Collectors.toMap(
                        Nutrient::getCode,
                        Nutrient::getId
                ));
    }

    public Map<NutrientCode, BigDecimal> convertToPerServingMap(
            List<FoodNutrient> foodNutrients,
            List<Nutrient> nutrients,
            BigDecimal servingSize
    ) {
        Map<Long, NutrientCode> nutrientIdCodeMap = convertToNutrientIdMap(nutrients);
        return foodNutrients.stream()
                .filter(n -> n.getValue() != null && nutrientIdCodeMap.containsKey(n.getNutrientId()))
                .collect(Collectors.toMap(
                        n -> nutrientIdCodeMap.get(n.getNutrientId()),
                        n -> NutrientCalculator.calculateNutrientPerServing(servingSize, n.getValue())
                ));
    }

    private Map<Long, NutrientCode> convertToNutrientIdMap(List<Nutrient> nutrients) {
        return nutrients.stream()
                .collect(Collectors.toMap(
                        Nutrient::getId,
                        Nutrient::getCode
                ));
    }

    public FoodDetailInfoResponse getFood(
            Long foodId,
            FoodType type,
            Member member
    ) {
        FoodWithNutrientInfo foodInfo = foodRepository.findFoodInfoWithNutrientByIdAndType(foodId, type, member);
        return FoodDetailInfoResponse.from(foodInfo, getNutrientValueMap(foodInfo));
    }

    private Map<NutrientCode, BigDecimal> getNutrientValueMap(
            FoodWithNutrientInfo foodInfo
    ) {
        return foodInfo.nutrients().stream()
                .collect(Collectors.toMap(
                        NutrientInfo::code,
                        nutrientInfo -> NutrientCalculator.calculateNutrientPerServing(foodInfo.servingSize(), nutrientInfo.value())
                ));
    }

    public List<FoodNameResponse> getSimilarFoods(String name) {
        validateFoodNameParam(name);
        Pageable limit = PageRequest.of(0, 10); // LIMIT 10
        return foodRepository.findByNameContainingAndSearchableIsTrueAndMemberIdIsNull(name, limit)
                .stream()
                .map(FoodNameResponse::from)
                .toList();
    }

    private void validateFoodNameParam(String name) {
        if(name == null || name.isBlank()) {
            throw new FoodBadRequestException(ILLEGAL_FOOD_NAME_BLANK);
        }
    }

    public List<FoodNameResponse> getRecentFoods(Long memberId) {
        return foodRepository.findTop10ByMemberIdAndSearchableIsTrueOrderByIdDesc(memberId)
                .stream()
                .map(FoodNameResponse::from)
                .toList();
    }

}
