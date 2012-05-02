package org.atomium.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.atomium.EntityManager;
import org.atomium.cfg.Configuration;
import org.atomium.util.Function1;
import org.atomium.util.query.Query;
import org.atomium.util.query.QueryBuilderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyEntityManager implements EntityManager {
	
	private static final Logger log = LoggerFactory.getLogger(MyEntityManager.class);
	
	private Configuration cfg;
	private ExecutorService executor;
	private Connection connection;
	private boolean run;
	private List<Query> queries = Collections.synchronizedList(new ArrayList<Query>());

	public MyEntityManager(Configuration cfg) throws ClassNotFoundException {
		Class.forName(cfg.driver());
		
		this.cfg = cfg;
		this.executor = Executors.newSingleThreadExecutor();
	}

	public void start() {
		try {
			connection = DriverManager.getConnection(cfg.connection(), cfg.user(), cfg.password());
			
			run = true;
			
			executor.submit(new Runnable() {
				public void run() {
					loop();
				}
			});
		} catch (SQLException e) {
			log.error("can't open connection because : {}", e.getMessage());
		}
	}

	public void stop() {
		run = false;
		executor.shutdown();
		try {
			flush();
			connection.close();
		} catch (SQLException e) {
			log.error("can't close connection because : {}", e.getMessage());
		}
	}
	
	private void loop() {
		while (run) {
			try {
				Thread.sleep(cfg.flushDelay());
				
			} catch (InterruptedException e) {
				log.error("FATAL ERROR");
			}
		}
	}
	
	private static <T> Iterable<T> copyAndClear(List<T> list) {
		Iterable<T> result = new ArrayList<T>(list);
		list.clear();
		return result;
	}
	
	private void flush() throws SQLException {
		Statement statement = null;
		try {
			statement = connection.createStatement();
			for (Query query : copyAndClear(queries)) {
				statement.execute(query.toString());
			}
		} catch (SQLException e) {
			log.error(e.toString());
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					log.error(e.toString());
				}
			}
		}
	}

	public QueryBuilderFactory builder() {
		return null;
	}

	public <T> T query(Query query, Function1<T, ResultSet> function) {
		return null;
	}

	public void execute(Query query) {
		queries.add(query);
	}

}
