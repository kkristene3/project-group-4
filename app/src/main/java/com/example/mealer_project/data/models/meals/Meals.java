package com.example.mealer_project.data.models.meals;

import androidx.annotation.NonNull;

import com.example.mealer_project.utils.Preconditions;
import com.example.mealer_project.utils.Response;
import com.example.mealer_project.utils.Result;

import java.util.HashMap;
import java.util.Map;

public class Meals {

    //<MealID, Meal> key-value pairs
    private final Map<String, Meal> menu;
    private final Map<String, Meal> meals;

    public Meals() {
        this.menu = new HashMap<>(); //<MealID, Meal> key-value pair
        this.meals = new HashMap<>(); //<MealID, Meal> key-value pair
    }

    /**
     * Retrieve a meal from all meals of the Chef by meal ID
     * @param mealID representing the ID of the meal
     * @return a Result object containing the meal is successful in getting the associated meal, else error message
     */
    protected Result<Meal, String> getMeal(@NonNull String mealID) {
        // guard-clause
        if (Preconditions.isNotEmptyString(mealID)) {
            // check if meal exists
            if (this.meals.get(mealID) != null) {
                return new Result<>(this.meals.get(mealID), null);
            } else {
                return new Result<>(null, "Could not find any meal for the provided meal ID");
            }
        } else {
            return new Result<>(null, "Invalid meal ID provided");
        }
    }

    /**
     * Add a new meal to the chef's list of meals (not menu)
     * @param newMeal Meal to be added
     */
    protected Response addMeal(@NonNull Meal newMeal) {
        // guard-clause
        // meal must have a valid id which will be used as a key
        if (Preconditions.isNotEmptyString(newMeal.getMealID())) {
            // check if meal already exists
            if (this.meals.get(newMeal.getMealID()) != null) {
                return new Response(false, "Meal with same ID already exists! Use updateMeal to update an existing meal");
            }
            // add the new meal
            this.meals.put(newMeal.getMealID(), newMeal);
            // return success
            return new Response(true);
        } else {
            return new Response(false, "Meal does not have a valid ID");
        }
    }

    /**
     * Add a new meal to the chef's menu
     * @param mealId ID of the meal that needs to be added to the menu
     * @return Response object indicating success, or failure (with an error message)
     */
    protected Response addMealToMenu(@NonNull String mealId) {
        // guard-clause
        if (Preconditions.isNotEmptyString(mealId)) {
            // check if meal exists and has a valid object
            if (this.menu.get(mealId) != null) {
                // check if meal if offered
                if (this.menu.get(mealId).isOffered()) {
                    // add meal to the menu
                    this.menu.put(mealId, this.menu.get(mealId));
                    // return success
                    return new Response(true);
                } else {
                    return new Response(false, "Meal is currently not being offered by the chef");
                }
            } else {
                return new Response(false, "Could not find the any meal for the provided ID");
            }
        } else {
            return new Response(false, "Meal does not have a valid ID");
        }
    }

    /**
     * Remove a meal from the list of all meals added by a Chef
     * @param mealId ID of the meal to be removed
     */
    protected Response removeMeal(@NonNull String mealId) {
        // guard-clause
        if (Preconditions.isNotEmptyString(mealId)) {
            // check if meal exists
            if (this.meals.get(mealId) != null) {
                // remove the meal
                this.meals.remove(mealId);
                // return operation success
                return new Response(true);
            } else {
                return new Response(false, "Could not find any meal for the provided meal ID");
            }
        } else {
            return new Response(false, "Invalid meal ID provided");
        }
    }

    /**
     * Remove a meal from Chef's menu
     * @param mealId ID of the meal to be removed
     */
    protected Response removeMealFromMenu(@NonNull String mealId) {
        // guard-clause
        if (Preconditions.isNotEmptyString(mealId)) {
            // check if meal exists
            if (this.menu.get(mealId) != null) {
                // remove the meal
                this.menu.remove(mealId);
                // return operation success
                return new Response(true);
            } else {
                return new Response(false, "Could not find any meal for the provided meal ID");
            }
        } else {
            return new Response(false, "Invalid meal ID provided");
        }
    };

    /**
     * Method to retrieve a map object containing meals which are part of Chef's menu
     * @return a Map containing Meal ID's as keys and Meal objects as values
     */
    protected Map<String, Meal> getMenu() {
        return this.menu;
    }

    /**
     * Method to retrieve a map object containing meals which are currently being offered by Chef
     * @return a Map containing Meal ID's as keys and Meal objects as values
     */
    protected Map<String, Meal> getOfferedMeals() {
        // map to store the result
        HashMap<String, Meal> offeredMeals = new HashMap<>();
        // filter and add offered meals to above map
        for (Meal meal : this.meals.values()) {
            if (meal.isOffered()) {
                offeredMeals.put(meal.getMealID(), meal);
            }
        }
        // return the result
        return offeredMeals;
    }

    /**
     * Method to retrieve a map object containing all meals added by the Chef
     * @return a Map containing Meal ID's as keys and Meal objects as values
     */
    protected Map<String, Meal> getAllMeals() {
        return this.meals;
    };
}
