package kobo.businessApp;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kobo.entities.AuthGroup;
import kobo.entities.AuthUser;
import kobo.entities.AuthUserGroup;
import kobo.entities.LoggerCase;
import kobo.entities.Sector;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.json.JSONObject;
import org.postgresql.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	//public static String KOBOCAT_URL = "http://li855-46.members.linode.com:8001";
	public static String KOBOCAT_URL = "http://192.168.2.11:8001";
	
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
	@Transactional
	private String login (HttpServletRequest httpRequest) throws UnirestException {
		//sessionFactory.getCurrentSession().createQuery("FROM Sector").list();
		String username = httpRequest.getParameter("username");
		String password = httpRequest.getParameter("password");
		HttpResponse<JsonNode> response = Unirest.post(KOBOCAT_URL + "/api/v1/login").
		basicAuth(username, password).asJson();
		JsonNode node = response.getBody();
		System.out.println("node==="+node);
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
		JsonNode node = (JsonNode)httpRequest.getSession().getAttribute("user");
		node.getObject().getJSONArray("groups").get(0);
		Integer idd = node.getObject().getInt("id");
		System.out.println("my idd =="+idd);
		List cases = null;
		if(node.getObject().getJSONArray("groups").get(0).equals("consultant")){
			cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
					.add(Restrictions.eq("consultant.id", idd))
					.list();
		}else{
			System.out.println("admin");
			 cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
					 //.add(Restrictions.eq("owner", idd))
					.list();
		}
		
		System.out.println("cases --==="+cases.size());
		return cases;
	}
	
	
	@RequestMapping(value="getConsultant",method=RequestMethod.GET)
	@ResponseBody
	@Transactional(readOnly=true)
	public List<AuthUser> getConsultant(HttpServletRequest httpRequest) {
		JsonNode node = (JsonNode)httpRequest.getSession().getAttribute("user");
		node.getObject().getJSONArray("groups").get(0);
		Integer idd = node.getObject().getInt("id");
		System.out.println("my idd =="+idd);
		List<AuthUser> auth = new ArrayList<AuthUser>();
		AuthGroup grpid = (AuthGroup) sessionFactory.getCurrentSession().createCriteria(AuthGroup.class)
				.add(Restrictions.eq("name", "consultant"))
				.uniqueResult();
		System.out.println("grp id==="+grpid.getId());
		List<AuthUserGroup> consulnt = sessionFactory.getCurrentSession().createCriteria(AuthUserGroup.class)
					.add(Restrictions.eq("authGroup.id", grpid.getId()))
					.list();
		for(AuthUserGroup aa:consulnt){
			AuthUser auth1 = (AuthUser) sessionFactory.getCurrentSession().createCriteria(AuthUser.class)
					.add(Restrictions.eq("id", aa.getAuthUser().getId()))
					.uniqueResult();
			auth.add(auth1);
		}
		
		System.out.println("cases --==="+auth.size());
		return auth;
		
	}
	
	@RequestMapping(value="assignConsultant/{id}",method=RequestMethod.GET)
	@ResponseBody
	@Transactional(readOnly=true)
	public List<AuthUser> assignConsultant(HttpServletRequest httpRequest,@PathVariable Long caseid) {
		System.out.println("case idddd 11=="+caseid);
		return null;
		
	}

}
