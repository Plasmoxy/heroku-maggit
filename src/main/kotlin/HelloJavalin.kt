import io.javalin.Javalin
import io.javalin.embeddedserver.Location
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

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
		
	}.get("/readCyka") {
		it.result(File("a.txt").readText())
	}

	app.ws("/timews") { ws ->
		
		ws.onConnect { session ->
			println("ws:timews : ${session.remoteAddress} connected")
		}
		
		ws.onMessage { session, msg ->
			println("ws:timews : received: $msg")
			
			if (msg == "getTime") {

				val cal = Calendar.getInstance()
				cal.time = Date()
				cal.add(Calendar.HOUR_OF_DAY, 2)
				val outTime = SimpleDateFormat("HH:mm:ss").format(cal.time)

				println("ws:timews : responding with time -> $outTime")
				session.send(outTime)
			}
			
		}
		
		ws.onClose { session, statusCode, reason ->
			println("ws:timews : ${session.remoteAddress} disconnected")
		}
		
		ws.onError { session, throwable ->
			println("ws:timews : Error -> ${throwable.message}")
		}
		
	}

	app.ws("/echo") {

		it.onConnect {
			println("ws:echo : ${it.remoteAddress} connected")
		}

		it.onMessage { session, msg ->
			session.send(msg)
		}

		it.onClose { sess, status, reason ->
			println("ws:echo : ${sess.remoteAddress} disconected")
		}

		it.onError { session, throwable ->
			println("ws:timews : Error -> ${throwable.message}")
		}

	}
	
}

private fun getHerokuAssignedPort(): Int {
	val processBuilder = ProcessBuilder()
	return if (processBuilder.environment()["PORT"] != null) {
		Integer.parseInt(processBuilder.environment()["PORT"])
	} else 7000
}