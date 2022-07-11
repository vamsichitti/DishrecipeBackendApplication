package com.recipes;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipes.controller.RecipeBackendController;
import com.recipes.entity.Ingredients;
import com.recipes.entity.Recipe;
import com.recipes.repository.IRecipeRepository;
import com.recipes.services.RecipeServices;



@AutoConfigureMockMvc
@ContextConfiguration
@SpringBootTest(classes= {RestControllerTest.class})
public class RestControllerTest {

	
	@Autowired
	MockMvc mockMvc;
	
	@Mock
	RecipeServices service;
	
	@Mock
	IRecipeRepository recipeRepo;
	
	@InjectMocks
	RecipeBackendController controller;
	
	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}
	
	
	@Test
	public void test_getAllRecipes() throws Exception {
		List<Ingredients> ingredientsList = new ArrayList<>();
		Ingredients ingredient1 = new Ingredients();
		ingredient1.setId(2);
		ingredient1.setIngredientsList("karam,chilli powder");
		
		Ingredients ingredient2 = new Ingredients();
		ingredient2.setId(3);
		ingredient2.setIngredientsList("chat masala,chicken powder");
		
		ingredientsList.add(ingredient1);
		ingredientsList.add(ingredient2);
		
		List<Recipe> recipeList = new ArrayList<>();
		Recipe recipe1 = new Recipe();
		recipe1.setName("maggi masala");
		recipe1.setRecipeId(1);
		recipe1.setInstructions("wait for 5 min ");
		recipe1.setServings(3);
		recipe1.setVeg(true);

		recipe1.setIngredientsList(ingredientsList);
		
		Recipe recipe2 = new Recipe();
		recipe1.setName("top raman maggi masala");
		recipe1.setRecipeId(1);
		recipe1.setInstructions("wait for 10 min ");
		recipe1.setServings(3);
		recipe1.setVeg(true);

		recipe1.setIngredientsList(ingredientsList);
		
		recipeList.add(recipe1);
		recipeList.add(recipe2);
		
		
		when(recipeRepo.findAll()).thenReturn(recipeList);
		
		this.mockMvc.perform(get("/recipes/allRecipes"))
		            .andExpect(status().isOk())
		            .andDo(print());
	}
	
	@Test
	public void test_getRecipeById() throws Exception {
		
		
		Recipe recipe1 = new Recipe(2,"maggi masala",true,5,"cook for 5 min",null);

		
		
		when(service.getRecipeById(recipe1.getRecipeId())).thenReturn(Optional.of(recipe1));
		when(recipeRepo.findById(recipe1.getRecipeId())).thenReturn(Optional.of(recipe1));
		int respid = recipe1.getRecipeId();
		
		this.mockMvc.perform(get("/recipes/getRecipeById/{id}",respid))
		            .andExpect(status().isFound())
		            .andExpect(MockMvcResultMatchers.jsonPath(".recipeId").value(2))
		            .andExpect(MockMvcResultMatchers.jsonPath(".name").value("maggi masala"))
		            .andExpect(MockMvcResultMatchers.jsonPath(".servings").value(5))
		            .andDo(print());
		
	}
	
	@Test
	public void test_getRecipeByName() throws Exception {
		Recipe recipe1 = new Recipe(2,"maggi masala",true,5,"cook for 5 min",null);
		when(service.getRecipeByName(recipe1.getName())).thenReturn(Optional.of(recipe1));
		when(recipeRepo.findByName(recipe1.getName())).thenReturn(Optional.of(recipe1));
		
		String recipeName = recipe1.getName();
		this.mockMvc.perform(get("/recipes/getRecipeByName/{recipeName}",recipeName))
		                    .andExpect(status().isFound())
		                    .andExpect(MockMvcResultMatchers.jsonPath(".name").value("maggi masala"))
		                    .andDo(print());
	}
	
	@Test
	public void test_createRecipe() throws Exception {
		Recipe recipe1 = new Recipe(2,"maggi masala",true,5,"cook for 5 min",null);
		
		when(recipeRepo.save(recipe1)).thenReturn(recipe1);
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonBody = mapper.writeValueAsString(recipe1);
		
		this.mockMvc.perform(post("/recipes/addRecipe").content(jsonBody).contentType(MediaType.APPLICATION_JSON))
		            .andExpect(status().isCreated())
		            .andDo(print());
	}
	
	@Test
	public void test_updateRecipe() throws Exception {
		Recipe recipe1 = new Recipe(2,"maggi masala",true,5,"cook for 5 min",null);
		
		
		int recipeId = 2;
		when(recipeRepo.findById(recipeId)).thenReturn(Optional.of(recipe1));
		when(recipeRepo.save(recipe1)).thenReturn(recipe1);
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonBody = mapper.writeValueAsString(recipe1);
		
		
		
		this.mockMvc.perform(put("/recipes/updateRecipe/{id}",recipeId).content(jsonBody).contentType(MediaType.APPLICATION_JSON))
		            .andExpect(status().isAccepted())
		            .andDo(print());
		
	}	
	
	@Test
	public void test_deleteRecipe() throws Exception{
Recipe recipe1 = new Recipe(2,"maggi masala",true,5,"cook for 5 min",null);
		
		
		int recipeId = 2;
		when(recipeRepo.findById(recipeId)).thenReturn(Optional.of(recipe1));
		
		this.mockMvc.perform(delete("/recipes//deleteRecipe/{recipeId}",recipeId))
		            .andExpect(status().isOk())
		            .andDo(print());
	}
	
	
}
