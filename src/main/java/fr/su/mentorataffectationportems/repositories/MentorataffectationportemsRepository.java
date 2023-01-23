package fr.su.mentorataffectationportems.repositories;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MentorataffectationportemsRepository extends AbstractRepository {

    @PersistenceContext
    EntityManager entityManager;

    Logger logger = LogManager.getLogger(MentorataffectationportemsRepository.class);

    @Override
    protected Logger getLogger() {
        return this.logger;
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

}
