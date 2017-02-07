package ru.javawebinar.topjava.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

/**
 * User: gkisline
 * Date: 26.08.2014
 */

@Transactional(readOnly = true)
@Repository
public class JpaMealRepositoryImpl implements MealRepository {

    @PersistenceContext
    private EntityManager em;
    @Transactional
    @Override
    public Meal save(Meal meal, int userId) {
        if(!meal.isNew() && get(meal.getId(), userId) == null) {
            return null;
        }
        User user = em.getReference(User.class, userId);
        meal.setUser(user);
        if(meal.isNew()) {
            em.persist(meal);
            return meal;
        } else {
            return em.merge(meal);
        }
    }
    @Transactional
    @Override
    public boolean delete(int id, int userId) {

        return em.createNamedQuery(Meal.DELETE)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
       Meal meal =  em.find(Meal.class, id);
       if(meal.getUser().getId() != userId) {
           return null;
       }
       return meal;
    }

    @Override
    public List<Meal> getAll(int userId) {

        return em.createNamedQuery(Meal.GET_ALL, Meal.class).setParameter("userId", userId).getResultList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {

        return em.createNamedQuery(Meal.GET_ALL_BETWEEN, Meal.class).setParameter("userId", userId)
                .setParameter("startTime", startDate)
                .setParameter("endTime", endDate).getResultList();
    }
}