package com.lambdaschool.javacountries.controllers;


import com.lambdaschool.javacountries.models.Country;
import com.lambdaschool.javacountries.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CountryController
{
	@Autowired
	CountryRepository countryRepos;
	
	private List<Country> findCountriesByFirstLetter(List<Country> cList, CheckCountry tester)
	{
		List<Country> tmpList = new ArrayList<>();
		for (Country c : cList)
		{
			if (tester.test(c))
			{
				tmpList.add(c);
			}
		}
		return tmpList;
	}
	
	// /names/all
	@GetMapping(value = "/names/all", produces = "application/json")
	public ResponseEntity<?> getAllCountries()
	{
		List<Country> cList = new ArrayList<>();
		countryRepos.findAll().iterator().forEachRemaining(cList::add);
		cList.sort((c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()));
		return new ResponseEntity<>(cList, HttpStatus.OK);
	}
	
	// /names/start/u
	@GetMapping(value = "/names/start/{letter}", produces = "application/json")
	public ResponseEntity<?> getCountriesByName(@PathVariable char letter)
	{
		List<Country> cList = new ArrayList<>();
		countryRepos.findAll().iterator().forEachRemaining(cList::add);
		List<Country> retList = findCountriesByFirstLetter(cList,
				(c) -> c.getName().toLowerCase().charAt(0) == letter);
		return new ResponseEntity<>(retList, HttpStatus.OK);
	}
	
	// /population/total
	@GetMapping(value = "/population/total", produces = "application/json")
	public ResponseEntity<?> getPopulationTotal()
	{
		List<Country> cList = new ArrayList<>();
		countryRepos.findAll().iterator().forEachRemaining(cList::add);
		
		long total = 0;
		for (Country c : cList)
		{
			total += c.getPopulation();
		}
		
		return new ResponseEntity<>(total, HttpStatus.OK);
	}
	
	// /population/min
	@GetMapping(value = "/population/min", produces = "application/json")
	public ResponseEntity<?> getPopulationMin()
	{
		List<Country> cList = new ArrayList<>();
		countryRepos.findAll().iterator().forEachRemaining(cList::add);
		
		Country returnCountry = new Country();
		for (Country c : cList)
		{
			if (returnCountry.getName() == null || returnCountry.getPopulation() > c.getPopulation())
			{
				returnCountry = c;
			}
		}
		return new ResponseEntity<>(returnCountry, HttpStatus.OK);
	}
	
	// /population/max
	@GetMapping(value = "/population/max", produces = "application/json")
	public ResponseEntity<?> getPopulationMax()
	{
		List<Country> cList = new ArrayList<>();
		countryRepos.findAll().iterator().forEachRemaining(cList::add);
		
		Country returnCountry = new Country();
		for (Country c : cList)
		{
			if (returnCountry.getName() == null || returnCountry.getPopulation() < c.getPopulation())
			{
				returnCountry = c;
			}
		}
		return new ResponseEntity<>(returnCountry, HttpStatus.OK);
	}
	
	// Stretch - /population/median
	@GetMapping(value = "/population/median", produces = "application/json")
	public ResponseEntity<?> getPopulationMedian()
	{
		List<Country> cList = new ArrayList<>();
		countryRepos.findAll().iterator().forEachRemaining(cList::add);
		
		Country country = new Country();
		cList.sort((c1, c2) -> (int)(c1.getPopulation() - c2.getPopulation()));
		if (cList.size() % 2 == 1)
		{
			return new ResponseEntity<>(cList.get((cList.size() - 1) / 2), HttpStatus.OK);
		}
		return new ResponseEntity<>(country, HttpStatus.OK);
	}
}
