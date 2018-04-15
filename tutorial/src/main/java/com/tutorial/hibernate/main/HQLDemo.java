package com.tutorial.hibernate.main;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.tutorial.hibernate.model.Employee1;
import com.tutorial.hibernate.util.HibernateUtil;

public class HQLDemo {
	static {
		System.out.println("Before log4j configuration");
		DOMConfigurator.configure("log4j.xml");
		System.out.println("After log4j configuration");
	}

	private static Logger logger = Logger.getLogger(HQLDemo.class);

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		// Get Session

		logger.info("Getting a fresh session");
		SessionFactory sessionFactory = HibernateUtil.getSessionJavaConfigFactory();
		Session session = sessionFactory.getCurrentSession();

		logger.info("Session : " + session);
		// start transaction
		session.beginTransaction();

		// Get all employees
		logger.info("Get all employees");
		Query query = session.createQuery("from Employee1");
		logger.info("Query : " + query);
		List<Employee1> empList = query.list();

		for (Employee1 emp : empList) {
			logger.info("List of Employees :" + emp.getId());
		}

		// HQL example - Get Employee with id
		logger.info("HQL example - Get Employee with id");
		query = session.createQuery("from Employee1 where id= :id");
		query.setLong("id", 19);
		Employee1 emp1 = (Employee1) query.uniqueResult();
		logger.info("Employee Name : " + emp1.getName());

		// HQL pagination example
		query = session.createQuery("from Employee1");
		query.setFirstResult(0); // starts with 0
		query.setFetchSize(2);
		empList = query.list();
		for (Employee1 emp2 : empList) {
			logger.info("Paginated Employees :" + emp2.getId());
		}

		// HQL Update Employee
		logger.info("Updating the Employee Name " + emp1.getName());
		query = session.createQuery("update Employee1 set name= :name where id= :id");
		query.setParameter("name", "Gaurav C Sahu");
		query.setLong("id", 19);
		int result = query.executeUpdate();
		System.out.println("Employee1 Update Status=" + result);

		// Commit transaction
		session.getTransaction().commit();

		// terminate session factory, otherwise program won't end
		sessionFactory.close();
		logger.info("DONE");
	}
}
