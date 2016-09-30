package kobo.businessApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kobo.entities.AuthGroup;
import kobo.entities.AuthUser;
import kobo.entities.AuthUserGroup;
import kobo.entities.LoggerCase;
import kobo.entities.LoggerCaseInstance;
import kobo.entities.LoggerXform;
import kobo.entities.Sector;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.json.JSONArray;
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
	
	@Resource(name = "myProps")
	private Properties myProps;
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
		}else{
			System.out.println("admin");
			 cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
					.list();
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
	
	@RequestMapping(value="filteredCase",method=RequestMethod.GET)
	@ResponseBody
	@Transactional(readOnly=true)
	public List allOwnedUserFilteredCases(HttpServletRequest httpRequest,@RequestParam("status") String status) {
		
		Criterion statusCri = null;
		
		if(status.equals("Open")) {
			statusCri = Restrictions.and(Restrictions.ne("status", "Closed"),
					Restrictions.ne("status", "Complete")); 
		}else {
			statusCri = Restrictions.eq("status", status);
		}
		
		
		AuthUser user = getUserFromRequest(httpRequest,sessionFactory);
		JsonNode node = (JsonNode)httpRequest.getSession().getAttribute("user");
		//System.out.println(user);
		List<LoggerCase> cases = null;
		
		if(node != null) {
			System.out.println("node not null");
			node.getObject().getJSONArray("groups").get(0);
			Integer idd = node.getObject().getInt("id");
			//System.out.println("my idd =="+idd);
			if(node.getObject().getJSONArray("groups").get(0).equals("consultant")){
				cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
						.add(statusCri)
						.add(Restrictions.eq("consultant.id", idd)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.list();
				for(LoggerCase c : cases) {
					c.setOwner_role(c.getOwner().getAuthUserGroups().get(0).getAuthGroup().getName());
				}
				
			}else{
				//System.out.println("admin");
				 cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						 //.add(Restrictions.eq("owner", idd))
						 .add(statusCri)
						 .list();
			}
		}else{
			System.out.println("admin");
			 cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
					 .add(statusCri).list();
			 for(LoggerCase c : cases) {
					c.setOwner_role(c.getOwner().getAuthUserGroups().get(0).getAuthGroup().getName());
				}
		}
		
		if(user != null){
			System.out.println("user not null");
			List<AuthUserGroup> authUserGroups = user.getAuthUserGroups();
			for(AuthUserGroup group : authUserGroups) {
				if(group.getAuthGroup().getName().equalsIgnoreCase("consultant")){
					cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
							.add(Restrictions.eq("consultant.id", user.getId())).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
							.add(statusCri).list();
					
					break;
				}
			
			}
            
			if(cases == null) {
				System.out.println("cases null");
				 cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
						 .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						 //.add(Restrictions.eq("owner", idd))
						 .add(statusCri)
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
/*		JsonNode node = (JsonNode)httpRequest.getSession().getAttribute("user");
		node.getObject().getJSONArray("groups").get(0);
		Integer idd = node.getObject().getInt("id");*/
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
	public FileSystemResource exportToExcel(HttpServletRequest httpRequest,HttpServletResponse response, 
			@RequestParam ("start") @DateTimeFormat(pattern="MMddyyyy") Date start,
			@RequestParam ("end") @DateTimeFormat(pattern="MMddyyyy") Date end,
			@RequestParam ("status") String status, 
			@RequestParam ("consult") Integer consult,
			@RequestParam ("sangini") Integer sangini) {
		
		String sequeenceForm[] = { "new_form", 
				"pre_sneha_program_on_preventio",
				"pre_police_intervention", 
				"pre_screening_questionnaire",
				"pre_rosenberg_self_esteem",
				"pre_empowerment",
				"post_screening_questionnaire", 
				"post_rosenberg_self_esteem",
				"post_empowerment" }; 
		
		ArrayList<String> columnLabelHn=new ArrayList<String>();
		ArrayList<String> columnNameEng=new ArrayList<String>();
		List<LoggerXform> forms = null;
		
		for(int ind=0; ind<sequeenceForm.length; ind++){
			forms = (List<LoggerXform>) sessionFactory.getCurrentSession().createCriteria(LoggerXform.class).add(Restrictions.eq("idString", sequeenceForm[ind])).list();
			
			if(forms.size() != 0){
				for(LoggerXform tempForm : forms){
					if (!tempForm.getJson().isEmpty()) {
						//System.out.println("-------" + tempForm.getIdString()+ "-----");

						JSONObject jObject = new JSONObject(tempForm.getJson().trim());
						JSONArray headerArr = (JSONArray) jObject.get("children");
						
						//System.out.println("length ---  " + headerArr.length());
						for (int i = 0; i < headerArr.length(); i++) {
							try {
								JSONObject rec = headerArr.getJSONObject(i);
								
								if(rec.getString("name").equals("group_lh92g49") || rec.getString("name").equals("group_if9kv81") || rec.getString("name").equals("group_vy3xo71") || rec.getString("name").equals("group_xw13n72") || rec.getString("name").equals("group_mc46c32") || rec.getString("name").equals("group_kk5pj32") || rec.getString("name").equals("group_pt4pq54") || rec.getString("name").equals("group_yd3xa05") || rec.getString("name").equals("group_fi8ft37")){
									JSONArray rec_children = (JSONArray) rec.get("children");
									System.out.println("children=========================");
									for (int k=0; k<rec_children.length(); k++) {
										JSONObject childObj = (JSONObject) rec_children.get(k);
										
										//************for inner groups********************
										if(childObj.getString("name").contains("group_yd3xa05") || childObj.getString("name").contains("group_xw13n72")){
											JSONArray rec_subchildren = (JSONArray) childObj.get("children");
											/*System.out.println(rec_subchildren.toString());
											if(childObj.getString("name").contains("group_xw13n72")){
												rec_subchildren = (JSONArray) ((JSONObject) childObj.get("children")).get("children");
											}*/
											for (int m=0; m<rec_subchildren.length(); m++) {
												JSONObject subchildObj = (JSONObject) rec_subchildren.get(m);
												String formLabel = tempForm.getIdString() + "_" + subchildObj.getString("label");
												String formName = tempForm.getIdString() + "_" + subchildObj.getString("name");
												if (columnLabelHn.size() != 0
														&& columnLabelHn.contains(formLabel)) {
												} else {
													columnLabelHn.add(formLabel);
												}

												if (columnNameEng.size() != 0
														&& columnNameEng.contains(formName)) {
												} else {
													System.out.println(formName);
													columnNameEng.add(formName);
												}
											}
										}
										//********End of inner group*************
										
										String formLabel = tempForm.getIdString() + "_" + childObj.getString("label");
										String formName = tempForm.getIdString() + "_" + childObj.getString("name");
										if (columnLabelHn.size() != 0
												&& columnLabelHn.contains(formLabel)) {
										} else {
											columnLabelHn.add(formLabel);
										}

										if (columnNameEng.size() != 0
												&& columnNameEng.contains(formName)) {
										} else {
											System.out.println(formName);
											columnNameEng.add(formName);
										}
									}
								}else {
									String formLabel = tempForm.getIdString() + "_" + rec.getString("label");
									String formName = tempForm.getIdString() + "_" + rec.getString("name");
								
									//System.out.println(tempForm.getIdString() + "_" + rec.getString("name")+"=");

									if (columnLabelHn.size() != 0
											&& columnLabelHn.contains(formLabel)) {
									} else {
										columnLabelHn.add(formLabel);
									}
	
									if (columnNameEng.size() != 0
											&& columnNameEng.contains(formName)) {
									} else {
										columnNameEng.add(formName);
									}
								}

							} catch (Exception e) {
								// System.out.println("not found..");
							}
						}

					}
				}
			}
		}
		
		String filename = "/home/kobo/NewExcelFile.xls" ;
		//String filename = "d:/NewExcelFile.xls" ;
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("FirstSheet");  

        HSSFRow rowhead = sheet.createRow((short)0);
        int i = 1;
    	rowhead.createCell(0).setCellValue("Case Id");
        for(String name : columnNameEng){
        	String convetedValue;
        	try{
        		convetedValue = myProps.getProperty(name);
        	}catch(Exception e){
        		e.printStackTrace();
        		convetedValue=name;
        		System.out.println("not found "+convetedValue);
        	}
        	rowhead.createCell(i).setCellValue(convetedValue);//todo
        	i++;
        }
        
        //-------------------------------
        
        List<LoggerCase> cases = null;		
        /*if(sangini != 0 && consult != 0 && !status.equals("0")){
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
		}*/
        
        cases = findcases(start, end, status, consult, sangini);
		//System.out.println("cases --==="+cases.size());
        
		Map<String, Map<String, String>> mainMap = new HashMap<String, Map<String,String>>();
		if(cases.size() != 0){
			for(LoggerCase tempCase : cases){
				//System.out.println("Outer name ==== "+tempCase.getStatus());
				if(!tempCase.getLoggerCaseInstances().isEmpty()){
					//System.out.println("inner name ==== "+tempCase.getStatus());
					Map<String,String> caseMap = new HashMap<String, String>();
					
					String currentCaseId = tempCase.getCaseId();
					//System.out.println("caseID-- "+currentCaseId);

					caseMap = mainMap.get(currentCaseId);
					if(caseMap == null){
						caseMap = new HashMap<String, String>();
					}
					
					for(LoggerCaseInstance instance : tempCase.getLoggerCaseInstances()){
						JSONObject jObject = new JSONObject(instance.getLoggerInstance().getJson().trim());
						System.out.println("---------form-----------"+instance.getLoggerInstance().getLoggerXform().getIdString());
						String preKeyValue = instance.getLoggerInstance().getLoggerXform().getIdString();
						
						Iterator<?> keys = jObject.keys();
						while( keys.hasNext() ) {
							String key = (String) keys.next();
						    if(!key.equals("meta/instanceID") && 
						    		!key.equals("_xform_id_string") && 
						    		!key.equals("start") && 
						    		!key.equals("formhub/uuid") && 
						    		!key.equals("end")) {
						    		//System.out.println(key);
						    		//System.out.println(jObject.getString(key));
						    		try{
						    			String temp = preKeyValue+"_"+key;
						    			if(temp.contains("group_pt4pq54") || temp.contains("group_xw13n72")|| temp.contains("group_kk5pj32") || temp.contains("group_mc46c32") || temp.contains("new_form_group_lh92g49") || temp.contains("new_form_group_if9kv81")){
						    				//System.out.println(myProps.getProperty("hello"));
						    				caseMap.put(myProps.getProperty(preKeyValue+"_"+key), jObject.getString(key));
						    				
						    			}else
						    			caseMap.put(preKeyValue+"_"+key, jObject.getString(key));
						    		}
						    		catch(Exception e){
						    			e.printStackTrace();
						    			System.out.println("in null");
						    		}
						    	
						    }
						 
						}
					}
					mainMap.put(currentCaseId+"#"+tempCase.getDateCreated(), caseMap);
				}else{
					mainMap.put(tempCase.getCaseId()+"#"+tempCase.getDateCreated(),  new HashMap<String, String>());
				}
			}
		}
		//System.out.println(mainMap);
		int rowCount = 1;
		for (Map.Entry<String, Map<String, String>> entry : mainMap.entrySet())
		{
			HSSFRow row = sheet.createRow((short)rowCount);
			rowCount++;
			String tempval[] = entry.getKey().split("\\#");
			row.createCell(0).setCellValue(tempval[0].replaceAll("_", " "));
			int colCount = 0;
			for(String colname : columnNameEng){
				colCount++;
				//System.out.println("--------"+colname+"------");
				Map<String,String> innermap = entry.getValue();
				for (Map.Entry<String, String> currentInnermap : innermap.entrySet()){
					System.out.println("key "+currentInnermap.getKey());
					String currentInnerKey = currentInnermap.getKey();
					//System.out.println("modified "+currentInnerKey);
					if(currentInnerKey != null && ("fe2ud57").contains(currentInnerKey)){
						currentInnerKey = myProps.getProperty(currentInnerKey);
						
					}
					if(currentInnerKey!=null && currentInnerKey.equals("new_form__")){
						row.createCell(colCount).setCellValue(currentInnermap.getValue().replaceAll("_", " "));
						break;
	    			}
					else if(currentInnerKey!=null && currentInnerKey.contains(colname)){
						//System.out.println(currentInnermap.getKey());
						//System.out.println("========"+currentInnermap.getValue().replaceAll("_", " "));
						row.createCell(colCount).setCellValue(currentInnermap.getValue().replaceAll("_", " "));
						break;
					}
				}
				
			}
			
		}
		 
        int d = 0;
        for(String name : columnNameEng){
        	sheet.autoSizeColumn(d);
        	d++;
        }
        
        try {
        	FileOutputStream fileOut = new FileOutputStream(filename);
        	workbook.write(fileOut);
        	fileOut.close();
        } catch(IOException e) {
        	e.printStackTrace();
        }
		File f = new File("/home/kobo/NewExcelFile.xls");
        //File f = new File("d:/NewExcelFile.xls");
        System.out.println("Your excel file has been generated!");
        response.setContentType("apphlication/xls");
        response.setHeader("Content-Disposition", "attachment;filename=CaseList.xls"); 
        
        return new FileSystemResource(f);
		
		/* */

	}
	
	public List<LoggerCase> findcases(Date start,
			Date end,
			String status, 
			Integer consult,
			Integer sangini){
		
		 Calendar cal = Calendar.getInstance();
	        cal.setTime(end);
	        cal.set(Calendar.SECOND, 59);
	        cal.set(Calendar.MINUTE, 59);
	        cal.set(Calendar.HOUR, 23);
	        end = cal.getTime();
	        
		List<LoggerCase> cases = null;		
		
		if(sangini != 0 && consult != 0 && !status.equals("0")){
			if(status.equals("open")){
				cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
						.add(Restrictions.and(Restrictions.eq("owner.id", sangini), Restrictions.and(Restrictions.eq("consultant.id", consult), Restrictions.isNull("status"))))
						.add(Restrictions.between("dateCreated",start, end)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.list();
			}else{
				cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
						.add(Restrictions.and(Restrictions.eq("owner.id", sangini), Restrictions.and(Restrictions.eq("consultant.id", consult), Restrictions.eq("status", status))))
						.add(Restrictions.between("dateCreated",start, end)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.list();
			}
			return cases;
		}
		
		if(sangini != 0 && consult != 0){
			//allCases = " where user_id = '"+sangini+"' and consultant_id = '"+consult+"'";
			cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
					.add(Restrictions.and(Restrictions.eq("owner.id", sangini), Restrictions.eq("consultant.id", consult)))
					.add(Restrictions.between("dateCreated",start, end)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
					.list();
			return cases;
		} 
			
		if(sangini != 0 && !status.equals("0")){
			//allCases = " where status = '"+status+"'";
			if(status.equals("open")){
				cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
						.add(Restrictions.and(Restrictions.eq("owner.id", sangini), Restrictions.isNull("status")))
						.add(Restrictions.between("dateCreated",start, end)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.list();
			}else{
				cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
						.add(Restrictions.and(Restrictions.eq("owner.id", sangini), Restrictions.eq("status", status)))
						.add(Restrictions.between("dateCreated",start, end)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.list();
			}

			return cases;
		}
		
		if(consult != 0 && !status.equals("0")){
			//allCases = " where status = '"+status+"'";
			if(status.equals("open")){
				cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
						.add(Restrictions.and(Restrictions.eq("consultant.id", consult), Restrictions.isNull("status")))
						.add(Restrictions.between("dateCreated",start, end)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.list();
			}else{
				cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
						.add(Restrictions.and(Restrictions.eq("consultant.id", consult), Restrictions.eq("status", status)))
						.add(Restrictions.between("dateCreated",start, end)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.list();
			}

			return cases;
		}
		
		//sql = "select * from loggercase where user_id"
		
		if(sangini != 0 ){
			//allCases = " where user_id = '"+sangini+"'";
			cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
					.add(Restrictions.eq("owner.id", sangini))
					.add(Restrictions.between("dateCreated",start, end)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
					.list();
			
			return cases;
		}
		if(consult != 0){
			//allCases = " where user_id = '"+sangini+"' and consultant_id = '"+consult+"'";
			cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
					.add(Restrictions.eq("consultant.id", consult))
					.add(Restrictions.between("dateCreated",start, end)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
					.list();
			return cases;
		}
		if(!status.equals("0")){
			//allCases = " where status = '"+status+"'";
			if(status.equals("open")){
				cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
						.add(Restrictions.isNull("status"))
						.add(Restrictions.between("dateCreated",start, end)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.list();
			}else{
				cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
						.add(Restrictions.eq("status", status))
						.add(Restrictions.between("dateCreated",start, end)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
						.list();
			}
			return cases;
		}
		
		cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
				.add(Restrictions.between("dateCreated",start, end)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.list();
		
		System.out.println("cases --==="+cases.size());
		return cases;
	}
	
	@RequestMapping(value="isCaseDeleted",method=RequestMethod.GET)
	@ResponseBody
	@Transactional(readOnly=true)
	public boolean checkIsDeleted(HttpServletRequest httpRequest,@RequestParam ("id") Integer id) {
		System.out.println(id);
		List<LoggerCase> cases = null;
		cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
				.add(Restrictions.eq("id", id)).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.list();
		//System.out.println("size is "+cases.size());
		if(cases.size() > 0){
			return false;
		}
		return true;
	}
}
