package me.chill

import me.chill.commands.events.InputEvent
import me.chill.commands.events.OnJoinEvent
import me.chill.commands.events.OnLeaveEvent
import me.chill.commands.framework.CommandContainer
import me.chill.configuration.isHerokuRunning
import me.chill.configuration.loadConfigurations
import me.chill.credential.Credentials
import me.chill.database.setupDatabase
import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import net.dv8tion.jda.core.OnlineStatus
import net.dv8tion.jda.core.entities.Game

// todo: add a command to dm all server owners if there is a problem detected
var credentials: Credentials? = null
fun main(args: Array<String>) {
	credentials = if (isHerokuRunning()) {
		Credentials(null)
	} else {
		val configurations = loadConfigurations() ?: return
		Credentials(configurations)
	}

	setupDatabase(credentials!!.database!!)

	val commandContainer = CommandContainer.loadContainer()

	val jda: JDA = JDABuilder(AccountType.BOT)
		.setStatus(OnlineStatus.ONLINE)
		.setToken(credentials!!.token)
		.setGame(Game.playing("${credentials!!.prefix}help"))
		.build()
	jda.addEventListener(OnJoinEvent(), OnLeaveEvent(), InputEvent(jda))
}