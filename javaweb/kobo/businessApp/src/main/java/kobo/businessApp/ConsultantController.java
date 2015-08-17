package kobo.businessApp;

import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.*;
import org.hibernate.cfg.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kobo.entities.AuthGroup;
import kobo.entities.AuthUser;
import kobo.entities.AuthUserGroup;
import kobo.entities.Cluster;
import kobo.entities.EmergencyContact;
import kobo.entities.LoggerCase;
import kobo.entities.Sector;
import kobo.vms.authVM;
import kobo.vms.clusterVM;
import kobo.vms.coordinateVM;
import kobo.vms.emergencyContactVM;
import kobo.vms.loggerCaseVM;
import kobo.vms.sanghniVM;
import kobo.vms.sectorVM;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.postgresql.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	@RequestMapping(value="update/cases/{caseId}/{status}/{conid}",method=RequestMethod.GET)
	@ResponseBody
	@Transactional(readOnly=true)
	public HashMap<String, String> updateCaseStatus(HttpServletRequest httpRequest,@PathVariable ("caseId") Integer caseid,@PathVariable ("status") String status,@PathVariable ("conid") Integer conId) {
		AuthUser user = ApplicationController.getUserFromRequest(httpRequest,sessionFactory);
		
		System.out.println("cass id =="+caseid);
		System.out.println("status =="+status);
		System.out.println("consult idd =="+conId);
		AuthUser auth1 = (AuthUser) sessionFactory.getCurrentSession().createCriteria(AuthUser.class)
				.add(Restrictions.eq("id", conId))
				.uniqueResult();
		
		LoggerCase cases = (LoggerCase) sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
				.add(Restrictions.eq("id", caseid))
				.uniqueResult();
		//Configuration cfg = new Configuration();
		//cfg.configure("hibernate.cfg.xml"); 
 
		//SessionFactory factory = cfg.buildSessionFactory();
		Session session = sessionFactory.openSession();	
 
		LoggerCase p=new LoggerCase();
		p.setId(caseid);  // 104 must be in the DB
		p.setConsultant(auth1);	
		p.setStatus("assigned");
		p.setNote(cases.getNote());
		p.setDateCreated(cases.getDateCreated());
		p.setOwner(cases.getOwner());
		Transaction tx = session.beginTransaction();
			session.update(p);
			
		tx.commit();
 
		System.out.println("Object Updated successfully.....!!");
		session.close();
		//factory.close();
	
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("name", auth1.getUsername());
		
		return map;
	}
	
	
	@RequestMapping(value="addsector",method=RequestMethod.POST)
	@ResponseBody
	@Transactional
	public HashMap<String, String> addSector(HttpServletRequest httpRequest,@RequestBody sectorVM secvm) {
		AuthUser user = ApplicationController.getUserFromRequest(httpRequest,sessionFactory);
		System.out.println("vmmm==="+secvm);
		Session session = sessionFactory.getCurrentSession();
		//session.beginTransaction();
		
		//Integer idd = Integer.parseInt(secvm.getItem());
		List<AuthUser> cord = new ArrayList<AuthUser>();
		List<AuthUser> sangg = new ArrayList<AuthUser>();
		List<EmergencyContact> emrcont = new ArrayList<EmergencyContact>();
		EmergencyContact emrc = null;
		for(emergencyContactVM e: secvm.getInc()){
			 emrc = new EmergencyContact();
			emrc.setAddress(e.getAddress());
			emrc.setName(e.getName());
			emrc.setContact(e.getContact());
			session.save(emrc);
			//session.getTransaction().commit();
			//session.close();
		}
		
		//Session session1 = sessionFactory.openSession();
		//session1.beginTransaction();
		
		for(int i=0;i<secvm.getCoordinators().size();i++){
			AuthUser authuser1 = (AuthUser) sessionFactory.getCurrentSession().createCriteria(AuthUser.class)
					 .add(Restrictions.eq("id", secvm.getCoordinators().get(i).getId()))
					.uniqueResult();
			cord.add(authuser1);
		}
		
		for(int i=0;i<secvm.getSanghni().size();i++){
			AuthUser authuser2 = (AuthUser) sessionFactory.getCurrentSession().createCriteria(AuthUser.class)
					 .add(Restrictions.eq("id", secvm.getSanghni().get(i).getId()))
					.uniqueResult();
			sangg.add(authuser2);
		}
		
		
		
		
		for(int i=0;i<secvm.getInc().size();i++){
			EmergencyContact emrcc =  (EmergencyContact) sessionFactory.getCurrentSession().createCriteria(EmergencyContact.class)
					 .add(Restrictions.eq("id", emrc.getId()))
					.uniqueResult();
			emrcont.add(emrcc);
		}
		
		Sector sec = new Sector();
		sec.setName(secvm.getName());
		sec.setAreaCoordinator(cord);
		sec.setSanghani(sangg);
		sec.setEmergencyContacts(emrcont);
		//sec.setEmergencyContacts(emrc);
		
		session.save(sec);
		session.flush();
		//session1.getTransaction().commit();
		//session1.close();
		return null;
	}
	
	@RequestMapping(value = "editSector", method = RequestMethod.POST)
	@ResponseBody
	@Transactional
	public HashMap<String, String> editSector(HttpServletRequest httpRequest,
			@RequestBody sectorVM secvm) {
		AuthUser user = ApplicationController.getUserFromRequest(httpRequest,
				sessionFactory);

		Session session1 = sessionFactory.getCurrentSession();

		EmergencyContact emrc = null;
		for (emergencyContactVM e : secvm.getInc()) {
			if(e.getId() != null){
				EmergencyContact emrc1 = (EmergencyContact) session1.get(EmergencyContact.class, e.getId());
				emrc1.setAddress(e.getAddress());
				emrc1.setName(e.getName());
				emrc1.setContact(e.getContact());
				session1.update(emrc1);
			}else{
				emrc = new EmergencyContact();
				emrc.setAddress(e.getAddress());
				emrc.setName(e.getName());
				emrc.setContact(e.getContact());
				session1.save(emrc);
			}
		}
		//session1.getTransaction().commit();
		//session1.close();
		System.out.println("cass id ==" + secvm);

		//Session session = sessionFactory.openSession();
		//session.beginTransaction();
		List<AuthUser> cord = new ArrayList<AuthUser>();
		List<AuthUser> sangg = new ArrayList<AuthUser>();
		List<EmergencyContact> emrcont = new ArrayList<EmergencyContact>();
		for (int i = 0; i < secvm.getCoordinators().size(); i++) {
			AuthUser authuser1 = (AuthUser) sessionFactory
					.getCurrentSession()
					.createCriteria(AuthUser.class)
					.add(Restrictions.eq("id", secvm.getCoordinators().get(i)
							.getId())).uniqueResult();
			cord.add(authuser1);
		}

		for (int i = 0; i < secvm.getSanghni().size(); i++) {
			AuthUser authuser2 = (AuthUser) sessionFactory
					.getCurrentSession()
					.createCriteria(AuthUser.class)
					.add(Restrictions.eq("id", secvm.getSanghni().get(i)
							.getId())).uniqueResult();
			sangg.add(authuser2);
		}

		
		  for(int i=0;i<secvm.getInc().size();i++){ 
			  EmergencyContact emrcc = (EmergencyContact)sessionFactory.getCurrentSession().createCriteria(EmergencyContact.class) 
					  .add(Restrictions.eq("id", secvm.getInc().get(i).getId())) 
					  .uniqueResult();
		  emrcont.add(emrcc); 
		  }
		 if(emrc != null){
		  EmergencyContact emrcc = (EmergencyContact)sessionFactory.getCurrentSession().createCriteria(EmergencyContact.class) 
				  .add(Restrictions.eq("id", emrc.getId())) 
				  .uniqueResult();
		  	emrcont.add(emrcc); 
		 }  
		  
		Sector sec = (Sector) session1.get(Sector.class, secvm.getId());
		sec.setName(secvm.getName());
		sec.setAreaCoordinator(cord);
		sec.setSanghani(sangg);
		sec.setEmergencyContacts(emrcont);
		session1.update(sec);
		session1.flush();
		return null;
	}
	
	
	@RequestMapping(value="getSectors",method=RequestMethod.GET)
	@ResponseBody
	@Transactional(readOnly=true)
	public List<sectorVM> getSectors(HttpServletRequest httpRequest) {
		JsonNode node = (JsonNode)httpRequest.getSession().getAttribute("user");
		List<Sector> sec = sessionFactory.getCurrentSession().createCriteria(Sector.class)
				 //.add(Restrictions.eq("owner", idd))
				.list();
		List<sectorVM> secVM = new ArrayList<sectorVM>();
		
		for(Sector s : sec){
			List<coordinateVM> coordinateVm = new ArrayList<coordinateVM>();;
			List<sanghniVM> sanghVm = new ArrayList<sanghniVM>();
			List<emergencyContactVM> emergencycontactVM = new ArrayList<emergencyContactVM>();
		
			for(AuthUser a : s.getAreaCoordinator()){
				 //coordinateVm = new ArrayList<coordinateVM>();
				coordinateVM au = new coordinateVM();
				au.setName(a.getFirstName());
				au.setId(a.getId());
				coordinateVm.add(au);
			}
			
			for(AuthUser a1 : s.getSanghani()){
				 //sanghVm = new ArrayList<sanghniVM>();
				sanghniVM au = new sanghniVM();
				au.setName(a1.getFirstName());
				au.setId(a1.getId());
				sanghVm.add(au);
			}
			
			for(EmergencyContact a1 : s.getEmergencyContacts()){
				emergencyContactVM au = new emergencyContactVM();
				au.setName(a1.getName());
				au.setAddress(a1.getAddress());
				au.setContact(a1.getContact());
				au.setId(a1.getId());
				emergencycontactVM.add(au);
			}
			
				sectorVM au = new sectorVM();
				au.setName(s.getName());
				au.setId(s.getId());
				au.setCoordinators(coordinateVm);
				au.setSanghni(sanghVm);
				au.setInc(emergencycontactVM);
				secVM.add(au);
			
		}
		
		
		
		
		System.out.println("getSectors --==="+sec.size());
		return secVM;
		
	}	
	
	@RequestMapping(value="getClusterOfSectors/{SID}",method=RequestMethod.POST)
	@ResponseBody
	@Transactional(readOnly=true)
	public List<sectorVM> getClusterOfSectors(HttpServletRequest httpRequest,@PathVariable ("SID") Integer sid) {
		JsonNode node = (JsonNode)httpRequest.getSession().getAttribute("user");
		List<sectorVM> secc  = new ArrayList<sectorVM>();
		AuthUser auth1 = (AuthUser) sessionFactory.getCurrentSession().createCriteria(AuthUser.class)
				.add(Restrictions.eq("id", sid))
				//.add(Restrictions.like("firstName", cordinateName,  MatchMode.START))
				.uniqueResult();
		
		System.out.println("sanghi id===="+auth1);
		
		List<Sector> sec = sessionFactory.getCurrentSession().createCriteria(Sector.class)
				.createCriteria("sanghani")
				 .add(Restrictions.eq("id", auth1.getId()))
				.list();
		
		System.out.println("sanghi id===="+sid);
		
		for(Sector s : sec){
			List<clusterVM> clus  = new ArrayList<clusterVM>();
			
			Sector sec1 = (Sector) sessionFactory.getCurrentSession().createCriteria(Sector.class)
					//.createCriteria("cluster")
					 .add(Restrictions.eq("id", s.getId()))
					.uniqueResult(); 
			
			for(Cluster c : sec1.getCluster()){
				clusterVM cvm = new clusterVM();
				cvm.setName(c.getName());
				cvm.setId(c.getId());
				clus.add(cvm);
			}
			sectorVM au = new sectorVM();
			au.setName(s.getName());
			au.setId(s.getId());
			au.setClustervm(clus);
			secc.add(au);
		}
		
		
		//System.out.println("getSectors --==="+sec.size());
		return secc;
		
	}	
	
	
	/*@RequestMapping(value="getSubSectors",method=RequestMethod.GET)
	@ResponseBody
	@Transactional(readOnly=true)
	public List<Sector> getSubSectors(HttpServletRequest httpRequest) {
		JsonNode node = (JsonNode)httpRequest.getSession().getAttribute("user");
		List<Sector> sec = sessionFactory.getCurrentSession().createCriteria(Sector.class)
				 .add(Restrictions.eq("owner", idd))
				.list();
		
		
		System.out.println("getSectors --==="+sec.size());
		return sec;
		
	}	*/
	
	@RequestMapping(value="getAreaConsultant",method=RequestMethod.GET)
	@ResponseBody
	@Transactional(readOnly=true)
	public List<AuthUser> getAreaConsultant(HttpServletRequest httpRequest) {
		JsonNode node = (JsonNode)httpRequest.getSession().getAttribute("user");
		node.getObject().getJSONArray("groups").get(0);
		Integer idd = node.getObject().getInt("id");
		System.out.println("my idd =="+idd);
		List<AuthUser> auth = new ArrayList<AuthUser>();
		AuthGroup grpid = (AuthGroup) sessionFactory.getCurrentSession().createCriteria(AuthGroup.class)
				.add(Restrictions.eq("name", "area consultant"))
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
	
	@RequestMapping(value="AreaCoordinator/{cordinate}",method=RequestMethod.GET)
	@ResponseBody
	@Transactional(readOnly=true)
	public List<authVM> AreaCoordinator(HttpServletRequest httpRequest,@PathVariable ("cordinate") String cordinateName) {
		
		System.out.println("coordinator==="+cordinateName);
		
		JsonNode node = (JsonNode)httpRequest.getSession().getAttribute("user");
		node.getObject().getJSONArray("groups").get(0);
		Integer idd = node.getObject().getInt("id");
		System.out.println("my idd =="+idd);
		List<AuthUser> auth = new ArrayList<AuthUser>();
		AuthGroup grpid = (AuthGroup) sessionFactory.getCurrentSession().createCriteria(AuthGroup.class)
				.add(Restrictions.eq("name", "area consultant"))
				.uniqueResult();
		System.out.println("grp id==="+grpid.getId());
		List<AuthUserGroup> consulnt = sessionFactory.getCurrentSession().createCriteria(AuthUserGroup.class)
					.add(Restrictions.eq("authGroup.id", grpid.getId()))
					.list();
		for(AuthUserGroup aa:consulnt){
			 AuthUser auth1 = (AuthUser) sessionFactory.getCurrentSession().createCriteria(AuthUser.class)
						.add(Restrictions.eq("id", aa.getAuthUser().getId()))
						//.add(Restrictions.like("firstName", cordinateName,  MatchMode.START))
						.uniqueResult();
				auth.add(auth1); 
			 
			
		}
		List<authVM> aaa = new ArrayList<authVM>();
		for(AuthUser a : auth){
			authVM au = new authVM();
			au.setName(a.getFirstName());
			au.setId(a.getId());
			aaa.add(au);
		}
		
		System.out.println("auth list =="+auth.size());
	
		return aaa;
		
	}
	
	@RequestMapping(value="Sanghnis/{query}",method=RequestMethod.GET)
	@ResponseBody
	@Transactional(readOnly=true)
	public List<authVM> Sanghnis(HttpServletRequest httpRequest,@PathVariable ("query") String cordinateName) {
		
		System.out.println("coordinator==="+cordinateName);
		
		JsonNode node = (JsonNode)httpRequest.getSession().getAttribute("user");
		node.getObject().getJSONArray("groups").get(0);
		Integer idd = node.getObject().getInt("id");
		System.out.println("my idd =="+idd);
		List<AuthUser> auth = new ArrayList<AuthUser>();
		AuthGroup grpid = (AuthGroup) sessionFactory.getCurrentSession().createCriteria(AuthGroup.class)
				.add(Restrictions.eq("name", "Sangani"))
				.uniqueResult();
		System.out.println("grp id==="+grpid.getId());
		List<AuthUserGroup> consulnt = sessionFactory.getCurrentSession().createCriteria(AuthUserGroup.class)
					.add(Restrictions.eq("authGroup.id", grpid.getId()))
					.list();
		for(AuthUserGroup aa:consulnt){
			 AuthUser auth1 = (AuthUser) sessionFactory.getCurrentSession().createCriteria(AuthUser.class)
						.add(Restrictions.eq("id", aa.getAuthUser().getId()))
						//.add(Restrictions.like("firstName", cordinateName,  MatchMode.START))
						.uniqueResult();
				auth.add(auth1); 
			 
			
		}
		List<authVM> aaa = new ArrayList<authVM>();
		for(AuthUser a : auth){
			authVM au = new authVM();
			au.setName(a.getFirstName());
			au.setId(a.getId());
			aaa.add(au);
		}
		
		System.out.println("auth list =="+auth.size());
	
		return aaa;
		
	}
	
	
	@RequestMapping(value="getAllSanghnis",method=RequestMethod.GET)
	@ResponseBody
	@Transactional(readOnly=true)
	public List<authVM> getAllSanghnis(HttpServletRequest httpRequest) {
		
		
		JsonNode node = (JsonNode)httpRequest.getSession().getAttribute("user");
		node.getObject().getJSONArray("groups").get(0);
		Integer idd = node.getObject().getInt("id");
		System.out.println("my idd =="+idd);
		List<AuthUser> auth = new ArrayList<AuthUser>();
		AuthGroup grpid = (AuthGroup) sessionFactory.getCurrentSession().createCriteria(AuthGroup.class)
				.add(Restrictions.eq("name", "Sangani"))
				.uniqueResult();
		System.out.println("grp id==="+grpid.getId());
		List<AuthUserGroup> consulnt = sessionFactory.getCurrentSession().createCriteria(AuthUserGroup.class)
					.add(Restrictions.eq("authGroup.id", grpid.getId()))
					.list();
		for(AuthUserGroup aa:consulnt){
			 AuthUser auth1 = (AuthUser) sessionFactory.getCurrentSession().createCriteria(AuthUser.class)
						.add(Restrictions.eq("id", aa.getAuthUser().getId()))
						//.add(Restrictions.like("firstName", cordinateName,  MatchMode.START))
						.uniqueResult();
				auth.add(auth1); 
			 
			
		}
		List<authVM> aaa = new ArrayList<authVM>();
		for(AuthUser a : auth){
			authVM au = new authVM();
			au.setName(a.getFirstName());
			au.setId(a.getId());
			aaa.add(au);
		}
		
		System.out.println("auth list =="+auth.size());
	
		return aaa;
		
	}
	
	@RequestMapping(value="getSearchCases",method=RequestMethod.GET)
	@ResponseBody
	@Transactional(readOnly=true)
	public List getSearchCases(HttpServletRequest httpRequest,@RequestParam ("start") @DateTimeFormat(pattern="MMddyyyy") Date start,
			@RequestParam ("end") @DateTimeFormat(pattern="MMddyyyy") Date end,
			@RequestParam ("sangini") Integer sangini,@RequestParam ("consult") Integer consult,
			@RequestParam ("status") String status) {
	
		List<loggerCaseVM> aaa = new ArrayList<loggerCaseVM>();
		List cases = null;
		System.out.println("start =="+start);
		
		System.out.println("end =="+end);
		
		System.out.println("sabgini =="+sangini);
		
		System.out.println("consult =="+consult);
		
		System.out.println("status =="+status);
	
		if(sangini != 0 && consult != 0 && !status.equals("0")){
			cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
					.add(Restrictions.or(Restrictions.eq("owner.id", sangini), Restrictions.or(Restrictions.eq("consultant.id", consult), Restrictions.eq("status", status))))
					.add(Restrictions.between("dateCreated",start, end))
					.list();
			return cases;
		}
		
		if(sangini != 0 && consult != 0){
			//allCases = " where user_id = '"+sangini+"' and consultant_id = '"+consult+"'";
			cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
					.add(Restrictions.or(Restrictions.eq("owner.id", sangini), Restrictions.eq("consultant.id", consult)))
					.add(Restrictions.between("dateCreated",start, end))
					.list();
			return cases;
		} 
			
		if(sangini != 0 && !status.equals("0")){
			//allCases = " where status = '"+status+"'";
			cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
					.add(Restrictions.or(Restrictions.eq("owner.id", sangini), Restrictions.eq("status", status)))
					.add(Restrictions.between("dateCreated",start, end))
					.list();
			return cases;
		}
		
		if(consult != 0 && !status.equals("0")){
			//allCases = " where status = '"+status+"'";
			cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
					.add(Restrictions.or(Restrictions.eq("consultant.id", consult), Restrictions.eq("status", status)))
					.add(Restrictions.between("dateCreated",start, end))
					.list();
			return cases;
		}
		
		//sql = "select * from loggercase where user_id"
		
		if(sangini != 0 ){
			//allCases = " where user_id = '"+sangini+"'";
			cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
					.add(Restrictions.eq("owner.id", sangini))
					.add(Restrictions.between("dateCreated",start, end))
					.list();
			
			return cases;
		}
		if(consult != 0){
			//allCases = " where user_id = '"+sangini+"' and consultant_id = '"+consult+"'";
			cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
					.add(Restrictions.eq("consultant.id", consult))
					.add(Restrictions.between("dateCreated",start, end))
					.list();
			return cases;
		}
		if(!status.equals("0")){
			//allCases = " where status = '"+status+"'";
			cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
					.add(Restrictions.eq("status", status))
					.add(Restrictions.between("dateCreated",start, end))
					.list();
			return cases;
		}
		
		cases = sessionFactory.getCurrentSession().createCriteria(LoggerCase.class)
				.add(Restrictions.between("dateCreated",start, end))
				.list();
		
		System.out.println("cases --==="+cases.size());
		return cases;
	}
	

}
