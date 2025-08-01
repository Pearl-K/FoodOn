package com.foodon.foodon.intakelog.domain;

import com.foodon.foodon.common.util.BigDecimalUtil;
import com.foodon.foodon.meal.domain.Meal;
import com.foodon.foodon.member.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.foodon.foodon.common.util.BigDecimalUtil.add;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "intake_logs",
        indexes = {
                @Index(name = "idx_member_date", columnList = "member_id, date")
        }
)
public class IntakeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intake_log_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private LocalDate date;

    @Column(precision = 7, scale = 2, nullable = false)
    private BigDecimal goalKcal;

    @Column(precision = 8, scale = 2, nullable = false)
    private BigDecimal intakeKcal;

    @Column(precision = 8, scale = 2, nullable = false)
    private BigDecimal intakeCarbs;

    @Column(precision = 8, scale = 2, nullable = false)
    private BigDecimal intakeProtein;

    @Column(precision = 8, scale = 2, nullable = false)
    private BigDecimal intakeFat;


    private IntakeLog(
            Member member,
            LocalDate date,
            BigDecimal goalKcal
    ){
        this.member = member;
        this.date = date;
        this.goalKcal = goalKcal;
    }

    public static IntakeLog createIntakeLogOfMember(
            Member member,
            LocalDate date,
            BigDecimal goalKcal
    ){
        return new IntakeLog(
                member,
                date,
                goalKcal
        );
    }

    public void updateIntakeFromMeal(Meal meal){
        this.intakeKcal = add(this.intakeKcal, meal.getTotalKcal());
        this.intakeCarbs = add(this.intakeCarbs, meal.getTotalCarbs());
        this.intakeProtein = add(this.intakeProtein, meal.getTotalProtein());
        this.intakeFat = add(this.intakeFat, meal.getTotalFat());
    }

}
