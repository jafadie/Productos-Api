package com.valorprofs;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin
@RestController
public class ProductController {
	private static final int ADMINISTRATOR = 1;
	private static final int USER = 2;
	
	private Map<Long, Product> products = new ConcurrentHashMap<>();
	
	private Long idUsado = (long) 0;
	
	
	@RequestMapping(value = "/products", method = RequestMethod.GET)
	public Collection<Product> getAllProducts() {
		return products.values();
	}
	
	@RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
	public ResponseEntity<Product> getProduct(@PathVariable long id) {
		Product p = products.get(id);
 
		if (p != null) 
			return new ResponseEntity<>(p, HttpStatus.OK);
 
	    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
 
	@RequestMapping(value = "/products", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public Product addProduct(@RequestBody Product p) {
		idUsado++;
		p.setId(idUsado);
		products.put(idUsado, p);
 
		return p;
	}
	
	@RequestMapping(value = "/products/{id}", method = RequestMethod.PUT) 
	public @ResponseBody Product updateProduct(@PathVariable("id") long id, @RequestBody Product product) {
		Product p = products.get(id);
		
		if (p != null)
		{
			product.setId(id);
			products.put(product.getId(), product);
		}
		
        return product;
	}
	
	@RequestMapping(value = "products/{id}", method = RequestMethod.DELETE)
	public HttpStatus delete(@PathVariable("id") long id) {
		Product p = products.get(id);
		
		if (p != null)
			products.remove(id);

	    return HttpStatus.OK;
	}
	
	@RequestMapping(value = "/products/authenticate", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public HttpStatus authenticate(@RequestBody User u) {
		
		if ((u.getLogin().equalsIgnoreCase("admin@admin.com") && u.getPass().equalsIgnoreCase("admin123"))
				|| (u.getLogin().equalsIgnoreCase("user@user.com") && u.getPass().equalsIgnoreCase("admin123")))
		{
			return HttpStatus.OK;
		}
		return HttpStatus.NOT_FOUND;
	}
	
	
	@RequestMapping(value = "/products/authorize", method = RequestMethod.POST)
	public ResponseEntity<Integer> authorize(@RequestBody User u) {
		
		if (u.getLogin().equalsIgnoreCase("admin@admin.com"))
		{
			return new ResponseEntity<>(ADMINISTRATOR, HttpStatus.OK);
		}
		else if (u.getLogin().equalsIgnoreCase("user@user.com"))
		{
			return new ResponseEntity<>(USER, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	
}
