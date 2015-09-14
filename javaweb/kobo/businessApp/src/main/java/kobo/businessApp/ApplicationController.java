package kobo.businessApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kobo.entities.AuthGroup;
import kobo.entities.AuthUser;
import kobo.entities.AuthUserGroup;
import kobo.entities.LoggerCase;
import kobo.entities.LoggerCaseInstance;
import kobo.entities.Sector;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.json.JSONObject;
import org.postgresql.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	public static String KOBOCAT_URL = "http://li855-46.members.linode.com:8001";
	//public static String KOBOCAT_URL = "http://192.168.2.11:8001";
	
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
		//System.out.println("node==="+node);
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
		//System.out.println(user);
		List cases = null;
		
		if(node != null) {
			node.getObject().getJSONArray("groups").get(0);
			Integer idd = node.getObject().getInt("id");
			//System.out.println("my idd =="+idd);
			if(node.getObject().getJSONArray("groups").get(0).equals("consultant")){
				cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
						.add(Restrictions.eq("consultant.id", idd)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.list();
			}else{
				//System.out.println("admin");
				 cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						 //.add(Restrictions.eq("owner", idd))
						.list();
			}
		}
		
		if(user != null){
			List<AuthUserGroup> authUserGroups = user.getAuthUserGroups();
			for(AuthUserGroup group : authUserGroups) {
				if(group.getAuthGroup().getName().equalsIgnoreCase("consultant")){
					cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
							.add(Restrictions.eq("consultant.id", user.getId())).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
							.list();
					
					break;
				}
			
			}
            
			if(cases == null) {
				//System.out.println("admin");
				 cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
						 .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						 //.add(Restrictions.eq("owner", idd))
						.list();
			}
		}
		
		//System.out.println("cases --==="+cases.size());
		return cases;
	}
	
	@RequestMapping(value="dashBoardcases",method=RequestMethod.GET)
	@ResponseBody
	@Transactional(readOnly=true)
	public List dashBoardcases(HttpServletRequest httpRequest, @RequestParam("start") @DateTimeFormat(pattern = "MMddyyyy") Date start , @RequestParam("end") @DateTimeFormat(pattern = "MMddyyyy") Date end, @RequestParam("status") String status) {
		AuthUser user = getUserFromRequest(httpRequest,sessionFactory);
		JsonNode node = (JsonNode)httpRequest.getSession().getAttribute("user");
		//System.out.println(user);
		List cases = null;
		
		//System.out.println("start =="+start);
		end.setHours(23);
		end.setMinutes(59);
		end.setSeconds(59);
		//System.out.println("end =="+end);
		
		if(node != null) {
			node.getObject().getJSONArray("groups").get(0);
			Integer idd = node.getObject().getInt("id");
			//System.out.println("my idd =="+idd);
			if(node.getObject().getJSONArray("groups").get(0).equals("consultant")){
				cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
						.add(Restrictions.between("dateCreated",start, end))
						.add(Restrictions.eq("consultant.id", idd)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.list();
			}else{
				System.out.println("admin");
				System.out.println(status);
				if(status.equals("all")){
					 cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
							 .add(Restrictions.between("dateCreated",start, end))
							 .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
							 //.add(Restrictions.eq("owner", idd))
							.list();
				}else if(status.equals("open")){
					cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
							 .add(Restrictions.between("dateCreated",start, end)).add(Restrictions.isNull("status"))
							 .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
							.list();
				}else{
					 cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
							 .add(Restrictions.between("dateCreated",start, end)).add(Restrictions.eq("status", status))
							 .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
							.list();
				}
			}
		}
		
		if(user != null){
			List<AuthUserGroup> authUserGroups = user.getAuthUserGroups();
			for(AuthUserGroup group : authUserGroups) {
				if(group.getAuthGroup().getName().equalsIgnoreCase("consultant")){
					cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
							.add(Restrictions.eq("consultant.id", user.getId())).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
							.list();
					
					break;
				}
			
			}
            
			if(cases == null) {
				//System.out.println("admin");
				 cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
						 .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						 //.add(Restrictions.eq("owner", idd))
						.list();
			}
		}
		
		//System.out.println("cases --==="+cases.size());
		return cases;
	}
	
	@RequestMapping(value="caseInformationById/{caseId}",method=RequestMethod.GET)
	@ResponseBody
	@Transactional(readOnly=true)
	public LoggerCase caseInformationById(HttpServletRequest httpRequest, @PathVariable Integer caseId) {
		
		LoggerCase cases = null;
		
		cases = (LoggerCase) sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
					.add(Restrictions.eq("id", caseId)).uniqueResult();
		
		
		//System.out.println("case --==="+cases);
		return cases;
	}
	
	@RequestMapping(value="getConsultant",method=RequestMethod.GET)
	@ResponseBody
	@Transactional(readOnly=true)
	public List<AuthUser> getConsultant(HttpServletRequest httpRequest) {
		JsonNode node = (JsonNode)httpRequest.getSession().getAttribute("user");
		node.getObject().getJSONArray("groups").get(0);
		Integer idd = node.getObject().getInt("id");
		//System.out.println("my idd =="+idd);
		List<AuthUser> auth = new ArrayList<AuthUser>();
		AuthGroup grpid = (AuthGroup) sessionFactory.getCurrentSession().createCriteria(AuthGroup.class)
				.add(Restrictions.eq("name", "consultant"))
				.uniqueResult();
		//System.out.println("grp id==="+grpid.getId());
		List<AuthUserGroup> consulnt = sessionFactory.getCurrentSession().createCriteria(AuthUserGroup.class)
					.add(Restrictions.eq("authGroup.id", grpid.getId()))
					.list();
		for(AuthUserGroup aa:consulnt){
			AuthUser auth1 = (AuthUser) sessionFactory.getCurrentSession().createCriteria(AuthUser.class)
					.add(Restrictions.eq("id", aa.getAuthUser().getId()))
					.uniqueResult();
			auth.add(auth1);
		}
		
		//System.out.println("cases --==="+auth.size());
		return auth;
		
	}
	
	@RequestMapping(value="assignConsultant/{id}",method=RequestMethod.GET)
	@ResponseBody
	@Transactional(readOnly=true)
	public List<AuthUser> assignConsultant(HttpServletRequest httpRequest,@PathVariable Long caseid) {
		//System.out.println("case idddd 11=="+caseid);
		return null;
		
	}

	@RequestMapping(value="exportToExcel", method=RequestMethod.GET)
	@ResponseBody
	@Transactional
	public FileSystemResource exportToExcel(HttpServletRequest httpRequest,HttpServletResponse response, @RequestParam ("status") String status, @RequestParam ("consult") Integer consult,
			@RequestParam ("sangini") Integer sangini) {
		//System.out.println("sangini =="+sangini);
		
		//System.out.println("consult =="+consult);
		
		//System.out.println("status =="+status);
		
		List<LoggerCase> cases = null;		
		if(sangini != 0 && consult != 0 && !status.equals("0")){
			if(status.equals("open")){
				cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
						.add(Restrictions.and(Restrictions.eq("owner.id", sangini), Restrictions.and(Restrictions.eq("consultant.id", consult), Restrictions.isNull("status"))))
						.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.list();
			}else{
				cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
						.add(Restrictions.and(Restrictions.eq("owner.id", sangini), Restrictions.and(Restrictions.eq("consultant.id", consult), Restrictions.eq("status", status))))
						.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.list();
			}
		}else if(sangini != 0 && consult != 0){
			//allCases = " where user_id = '"+sangini+"' and consultant_id = '"+consult+"'";
			cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
					.add(Restrictions.and(Restrictions.eq("owner.id", sangini), Restrictions.eq("consultant.id", consult)))
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
					.list();
		}else if(sangini != 0 && !status.equals("0")){
			//allCases = " where status = '"+status+"'";
			if(status.equals("open")){
				cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
						.add(Restrictions.and(Restrictions.eq("owner.id", sangini), Restrictions.isNull("status")))
						.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.list();
			}else{
				cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
						.add(Restrictions.and(Restrictions.eq("owner.id", sangini), Restrictions.eq("status", status)))
						.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.list();
			}
		}else if(consult != 0 && !status.equals("0")){
			//allCases = " where status = '"+status+"'";
			if(status.equals("open")){
				cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
						.add(Restrictions.and(Restrictions.eq("consultant.id", consult), Restrictions.isNull("status")))
						.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.list();
			}else{
				cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
						.add(Restrictions.and(Restrictions.eq("consultant.id", consult), Restrictions.eq("status", status)))
						.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.list();
			}
		}else if(sangini != 0 ){
			//allCases = " where user_id = '"+sangini+"'";
			cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
					.add(Restrictions.eq("owner.id", sangini))
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
					.list();
		}else if(consult != 0){
			//allCases = " where user_id = '"+sangini+"' and consultant_id = '"+consult+"'";
			cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
					.add(Restrictions.eq("consultant.id", consult))
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
					.list();
		}else if(!status.equals("0")){
			//allCases = " where status = '"+status+"'";
			if(status.equals("open")){
				cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
						.add(Restrictions.isNull("status"))
						.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.list();
			}else{
				cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
						.add(Restrictions.eq("status", status))
						.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.list();
			}
		}else{
			cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();	
		}
		
		
		List<String> questions = new ArrayList();
		Boolean flag = true;
		Map<Map<String,String>, Map<String, String>> mainMap = new HashMap<Map<String,String>, Map<String,String>>();
		if(cases.size() != 0){
			//System.out.println("cases.size()" + cases.size());
			for(LoggerCase tempCase : cases){
				if(!tempCase.getLoggerCaseInstances().isEmpty()){
					Map<String,String> caseMap = new HashMap<String, String>();
					for(LoggerCaseInstance instance : tempCase.getLoggerCaseInstances()){
						JSONObject jObject = new JSONObject(instance.getLoggerInstance().getJson().trim());
						Iterator<?> keys = jObject.keys();
						while( keys.hasNext() ) {
						    String key = (String)keys.next();
						    if(!key.equals("meta/instanceID") && !key.equals("_xform_id_string") && !key.equals("start") && !key.equals("formhub/uuid") && !key.equals("end")){
						    	//if(flag){
							    	//System.out.println(key);
							    	Iterator<String> s = questions.iterator();
							    	Boolean questionFlag = true;
							    	while(s.hasNext()){
							    		if(s.next().equalsIgnoreCase(key)){
							    			questionFlag = false;
							    		}
							    	}
							    	if(questionFlag){
									    questions.add(key);
							    	}
								//}
								caseMap.put(key, (String) jObject.get(key));
						    }
						}
					}
				flag = false;
				Map<String,String> idMap = new HashMap<String, String>();
				idMap.put(tempCase.getId().toString(), tempCase.getCaseId());
				mainMap.put(idMap, caseMap);
				}else{
					Map<String,String> caseMap = new HashMap<String, String>();
					Map<String,String> idMap = new HashMap<String, String>();
					idMap.put(tempCase.getId().toString(), tempCase.getCaseId());
					mainMap.put(idMap, caseMap);
				}
			}
		}
	    //System.out.println("mainMap size" + mainMap.size());
	    String filename = "E:/NewExcelFile.xls" ;
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("FirstSheet");  

        HSSFRow rowhead = sheet.createRow((short)0);
        int i = 1;
    	rowhead.createCell(0).setCellValue("Case Id");
        for(String question : questions){
        	rowhead.createCell(i).setCellValue(question.replaceAll("_", " "));
        	i++;
        }

        int j = 1;
        for (Map.Entry<Map<String,String>, Map<String, String>> entry : mainMap.entrySet()) {
            HSSFRow row = sheet.createRow((short)j);
    		//System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
            for (Entry<String, String> keyEntry : entry.getKey().entrySet()) {
        		//System.out.println("Key : " + keyEntry.getKey() + " Value : " + keyEntry.getValue());
                row.createCell(0).setCellValue(keyEntry.getValue().replaceAll("_", " "));
            }
            for (Entry<String, String> valueEntry : entry.getValue().entrySet()) {
        		//System.out.println("Key : " + valueEntry.getKey() + " Value : " + valueEntry.getValue() + "que Index" + questions.indexOf(valueEntry.getKey()));
                row.createCell(questions.indexOf(valueEntry.getKey()) + 1).setCellValue(valueEntry.getValue().replaceAll("_", " "));
            }
            j++;
    	}
        
    	int d = 0;
        for(String question : questions){
        	sheet.autoSizeColumn(d);
        	d++;
        }
        
        try {
        	FileOutputStream fileOut = new FileOutputStream(filename);
        	workbook.write(fileOut);
        	fileOut.close();
        } catch(IOException e) {
        	
        }
        File f = new File("E:/NewExcelFile.xls");
        System.out.println("Your excel file has been generated!");
        response.setContentType("apphlication/xls");
        response.setHeader("Content-Disposition", "attachment;filename=CaseList.xls"); 
	    return new FileSystemResource(f);
	}
	
}
