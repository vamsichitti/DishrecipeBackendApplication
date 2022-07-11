package com.recipes.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.recipes.entity.Recipe;
import com.recipes.exception.RecipeNotFoundException;
import com.recipes.repository.IRecipeRepository;
import com.recipes.services.RecipeServices;



@RestController
@RequestMapping("/recipes")
public class RecipeBackendController {
	
	@Autowired
	RecipeServices recipeService;
	
	@Autowired
	IRecipeRepository recipeRepo;
	
	Logger logger = LoggerFactory.getLogger(RecipeBackendController.class); 
	
	
	@PostMapping("/addRecipe")
	public ResponseEntity<?> createRecipe(@RequestBody Recipe recipe) throws RecipeNotFoundException{
		Optional<?> opt1 = recipeRepo.findById(recipe.getRecipeId());
		Optional<?> opt2 = recipeRepo.findByName(recipe.getName());
		
		if(opt1.isPresent() || opt2.isPresent()) {
			logger.info("Recipe with ID {} is already EXISTS",recipe.getRecipeId());
			throw new RecipeNotFoundException(" Id "+recipe.getRecipeId()+" already exists");
			
		}
		else {
			recipeService.addRecipe(recipe);
			logger.info("Recipe with ID {} is created SUCCESSFULLY",recipe.getRecipeId());
			return new ResponseEntity<>("Successfully created",HttpStatus.CREATED);
			
		}
	}
	
	@PutMapping("/updateRecipe/{recipeId}")
	public ResponseEntity<?> updateRecipe(@RequestBody Recipe recipe , @PathVariable("recipeId") int recipeId) throws RecipeNotFoundException {

		//Recipe recipeRecords = recipeRepo.findById(recipe.getRecipeId()).get();
		logger.info("Updating the recipe with ID {}",recipeId);
		Optional<?> recipeData = recipeRepo.findById(recipeId);
		if (!recipeData.isPresent()) {
			logger.info("RecipeID with ID {} is Not found",recipeId);
			throw new RecipeNotFoundException("Recipe Id " + recipeId + " is not found ");
			
		} else {
            Recipe updatedRecipe = (Recipe) recipeData.get();
			
			updatedRecipe.setName(recipe.getName());
			updatedRecipe.setRecipeId(recipe.getRecipeId());
			updatedRecipe.setInstructions(recipe.getInstructions());
			updatedRecipe.setServings(recipe.getServings());
			updatedRecipe.setVeg(recipe.isVeg());
			updatedRecipe.setIngredientsList(recipe.getIngredientsList());
			updatedRecipe.setCreated(recipe.getCreated());

			recipeService.updateRecipe(updatedRecipe);
			
			logger.info(" ID : {} is UPDATED SUCCESSFULLY",recipeId);

			return new ResponseEntity<>(" Recipe Id " + recipeId + " is updated successfully ",
					HttpStatus.ACCEPTED);
		}
		
		

	}
	
	@GetMapping("/getRecipeById/{id}")
	public ResponseEntity<Optional<Recipe>> getRecipeById(@PathVariable("id") int id) throws RecipeNotFoundException{
		Optional<Recipe> opt = recipeRepo.findById(id);
		
		logger.info("Calling a recipe By ID {}",id);
		if(opt.isPresent()) {
			Optional<Recipe> resp=recipeService.getRecipeById(id);
			logger.info("Recipe with ID {} is found",id);
			return new ResponseEntity<>(resp,HttpStatus.FOUND);
		}
		else {
			logger.info("RecipeID with ID {} is Not found",id);
			throw new RecipeNotFoundException("ID with "+id+" not found ");
		}
			
	}
	@GetMapping("getRecipeByName/{recipeName}")
	public ResponseEntity<?> getRecipeByName(@PathVariable("recipeName") String recipeName) throws RecipeNotFoundException {
         
		logger.info("Calling Recipe By Name {}",recipeName);
		Optional<Recipe> opt = recipeRepo.findByName(recipeName);

		if (opt.isPresent()) {
			logger.info(" Recipe with Name {} is found",recipeName);

			return new ResponseEntity<>( recipeService.getRecipeByName(recipeName), HttpStatus.FOUND);

		}

		else {
			logger.info(" Recipe with Name {} not found",recipeName);

			throw new RecipeNotFoundException(" Recipe with name : " + recipeName + " is not found ");
		}

	}
	

	
	@DeleteMapping("/deleteRecipe/{recipeId}")
	public ResponseEntity<String> deleteRecipe(@PathVariable("recipeId") int recipeId) throws RecipeNotFoundException {

		Optional<Recipe> opt = recipeRepo.findById(recipeId);

		if (opt.isPresent()) {

			recipeService.deleteRecipe(recipeId);
			
			logger.info("ID with {} is DELETED SUCCESSFULLY",recipeId);

			return new ResponseEntity<>("Recipe id : " + recipeId + " is deleted successfully", HttpStatus.OK);
		} else {
			logger.info("ID with {} is not found",recipeId);

			throw new RecipeNotFoundException(" Recipe id : " + recipeId + " is not found ");

		}
	}
	
	@GetMapping("/allRecipes")
	public ResponseEntity<?> getAllRecipes() throws RecipeNotFoundException {

		if (!recipeRepo.findAll().isEmpty()) {

			return new ResponseEntity<>(recipeService.getRecipesList(), HttpStatus.OK);

		} else {
			logger.info("Empty List of Recipes");

			throw new RecipeNotFoundException("No recipes in the list ");

		}
	}
	
	

}
