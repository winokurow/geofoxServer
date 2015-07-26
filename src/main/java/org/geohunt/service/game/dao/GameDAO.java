package org.geohunt.service.game.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geohunt.service.game.common.CommonUtils;
import org.geohunt.service.game.entities.Game;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public class GameDAO implements IGameDAO {
	DataSource datasource;

	private JdbcTemplate templGame;
	private SimpleJdbcInsert insertGame;
	private SimpleJdbcInsert insertMember;
	private SimpleJdbcInsert insertPerson;

	final Logger logger = LogManager.getLogger(this.getClass());

	public void setDataSource(DataSource dataSource) {
		this.templGame = new JdbcTemplate(dataSource);
		this.insertGame = new SimpleJdbcInsert(dataSource).withTableName("games");
		this.insertMember = new SimpleJdbcInsert(dataSource).withTableName("members");
		this.insertPerson = new SimpleJdbcInsert(dataSource).withTableName("users");
	}

	public UUID createGame(Game game) {
		if (game != null) {
			int count = 0;
			UUID idOne = UUID.randomUUID();
			String sql;
			try {
				sql = "SELECT count(*) FROM games WHERE name = ?";
				count = templGame.queryForObject(sql, new Object[] { game.getName() }, Integer.class);
			} catch (EmptyResultDataAccessException e) {
			}
			if (count == 0) {
				sql = "SELECT count(*) FROM users WHERE name = ?";
				count = 0;
				try {
					count = templGame.queryForObject(sql, new Object[] { game.getUser() }, Integer.class);
				} catch (EmptyResultDataAccessException e) {
				}

				if (count == 0) {
					Map<String, Object> parameters = new HashMap<String, Object>(1);
					parameters.put("name", game.getUser());
					insertPerson.execute(parameters);
				}

				Map<String, Object> parameters = new HashMap<String, Object>(7);
				parameters.put("name", game.getName());
				logger.debug(game.getTyp().trim());
				parameters.put("typ", Integer.parseInt(game.getTyp().trim()));

				String password = CommonUtils.getHash(game.getPassword());
				parameters.put("password", password);
				parameters.put("status", 1);
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.add(Calendar.HOUR, 2);
				Date newDate = cal.getTime();
				parameters.put("gameend", new java.sql.Date(newDate.getTime()));
				insertGame.execute(parameters);
				sql = "Select id FROM users WHERE name = ?";
				int personid = templGame.queryForObject(sql, new Object[] { game.getUser() }, Integer.class);
				sql = "Select id FROM games WHERE name = ?";
				int gameid = templGame.queryForObject(sql, new Object[] { game.getName() }, Integer.class);

				parameters = new HashMap<String, Object>(7);
				parameters.put("sessionid", "test12345");
				parameters.put("typ", new Integer(1));
				parameters.put("coordx", 0);
				parameters.put("coordy", 0);
				parameters.put("speed", 0);
				parameters.put("userid", personid);
				parameters.put("gameid", gameid);
				parameters.put("lastupdate", new java.sql.Date(new Date().getTime()));
				parameters.put("sessionid", idOne);
				insertMember.execute(parameters);

				return idOne;
			} else {
				return null;
			}
		}
		return null;
	}

}
