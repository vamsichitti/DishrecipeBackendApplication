package com.recipes.services;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.recipes.entity.Recipe;
import com.recipes.exception.RecipeNotFoundException;
import com.recipes.repository.IRecipeRepository;



@Service
public class RecipeServices implements IRecipeService{
	
	
	@Autowired
	IRecipeRepository recipeRepo;

	@Override
	public List<Recipe> getRecipesList() {
		List<Recipe> listOfRecipes = recipeRepo.findAll();
		return listOfRecipes;
	}

	@Override
	public void addRecipe(Recipe recipe) {
		recipeRepo.save(recipe);
		
	}

	@Override
	public void updateRecipe(Recipe recipe) {
		recipeRepo.save(recipe);
		
	}

	@Override
	public void deleteRecipe(int recipeId) {
		recipeRepo.deleteById(recipeId);
		
	}

	@Override
	public Optional<Recipe> getRecipeById (int recipeId) {
		
		
		Optional<Recipe> recipe= recipeRepo.findById(recipeId);
		
		return recipe;
	}

	@Override
	public Optional<Recipe> getRecipeByName(String name)  {
		Optional<Recipe> recipe =recipeRepo.findByName(name);
		return recipe;
	}

}
