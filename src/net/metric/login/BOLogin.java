package net.metric.login;

import java.util.Iterator;

import com.businessobjects.enterprise.infoobject.InfoObjects;
import com.businessobjects.sdk.plugin.desktop.publication.IPublication;
import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.framework.CrystalEnterprise;
import com.crystaldecisions.sdk.framework.IEnterpriseSession;
import com.crystaldecisions.sdk.framework.ISessionMgr;
import com.crystaldecisions.sdk.occa.infostore.CeScheduleType;
import com.crystaldecisions.sdk.occa.infostore.IEffectiveLimit;
import com.crystaldecisions.sdk.occa.infostore.IEffectiveLimits;
import com.crystaldecisions.sdk.occa.infostore.IEffectivePrincipal;
import com.crystaldecisions.sdk.occa.infostore.IEffectivePrincipals;
import com.crystaldecisions.sdk.occa.infostore.IEffectiveRight;
import com.crystaldecisions.sdk.occa.infostore.IEffectiveRights;
import com.crystaldecisions.sdk.occa.infostore.IEffectiveRole;
import com.crystaldecisions.sdk.occa.infostore.IEffectiveRoles;
import com.crystaldecisions.sdk.occa.infostore.IExplicitPrincipal;
import com.crystaldecisions.sdk.occa.infostore.IExplicitPrincipals;
import com.crystaldecisions.sdk.occa.infostore.IExplicitRight;
import com.crystaldecisions.sdk.occa.infostore.IExplicitRights;
import com.crystaldecisions.sdk.occa.infostore.IInfoObject;
import com.crystaldecisions.sdk.occa.infostore.IInfoObjects;
import com.crystaldecisions.sdk.occa.infostore.IInfoStore;
import com.crystaldecisions.sdk.occa.infostore.IPluginBasedRightIDs;
import com.crystaldecisions.sdk.occa.infostore.IRightID;
import com.crystaldecisions.sdk.occa.infostore.ISchedulingInfo;
import com.crystaldecisions.sdk.occa.infostore.ISecurityInfo2;
import com.crystaldecisions.sdk.occa.infostore.RightDescriptor;
import com.crystaldecisions.sdk.occa.pluginmgr.IPluginInfo;
import com.crystaldecisions.sdk.occa.pluginmgr.IPluginMgr;
import com.crystaldecisions.sdk.occa.security.CeSecurityOptions;
import com.crystaldecisions.sdk.plugin.CeKind;
import com.crystaldecisions.sdk.plugin.desktop.calendar.IBusinessCalendarDay;
import com.crystaldecisions.sdk.plugin.desktop.calendar.IBusinessCalendarDays;
import com.crystaldecisions.sdk.plugin.desktop.calendar.ICalendar;
import com.crystaldecisions.sdk.plugin.desktop.common.IReportFormatOptions;
import com.crystaldecisions.sdk.plugin.desktop.common.IReportPrinterOptions;
import com.crystaldecisions.sdk.plugin.desktop.report.CeReportRightID;
import com.crystaldecisions.sdk.plugin.desktop.report.IReport;
import com.crystaldecisions.sdk.plugin.desktop.user.IUser;
import com.crystaldecisions.sdk.plugin.desktop.usergroup.IUserGroup;

import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;

