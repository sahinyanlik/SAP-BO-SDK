package net.metric.login;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.businessobjects.sdk.plugin.desktop.publication.IPublication;
import com.crystaldecisions.sdk.exception.SDKException;
import com.crystaldecisions.sdk.occa.infostore.IInfoObject;
import com.crystaldecisions.sdk.occa.infostore.IInfoObjects;
import com.crystaldecisions.sdk.occa.infostore.IInfoStore;
import com.crystaldecisions.sdk.plugin.CeKind;

public class MdlPublication {

	public static List getPublications(IInfoStore infoStore,String si_name){
		// Publication'ýn olacaðý yerin querysini al
		String query="SELECT SI_ID FROM CI_INFOOBJECTS WHERE SI_KIND='"+CeKind.FOLDER+"' AND SI_NAME='"+si_name+"'";
		List igetPublications=new ArrayList();
		
		try {
			IInfoObjects iInfoObjects = infoStore.query(query);
			//IInfoObject iInfoObject=(IInfoObject)iInfoObjects.get(0);
			Iterator iT=iInfoObjects.iterator();
			while(iT.hasNext()){
				IInfoObject iInfoObject=(IInfoObject) iT.next();
				igetPublications.add(iInfoObject);
			}
		
		} catch (SDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return igetPublications;
	}
	
	public static boolean createPublication(IInfoStore infoStore, IInfoObject iFolder){
		IInfoObjects iInfoObjects=infoStore.newInfoObjectCollection();
		try {
			IPublication iPublication=(IPublication)iInfoObjects.add(CeKind.PUBLICATION);
			iPublication.setTitle("Sahin Title");;
			iPublication.setDescription("Bu sample desc");
			iPublication.setParentID(iFolder.getID());
			iPublication.save();
			
		} catch (SDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void deletePublication(IInfoStore infoStore, String iPubName){
		// Publication'ý bul.
		String publicationQuery="SELECT SI_ID, SI_PUBLICATION_DOCUMENTS FROM CI_INFOOBJECTS"+
								" WHERE SI_KIND='"+CeKind.PUBLICATION+"' AND SI_NAME='"+iPubName+"' AND SI_INSTANCE=0";
		
		try {
			IInfoObjects iInfoObjects=infoStore.query(publicationQuery);
			IPublication iPublication=(IPublication)iInfoObjects.get(0);
			iPublication.deleteNow();
			System.out.println("ok");
			// Delete Publication
			
		} catch (SDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//IInfoObjects iInfoObjects=(IInfoObjects) getPublications(infoStore,iFolder);
	}
	
	
	public boolean addPublicationDoc(IInfoStore infoStore,String iPubName){
		String publicationQuery="SELECT SI_ID, SI_PUBLICATION_DOCUMENTS FROM CI_INFOOBJECTS"+
								" WHERE SI_KIND='"+CeKind.PUBLICATION+"' AND SI_NAME='"+iPubName+"' AND SI_INSTANCE=0";
		
		// SI_PROCESSINFO -> CRYSTAL_REPORT, WEBI,FullClient,FullClientAddin,FullClientTemplate
		try {
			IInfoObjects iInfoObjects=infoStore.query(publicationQuery);
			IPublication iPublication=(IPublication)iInfoObjects.get(0);
			
			// Ekleyeceðimiz Dökümaný Bul
			String docQuery="SELECT SI_ID,SI_PROCESSINFO FROM CI_INFOOBJECTS "+
							" WHERE SI_NAME='World Sales Report' AND SI_INSTANCE=0";
			IInfoObjects iDocInfoObjects=infoStore.query(docQuery);
			IInfoObject iDocInfoObject=(IInfoObject)iDocInfoObjects.get(0);
			
			// Bu Publication'ýnýn dökümanlarýný bulmalý
			// Publication dökümanlarý Collection olarak döner.
			Collection iCollection=iPublication.getDocuments();
			Integer documentId=new Integer(iDocInfoObject.getID());
			iCollection.add(documentId);
			
			System.out.print(iCollection);
			// Dökümanýn processinginfo'sunu rapor objesinden publication'a göndermemiz lazým.
			
			iPublication.setDocumentProcessingInfo(documentId, iDocInfoObject.getKind(), iDocInfoObject.getProcessingInfo().properties());
			
			iPublication.save();
			
			} catch (Exception e) {
			// TODO: handle exception
		}
		return true;
	}
	
	public boolean addPublicationUser(IInfoStore infoStore,String iPubName){
		String query="SELECT SI_ID,SI_PRINCIPALS FROM CI_INFOOBJECTS WHERE SI_KIND='"+CeKind.PUBLICATION+"' AND SI_NAME='"+iPubName+"' AND SI_INSTANCE=0"; 
		IInfoObjects iPublications;
		try {
			iPublications = infoStore.query(query);
			IPublication iPublication=(IPublication)iPublications.get(0);
			
			// Örneðin sahinyanlik'i bunu görebilecek kullanýcýlara ekleyelim.
			String uQuery="SELECT SI_ID FROM CI_SYSTEMOBJECTS WHERE SI_KIND='"+
			CeKind.USER+"' AND SI_NAME='sahinyanlik'";
			
			IInfoObjects iUserObjects=infoStore.query(uQuery);
			IInfoObject iUserObject=(IInfoObject)iUserObjects.get(0);
			
			// Subscribe ile buna baðlamalýyýz.
			Integer userId=new Integer(iUserObject.getID());
			iPublication.subscribe(userId);
			iPublication.save();
			
		} catch (SDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	
	public boolean addPublicationGroup(IInfoStore infoStore,String iPubName){
		
		String query="SELECT SI_ID,SI_PRINCIPALS FROM CI_INFOOBJECTS WHERE SI_KIND='"+CeKind.PUBLICATION+"' AND SI_NAME='"+iPubName+"' AND SI_INSTANCE=0";
		try {
			IInfoObjects iInfoObjects=infoStore.query(query);
			IPublication iPublication=(IPublication)iInfoObjects.get(0);
			
			// Gruplarý ala
			
			String gQuery="SELECT SI_ID FROM CI_SYSTEMOBJECTS WHERE SI_KIND='"+CeKind.USERGROUP+"' AND SI_NAME='Translators'";
			
			IInfoObjects iGroupObjects=infoStore.query(gQuery);
			IInfoObject iGroupObject=(IInfoObject)iGroupObjects.get(0);
			
			//Grubun Id'sini
			Integer groupID=new Integer(iGroupObject.getID());
			// Principal Kolleksiyonunu alýp buna eklememiz gerekiyor
			Collection iPrincipals=iPublication.getPrincipals();
			iPrincipals.add(groupID);
			iPublication.save();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
			return false;
		}
		
		
		return true;
	}
	
	
	
}
