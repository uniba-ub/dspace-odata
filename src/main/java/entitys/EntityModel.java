package entitys;


import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;

public interface EntityModel {
	
	public CsdlEntityType getEntityType();
	public FullQualifiedName getFullQualifiedName();
	public String getEntitySetName();
	public CsdlEntitySet getEntitySet();
	public String getRecourceTypeFilter();
	public String getIDConverterTyp();
	public String getNavigationFilter(String sourceType, String id);

}
