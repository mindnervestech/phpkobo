package kobo.businessApp;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kobo.entities.AuthUser;
import kobo.entities.LoggerCase;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.postgresql.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Controller
public class ConsultantController {
	
	@Autowired
    private JdbcTemplate jt;
	
	@Autowired
    private SessionFactory sessionFactory;
	
	
	@RequestMapping(value="mycases",method=RequestMethod.GET)
	@ResponseBody
	@Transactional(readOnly=true)
	public List myCases(HttpServletRequest httpRequest) {
		AuthUser user = ApplicationController.getUserFromRequest(httpRequest,sessionFactory);
		
		List cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
		.add(Restrictions.eq("consultant", user))
		.list();
		return cases;
	}
	
	@RequestMapping(value="assign/cases/{caseId}/user/{userId}",method=RequestMethod.PUT)
	@ResponseBody
	@Transactional(readOnly=true)
	public List assignCaseTo(HttpServletRequest httpRequest) {
		AuthUser user = ApplicationController.getUserFromRequest(httpRequest,sessionFactory);
		
		
		return null;
	}
	
	@RequestMapping(value="update/cases/{caseId}/status/{status}",method=RequestMethod.PUT)
	@ResponseBody
	@Transactional(readOnly=true)
	public List updateCaseStatus(HttpServletRequest httpRequest) {
		AuthUser user = ApplicationController.getUserFromRequest(httpRequest,sessionFactory);
		
		
		return null;
	}
	
	
	
	
	

}
