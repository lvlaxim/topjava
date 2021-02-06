package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class UserMealWithExcess {

    private static int caloriesLimit;

    private static Map<LocalDate, DayInfo> dayInfoMap = new HashMap<>();

    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private Boolean excess;

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    public UserMealWithExcess(UserMeal userMeal) {
        LocalDateTime dateTime = userMeal.getDateTime();
        this.dateTime = dateTime;
        this.description = userMeal.getDescription();
        int mealCalories = userMeal.getCalories();
        this.calories = mealCalories;
        LocalDate date = dateTime.toLocalDate();
        dayInfoMap.merge(date, new DayInfo(date, mealCalories, caloriesLimit), (oldDayInfo, newDayInfo) -> {
            oldDayInfo.addCalories(mealCalories);
            return oldDayInfo;
        });
        this.excess = dayInfoMap.get(date).isExceed;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public static void setCaloriesLimit(int caloriesLimit) {
        UserMealWithExcess.caloriesLimit = caloriesLimit;
    }

    public void calculateExcess() {
        LocalDate date = dateTime.toLocalDate();
        this.excess = dayInfoMap.get(date).isExceed;
    }

    @Override
    public String toString() {
        return "UserMealWithExcess{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }

    private class DayInfo {
        LocalDate date;
        int calories;
        int caloriesLimit;
        Boolean isExceed;

        public DayInfo(LocalDate date, int calories, int caloriesLimit) {
            this.date = date;
            this.caloriesLimit = caloriesLimit;
            this.addCalories(calories);
        }

        public void addCalories(int calories) {
            this.calories = this.calories + calories;
            if (this.calories > caloriesLimit) {
                isExceed = true;
            } else {
                isExceed = false;
            }
        }
    }
}
