package com.recipes.services;

import java.util.List;
import java.util.Optional;

import com.recipes.entity.Recipe;
import com.recipes.exception.RecipeNotFoundException;

public interface IRecipeService {

	public List<Recipe> getRecipesList();

	public void addRecipe(Recipe recipe);

	public void updateRecipe(Recipe recipe);

	public void deleteRecipe(int recipeId);
	
	public Optional<Recipe> getRecipeById(int recipeId) ;
	
	public Optional<Recipe> getRecipeByName(String name);
	
	
	



}
