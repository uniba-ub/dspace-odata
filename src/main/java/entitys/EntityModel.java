package entitys;

import java.util.List;

import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
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
	public List<String> getNavigationFilterList();

}
