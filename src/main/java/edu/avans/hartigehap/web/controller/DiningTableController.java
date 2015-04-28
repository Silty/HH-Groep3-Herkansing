package edu.avans.hartigehap.web.controller;

import java.util.Collection;
//import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import edu.avans.hartigehap.domain.*;
import edu.avans.hartigehap.service.*;
import edu.avans.hartigehap.web.form.Message;

import org.springframework.web.servlet.mvc.support.*;

@Controller
public class DiningTableController {

	final Logger logger = LoggerFactory.getLogger(DiningTableController.class);

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private RestaurantService restaurantService;
	@Autowired
	private DiningTableService diningTableService;
	@Autowired
	private MenuItemService menuItemService;

	@RequestMapping(value = "/diningTables/{diningTableId}", method = RequestMethod.GET)
	public String showTable(@PathVariable("diningTableId") String diningTableId, Model uiModel) {
		logger.info("diningTable = " + diningTableId);

		warmupRestaurant(diningTableId, uiModel);

		return "hartigehap/diningtable";
	}
	
	@RequestMapping(value = "/diningTables/{diningTableId}/menuItems", method = RequestMethod.POST)
	public String addMenuItem(
			@PathVariable("diningTableId") String diningTableId,
			@RequestParam String menuItemName, Model uiModel, HttpServletRequest req) {
		
		DiningTable diningTable = diningTableService.fetchWarmedUp(Long.valueOf(diningTableId));
		uiModel.addAttribute("diningTable", diningTable);

		MenuItem choosenMenuItem = diningTableService.findMenuItem(menuItemName);
		
		int i = 0;
		OrderItem orderItem = new OrderItem(choosenMenuItem, 1);
		OrderOption orderOption = null;
		
		// Get all MenuOptions
		//Iterator<MenuItem> menuItems = menuItemService.findAll().iterator();
		Iterator menuItems = createIterator((MenuItem[]) menuItemService.findAll().toArray(new MenuItem[0]));
		while(menuItems.hasNext()) {
			MenuItem menuItem = (MenuItem) menuItems.next();
			if(req.getParameter("quantityOption" + menuItem.getId()) != null) {
				if(Integer.parseInt(req.getParameter("quantityOption" + menuItem.getId())) > 0) {
					
					int quantity = Integer.parseInt(req.getParameter("quantityOption" + menuItem.getId()));
					
					if (i == 0) {
						diningTableService.saveBaseOrderItem(orderItem);
						orderOption = new OrderOption(orderItem, menuItem, quantity);
						diningTableService.saveBaseOrderItem(orderOption);
						i++;
					} else {
						orderOption = new OrderOption(orderOption, menuItem, quantity);
						diningTableService.saveBaseOrderItem(orderOption);
					}
					
					System.out.println("Optie: " + menuItem.getId() + " aantal: " + req.getParameter("quantityOption" + menuItem.getId()));
					//diningTableService.addOrderItemOption(diningTable, menuItemName, menuItem.getId());
				}
			}
		}
		
		if (orderOption != null) {
			diningTableService.addDecoratedOrderItem(diningTable, orderOption);
		} else {
			diningTableService.addOrderItem(diningTable, choosenMenuItem);
		}
		
		//diningTableService.addOrderItem(diningTable, choosenMenuItem);
		
		
		return "redirect:/diningTables/" + diningTableId;
	}
	
	@RequestMapping(value = "/diningTables/{diningTableId}/menuItems/{menuItemName}", method = RequestMethod.DELETE)
	public String deleteMenuItem(
			@PathVariable("diningTableId") String diningTableId,
			@PathVariable("menuItemName") String menuItemName,
			Model uiModel) {

		DiningTable diningTable = diningTableService.fetchWarmedUp(Long.valueOf(diningTableId));
		uiModel.addAttribute("diningTable", diningTable);
		
		diningTableService.deleteOrderItem(diningTable, menuItemName);

		return "redirect:/diningTables/" + diningTableId;
	}

