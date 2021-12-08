package edu.rice.sfs.registry.database.repo;

import edu.rice.sfs.registry.database.po.FeatureTablePO;
import org.springframework.data.repository.CrudRepository;

public interface FeatureTableRepo extends CrudRepository<FeatureTablePO, String> {

}
