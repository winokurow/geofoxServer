package org.geohunt.service.game.dao;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.geohunt.service.game.entities.Game;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public class GameDAO implements IGameDAO {
	DataSource datasource;

	private JdbcTemplate templGame;
	private SimpleJdbcInsert insertGame;
	private SimpleJdbcInsert insertMember;
	private SimpleJdbcInsert insertPerson;

	final static Logger logger = Logger.getLogger(classname.class);

	public void setDataSource(DataSource dataSource) {
		this.templGame = new JdbcTemplate(dataSource);
		this.insertGame = new SimpleJdbcInsert(dataSource).withTableName("games");
		this.insertMember = new SimpleJdbcInsert(dataSource).withTableName("members");
		this.insertPerson = new SimpleJdbcInsert(dataSource).withTableName("users");
	}

	public int createGame(Game game) {
		if (game != null) {
			if ((templGame.queryForInt("Select count(1) FROM games WHERE name = '" + game.getName() + "'")) == 0) {

				if ((templGame.queryForInt("Select count(1) FROM users WHERE name = '" + game.getUser() + "'")) == 0) {
					Map<String, Object> parameters = new HashMap<String, Object>(1);
					parameters.put("name", game.getUser());
					insertPerson.execute(parameters);
				}

				Map<String, Object> parameters = new HashMap<String, Object>(7);
				parameters.put("name", game.getName());
				logger.debug(game.getTyp().trim());
				parameters.put("typ", Integer.parseInt(game.getTyp().trim()));

				MessageDigest msgDigest;
				byte[] hash = null;
				try {
					msgDigest = MessageDigest.getInstance("SHA-256");
					byte[] bytesOfMessage = game.getPassword().getBytes("UTF-8");
					hash = msgDigest.digest(bytesOfMessage);
					game.setPassword(new String(hash, "UTF-8"));
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				parameters.put("password", game.getPassword());
				parameters.put("status", 1);
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.add(Calendar.HOUR, 2);
				Date newDate = cal.getTime();
				parameters.put("gameend", new java.sql.Date(newDate.getTime()));
				insertGame.execute(parameters);

				int personid = templGame.queryForInt("Select id FROM users WHERE name = '" + game.getUser() + "'");
				int gameid = templGame.queryForInt("Select id FROM games WHERE name = '" + game.getName() + "'");

				parameters = new HashMap<String, Object>(7);
				parameters.put("sessionid", "test12345");
				parameters.put("typ", new Integer(1));
				parameters.put("coordx", 0);
				parameters.put("coordy", 0);
				parameters.put("speed", 0);
				parameters.put("userid", personid);
				parameters.put("gameid", gameid);
				parameters.put("lastupdate", new java.sql.Date(new Date().getTime()));
				insertMember.execute(parameters);

				return 0;
			} else {
				return 1;
			}
		}
		return 0;
	}

}
