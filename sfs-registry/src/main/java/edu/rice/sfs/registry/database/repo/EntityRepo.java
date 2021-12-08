package edu.rice.sfs.registry.database.repo;

import edu.rice.sfs.registry.database.po.EntityPO;
import org.springframework.data.repository.CrudRepository;

public interface EntityRepo extends CrudRepository<EntityPO, String> {

}
