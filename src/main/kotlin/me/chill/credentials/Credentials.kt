package me.chill.credentials

import me.chill.configurations.Configuration
import me.chill.configurations.isHerokuRunning
import me.chill.exceptions.TaigaException

// todo: make the exception messages more informative
class Credentials(configuration: Configuration?) {
	var token: String? = ""
	var database: String? = ""
	var prefix: String? = ""

	init {
		if (isHerokuRunning()) {
			token = System.getenv("BOT_TOKEN")
			database = System.getenv("JDBC_DATABASE_URL")
			prefix = System.getenv("PREFIX")
			if (token == null) throw TaigaException("Set a token environment variable in Heroku to proceed")
			if (database == null) throw TaigaException("Set up Heroku Postgres to proceed")
			if (prefix == null) throw TaigaException("Set a prefix environment variable in Heroku to proceed")
		} else {
			if (configuration == null) throw TaigaException("Configuration cannot be null for a local instance of Taiga")

			token = configuration.token
			database = configuration.database
			prefix = configuration.prefix

			if (token == "enter your token") throw TaigaException("Set a token in the config.json to proceed")
			if (database == "enter your database url") throw TaigaException("Set a database url in the config.json to proceed")
			if (prefix == "enter your prefix") throw TaigaException("Set up a prefix in the config.json to proceed")
		}
	}
}
