package edu.avans.hartigehap.service.impl;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.avans.hartigehap.domain.*;
import edu.avans.hartigehap.domain.MenuItem.MenuType;
import edu.avans.hartigehap.repository.*;
import edu.avans.hartigehap.service.*;

import org.joda.time.DateTime;

@Service("restaurantPopulatorService")
@Repository
@Transactional
public class RestaurantPopulatorServiceImpl implements RestaurantPopulatorService {
	final Logger logger = LoggerFactory.getLogger(RestaurantPopulatorServiceImpl.class);
	
	@Autowired
	private RestaurantRepository restaurantRepository;
	@Autowired
	private FoodCategoryRepository foodCategoryRepository;
	@Autowired
	private MenuItemRepository menuItemRepository;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private BaseOrderItemRepository baseOrderItemRepository;
	
	private List<Meal> meals = new ArrayList<Meal>();
	private List<MealOption> mealOptions = new ArrayList<MealOption>();
	private List<FoodCategory> foodCats = new ArrayList<FoodCategory>();
	private List<Drink> drinks = new ArrayList<Drink>();
	private List<Customer> customers = new ArrayList<Customer>();

		
	/**
	 *  menu items, food categories and customers are common to all restaurants and should be created only once.
	 *  Although we can safely assume that the are related to at least one restaurant and therefore are saved via
	 *  the restaurant, we save them explicitly anyway
	 */
	private void createCommonEntities() {
		
		// Create Food Categories
		createFoodCategory("low fat");
		createFoodCategory("high energy");
		createFoodCategory("vegatarian");
		createFoodCategory("italian");
		createFoodCategory("pizza's");
		createFoodCategory("alcoholic drinks");
		createFoodCategory("energizing drinks");
		
		// Create Meal options
		createMealOption("bell pepper", "pizza.jpg", 2, MenuType.MealOption, "easy",
				Arrays.<FoodCategory>asList(new FoodCategory[]{foodCats.get(4)}));
		createMealOption("mushrooms", "pizza.jpg", 3, MenuType.MealOption, "easy",
				Arrays.<FoodCategory>asList(new FoodCategory[]{foodCats.get(4)}));
		createMealOption("mozzarella", "pizza.jpg", 1, MenuType.MealOption, "easy",
				Arrays.<FoodCategory>asList(new FoodCategory[]{foodCats.get(4)}));
		createMealOption("shrimps", "pizza.jpg", 5, MenuType.MealOption, "easy",
				Arrays.<FoodCategory>asList(new FoodCategory[]{foodCats.get(4)}));
		createMealOption("cream cheese", "pizza.jpg", 5, MenuType.MealOption, "easy",
				Arrays.<FoodCategory>asList(new FoodCategory[]{foodCats.get(4)}));
		
		
		// Create Meals
		createMeal("spaghetti", "spaghetti.jpg", 8, MenuType.Meal, "easy",
			Arrays.<FoodCategory>asList(new FoodCategory[]{foodCats.get(3), foodCats.get(1)}));
		createMeal("macaroni", "macaroni.jpg", 8, MenuType.Meal, "easy",
			Arrays.<FoodCategory>asList(new FoodCategory[]{foodCats.get(3), foodCats.get(1)}));		
		createMeal("canneloni", "canneloni.jpg", 9, MenuType.Meal, "easy",
			Arrays.<FoodCategory>asList(new FoodCategory[]{foodCats.get(3), foodCats.get(1)}));
		createMeal("pizza shoarma", "pizza.jpg", 9, MenuType.Meal, "easy",
			Arrays.<FoodCategory>asList(new FoodCategory[]{foodCats.get(4)}),
			Arrays.<MealOption>asList(new MealOption[]{mealOptions.get(0), mealOptions.get(1)}));
		createMeal("pizza margarita", "pizza.jpg", 9, MenuType.Meal, "easy",
				Arrays.<FoodCategory>asList(new FoodCategory[]{foodCats.get(4)}),
				Arrays.<MealOption>asList(new MealOption[]{mealOptions.get(0), mealOptions.get(1)}));
		createMeal("pizza hawai", "pizza.jpg", 9, MenuType.Meal, "easy",
				Arrays.<FoodCategory>asList(new FoodCategory[]{foodCats.get(4)}),
				Arrays.<MealOption>asList(new MealOption[]{mealOptions.get(1), mealOptions.get(2)}));
		createMeal("pizza salami", "pizza.jpg", 9, MenuType.Meal, "easy",
				Arrays.<FoodCategory>asList(new FoodCategory[]{foodCats.get(4)}),
				Arrays.<MealOption>asList(new MealOption[]{mealOptions.get(2), mealOptions.get(3)}));
		createMeal("pizza durum", "pizza.jpg", 9, MenuType.Meal, "easy",
				Arrays.<FoodCategory>asList(new FoodCategory[]{foodCats.get(4)}),
				Arrays.<MealOption>asList(new MealOption[]{mealOptions.get(3), mealOptions.get(4)}));
		createMeal("carpaccio", "carpaccio.jpg", 7, MenuType.Meal, "easy",
			Arrays.<FoodCategory>asList(new FoodCategory[]{foodCats.get(3), foodCats.get(0)}));
		createMeal("ravioli", "ravioli.jpg", 8, MenuType.Meal, "easy",
			Arrays.<FoodCategory>asList(new FoodCategory[]{foodCats.get(3), foodCats.get(1), foodCats.get(2)}));

		// Create Drinks
		createDrink("beer", "beer.jpg", 1, MenuType.Drink, Drink.Size.LARGE,
			Arrays.<FoodCategory>asList(new FoodCategory[]{foodCats.get(5)}));
		createDrink("coffee", "coffee.jpg", 1, MenuType.Drink, Drink.Size.MEDIUM,
			Arrays.<FoodCategory>asList(new FoodCategory[]{foodCats.get(6)}));
		
		// Create customers
		byte[] photo = new byte[]{127,-128,0};
		createCustomer("piet", "bakker", new DateTime(), 1, "description", photo);
		createCustomer("piet", "bakker", new DateTime(), 1, "description", photo);
		createCustomer("piet", "bakker", new DateTime(), 1, "description", photo);

	}

