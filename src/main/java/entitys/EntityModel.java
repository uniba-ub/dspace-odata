package entitys;


import java.util.ArrayList;
import java.util.HashMap;

import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;

public interface EntityModel {

	public CsdlEntityType getEntityType();
	public FullQualifiedName getFullQualifiedName();
	public String getEntitySetName();
	public CsdlEntitySet getEntitySet();
	public String getRecourceTypeFilter();
	public HashMap<String, String> getIdConverter();
	public String getNavigationFilter(String sourceType, String id);
	public HashMap<String, String> getMapping();
	public ArrayList<String> getEntityFilter();
	public String getLegacyPrefix();


}