	@RequestMapping(value = "/diningTables/{diningTableId}", method = RequestMethod.PUT)
	public String receiveEvent(
			@PathVariable("diningTableId") String diningTableId, 
			@RequestParam String event, 
			RedirectAttributes redirectAttributes,
			Model uiModel, Locale locale) { 
		
		logger.info("(receiveEvent) diningTable = " + diningTableId);

		
		// because of REST, the "event" parameter is part of the body. It therefore cannot be used for
		// the request mapping so all events for the same resource will be handled by the same
		// controller method; so we end up with an if statement
		
		switch(event) {
		case "submitOrder":
			return submitOrder(diningTableId, redirectAttributes, uiModel, locale);
			// break unreachable

		case "submitBill":	
			return submitBill(diningTableId, redirectAttributes, uiModel, locale);
			// break unreachable
			
		default:	
			warmupRestaurant(diningTableId, uiModel);
			logger.error("internal error: event " + event + "not recognized");
			return "hartigehap/diningtable";			
		}
	}
	
	private String submitOrder(String diningTableId, RedirectAttributes redirectAttributes, Model uiModel, Locale locale) {
		DiningTable diningTable = warmupRestaurant(diningTableId, uiModel);
		try {
			diningTableService.submitOrder(diningTable);
		} catch (StateException e) {
			logger.error("StateException", e);
			uiModel.addAttribute("message", new Message("error",
					messageSource.getMessage("message_submit_order_fail", new Object[]{}, locale)));

			// StateException triggers a rollback; consequently all Entities are invalidated by Hibernate
			// So new warmup needed
			diningTable = warmupRestaurant(diningTableId, uiModel);

			return "hartigehap/diningtable";
		}
		// store the message temporarily in the session to allow displaying after redirect
		redirectAttributes.addFlashAttribute("message", new Message("success", 
				messageSource.getMessage("message_submit_order_success", new Object[]{}, locale))); 
		return "redirect:/diningTables/" + diningTableId;
		
	}
		
	private String submitBill(String diningTableId, RedirectAttributes redirectAttributes, Model uiModel, Locale locale) {
		DiningTable diningTable = warmupRestaurant(diningTableId, uiModel);
		try {
			diningTableService.submitBill(diningTable);
		} catch(EmptyBillException e) {
			logger.error("EmptyBillException", e);
			uiModel.addAttribute("message", new Message("error", messageSource.getMessage("message_submit_empty_bill_fail", new Object[]{}, locale))); 
			return "hartigehap/diningtable";
		} catch(StateException e) {
			logger.error("StateException", e);
			uiModel.addAttribute("message", new Message("error", messageSource.getMessage("message_submit_bill_fail", new Object[]{}, locale))); 

			// StateException triggers a rollback; consequently all Entities are invalidated by Hibernate
			// So new warmup needed
			diningTable = warmupRestaurant(diningTableId, uiModel);

			return "hartigehap/diningtable";
		}
		// store the message temporarily in the session to allow displaying after redirect
		redirectAttributes.addFlashAttribute("message", new Message("success",
				messageSource.getMessage("message_submit_bill_success", new Object[]{}, locale))); 
		return "redirect:/diningTables/" + diningTableId;
	}

	private DiningTable warmupRestaurant(String diningTableId, Model uiModel) {
		Collection<Restaurant> restaurants = restaurantService.findAll();
		uiModel.addAttribute("restaurants", restaurants);
		DiningTable diningTable = diningTableService.fetchWarmedUp(Long.valueOf(diningTableId));
		uiModel.addAttribute("diningTable", diningTable);		
		Restaurant restaurant = restaurantService.fetchWarmedUp(diningTable.getRestaurant().getId());
		uiModel.addAttribute("restaurant", restaurant);
		
		return diningTable;
	}
	
	public Iterator createIterator(MenuItem[] menuItems) {
		return new MenuItemIterator(menuItems);
	}
	
}
