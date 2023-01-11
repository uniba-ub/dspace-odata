package entitys;


import java.util.ArrayList;
import java.util.HashMap;

import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;

public interface EntityModel {

	CsdlEntityType getEntityType();
	FullQualifiedName getFullQualifiedName();
	String getEntitySetName();
	CsdlEntitySet getEntitySet();
	String getRecourceTypeFilter();
	String getIDConverterTyp();
	String getNavigationFilter(String sourceType, String id);
	HashMap<String, String> getMapping();
	ArrayList<String> getEntityFilter();


}
