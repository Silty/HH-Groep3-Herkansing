package edu.avans.hartigehap.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import edu.avans.hartigehap.domain.MenuItem;
import edu.avans.hartigehap.repository.MenuItemRepository;
import edu.avans.hartigehap.service.MenuItemService;

@Service("menuItemService")
public class MenuItemServiceImpl implements MenuItemService {

	@Autowired
	private MenuItemRepository menuItemRepository;
	
	@Transactional(readOnly=true)
	public List<MenuItem> findAll() {
		return Lists.newArrayList(menuItemRepository.findAll());
	}
}
