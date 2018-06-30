import io.javalin.Javalin
import io.javalin.embeddedserver.Location
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

object ResLoader {
	fun read(name: String): String {
		return javaClass.getResource(name).readText()
	}
}

fun main(args: Array<String>) {
	
	val app = Javalin.create()
			.port(getHerokuAssignedPort())
			.enableStaticFiles("static", Location.EXTERNAL)
			.start()

	app.error(404) {
		it.html("<b>Theres no page like this my dude yo got balboolzeld xDD</b>")
	}
	
	app.get("/") {
		
		println("${it.ip()} GET /")
		
		var initStr = File("static/index.html").readText()
		
		it.html(initStr)
	}

	app.ws("/websocket") { ws ->
		
		ws.onConnect { session ->
			println("WEBSOCKET : ${session.remoteAddress} connected")
		}
		
		ws.onMessage { session, msg ->
			println("WEBSOCKET : received: $msg")
			
			if (msg == "getTime") {
				val time = SimpleDateFormat("HH:mm:ss").format(Date())
				println("WEBSOCKET : responding with time -> $time")
				session.send(time)
			}
			
		}
		
		ws.onClose { session, statusCode, reason ->
			println("WEBSOCKET : closed")
		}
		
		ws.onError { session, throwable ->
			println("WEBSOCKET : Error -> ${throwable.message}")
		}
		
	}

	app.get("/readCyka") {
		it.result(File("a.txt").readText())
	}
	
}

private fun getHerokuAssignedPort(): Int {
	val processBuilder = ProcessBuilder()
	return if (processBuilder.environment()["PORT"] != null) {
		Integer.parseInt(processBuilder.environment()["PORT"])
	} else 7000
}