	private void createFoodCategory(String tag) {
		FoodCategory foodCategory = new FoodCategory(tag);
		foodCategory = foodCategoryRepository.save(foodCategory);
		foodCats.add(foodCategory);
	}
	
	private void createMeal(String name, String image, int price, MenuType type, String recipe, List<FoodCategory> foodCats) {
		Meal meal = new Meal(name, image, price, type, recipe);
		// as there is no cascading between FoodCategory and MenuItem (both ways), it is important to first 
		// save foodCategory and menuItem before relating them to each other, otherwise you get errors
		// like "object references an unsaved transient instance - save the transient instance before flushing:"
		meal.addFoodCategories(foodCats);
		meal = menuItemRepository.save(meal);
		meals.add(meal);
	}
	
	private void createMeal(String name, String image, int price, MenuType type, String recipe, List<FoodCategory> foodCats, List<MealOption> mealOptions) {
		Meal meal = new Meal(name, image, price, type, recipe);
		// as there is no cascading between FoodCategory and MenuItem (both ways), it is important to first 
		// save foodCategory and menuItem before relating them to each other, otherwise you get errors
		// like "object references an unsaved transient instance - save the transient instance before flushing:"
		meal.addFoodCategories(foodCats);
		meal.addMealOptions(mealOptions);
		meal = menuItemRepository.save(meal);
		meals.add(meal);
	}
	
	private void createMealOption(String name, String image, int price, MenuType type, String recipe, List<FoodCategory> foodCats) {
		MealOption mealOption = new MealOption(name, image, price, type, recipe);
		// as there is no cascading between FoodCategory and MenuItem (both ways), it is important to first 
		// save foodCategory and menuItem before relating them to each other, otherwise you get errors
		// like "object references an unsaved transient instance - save the transient instance before flushing:"
		mealOption.addFoodCategories(foodCats);
		mealOption = menuItemRepository.save(mealOption);
		mealOptions.add(mealOption);
	}
	
	private void createDrink(String name, String image, int price, MenuType type, Drink.Size size, List<FoodCategory> foodCats) {
		Drink drink = new Drink(name, image, price, type, size);
		drink = menuItemRepository.save(drink);
		drink.addFoodCategories(foodCats);
		drinks.add(drink);
	}
	
	private void createCustomer(String firstName, String lastName, DateTime birthDate,
		int partySize, String description, byte[] photo) {
		Customer customer = new Customer(firstName, lastName, birthDate, partySize, description, photo); 
		customers.add(customer);
		customerRepository.save(customer);
	}
	
	private void createDiningTables(int numberOfTables, Restaurant restaurant) {
		for(int i=0; i<numberOfTables; i++) {
			DiningTable diningTable = new DiningTable(i+1);
			diningTable.setRestaurant(restaurant);
			restaurant.getDiningTables().add(diningTable);
		}
	}
	
	private Restaurant populateRestaurant(Restaurant restaurant) {
				
		// will save everything that is reachable by cascading
		// even if it is linked to the restaurant after the save
		// operation
		restaurant = restaurantRepository.save(restaurant);

		// every restaurant has its own dining tables
		createDiningTables(5, restaurant);

		// for the moment every restaurant has all available food categories 
		for(FoodCategory foodCat : foodCats) {
			restaurant.getMenu().getFoodCategories().add(foodCat);
		}

		// for the moment every restaurant has the same menu 
		for(Meal meal : meals) {
			restaurant.getMenu().getMeals().add(meal);
		}
		
		// for the moment every restaurant has the same meal options 
		for(MealOption mealOption : mealOptions) {
			restaurant.getMenu().getMealOptions().add(mealOption);
		}

		// for the moment every restaurant has the same menu 
		for(Drink drink : drinks) {
			restaurant.getMenu().getDrinks().add(drink);
		}
		
		// for the moment, every customer has dined in every restaurant
		// no cascading between customer and restaurant; therefore both restaurant and customer
		// must have been saved before linking them one to another
		for(Customer customer : customers) {
			customer.getRestaurants().add(restaurant);
			restaurant.getCustomers().add(customer);
		}
		
		return restaurant;
		
	}

	
	public void createRestaurantsWithInventory() {
		
		createCommonEntities();

		Restaurant restaurant = new Restaurant(HARTIGEHAP_RESTAURANT_NAME, "deHartigeHap.jpg");
		restaurant = populateRestaurant(restaurant);
		
		restaurant = new Restaurant(PITTIGEPANNEKOEK_RESTAURANT_NAME, "dePittigePannekoek.jpg");
		restaurant = populateRestaurant(restaurant);
		
		restaurant = new Restaurant(HMMMBURGER_RESTAURANT_NAME, "deHmmmBurger.jpg");
		restaurant = populateRestaurant(restaurant);
	}	
}
