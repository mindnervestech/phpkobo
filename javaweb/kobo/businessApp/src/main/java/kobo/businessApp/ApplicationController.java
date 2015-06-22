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
public class ApplicationController {
	
	@Autowired
    private JdbcTemplate jt;
	
	@Autowired
    private SessionFactory sessionFactory;
	
	public static String KOBOCAT_URL = "http://192.168.1.9:8001";
	
	@RequestMapping(value="case",method=RequestMethod.POST)
	@ResponseBody
	@Transactional
	public LoggerCase createCase(HttpServletRequest httpRequest,@RequestBody String payload) throws JsonParseException, JsonMappingException, IOException {
		
		AuthUser user = getUserFromRequest(httpRequest,sessionFactory);
		if(user != null) {
			ObjectMapper mapper = new ObjectMapper();
			LoggerCase casse = mapper.readValue(payload, LoggerCase.class);
			casse.setOwner(user);
			sessionFactory.getCurrentSession().persist(casse);	
			return casse;
		}
		return null;
	}
	
	@RequestMapping(value="login",method=RequestMethod.POST)
	@ResponseBody
	private String login (HttpServletRequest httpRequest) throws UnirestException {
		String username = httpRequest.getParameter("username");
		String password = httpRequest.getParameter("password");
		HttpResponse<JsonNode> response = Unirest.post(KOBOCAT_URL + "/api/v1/login").
		basicAuth(username, password).asJson();
		JsonNode node = response.getBody();
		HttpSession session = httpRequest.getSession();
		session.setAttribute("user", node);
		return node.toString();
	}
	
	@RequestMapping(value="logout",method=RequestMethod.POST)
	@ResponseBody
	private String logout (HttpServletRequest httpRequest) throws UnirestException {
		HttpSession session = httpRequest.getSession();
		session.invalidate();
		return "logout";
	}

	public static AuthUser getUserFromRequest(HttpServletRequest httpRequest,SessionFactory sessionFactory) {
		
		sessionFactory.getCurrentSession().createCriteria(AuthUser.class)
		.add(Restrictions.eq("username", "?"))
			.createCriteria("authtokenTokens")
			.add(Restrictions.eq("key", "?"))
		.list();	
		final Map map = new HashMap();
		final String authorization = httpRequest.getHeader("Authorization");
		if (authorization != null && authorization.startsWith("Basic")) {
			String base64Credentials = authorization.substring("Basic".length()).trim();
			String credentials = new String(Base64.decode(base64Credentials),
	                Charset.forName("UTF-8"));
			final String[] values = credentials.split(":",2);
			
			
			return (AuthUser)sessionFactory.getCurrentSession().createCriteria(AuthUser.class)
			.add(Restrictions.eq("username", values[0]))
				.createCriteria("authtokenTokens")
				.add(Restrictions.eq("key", values[1]))
			.uniqueResult();
			
			/*return jt.queryForObject("Select auth_user.* from auth_user,authtoken_token where username = ? and key = ? and authtoken_token.user_id = auth_user.id",new Object[] {values[0],values[1]}, 
					new RowMapper<Map>() {
                           
						public Map mapRow(ResultSet rs, int rowNum)
								throws SQLException {
							map.put("first_name", rs.getString("first_name"));
							map.put("last_name", rs.getString("last_name"));
							map.put("username", rs.getString("username"));
							map.put("email", rs.getString("email"));
							map.put("id", rs.getInt("id"));
							return map;
						}
				        
					});*/
		}
		
		return null;
	}
	
	@RequestMapping(value="case",method=RequestMethod.GET)
	@ResponseBody
	@Transactional(readOnly=true)
	public List allOwnedUserCases(HttpServletRequest httpRequest) {
		AuthUser user = getUserFromRequest(httpRequest,sessionFactory);
		
		List cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
		.add(Restrictions.eq("owner", user))
		.list();
		return cases;
	}
	
	
	
	
	

}