public class BOLogin {
	public static IInfoStore Login(String username, String password, String host, String auth){
		
		try {
			ISessionMgr iSessionMgr = CrystalEnterprise.getSessionMgr();
			IEnterpriseSession iEnterPriseSession = iSessionMgr.logon(username,password,host,auth);
			IInfoStore infoStore = (IInfoStore) iEnterPriseSession.getService("InfoStore");
		return infoStore;
	} catch (Exception e) {
	}
	return null;
}

public static IUser GetUsers(IInfoStore infoStore){
	
	String query = "SELECT SI_ID, SI_NAME FROM CI_SYSTEMOBJECTS WHERE SI_KIND='User'";
	IUser user = null;
	try {
		IInfoObjects infoObjects = infoStore.query(query);
		for ( int i=0; i<infoObjects.size(); i++){
			IInfoObject infoObject = (IInfoObject) infoObjects.get(i);
			//System.out.println(">>>>>>>");
			//System.out.println(infoObject.getTitle());
			//System.out.println(user.getTitle());
			user = (IUser) infoObjects.get(i);
			}
		} catch (Exception e) {
	}
	return user;
}

public static void GetGroups(IInfoStore infoStore){
	String query="SELECT SI_ID,SI_NAME FROM CI_SYSTEMOBJECTS WHERE SI_KIND='UserGroup'";
	try{
		IInfoObjects iInfoObjects = infoStore.query(query);
		for(int i=0;i<iInfoObjects.size();i++){
			IUserGroup iGroup=(IUserGroup)iInfoObjects.get(i);
			System.out.println(iGroup.getTitle());
		}
		System.out.print(iInfoObjects.toString());	
	}catch(Exception e){}
}


public static void GetGroups2(IInfoStore infoStore){
	String query="SELECT SI_ID,SI_NAME FROM CI_SYSTEMOBJECTS WHERE SI_KIND='UserGroup'";
	try{
		IInfoObjects iInfoObjects=infoStore.query(query);
		for(int i=0;i<iInfoObjects.size();i++){
			IUserGroup iUserGroup=(IUserGroup)iInfoObjects.get(i);
			System.out.println(iUserGroup.getTitle());
		}
	}catch(Exception e){}
}


public static void GetGroups3(IInfoStore infoStore) {
	String query="SELECT SI_ID,SI_NAME FROM CI_SYSTEMOBJECTS WHERE SI_KIND='UserGroup'";
	try{
		IInfoObjects iInfoObjects=infoStore.query(query);
		for(int i=0;i<iInfoObjects.size();i++){
			IUserGroup iUserGroup=(IUserGroup)iInfoObjects.get(i);
			System.out.println(iUserGroup.getTitle());
		}
	}catch(Exception e){}
	
}


public static void GetReports(IInfoStore infoStore){
	try {
		IInfoObjects infoObjects=infoStore.query("SELECT SI_ID FROM CI_INFOOBJECTS"
												+" WHERE SI_NAME='World Sales Report' And SI_INSTANCE=0");
		IInfoObject report=(IInfoObject)infoObjects.get(0);
		
		// Security'i al
		ISecurityInfo2 iSecurityInfo=report.getSecurityInfo2();
		// Tüm haklarý olanlarý almak için getEffectivePrincipals methodunu alacaðýz.
		IEffectivePrincipals iEffectivePrincipals=iSecurityInfo.getEffectivePrincipals();
		// Tüm bilgileri iterator ile alabiliriz.
		Iterator it=iEffectivePrincipals.iterator();
		while(it.hasNext()){
			IEffectivePrincipal iEffectivePrincipal=(IEffectivePrincipal)it.next();
			
			
			// Rolleri almak için
			IEffectiveRoles effectiveRoles=iEffectivePrincipal.getRoles();
			// Ýterator ile arama
			Iterator iteratorRoles=effectiveRoles.iterator();
			while(iteratorRoles.hasNext()){
				IEffectiveRole effectiveRole=(IEffectiveRole)iteratorRoles.next();
				//System.out.println(effectiveRole.getTitle());
				
			}
			
			// Haklarý alma...
			IEffectiveRights iEffectiveRights=iEffectivePrincipal.getRights();
			Iterator iteratorRights=iEffectiveRights.iterator();
			while(iteratorRights.hasNext()){
				IEffectiveRight iEffectiveRight=(IEffectiveRight)iteratorRights.next();
				//System.out.println(iEffectiveRight.getDescription(java.util.Locale.ENGLISH));
			}
			
			// Efektif Limitleri Alma 
			IEffectiveLimits iEffectiveLimits=iEffectivePrincipal.getLimits();
			Iterator itEffectiveLimits=iEffectiveLimits.iterator();
			while(itEffectiveLimits.hasNext()){
				IEffectiveLimit iEffectiveLimit=(IEffectiveLimit)itEffectiveLimits.next();
				System.out.println(iEffectiveLimit.getDescription(java.util.Locale.ENGLISH));
			}
		}
		
		
		
		
		
		
	} catch (SDKException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}


public static void rightControls(IInfoStore infoStore){
	String query="Select SI_ID From CI_INFOOBJECTS Where SI_NAME='World Sales Report' And SI_INSTANCE=0";
	try {
		IInfoObjects iInfoObjects=infoStore.query(query);
		IInfoObject iInfoObject=(IInfoObject)iInfoObjects.get(0);
		// Güvenlikler için ISecurityInfo alýp bunun rightlarýný larýný kullanmamamýz gerekmekte.
		ISecurityInfo2 iSecurityInfo2=iInfoObject.getSecurityInfo2();
		// getKnownRightsId, Map objesi
		// CeProgId -> obje tipi -> Set 
		// IRightID tip özel haklar.
		IPluginBasedRightIDs iPluginBasedRightIds=iSecurityInfo2.getKnownRightsByPlugin();
		
		Map pluginMap=iPluginBasedRightIds.getPluginRights();
		Iterator it=pluginMap.keySet().iterator();
		
		while(it.hasNext()){
			Object progId=it.next(); // Proje Tipini verecektir. 
			Set rightIds=(Set)pluginMap.get(progId);
			Iterator sIt=rightIds.iterator();
			while(sIt.hasNext()){
				IRightID key=(IRightID)sIt.next();
				// RightDescripttor ile haklarý check edecek bir obje oluþturmalýyýz.
				RightDescriptor rightDescriptor=new RightDescriptor(key.getBaseID(),key.getRightPluginKind(),key.isOwner(),CeSecurityOptions.RightScope.CURRENT_OBJECT,progId);
				IUser iUser=(IUser)GetUsers(infoStore);
				
				Boolean haklar=iSecurityInfo2.checkRight(rightDescriptor,iUser.getID(),true);
				System.out.println(haklar.toString());
				
			}
		}
		
	} catch (Exception e) {
		// TODO: handle exception
	}
}


public static void setRights(IInfoStore infoStore,IUser iUser){
	//System.out.println(iUser.getTitle());
	String query="SELECT SI_ID From CI_INFOOBJECTS WHERE SI_NAME='World Sales Report' And SI_INSTANCE=0";
	try {
		IInfoObjects iInfoObjects=infoStore.query(query);
		IInfoObject iInfoObject=(IInfoObject)iInfoObjects.get(0);
		
		ISecurityInfo2 securityInfo=iInfoObject.getSecurityInfo2();
		// Kullanýcýya hak verme
		IExplicitPrincipals iExplicitPrincipals=securityInfo.getExplicitPrincipals();
		// Hak verme ve Hak almak farklý iki process.
		IExplicitPrincipal iExplicitPrincipal = iExplicitPrincipals.add(iUser.getID());
		RightDescriptor rightDescriptor=new RightDescriptor(CeReportRightID.REFRESH_ON_DEMAND,CeKind.CRYSTAL_REPORT, false);
		
		IExplicitRights iExplicitRights=iExplicitPrincipal.getRights();
		IExplicitRight iExplicitRight=iExplicitRights.add(rightDescriptor);			
		iExplicitRight.setGranted(true);
		
	} catch (Exception e) {
		// TODO: handle exception
	}
}



/* Yeni Kullanýcý Oluþturma */
public static void CreateUser(IInfoStore infoStore){
	IInfoObjects newUsers=infoStore.newInfoObjectCollection();
	try {
		IUser newUser=(IUser)newUsers.add(CeKind.USER);
		newUser.setTitle("sahinyanlik");
		newUser.setNewPassword("19051987");
		newUser.setConnection(IUser.CONCURRENT);
		
		infoStore.commit(newUsers);
	} catch (SDKException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

// Schedule Report
public static void ScheduleReport(IInfoStore infoStore){
	String query="SELECT SI_ID,SI_NAME FROM CI_INFOOBJECTS WHERE SI_KIND='CrystalReport' AND SI_NAME='World Sales Report' AND SI_INSTANCE=0";
	try {
		IInfoObjects infoObjects=infoStore.query(query);
		IInfoObject rapor=(IInfoObject)infoObjects.get(0);
		ISchedulingInfo iSchedulingInfo=rapor.getSchedulingInfo();
	
		iSchedulingInfo.setType(CeScheduleType.ONCE);
		iSchedulingInfo.setRightNow(true);
		// Rapor formatýný almasý için
		IReportFormatOptions reportFormatOptions = ((IReport)rapor).getReportFormatOptions();
		// Rapor formatýný set etme.
		reportFormatOptions.setFormat(IReportFormatOptions.CeReportFormat.CRYSTAL_REPORT);
		
		// Printera doðrudan yazdýr
		
		IReportPrinterOptions iPrinterOptions=((IReport)rapor).getReportPrinterOptions();
		iPrinterOptions.setCopies(1);
		iPrinterOptions.setEnabled(true);
		iPrinterOptions.setFromPage(1);
		iPrinterOptions.setToPage(1);
		iPrinterOptions.setPrinterName("XPS");

		//Scheduler report using InfoStore
		IInfoObjects objectsToSchedule=infoStore.newInfoObjectCollection();
		objectsToSchedule.add(rapor);
		infoStore.schedule(objectsToSchedule);
		System.out.println("Ok");
	} catch (Exception e) {
		// TODO: handle exception
	}
	
}


public static void ScheduleReportByDate(IInfoStore infoStore){
	//String query="SELECT SI_ID,SI_NAME FROM CI_INFOOBJECTS WHERE SI_KIND='CrystalReport' AND SI_NAME='World Sales Report' AND SI_INSTANCE=0";
	IPluginMgr iPluginMgr=infoStore.getPluginMgr();
    try {
		IPluginInfo calendarPlugin=iPluginMgr.getPluginInfo("CrstalEnterprise.Calendar");
		IInfoObjects newInfoObjects=infoStore.newInfoObjectCollection();
		newInfoObjects.add(calendarPlugin);
		IInfoObject iInfoObject=(IInfoObject)newInfoObjects.get(0);
		
		// Baþlýðýný ayarla
		iInfoObject.setTitle("Takvimim");
		iInfoObject.setDescription("Description");
		
		// Tarihleri ayarla
		int sDay=01;
		int sMonth=0;
		int sYear=2010;
		
		int eDay=31;
		int eMonth=11;
		int eYil=2010;
		
		// Hepsini tanýmlamak için gerekli....
		int dayOfWeek=java.util.Calendar.MONDAY;
		int weekOfMonth=IBusinessCalendarDay.ALL;
		
		ICalendar calendar=(ICalendar)iInfoObject;
		IBusinessCalendarDays days=calendar.getDays();
		int calendarID=days.getNextGroupID();
		days.add(sDay,sMonth,sYear,eDay,eMonth,eYil,dayOfWeek,weekOfMonth,calendarID);
		
		// InfoObjecti
		infoStore.commit(newInfoObjects);
		
    } catch (SDKException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
    
	
	
	try {
		
	} catch (Exception e) {
		// TODO: handle exception
	}
	
}

// Bir Yayýn Oluþtur




 public static void main(String[] args) {
	String username = "administrator";
	String password = "";
	String host = "WINBOMDM";
	String auth = "secEnterprise";
	
	IInfoStore infoStore = Login(username, password, host, auth);
	if ( infoStore == null )
		System.out.println("Connection Error");
	else {
		
		IUser iUser=GetUsers(infoStore);
		
		
		MdlPublication mdlPublication=new MdlPublication();
		
		// Publication Parent Folder'ý alýr...
		String si_name="Feature Samples";
		List iPublications=mdlPublication.getPublications(infoStore,si_name);
		
		
		IInfoObject iInfoObject=(IInfoObject)iPublications.get(0);
		
		
		// PUBLICATION OLUÞTURMA
		//boolean pub=mdlPublication.createPublication(infoStore,iInfoObject);
		//System.out.println(pub);
		
		// PUBLICATION SÝLME
		// Publication name = "Sahin Title";
		String pub_name="Sahin Title";
		//mdlPublication.deletePublication(infoStore, si_name, pub_name);
		
		//PUBLICATION DOSYA EKLEME
		//Boolean kaydet=mdlPublication.addPublicationDoc(infoStore,pub_name);
		//System.out.println(kaydet);
		
		// PUBLICATION'a kullanýcý Ata. Þimdilik sahinyanlik Kullanýcýsýný atar
		//Boolean userAta=mdlPublication.addPublicationUser(infoStore,pub_name);
		//System.out.println(userAta);
		
		
		// PUBLICATION'a Grup Ata
		Boolean grupAta=mdlPublication.addPublicationGroup(infoStore,pub_name);
		System.out.println(grupAta);
		
		
		
		
		/* 
		Iterator iT=iPublications.iterator();
		while(iT.hasNext()){
			IPublication iPublication=(IPublication) iT.next();
			System.out.println(iPublication.getTitle());
		}
		*/
		
		//GetUsers(infoStore);
		//GetGroups(infoStore);
		//CreateUser(infoStore);
		//GetReports(infoStore);
		//rightControls(infoStore);
		//setRights(infoStore,iUser);
		//ScheduleReport(infoStore);
		//ScheduleReportByDate(infoStore);
		System.out.println("Connection is Successfull");
		}
	} 
	
}
