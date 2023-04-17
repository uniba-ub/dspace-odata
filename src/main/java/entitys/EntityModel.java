package entitys;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;

public interface EntityModel {

	CsdlEntityType getEntityType();
	FullQualifiedName getFullQualifiedName();
	String getEntitySetName();
	CsdlEntitySet getEntitySet();
	String getRecourceTypeFilter();
	HashMap<String, String> getIdConverter();
	String getNavigationFilter(String sourceType, String id);
	HashMap<String, List<String>> getMapping();
	ArrayList<String> getEntityFilter();
	String getLegacyPrefix();


}
